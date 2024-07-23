import React from "react";
import ReactDOM from "react-dom/client";
import HomePage from "./pages/users/homePage";
import { BrowserRouter } from "react-router-dom";
import RouterCustom from "./router";
import { ThemeProvider } from "styled-components";
import GlobalStyle from "./styles/pages/GlobalStyle";
import theme from "./styles/pages/theme";
import { ConfigProvider } from "antd/es";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <ConfigProvider
    theme={{
      components: {
        Input: {
          activeShadow: "0 0 0 2px rgba(34, 190, 34, 0.1)",
        },
      },
      token: {
        colorPrimary: "green",
        colorPrimaryActive: "green",
        colorPrimaryHover: "green",
      },
    }}
  >
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <BrowserRouter>
        <RouterCustom></RouterCustom>
      </BrowserRouter>
    </ThemeProvider>
  </ConfigProvider>
);
