const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  console.log("abc");
  app.use(
    "/api",
    createProxyMiddleware({
      target: "http://localhost:9999",
      changeOrigin: true,
      logLevel: "debug",
    })
  );
};
