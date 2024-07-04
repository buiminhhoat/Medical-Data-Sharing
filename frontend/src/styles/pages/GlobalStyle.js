import { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`
    @font-face {
        font-family: 'Poppins';
        src: url("/fonts/Poppins-Regular.ttf") format('truetype');
        font-weight: normal;
        font-style: normal;
    }
    
    html,
    body {
        margin: 0;
    }

    * {
        font-family: Poppins;
        padding: 0;
        margin: 0;
    }

    .container {
        max-width: 1170px;
        width: 100%;
        padding-left: 20px;
        padding-right: 20px;
        margin: auto;
    }

    .row {
        display: flex;
        flex-wrap: wrap;
    }

    .col-6 {
        flex: 0 0 50%;
        max-width: 50%;
    }
`;

export default GlobalStyle;
