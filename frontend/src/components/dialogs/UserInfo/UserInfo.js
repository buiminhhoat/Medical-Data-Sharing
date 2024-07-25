import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Popover, QRCode, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import { Alert, notification } from "antd";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select, Image } from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
import MedicalRecordList from "../MedicalRecordList/MedicalRecordList";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
import PrescriptionDetail from "../PrescriptionDetail/PrescriptionDetail";
import SellingPrescriptionDrug from "../SellingPrescriptionDrug/SellingPrescriptionDrug";
import ConfirmModal from "../ConfirmModal/ConfirmModal";
import { ScanOutlined } from "@ant-design/icons";
import TextWithQRCode from "../../TextWithQRCode/TextWithQRCode";
const { Option } = Select;

const UserInfoStyle = styled.div``;

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
  name: "UserInfo",
});

const UserInfo = ({ user, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState("");
  const [loading, setLoading] = useState(true);

  let apiGetUserInfo = API.ADMIN.GET_USER_INFO;

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const fetchGetUserInfo = async () => {
    if (access_token) {
      console.log("id: ", user.id);
      const formData = new FormData();
      formData.append("id", user.id);

      console.log(access_token);

      try {
        console.log("***");
        const response = await fetch(apiGetUserInfo, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetUserInfo().then((r) => {});
  }, [access_token]);

  const [additionalFields, setAdditionalFields] = useState(null);

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

  //   const defineRequest = async () => {
  //     if (access_token) {
  //       setIsConfirmModalOpen(false);
  //       setDisabledButton(true);

  //       const userStatus = userStatusConfirm;

  //       const formData = new FormData();
  //       formData.append("id", user.id);
  //       formData.append("userType", user.userType);
  //       formData.append("userStatus", userStatus);

  //       console.log(access_token);

  //       openNotification(
  //         "topRight",
  //         "info",
  //         "Đã gửi yêu cầu",
  //         "Hệ thống đã tiếp nhận yêu cầu!"
  //       );
  //       try {
  //         console.log("***");
  //         const response = await fetch(apiDefineRequest, {
  //           method: "POST",
  //           headers: {
  //             Authorization: `Bearer ${access_token}`,
  //           },
  //           body: formData,
  //         });

  //         if (response.status === 200) {
  //           let json = await response.json();
  //           console.log("json: ", json);
  //           openNotification(
  //             "topRight",
  //             "success",
  //             "Thành công",
  //             "Đã tạo yêu cầu thành công!",
  //             handleCancel
  //           );
  //         }
  //       } catch (e) {
  //         console.log(e);
  //         openNotification(
  //           "topRight",
  //           "error",
  //           "Thất bại",
  //           "Đã có lỗi xảy ra khi tạo yêu cầu!",
  //           handleCancel
  //         );
  //       }
  //     }
  //   };

  const renderButton = () => {
    // if (data.userType === "Đặt lịch khám") {
    //   if (data.senderId === userId) {
    //     return (
    //       <>
    //         <Button
    //           style={{ marginRight: "3%" }}
    //           onClick={() => openMedicalRecord(data.senderId)}
    //           disabled={disabledButton}
    //         >
    //           Xem hồ sơ y tế
    //         </Button>
    //         {(data.userStatus === "Chờ xử lý" ||
    //           data.userStatus === "Chấp thuận") && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Thu hồi")}
    //               disabled={disabledButton}
    //             >
    //               Thu hồi
    //             </Button>
    //           </>
    //         )}
    //       </>
    //     );
    //   }
    //   if (data.email === userId) {
    //     return (
    //       <>
    //         {data.userStatus === "Chờ xử lý" && (
    //           <Button
    //             style={{ marginRight: "3%" }}
    //             onClick={() => handleConfirm("Chấp thuận")}
    //             disabled={disabledButton}
    //           >
    //             Chấp thuận
    //           </Button>
    //         )}
    //         {(data.userStatus === "Chờ xử lý" ||
    //           data.userStatus === "Chấp thuận") && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Từ chối")}
    //               disabled={disabledButton}
    //             >
    //               Từ chối
    //             </Button>
    //           </>
    //         )}
    //         {data.userStatus !== "Đồng ý" && (
    //           <>
    //             <Button
    //               onClick={() => createMedicalRecord(data.id)}
    //               disabled={disabledButton}
    //             >
    //               Tạo hồ sơ y tế
    //             </Button>
    //           </>
    //         )}
    //       </>
    //     );
    //   }
    // }
    // if (data.userType === "Xem hồ sơ y tế") {
    //   if (data.senderId === userId) {
    //     return (
    //       <>
    //         {data.userStatus === "Đồng ý" && (
    //           <Button
    //             style={{ marginRight: "3%" }}
    //             onClick={() => openMedicalRecord(data.email)}
    //             disabled={disabledButton}
    //           >
    //             Xem hồ sơ y tế
    //           </Button>
    //         )}
    //         {(data.userStatus === "Chờ xử lý" ||
    //           data.userStatus === "Chấp thuận") && (
    //           <>
    //             <Button style={{ marginRight: "3%" }} disabled={disabledButton}>
    //               Thu hồi
    //             </Button>
    //           </>
    //         )}
    //       </>
    //     );
    //   }
    //   if (data.email === userId) {
    //     return (
    //       <>
    //         <Button
    //           style={{ marginRight: "3%" }}
    //           onClick={() => openMedicalRecord(data.email)}
    //           disabled={disabledButton}
    //         >
    //           Xem hồ sơ y tế
    //         </Button>
    //         {data.userStatus === "Chờ xử lý" && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Đồng ý")}
    //               disabled={disabledButton}
    //             >
    //               Đồng ý
    //             </Button>
    //           </>
    //         )}
    //         {data.userStatus === "Chờ xử lý" && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Từ chối")}
    //               disabled={disabledButton}
    //             >
    //               Từ chối
    //             </Button>
    //           </>
    //         )}
    //         {data.userStatus === "Đồng ý" && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Thu hồi")}
    //               disabled={disabledButton}
    //             >
    //               Thu hồi
    //             </Button>
    //           </>
    //         )}
    //       </>
    //     );
    //   }
    // }
    // if (data.userType === "Xem đơn thuốc") {
    //   if (data.senderId === userId) {
    //     return (
    //       <>
    //         {data.userStatus === "Đồng ý" && (
    //           <Button
    //             style={{ marginRight: "3%" }}
    //             onClick={() => openPrescriptionDetail(data.prescriptionId)}
    //             disabled={disabledButton}
    //           >
    //             Xem đơn thuốc
    //           </Button>
    //         )}
    //         {(data.userStatus === "Chờ xử lý" ||
    //           data.userStatus === "Chấp thuận") && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Thu hồi")}
    //               disabled={disabledButton}
    //             >
    //               Thu hồi
    //             </Button>
    //           </>
    //         )}
    //         {data.senderId === userId && role === "Cửa hàng thuốc" && (
    //           <Button
    //             style={{ marginRight: "3%" }}
    //             onClick={() => openSellingPrescriptionDrug(data.prescriptionId)}
    //             disabled={disabledButton}
    //           >
    //             Bán thuốc
    //           </Button>
    //         )}
    //       </>
    //     );
    //   }
    //   if (data.email === userId) {
    //     return (
    //       <>
    //         <Button
    //           style={{ marginRight: "3%" }}
    //           onClick={() => openPrescriptionDetail(data.prescriptionId)}
    //           disabled={disabledButton}
    //         >
    //           Xem đơn thuốc
    //         </Button>
    //         {data.userStatus === "Chờ xử lý" && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Đồng ý")}
    //               disabled={disabledButton}
    //             >
    //               Đồng ý
    //             </Button>
    //           </>
    //         )}
    //         {data.userStatus === "Chờ xử lý" && (
    //           <>
    //             <Button
    //               style={{ marginRight: "3%" }}
    //               onClick={() => handleConfirm("Từ chối")}
    //               disabled={disabledButton}
    //             >
    //               Từ chối
    //             </Button>
    //           </>
    //         )}
    //       </>
    //     );
    //   }
    // }
  };

  useEffect(() => {
    if (data) {
      console.log(data);
      setLoading(false);
      setAdditionalFields(renderButton);
    }
  }, [data]);

  useEffect(() => {
    setAdditionalFields(renderButton);
  }, [disabledButton]);

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

  //   const openMedicalRecord = (patientId) => {
  //     console.log("openMedicalRecord");
  //     console.log(patientId);
  //     openModal(DIALOGS.MEDICAL_RECORD);
  //     setPatientId(patientId);
  //   };

  return (
    <Context.Provider value={"Thông tin người dùng"}>
      {contextHolder}
      <UserInfoStyle>
        <Modal
          title="Thông tin người dùng"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"55%"}
          loading={loading}
        >
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
              src="https://i.pinimg.com/originals/60/07/0e/60070ed889df308cbe80253e8c36b3a3.jpg"
            />
          </div>
          <div style={{ marginTop: "20px", marginLeft: "20px" }}>
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
          </div>

          <div
            style={{
              display: "flex",
              justifyContent: "center",
              justifyItems: "center",
            }}
          >
            {additionalFields}
          </div>
        </Modal>

        {/* {openDialog === DIALOGS.MEDICAL_RECORD && (
          <div>
            <MedicalRecordList
              patientId={patientId}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )} */}

        {/* <ConfirmModal
          isOpen={isConfirmModalOpen}
          handleOk={defineRequest}
          handleCancel={handleConfirmModalCancel}
          title="Xác nhận"
          content="Bạn có chắc chắn không?"
        /> */}
      </UserInfoStyle>
    </Context.Provider>
  );
};

export default UserInfo;
