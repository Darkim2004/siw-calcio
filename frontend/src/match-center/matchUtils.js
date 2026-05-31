export function normalize(value) {
  return String(value || "").toLowerCase().trim();
}

export function joinPath(contextPath, ...parts) {
  const base = contextPath.endsWith("/") ? contextPath : `${contextPath}/`;
  return `${base}${parts.map((part) => encodeURIComponent(String(part))).join("/")}`;
}

export function assetPath(contextPath, path) {
  if (!path) {
    return joinPath(contextPath, "images", "SiwCalcio_logo.png");
  }
  if (/^(https?:)?\/\//.test(path) || path.startsWith("data:")) {
    return path;
  }
  if (path.startsWith("/")) {
    return `${contextPath.replace(/\/$/, "")}${path}`;
  }
  return `${contextPath.endsWith("/") ? contextPath : `${contextPath}/`}${path}`;
}

export function dateValue(match) {
  const value = Date.parse(match.dateTime || "");
  return Number.isNaN(value) ? 0 : value;
}

export function statusLabel(status) {
  return {
    SCHEDULED: "Scheduled",
    ONGOING: "Ongoing",
    PLAYED: "Played"
  }[status] || "Scheduled";
}

export function groupByDate(matches) {
  return matches.reduce((groups, match) => {
    const key = match.dateLabel || "Date not set";
    if (!groups.has(key)) {
      groups.set(key, []);
    }
    groups.get(key).push(match);
    return groups;
  }, new Map());
}
