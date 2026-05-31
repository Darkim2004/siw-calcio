import React from "react";
import { createRoot } from "react-dom/client";
import { MatchCenter } from "./match-center/MatchCenter.jsx";

const rootElement = document.getElementById("match-center-root");
const config = window.SIW_MATCH_CENTER || {};

if (rootElement) {
  createRoot(rootElement).render(<MatchCenter config={config} />);
  document.body.classList.add("react-match-center-ready");
}
