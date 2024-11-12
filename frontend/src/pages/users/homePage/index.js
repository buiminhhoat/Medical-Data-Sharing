import { memo, useEffect, useState } from "react";
import styled from "styled-components";
import { ConfigProvider, Space, Table, Tag } from "antd";
import { Calendar, theme } from "antd";
import Storage from '@Utils/Storage';
import { API } from "@Const";

const HomePageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const HomePage = () => {
  const { access_token, userId, role } = Storage.getData();
  

  return <HomePageStyle></HomePageStyle>;
};

export default memo(HomePage);
