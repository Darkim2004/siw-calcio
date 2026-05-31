import React from "react";

export function MatchFilters({
  query,
  status,
  tournament,
  sort,
  tournamentOptions,
  resultCount,
  totalCount,
  onQueryChange,
  onStatusChange,
  onTournamentChange,
  onSortChange,
  onReset
}) {
  return (
    <div className="match-center-panel">
      <div className="match-center-toolbar">
        <div className="form-field match-search-field">
          <label htmlFor="match-search">Search</label>
          <input
            id="match-search"
            type="search"
            value={query}
            placeholder="Team, tournament, venue, referee"
            onChange={(event) => onQueryChange(event.target.value)}
          />
        </div>

        <div className="form-field match-filter-field">
          <label htmlFor="match-status-filter">Status</label>
          <select
            id="match-status-filter"
            value={status}
            onChange={(event) => onStatusChange(event.target.value)}
          >
            <option value="ALL">All</option>
            <option value="SCHEDULED">Scheduled</option>
            <option value="ONGOING">Ongoing</option>
            <option value="PLAYED">Played</option>
          </select>
        </div>

        <div className="form-field match-filter-field">
          <label htmlFor="match-tournament-filter">Tournament</label>
          <select
            id="match-tournament-filter"
            value={tournament}
            onChange={(event) => onTournamentChange(event.target.value)}
          >
            <option value="ALL">All</option>
            {tournamentOptions.map((option) => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>

        <div className="form-field match-filter-field">
          <label htmlFor="match-sort">Sort</label>
          <select id="match-sort" value={sort} onChange={(event) => onSortChange(event.target.value)}>
            <option value="ASC">Soonest first</option>
            <option value="DESC">Latest first</option>
          </select>
        </div>

        <button className="back-button match-reset-button" type="button" onClick={onReset}>Reset</button>
      </div>

      <div className="match-results-summary">
        Showing {resultCount} of {totalCount} matches
      </div>
    </div>
  );
}
