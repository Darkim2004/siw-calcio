import React from "react";
import { assetPath, joinPath, statusLabel } from "../matchUtils.js";

function MatchTeam({ team, home, contextPath }) {
  const safeTeam = team || {};
  const name = safeTeam.name || "Team";
  const content = [
    <a key="name" className="match-team-name" href={safeTeam.id ? joinPath(contextPath, "teams", safeTeam.id) : "#"}>
      {name}
    </a>,
    <img
      key="logo"
      className="match-team-logo"
      src={assetPath(contextPath, safeTeam.logo)}
      alt={`Logo ${name}`}
    />
  ];

  return (
    <div className={`match-team ${home ? "match-team-home" : "match-team-away"}`}>
      {home ? content : content.reverse()}
    </div>
  );
}

function MatchResult({ match }) {
  if (match.state === "PLAYED") {
    return (
      <div className="match-score">
        <span>{match.goalsHome}</span>
        <span className="score-separator">|</span>
        <span>{match.goalsAway}</span>
      </div>
    );
  }

  return <div className="match-center">VS</div>;
}

function MatchAdminActions({ match, contextPath, config }) {
  if (!config.isAdmin) {
    return null;
  }

  const csrf = config.csrf || {};

  return (
    <>
      <a className="add-button" href={joinPath(contextPath, "admin", "matches", match.id, "edit")}>Edit match</a>
      <form
        className="delete-form"
        action={joinPath(contextPath, "admin", "matches", match.id, "delete")}
        method="post"
        onSubmit={(event) => {
          if (!window.confirm("Are you sure you want to delete this match?")) {
            event.preventDefault();
          }
        }}
      >
        {csrf.parameterName && csrf.token ? (
          <input type="hidden" name={csrf.parameterName} value={csrf.token} />
        ) : null}
        <button className="danger-button" type="submit">Delete match</button>
      </form>
    </>
  );
}

export function MatchCard({ match, contextPath, config, onSelect }) {
  return (
    <article className="match-row match-row-manageable">
      <div className="match-date">
        <span className="match-day">{match.dateLabel || "Date not set"}</span>
        <span className="match-time">{match.timeLabel || "--:--"}</span>
      </div>

      <div className="match-react-body">
        <div className="match-teams">
          <MatchTeam team={match.homeTeam} home contextPath={contextPath} />
          <MatchResult match={match} />
          <MatchTeam team={match.awayTeam} contextPath={contextPath} />
        </div>

        <div className="match-react-meta">
          {match.tournamentName ? <span>{match.tournamentName}</span> : null}
          {match.venue ? <span>{match.venue}</span> : null}
          {match.refereeName ? <span>{match.refereeName}</span> : null}
          <span>{statusLabel(match.state)}</span>
        </div>
      </div>

      <div className="admin-actions match-admin-actions">
        <button className="add-button" type="button" onClick={onSelect}>Preview</button>
        <a className="add-button" href={joinPath(contextPath, "matches", match.id)}>Details</a>
        <MatchAdminActions match={match} contextPath={contextPath} config={config} />
      </div>
    </article>
  );
}
