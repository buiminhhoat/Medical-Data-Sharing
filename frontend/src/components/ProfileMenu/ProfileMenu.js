import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { API, DIALOGS } from "@Const";
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
  const [fullName, setFullName] = useState("");
  const [role, setRole] = useState("");
  const apiGetUserData = API.PUBLIC.GET_USER_DATA;
  const fetchUserData = async () => {
    if (access_token) {
      try {
        const response = await fetch(
            apiGetUserData,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${access_token}`,
            },
          }
        );
        console.log(response);

        if (response.status === 200) {
          const json = await response.json();
          console.log(json);
          const fullName = json.firstName + " " + json.lastName;
          const role = json.role;
          setFullName(fullName);
          setRole(role);
        }
      } catch (e) {}
    }
  };

  useEffect(() => {
    if (access_token) fetchUserData().then((r) => {});
  }, [access_token]);

  return (
    <ProfileMenuStyle>
      <div className="info">
        <div>
          <ul style={{ marginRight: 20, textAlign: "right" }}>
            <li>{fullName}</li>
            <li style={{ fontFamily: "Poppins", fontWeight: 300 }}>{role}</li>
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
