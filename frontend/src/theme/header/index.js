import { memo, useState } from "react";
import styled from "styled-components";
import theme from "../../styles/pages/theme";
import { useCookies } from "react-cookie";
import { DIALOGS } from "@Const";
import ProfileMenu from "@Components/ProfileMenu/ProfileMenu";
import LoginDialog from "@Components/dialogs/LoginDialog/LoginDialog";

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

  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5); /* Màu nền đục */
    z-index: 1000; /* Đảm bảo nằm trên các phần tử khác */
    display: flex;
    justify-content: center;
    align-items: center;
  }
`;

const Header = () => {
  const [menuItems, setMenuItems] = useState([{}]);

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

  const [cookies] = useCookies(["access_token"]);

  console.log(openDialog);
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
              <div>Medical Data Sharing</div>
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
