import React from "react";
import { Modal } from "antd";
import styled from "styled-components";

// Styled scrollbar container
const ScrollBar = styled.div`
  overflow-y: scroll;
  max-height: 80vh;
  padding-right: 3px;

  &::-webkit-scrollbar {
    width: 5px;
    height: 50px;
  }

  &::-webkit-scrollbar-track {
    background: #fff;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(10, 101, 22, 0.5);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: rgba(10, 101, 22, 0.75);
  }
`;

// Modal wrapper component that applies custom scrollbar to content
const ModalWrapper = ({ children, ...props }) => {
  return (
    <Modal {...props}>
      <ScrollBar>{children}</ScrollBar>
    </Modal>
  );
};

export default ModalWrapper;
