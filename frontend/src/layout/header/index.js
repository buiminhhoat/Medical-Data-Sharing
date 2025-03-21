import { memo, useEffect, useState } from "react";
import styled from "styled-components";
import theme from "../../styles/pages/theme";
import Storage from '@Utils/Storage';
import { DIALOGS } from "@Const";
import ProfileMenu from "@Components/ProfileMenu/ProfileMenu";
import LoginDialog from "@Components/dialogs/LoginDialog/LoginDialog";
import { ROUTERS } from "@Utils/router";
import { Link, useLocation, useNavigate } from "react-router-dom";

import { ConfigProvider, Layout, Menu } from "antd";
import RegisterUserDialog from "../../components/dialogs/RegisterUser/RegisterUser";
import RegisterDialog from "../../components/dialogs/RegisterDialog/RegisterDialog";

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
  const { access_token, userId, role } = Storage.getData();
  const [current, setCurrent] = useState(null);
  // 
  // 
  // 

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
      name: "Danh sách thuốc",
      path: ROUTERS.PATIENT.DRUG_MANAGEMENT_PAGE,
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

  // todo
  const [medicalInstitutionMenuItems, setMedicalInstitutionMenuItems] =
    useState([
      {
        name: "Quản lý bác sĩ",
        path: ROUTERS.MEDICAL_INSTITUTION.DOCTOR_MANAGEMENT_PAGE,
      },
    ]);

  const [researchCenterMenuItems, setResearchCenterMenuItems] = useState([
    {
      name: "Quản lý nhà khoa học",
      path: ROUTERS.RESEARCH_CENTER.SCIENTIST_MANAGEMENT_PAGE,
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
    {
      name: "Quản lý phản ứng thuốc",
      path: ROUTERS.MANUFACTURER.DRUG_REACTION_MANAGEMENT_PAGE,
    },
    {
      name: "Quản lý bệnh nhân",
      path: ROUTERS.MANUFACTURER.PATIENT_MANAGED_BY_MANUFACTURER_PAGE,
    },
    {
      name: "Quản lý yêu cầu",
      path: ROUTERS.USER.REQUEST,
    },
  ]);

  const [scientistMenuItems, setScientistMenuItems] = useState([
    {
      name: "Quản lý bệnh nhân",
      path: ROUTERS.SCIENTIST.PATIENT_MANAGED_BY_SCIENTIST_PAGE,
    },
    {
      name: "Quản lý yêu cầu",
      path: ROUTERS.USER.REQUEST,
    },
  ]);

  const [drugStoreMenuItems, setDrugStoreItems] = useState([
    {
      name: "Kho thuốc",
      path: ROUTERS.DRUGSTORE.DRUG_MANAGEMENT_PAGE,
    },
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

      if (Storage.getItem("role") === "Bệnh nhân") {
        patientMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Bác sĩ") {
        doctorMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Nhà thuốc") {
        drugStoreMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Quản trị viên") {
        adminMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Công ty sản xuất thuốc") {
        manufacturerMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Cơ sở y tế") {
        medicalInstitutionMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Trung tâm nghiên cứu") {
        researchCenterMenuItems.map((item, key) => {
          items.push({
            key: item.path,
            label: item.name,
          });
        });
      }

      if (Storage.getItem("role") === "Nhà khoa học") {
        scientistMenuItems.map((item, key) => {
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
              <ProfileMenu openModal={openModal} setMenuItems={setMenuItems} />
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

      {openDialog === DIALOGS.REGISTER && (
        <div className="modal-overlay">
          <RegisterDialog
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </HeaderStyle>
  );
};

export default memo(HeaderLayout);
