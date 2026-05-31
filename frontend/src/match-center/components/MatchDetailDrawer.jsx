import React from "react";
import { assetPath, joinPath, statusLabel } from "../matchUtils.js";

export function MatchDetailDrawer({ match, contextPath, onClose }) {
  if (!match) {
    return null;
  }

  const homeName = match.homeTeam?.name || "Home team";
  const awayName = match.awayTeam?.name || "Away team";

  return (
    <div className="match-drawer-backdrop" role="presentation" onClick={onClose}>
      <aside
        className="match-drawer"
        role="dialog"
        aria-modal="true"
        aria-label={`${homeName} vs ${awayName}`}
        onClick={(event) => event.stopPropagation()}
      >
        <div className="match-drawer-head">
          <div>
            <span className="auth-kicker">{statusLabel(match.state)}</span>
            <h2>{homeName} vs {awayName}</h2>
          </div>
          <button className="back-button compact-button" type="button" onClick={onClose}>Close</button>
        </div>

        <div className="match-drawer-score">
          <img src={assetPath(contextPath, match.homeTeam?.logo)} alt={`Logo ${homeName}`} />
          <strong>{match.state === "PLAYED" ? `${match.goalsHome} | ${match.goalsAway}` : "VS"}</strong>
          <img src={assetPath(contextPath, match.awayTeam?.logo)} alt={`Logo ${awayName}`} />
        </div>

        <div className="match-drawer-meta">
          <span><strong>Date:</strong> {match.dateLabel || "Date not set"} {match.timeLabel || ""}</span>
          {match.tournamentName ? <span><strong>Tournament:</strong> {match.tournamentName}</span> : null}
          {match.venue ? <span><strong>Venue:</strong> {match.venue}</span> : null}
          {match.refereeName ? <span><strong>Referee:</strong> {match.refereeName}</span> : null}
        </div>

        <div className="actions detail-actions">
          <a className="add-button" href={joinPath(contextPath, "matches", match.id)}>Open match page</a>
          {match.homeTeam?.id ? <a className="back-button" href={joinPath(contextPath, "teams", match.homeTeam.id)}>{homeName}</a> : null}
          {match.awayTeam?.id ? <a className="back-button" href={joinPath(contextPath, "teams", match.awayTeam.id)}>{awayName}</a> : null}
        </div>
      </aside>
    </div>
  );
}
