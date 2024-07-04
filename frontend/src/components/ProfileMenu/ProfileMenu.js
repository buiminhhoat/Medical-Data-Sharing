import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { DIALOGS } from "@Const";
import styled from "styled-components";
import theme from "../../styles/pages/theme";

const ProfileMenuStyle = styled.div`
  .breadcrumb-wrap {
    font-size: 20px;
    font-weight: 700;
    padding: 20px 0;
  }

  .uppercase-text {
    text-transform: uppercase;
  }

  .pointer-cursor {
    cursor: pointer; /* Thiết lập biểu tượng ngón tay */
  }
`;
const ProfileMenu = ({ openModal }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;

  return (
    <ProfileMenuStyle>
      <div className="info">
        <div>
          <ul style={{ marginRight: 20, textAlign: "right" }}>
            <li>Bùi Minh Hoạt</li>
            <li style={{ fontFamily: "Poppins", fontWeight: 300 }}>Admin</li>
          </ul>
        </div>

        <div
          className="pointer-cursor"
          onClick={() => openModal(DIALOGS.LOGIN)}
        >
          <Space direction="vertical" size={16}>
            <Space wrap size={16}>
              <Avatar size={45} icon={<UserOutlined />} />
            </Space>
          </Space>
        </div>
      </div>
    </ProfileMenuStyle>
  );
};

export default ProfileMenu;
