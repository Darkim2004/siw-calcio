import React, { useMemo, useState } from "react";
import { MatchFilters } from "./components/MatchFilters.jsx";
import { MatchStats } from "./components/MatchStats.jsx";
import { MatchGroup } from "./components/MatchGroup.jsx";
import { MatchDetailDrawer } from "./components/MatchDetailDrawer.jsx";
import { dateValue, groupByDate, normalize } from "./matchUtils.js";

export function MatchCenter({ config }) {
  const matches = Array.isArray(config.matches) ? config.matches : [];
  const contextPath = config.contextPath || "/";
  const [query, setQuery] = useState("");
  const [status, setStatus] = useState("ALL");
  const [tournament, setTournament] = useState("ALL");
  const [sort, setSort] = useState("ASC");
  const [selectedMatch, setSelectedMatch] = useState(null);

  const tournamentOptions = useMemo(() => {
    return Array.from(new Set(matches.map((match) => match.tournamentName).filter(Boolean))).sort();
  }, [matches]);

  const visibleMatches = useMemo(() => {
    const search = normalize(query);

    return matches
      .filter((match) => {
        const searchableText = normalize([
          match.homeTeam?.name,
          match.awayTeam?.name,
          match.tournamentName,
          match.venue,
          match.refereeName
        ].join(" "));

        return (status === "ALL" || match.state === status)
          && (tournament === "ALL" || match.tournamentName === tournament)
          && (!search || searchableText.includes(search));
      })
      .sort((left, right) => (
        sort === "ASC" ? dateValue(left) - dateValue(right) : dateValue(right) - dateValue(left)
      ));
  }, [matches, query, status, tournament, sort]);

  const groupedMatches = useMemo(() => groupByDate(visibleMatches), [visibleMatches]);

  function resetFilters() {
    setQuery("");
    setStatus("ALL");
    setTournament("ALL");
    setSort("ASC");
  }

  return (
    <section className="match-center-app">
      <MatchStats matches={matches} />
      <MatchFilters
        query={query}
        status={status}
        tournament={tournament}
        sort={sort}
        tournamentOptions={tournamentOptions}
        resultCount={visibleMatches.length}
        totalCount={matches.length}
        onQueryChange={setQuery}
        onStatusChange={setStatus}
        onTournamentChange={setTournament}
        onSortChange={setSort}
        onReset={resetFilters}
      />

      {visibleMatches.length > 0 ? (
        <div className="match-center-groups">
          {Array.from(groupedMatches.entries()).map(([dateLabel, groupMatches]) => (
            <MatchGroup
              key={dateLabel}
              dateLabel={dateLabel}
              matches={groupMatches}
              contextPath={contextPath}
              config={config}
              onSelectMatch={setSelectedMatch}
            />
          ))}
        </div>
      ) : (
        <p className="empty-state">
          {matches.length === 0 ? "No matches are currently available." : "No matches match the selected filters."}
        </p>
      )}

      <MatchDetailDrawer
        match={selectedMatch}
        contextPath={contextPath}
        onClose={() => setSelectedMatch(null)}
      />
    </section>
  );
}
