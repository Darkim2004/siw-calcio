import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "../src/main/resources/static/js/react",
    emptyOutDir: true,
    target: "es2018",
    rollupOptions: {
      input: "src/main.jsx",
      output: {
        entryFileNames: "match-center.js",
        chunkFileNames: "[name].js",
        assetFileNames: "[name][extname]"
      }
    }
  }
});
