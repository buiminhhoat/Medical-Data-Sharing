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

    .container {
        max-width: 80%;
        width: 100%;
        padding-left: 20px;
        padding-right: 20px;
        margin: auto;
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
        background-color: rgb(250, 250, 250);
        color: #333333;
        font-size: 16px;
        overflow-x: hidden;
        height: 100%;
        width: 100%;
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

    h2 {
        font-size: 30px;
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
