import React from "react";

const STATUSES = [
  ["ALL", "Total"],
  ["SCHEDULED", "Scheduled"],
  ["ONGOING", "Ongoing"],
  ["PLAYED", "Played"]
];

export function MatchStats({ matches }) {
  return (
    <div className="match-center-stats" aria-label="Match summary">
      {STATUSES.map(([status, label]) => {
        const count = status === "ALL"
          ? matches.length
          : matches.filter((match) => match.state === status).length;

        return (
          <div className="match-stat-card" key={status}>
            <span>{label}</span>
            <strong>{count}</strong>
          </div>
        );
      })}
    </div>
  );
}
