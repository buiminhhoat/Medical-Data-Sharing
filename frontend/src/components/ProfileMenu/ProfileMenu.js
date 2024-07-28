import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Alert, Avatar, Dropdown, Popover, Space } from "antd";
import { API, DIALOGS } from "@Const";
import styled from "styled-components";
import theme from "../../styles/pages/theme";
import { useNavigate } from "react-router-dom";
import { useLogout } from "../../utils/logout";
import { notification } from "antd";

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
    cursor: pointer;
  }
`;

const Context = React.createContext({
  name: "ProfileMenuContext",
});

const ProfileMenu = ({ setMenuItems, openModal }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;
  const [fullName, setFullName] = useState("");
  const [role, setRole] = useState("");
  const apiGetUserData = API.PUBLIC.GET_USER_DATA;
  const fetchUserData = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetUserData, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        });
        console.log(response);

        if (response.status === 200) {
          const json = await response.json();
          console.log(json);
          const fullName = json.fullName;
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

  const [api, contextHolder] = notification.useNotification();
  const openNotification = (placement, type, message, description, onClose) => {
    api[type]({
      message: message,
      description: description,
      placement,
      showProgress: true,
      pauseOnHover: true,
      onClose: onClose,
    });
  };

  const logout = useLogout(api, contextHolder);

  const handleLogout = () => {
    logout()
      .then(() => {
        setMenuItems(null);
      })
      .catch((error) => {});
  };

  const renderHasAccessToken = () => {
    const items = [
      {
        key: "1",
        label: <a onClick={handleLogout}>Đăng xuất</a>,
      },
    ];

    return (
      <Dropdown
        menu={{
          items,
        }}
        placement="bottom"
      >
        <div className="info">
          <div>
            <ul style={{ marginRight: 20, textAlign: "right" }}>
              <li>{fullName}</li>
              <li style={{ fontFamily: "Poppins", fontWeight: 300 }}>{role}</li>
            </ul>
          </div>

          <a href="/profile">
            <div className="pointer-cursor">
              <Space direction="vertical" size={16}>
                <Space wrap size={16}>
                  <Avatar size={45} icon={<UserOutlined />} />
                </Space>
              </Space>
            </div>
          </a>
        </div>
      </Dropdown>
    );
  };

  const renderNoAccessToken = () => {
    return (
      <div className="info">
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
    );
  };

  return (
    <Context.Provider value={"Profile Menu"}>
      {contextHolder}
      <ProfileMenuStyle>
        {access_token ? renderHasAccessToken() : renderNoAccessToken()}
      </ProfileMenuStyle>
    </Context.Provider>
  );
};

export default ProfileMenu;
