import React from "react";
import { MatchCard } from "./MatchCard.jsx";

export function MatchGroup({ dateLabel, matches, contextPath, config, onSelectMatch }) {
  return (
    <section className="match-date-group">
      <div className="section-heading match-date-group-heading">
        <h2>{dateLabel}</h2>
        <span className="role-count">{matches.length}</span>
      </div>

      <div className="match-calendar">
        {matches.map((match) => (
          <MatchCard
            key={match.id}
            match={match}
            contextPath={contextPath}
            config={config}
            onSelect={() => onSelectMatch(match)}
          />
        ))}
      </div>
    </section>
  );
}
