import { memo } from "react";
import Header from "@Theme/header";
import Footer from "@Theme/footer";
import styled from "styled-components";

const MasterLayoutStyle = styled.div`
  .div_block {
    width: 100%;
    height: 300px;
  }

  @keyframes showBtn {
    from {
      opacity: 0.4;
    }
    to {
      opacity: 1;
    }
  }

  #btn-back-to-top.showBtn {
    animation: showBtn 0.5s ease-in-out !important;
  }

  #btn-back-to-top:hover {
    background-color: #a80000 !important;
  }

  #btn-back-to-top.hideBtn {
    opacity: 0;
  }
`;

const MasterLayout = ({ children, ...props }) => {
  return (
    <MasterLayoutStyle>
      <div {...props}>
        <Header />
        {children}
        <Footer />
      </div>
    </MasterLayoutStyle>
  );
};

export default memo(MasterLayout);
