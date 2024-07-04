import { memo } from "react";
import styled from "styled-components";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import theme from "../../styles/pages/theme";

const HeaderStyle = styled.div`
  font-weight: 600;
  color: white;
  .header-top {
    background: ${theme.colors.green_dark};
    padding: 10px;
  }

  .header-top-left {
    display: flex;
    text-align: left;
    justify-content: left;
    align-items: center;
    ul {
      column-gap: 50px;
      list-style-type: none;
    }
  }

  .header-top-right {
    display: grid;
    justify-content: end;
    ul {
      column-gap: 50px;
      list-style-type: none;
    }
  }

  .info {
    font-size: 15px;
    display: flex;
  }
`;
const Header = () => {
  return (
    <HeaderStyle>
      <div className="header-top">
        <div className="container">
          <div className="row">
            <div className="col-6 header-top-left">
              <div style={{ marginRight: 20 }}>
                <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    opacity="0.8"
                    d="M12 0V24"
                    stroke="#F4FFF3"
                    stroke-width="8"
                    stroke-miterlimit="10"
                  />
                  <path
                    opacity="0.6"
                    d="M0 12H24"
                    stroke="#F4FFF3"
                    stroke-width="8"
                    stroke-miterlimit="10"
                  />
                </svg>
              </div>
              <div>Medical Data Sharing</div>
            </div>
            <div className="col-6 header-top-right">
              <div className="info">
                <div>
                  <ul style={{ marginRight: 20, textAlign: "right" }}>
                    <li>Bùi Minh Hoạt</li>
                    <li style={{ fontFamily: "Poppins", fontWeight: 300 }}>
                      Admin
                    </li>
                  </ul>
                </div>

                <div>
                  <Space direction="vertical" size={16}>
                    <Space wrap size={16}>
                      <Avatar size={45} icon={<UserOutlined />} />
                    </Space>
                  </Space>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </HeaderStyle>
  );
};

export default memo(Header);
