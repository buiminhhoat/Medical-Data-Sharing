import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, QRCode, Space } from "antd";
import { DIALOGS } from "@Const";
import styled from "styled-components";
import theme from "../../../styles/pages/theme";
import { Card, Input, Button, Image, ConfigProvider, Row, Col } from "antd";
import { Alert, notification } from "antd";
import { SearchOutlined, CalendarOutlined } from "@ant-design/icons";
import { API } from "@Const";
import SendRequestDialog from "../../../components/dialogs/SendRequest/SendRequest";
import TextWithQRCode from "../../../components/TextWithQRCode/TextWithQRCode";
import ChangePasswordDialog from "../../../components/dialogs/ChangePasswordDialog/ChangePasswordDialog";
import UpdateInformationDialog from "../../../components/dialogs/UpdateInformationDialog/UpdateInformationDialog";
import { useLogout } from "../../../utils/logout";

const ProfilePageStyle = styled.div`
  .fullName {
    font-size: 21px;
    font-weight: 600;
    color: ${theme.colors.green_dark};
  }
  .department {
    font-size: 16px;
    font-weight: 300;
    color: ${theme.colors.east_bay};
  }
  .medicalInstitutionName {
    font-size: 16px;
    font-weight: 300;
    color: ${theme.colors.east_bay};
  }

  .avatar {
    margin: 0 8% 0 4%;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40%;
  }

  .doctorlist {
    justify-content: center;
    align-items: center;
    display: flex;
    flex-wrap: wrap;
  }

  .doctorinfo {
    width: 100%;
  }
`;

const Info = styled.div`
  display: flex;
  /* justify-content: center; */
  /* justify-items: center; */
  align-items: center;
  margin-bottom: 15px;
  .field {
    width: 20%;
    margin-right: 3%;
  }
`;

const Context = React.createContext({
  name: "ProfilePage",
});

