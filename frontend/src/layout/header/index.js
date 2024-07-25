import { memo, useEffect, useState } from "react";
import styled from "styled-components";
import theme from "../../styles/pages/theme";
import { Cookies, useCookies } from "react-cookie";
import { DIALOGS } from "@Const";
import ProfileMenu from "@Components/ProfileMenu/ProfileMenu";
import LoginDialog from "@Components/dialogs/LoginDialog/LoginDialog";
import { ROUTERS } from "@Utils/router";
import { Link, useLocation, useNavigate } from "react-router-dom";

import { ConfigProvider, Layout, Menu } from "antd";

const { Header } = Layout;

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

    .col {
      margin-right: 25px;
      ul {
        column-gap: 20px;
        list-style-type: none;
      }
    }
  }

  .header-container {
    max-width: 85%;
    width: 100%;
    margin: auto;
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

  .link {
    color: white;
    width: 100%;
    display: flex;
    justify-content: center;
    justify-items: center;
    align-items: center;
    align-content: center;
  }
`;

const HeaderLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const [current, setCurrent] = useState(null);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;

  const [patientMenuItems, setPatientMenuItems] = useState([
    {
      name: "Đặt lịch khám",
      path: ROUTERS.PATIENT.APPOINTMENT,
    },
    {
      name: "Quản lý hồ sơ y tế",
      path: ROUTERS.PATIENT.MEDICAL_RECORD_MANAGEMENT_PAGE,
    },
    {
      name: "Lịch sử giao dịch",
      path: ROUTERS.PATIENT.HISTORY_PURCHASE,
    },
    {
      name: "Quản lý yêu cầu",
      path: ROUTERS.USER.REQUEST,
    },
  ]);

  const [doctorMenuItems, setDoctorMenuItems] = useState([
    {
      name: "Quản lý bệnh nhân",
      path: ROUTERS.DOCTOR.PATIENT_MANAGED_BY_DOCTOR_PAGE,
    },
    {
      name: "Quản lý yêu cầu",
      path: ROUTERS.USER.REQUEST,
    },
  ]);

  const [adminMenuItems, setAdminMenuItems] = useState([
    {
      name: "Quản lý người dùng",
      path: ROUTERS.ADMIN.USER_MANAGEMENT,
    },
  ]);

  const [medicalInstitutionMenuItems, setMedicalInstitutionMenuItems] =
    useState([
      {
        name: "Quản lý bác sĩ",
        path: ROUTERS.MEDICAL_INSTITUTION.DOCTOR_MANAGEMENT_PAGE,
      },
    ]);

  const [manufacturerMenuItems, setManufacturerMenuItems] = useState([
    {
      name: "Quản lý loại thuốc",
      path: ROUTERS.MANUFACTURER.MEDICATION_MANAGEMENT_PAGE,
    },
    {
      name: "Quản lý thuốc",
      path: ROUTERS.MANUFACTURER.DRUG_MANAGEMENT_PAGE,
    },
  ]);

  const [drugStoreMenuItems, setDrugStoreItems] = useState([
    {
      name: "Lịch sử giao dịch",
      path: ROUTERS.DRUGSTORE.HISTORY_PURCHASE,
    },
    {
      name: "Quản lý yêu cầu",
      path: ROUTERS.USER.REQUEST,
    },
  ]);

  const [menuItems, setMenuItems] = useState(null);

  useEffect(() => {
    setCurrent(location.pathname);

    if (!menuItems) {
      let items = [];

      if (role === "Bệnh nhân") {
        patientMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (role === "Bác sĩ") {
        doctorMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (role === "Cửa hàng thuốc") {
        drugStoreMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (role === "Quản trị viên") {
        adminMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (role === "Công ty sản xuất thuốc") {
        manufacturerMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (role === "Cơ sở y tế") {
        medicalInstitutionMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      setMenuItems(items);
    }
  });

  const [openDialog, setOpenDialog] = useState(null);

  const handleDialogSwitch = (dialogName) => {
    openModal(dialogName);
  };

  const handleDialogClose = () => {
    closeModal();
  };

  const openModal = (dialogName) => {
    setOpenDialog(dialogName);
  };

  const closeModal = () => {
    setOpenDialog(null);
  };

  const onClick = (e) => {
    navigate(e.key);
    setCurrent(e.key);
  };

  console.log(openDialog);
  return (
    <HeaderStyle>
      <ConfigProvider
        theme={{
          components: {
            Layout: {
              headerBg: "#285430",
            },
            Menu: {
              darkItemBg: "#285430",
              itemBg: "#285430",
              darkPopupBg: "#285430",
              darkItemSelectedBg: "#224728",
            },
          },
        }}
      >
        <Layout>
          <Header
            style={{
              display: "flex",
              alignItems: "center",
              paddingLeft: "9%",
              paddingRight: "9%",
            }}
          >
            <div
              style={{
                marginRight: "40px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <div
                style={{
                  minWidth: "28px",
                  minHeight: "28px",
                  display: "flex",
                  marginRight: "15px",
                  justifyItems: "center",
                  justifyContent: "center",
                }}
              >
                <svg
                  width="27.2"
                  height="27.2"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    opacity="0.8"
                    d="M12 0V24"
                    stroke="#F4FFF3"
                    style={{ strokeWidth: 8, strokeMiterlimit: 10 }}
                  />
                  <path
                    opacity="0.6"
                    d="M0 12H24"
                    stroke="#F4FFF3"
                    style={{ strokeWidth: 8, strokeMiterlimit: 10 }}
                  />
                </svg>
              </div>
              <div>
                <Link
                  to={ROUTERS.USER.HOME}
                  className="link"
                  style={{ fontSize: "18px" }}
                >
                  Medical Data Sharing
                </Link>
              </div>
            </div>

            <Menu
              theme="dark"
              mode="horizontal"
              onClick={onClick}
              selectedKeys={[current]}
              items={menuItems}
              style={{ flex: 1, minWidth: 0 }}
            />

            <div
              style={{ marginLeft: "30px", color: "white", lineHeight: "25px" }}
            >
              <ProfileMenu openModal={openModal} />
            </div>
          </Header>
        </Layout>
      </ConfigProvider>

      {openDialog === DIALOGS.LOGIN && (
        <div className="modal-overlay">
          <LoginDialog
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </HeaderStyle>
  );
};

export default memo(HeaderLayout);
