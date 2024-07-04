import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { DIALOGS } from "@Const";
import styled from "styled-components";
import theme from "../../../styles/pages/theme";

const LoginDialogStyle = styled.div``;
const LoginDialog = ({ onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;

  return (
    <LoginDialogStyle>
      <div>Login</div>
    </LoginDialogStyle>
  );
};

export default LoginDialog;
