import React from "react";
import ReactDOM from "react-dom/client";
import HomePage from "./pages/users/homePage";
import { BrowserRouter } from "react-router-dom";
import RouterCustom from "./router";
import { ThemeProvider } from "styled-components";
import GlobalStyle from "./styles/pages/GlobalStyle";
import theme from "./styles/pages/theme";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <ThemeProvider theme={theme}>
        <GlobalStyle />
        <BrowserRouter>
            <RouterCustom></RouterCustom>
        </BrowserRouter>
    </ThemeProvider>
);
