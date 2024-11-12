import { memo, useEffect, useState } from "react";
import styled from "styled-components";
import { ConfigProvider, Space, Table, Tag } from "antd";
import { Calendar, theme } from "antd";
import { useCookies } from "react-cookie";
import { API } from "@Const";

const HomePageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const HomePage = () => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;

  return <HomePageStyle></HomePageStyle>;
};

export default memo(HomePage);
