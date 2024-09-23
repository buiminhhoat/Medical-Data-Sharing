import { memo } from "react";
import HeaderLayout from "../header";
import Footer from "../footer";
import styled from "styled-components";

const MasterLayoutStyle = styled.div`
  width: 100%;
  height: 100%;

  .main {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
  }
`;

const MasterLayout = ({ children, ...props }) => {
  return (
    <MasterLayoutStyle>
      <div className="main" {...props}>
        <HeaderLayout />
        {children}
        <Footer />
      </div>
    </MasterLayoutStyle>
  );
};

export default memo(MasterLayout);