const ProfilePage = () => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;

  const [openDialog, setOpenDialog] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(true);
  let apiGetUserInfo = API.PUBLIC.GET_USER_INFO;

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

  console.log("apiGetUserInfo: ", apiGetUserInfo);

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

  const [data, setData] = useState("");
  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const logout = useLogout(api, contextHolder);

  const handleLogout = () => {
    logout()
      .then(() => {})
      .catch((error) => {});
  };

  const fetchGetUserInfo = async () => {
    if (access_token) {
      console.log("id: ", userId);
      const formData = new FormData();
      formData.append("id", userId);

      console.log(access_token);

      try {
        const response = await fetch(apiGetUserInfo, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
        } else {
          handleLogout();
        }
      } catch (e) {
        console.log(e);
      }
    } else {
      handleLogout();
    }
  };

  useEffect(() => {
    if (access_token) {
      fetchGetUserInfo().then((r) => {});
    } else {
      handleLogout();
    }
  }, [access_token, openDialog]);

  useEffect(() => {
    console.log("data: ", data);
  }, [data]);

  const [additionalFields, setAdditionalFields] = useState(null);

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [disabledButton, setDisabledButton] = useState(false);
  const [userStatusConfirm, setRequestStatusConfirm] = useState("");

  const handleConfirm = (userStatus) => {
    setIsConfirmModalOpen(true);
    setRequestStatusConfirm(userStatus);
  };

  const handleConfirmModalCancel = () => {
    setIsConfirmModalOpen(false);
  };

  useEffect(() => {
    console.log("disabledButton", disabledButton);
  }, [disabledButton]);

  return (
    <Context.Provider value={"Profile Page"}>
      {contextHolder}
      <ProfilePageStyle
        style={{ backgroundColor: "rgb(250, 250, 250)", height: "100%" }}
      >
        <div className="page">
          <div className="container">
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                justifyItems: "center",
                justifySelf: "center",
              }}
            >
              <Card
                title={
                  <h1
                    style={{
                      textAlign: "center",
                    }}
                  >
                    Thông tin cá nhân
                  </h1>
                }
                style={{
                  width: "70%",
                  marginTop: "30px",
                }}
              >
                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    justifyItems: "center",
                  }}
                >
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "center",
                      justifyItems: "center",
                      width: "30%",
                    }}
                  >
                    <div>
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "center",
                          justifyItems: "center",
                        }}
                      >
                        <Image
                          width={200}
                          height={200}
                          style={{ maxWidth: "100%" }}
                          src={
                            data.avatar
                              ? data.avatar
                              : "https://i.pinimg.com/originals/60/07/0e/60070ed889df308cbe80253e8c36b3a3.jpg"
                          }
                        />
                      </div>
                      <h2>
                        <p style={{ textAlign: "center" }}>{data.fullName}</p>
                      </h2>
                    </div>
                  </div>

                  <div
                    style={{
                      marginTop: "20px",
                      marginLeft: "5%",
                      width: "70%",
                    }}
                  >
                    <Info>
                      <div className="field">ID người dùng</div>
                      <TextWithQRCode value={data.id}></TextWithQRCode>
                    </Info>
                    <Info>
                      <div className="field">Tên người dùng</div>
                      <div>{data.fullName}</div>
                    </Info>

                    <Info>
                      <div className="field">Email</div>
                      <TextWithQRCode value={data.email}></TextWithQRCode>
                    </Info>

                    <Info>
                      <div className="field">Địa chỉ</div>
                      <div>{data.address}</div>
                    </Info>

                    {data.dateBirthday && (
                      <Info>
                        <div className="field">Ngày sinh</div>
                        <div>{data.dateBirthday}</div>
                      </Info>
                    )}

                    {data.gender && (
                      <Info>
                        <div className="field">Giới tính</div>
                        <div>{data.gender}</div>
                      </Info>
                    )}

                    <Info>
                      <div className="field">Vai trò</div>
                      <div>{data.role}</div>
                    </Info>

                    <Info>
                      <div className="field">Trạng thái</div>
                      <div>
                        {data.enabled === "true" ? "Đang hoạt động" : "Bị khóa"}
                      </div>
                    </Info>

                    {data.department && (
                      <Info>
                        <div className="field">Chuyên khoa</div>
                        <div>{data.department}</div>
                      </Info>
                    )}

                    {data.medicalInstitutionId && (
                      <Info>
                        <div className="field">ID cơ sở y tế</div>
                        <TextWithQRCode
                          value={data.medicalInstitutionId}
                        ></TextWithQRCode>
                      </Info>
                    )}

                    {data.medicalInstitutionName && (
                      <Info>
                        <div className="field">Tên cơ sở y tế</div>
                        <div>{data.medicalInstitutionName}</div>
                      </Info>
                    )}

                    {data.businessLicenseNumber && (
                      <Info>
                        <div className="field">Giấy phép kinh doanh</div>
                        <div>{data.businessLicenseNumber}</div>
                      </Info>
                    )}

                    <div
                      style={{
                        display: "flex",
                        justifyContent: "center",
                        justifyItems: "center",
                      }}
                    >
                      <Button
                        onClick={() => openModal(DIALOGS.UPDATE_INFORMATION)}
                      >
                        Cập nhật thông tin
                      </Button>
                      <Button
                        style={{ marginLeft: "10%" }}
                        onClick={() => openModal(DIALOGS.CHANGE_PASSWORD)}
                      >
                        Đổi mật khẩu
                      </Button>
                    </div>
                  </div>
                </div>
              </Card>
            </div>
            {openDialog === DIALOGS.CHANGE_PASSWORD && (
              <div className="modal-overlay">
                <ChangePasswordDialog
                  userId={userId}
                  onClose={handleDialogClose}
                  onSwitch={handleDialogSwitch}
                />
              </div>
            )}

            {openDialog === DIALOGS.UPDATE_INFORMATION && (
              <div className="modal-overlay">
                <UpdateInformationDialog
                  userId={userId}
                  onClose={handleDialogClose}
                  onSwitch={handleDialogSwitch}
                />
              </div>
            )}
          </div>
        </div>
      </ProfilePageStyle>
    </Context.Provider>
  );
};

export default ProfilePage;
