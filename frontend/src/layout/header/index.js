import { memo, useEffect, useState } from "react";
import styled from "styled-components";
import theme from "../../styles/pages/theme";
import { Cookies, useCookies } from "react-cookie";
import { DIALOGS } from "@Const";
import ProfileMenu from "@Components/ProfileMenu/ProfileMenu";
import LoginDialog from "@Components/dialogs/LoginDialog/LoginDialog";
import { ROUTERS } from "@Utils/router";
import { Link } from "react-router-dom";

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

const Header = () => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;

  const [userMenuItems, setUserMenuItems] = useState([
    {
      name: "Quản lý yêu cầu",
      path: ROUTERS.USER.REQUEST,
    },
  ]);

  const [patientMenuItems, setPatientMenuItems] = useState([
    {
      name: "Đặt lịch khám",
      path: ROUTERS.PATIENT.APPOINTMENT,
    },
    {
      name: "Quản lý hồ sơ y tế",
      path: ROUTERS.PATIENT.MEDICAL_RECORD_MANAGEMENT_PAGE,
    },
  ]);

  const [doctorMenuItems, setDoctorMenuItems] = useState([
    {
      name: "Quản lý bệnh nhân được ủy quyền",
      path: ROUTERS.DOCTOR.AUTHORIZED_PATIENT_MANAGEMENT_PAGE,
    },
  ]);

  const [menuItems, setMenuItems] = useState(null);

  useEffect(() => {
    if (!menuItems) {
      let items = [];

      if (role === "Bệnh nhân") {
        patientMenuItems.map((item, key) => {
          items.push(item);
        });
      }

      if (role === "Bác sĩ") {
        doctorMenuItems.map((item, key) => {
          items.push(item);
        });
      }

      userMenuItems.map((item, key) => {
        items.push(item);
      });
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

  console.log(openDialog);
  return (
    <HeaderStyle>
      <div className="header-top">
        <div className="header-container">
          <div className="row">
            <div className="col-3 header-top-left">
              <div className="col">
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
              <div className="col">
                <Link to={ROUTERS.USER.HOME} className="link">
                  Medical Data Sharing
                </Link>
              </div>
              <div className="col">
                <ul style={{ display: "flex" }}>
                  {menuItems?.map((menu, index) => (
                    <li key={index}>
                      <Link to={menu?.path} className="link">
                        {menu?.name}
                      </Link>
                    </li>
                  ))}
                </ul>
              </div>
            </div>

            <div className="col-6 header-top-right">
              <ProfileMenu openModal={openModal} />
            </div>
          </div>
        </div>
      </div>

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

export default memo(Header);
