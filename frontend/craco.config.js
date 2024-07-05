const path = require("path");

module.exports = {
  webpack: {
    alias: {
      "@": path.resolve(__dirname, "src/"),
      "@Utils": path.resolve(__dirname, "src/utils"),
      "@Const": path.resolve(__dirname, "src/utils/const.js"),
      "@Components": path.resolve(__dirname, "src/components"),
      "@Pages": path.resolve(__dirname, "src/pages"),
      "@Layout": path.resolve(__dirname, "src/layout"),
    },
  },
};
