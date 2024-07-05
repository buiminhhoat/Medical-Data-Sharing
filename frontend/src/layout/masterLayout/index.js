import { memo } from "react";
import Header from "../header";
import Footer from "../footer";
import styled from "styled-components";

const MasterLayoutStyle = styled.div``;

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
