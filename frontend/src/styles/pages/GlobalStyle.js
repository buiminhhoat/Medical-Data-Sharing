import { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`
    @font-face {
        font-family: 'Poppins';
        src: url("/fonts/Poppins-Regular.ttf") format('truetype');
        font-weight: normal;
        font-style: normal;
    }


    * {
        font-family: Poppins;
        padding: 0;
        margin: 0;
    }

    .page {
        background-color: rgb(250, 250, 250);
        min-height: 100%;
        width: 100%;
    }

    .container {
        max-width: 82%;
        width: 100%;
        margin: auto;
        padding-top: 20px;
    }

    .row {
        display: flex;
        flex-wrap: wrap;
    }

    .col-3 {
        flex: 0 0 50%;
        max-width: 50%;
    }

    .col-6 {
        flex: 0 0 50%;
        max-width: 50%;
    }

    html,
    body {
        margin: 0;
        padding: 0;
        color: #333333;
        font-size: 16px;
        height: 100%;
        width: 100%;
    }

    body {
        overflow-y: scroll;
    }

    body::-webkit-scrollbar {
        z-index: 1000;
        position: fixed;
        width: 5px;
        height: 5px;
    }

    body::-webkit-scrollbar-track {
        background: rgb(216, 236, 203);
    }

    body::-webkit-scrollbar-thumb {
        background-color: rgba(10, 101, 22, 0.5);
    }

    body::-webkit-scrollbar-thumb:hover {
        background-color: rgba(10, 101, 22, 0.75);
    }


    .no-padding {
        padding: 0;
    }

    ul {
        padding: 0;
        margin: 0;
        list-style: none;
    }

    a {
        text-decoration: none;
        color: #333333;
    }

    a:hover {
        color: #285430;
    }

    a:hover,
    a:focus {
        outline: none;
        text-decoration: none;
    }

    h1, h2, h3, h4, h5, h6 {
        font-family: "Poppins";
        font-weight: 600;
    }

    h1 {
        font-size: 30px;
    }

    h2 {
        font-size: 25px;
        font-weight: 700;
        line-height: 40px;
        margin: 0;
        padding-bottom: 10px;
    }

    img {
        border: none;
    }
`;

export default GlobalStyle;
