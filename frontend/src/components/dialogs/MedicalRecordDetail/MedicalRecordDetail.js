import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import {
  Button,
  Modal,
  Checkbox,
  Form,
  Input,
  Select,
  List,
  Typography,
} from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
import PrescriptionDetail from "../PrescriptionDetail/PrescriptionDetail";
import { Alert, notification } from "antd";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
import { GATEWAY_IPFS } from "../../../utils/const";
const { Option } = Select;

const MedicalRecordDetailStyle = styled.div`
  overflow: auto;
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

const StyledList = styled(List)`
  .ant-list-items > .ant-list-item:nth-child(odd) {
    background-color: rgb(246, 255, 237);
  }
  .ant-list-items > .ant-list-item:nth-child(even) {
    background-color: rgb(230, 230, 230);
  }
`;

const Context = React.createContext({
  name: "MedicalRecordDetail",
});

const MedicalRecordDetail = ({ medicalRecord, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiGetMedicalRecordByMedicalRecordId =
    API.PATIENT.GET_MEDICAL_RECORD_BY_MEDICAL_RECORD_ID;

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

  const handleSwitchToOtherDialog = (dialogName) => {
    onSwitch(dialogName);
  };
  
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const fetchGetMedicalRecordByMedicalRecordId = async () => {
    if (access_token) {
      const formData = new FormData();
      formData.append("medicalRecordId", medicalRecord.medicalRecordId);

      try {
        const response = await fetch(apiGetMedicalRecordByMedicalRecordId, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
          console.log(data);
          setLoading(false);
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetMedicalRecordByMedicalRecordId().then((r) => {});
  }, [access_token]);

  console.log(userId);

  const openPrescriptionDetail = (patientId) => {
    console.log("openPrescriptionDetail");
    openModal(DIALOGS.PRESCRIPTION_DETAIL);
  };

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

  let apiDefineMedicalRecord = API.PATIENT.DEFINE_MEDICAL_RECORD;

  if (role === "Bệnh nhân") {
    apiDefineMedicalRecord = API.PATIENT.DEFINE_MEDICAL_RECORD;
  }
  if (role === "Bác sĩ") {
    apiDefineMedicalRecord = API.DOCTOR.DEFINE_MEDICAL_RECORD;
  }

  const defineMedicalRecord = async (medicalRecordStatus) => {
    if (access_token) {
      const formData = new FormData();
      formData.append("medicalRecordId", medicalRecord.medicalRecordId);
      formData.append("medicalRecordStatus", medicalRecordStatus);

      console.log(access_token);

      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );
      try {
        console.log("***");
        const response = await fetch(apiDefineMedicalRecord, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          let json = await response.json();
          console.log("json: ", json);
          openNotification(
            "topRight",
            "success",
            "Thành công",
            "Đã tạo yêu cầu thành công!",
            handleCancel
          );
        }
      } catch (e) {
        console.log(e);
        openNotification(
          "topRight",
          "error",
          "Thất bại",
          "Đã có lỗi xảy ra khi tạo yêu cầu!",
          handleCancel
        );
      }
    }
  };

  return (
    <Context.Provider value={"Chi tiết hồ sơ y tế"}>
      {contextHolder}
      <MedicalRecordDetailStyle>
        <ModalWrapper
          title="Chi tiết hồ sơ y tế"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          loading={loading}
        >
          <StyledList
            bordered
            dataSource={data}
            renderItem={(item) => (
              <List.Item>
                <div style={{ width: "100%" }}>
                  <Info>
                    <div className="field">ID hồ sơ y tế</div>
                    <div>{item.medicalRecordId}</div>
                  </Info>

                  <Info>
                    <div className="field">ID bệnh nhân</div>
                    <div>{item.patientId}</div>
                  </Info>

                  <Info>
                    <div className="field">Tên bệnh nhân</div>
                    <div>{item.patientName}</div>
                  </Info>

                  <Info>
                    <div className="field">ID bác sĩ</div>
                    <div>{item.doctorId}</div>
                  </Info>

                  <Info>
                    <div className="field">Tên bác sĩ</div>
                    <div>{item.patientName}</div>
                  </Info>

                  <Info>
                    <div className="field">ID bệnh viện</div>
                    <div>{item.medicalInstitutionId}</div>
                  </Info>

                  <Info>
                    <div className="field">Tên bệnh viện</div>
                    <div>{item.medicalInstitutionName}</div>
                  </Info>

                  <Info>
                    <div className="field">Tên xét nghiệm</div>
                    <div>{item.testName}</div>
                  </Info>

                  <Info>
                    <div className="field">Chi tiết xét nghiệm</div>
                    <div>{item.details}</div>
                  </Info>

                  {item.hashFile && (
                    <Info>
                      <div className="field">File</div>
                      <a href={GATEWAY_IPFS + item.hashFile} target="_blank">
                        {item.hashFile}
                      </a>
                    </Info>
                  )}

                  <Info>
                    <div className="field">ID đơn thuốc</div>
                    <div>{item.prescriptionId}</div>
                  </Info>

                  <Info>
                    <div className="field">Ngày tạo</div>
                    <div>{item.dateCreated}</div>
                  </Info>

                  <Info>
                    <div className="field">Ngày chỉnh sửa</div>
                    <div>{item.dateModified}</div>
                  </Info>

                  <Info>
                    <div className="field">Trạng thái hồ sơ y tế</div>
                    <div>{item.medicalRecordStatus}</div>
                  </Info>
                </div>
              </List.Item>
            )}
          />

          <div
            style={{
              display: "flex",
              justifyContent: "center",
              justifyItems: "center",
              marginTop: "1%",
            }}
          >
            {medicalRecord.patientId === userId && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => openPrescriptionDetail()}
                >
                  Xem đơn thuốc
                </Button>
              </>
            )}

            {medicalRecord.patientId === userId &&
              medicalRecord.medicalRecordStatus === "Chờ xử lý" && (
                <>
                  <Button
                    style={{ marginRight: "3%" }}
                    onClick={() => defineMedicalRecord("Đồng ý")}
                  >
                    Đồng ý
                  </Button>
                </>
              )}

            {medicalRecord.patientId === userId &&
              medicalRecord.medicalRecordStatus === "Chờ xử lý" && (
                <>
                  <Button
                    style={{ marginRight: "3%" }}
                    onClick={() => defineMedicalRecord("Từ chối")}
                  >
                    Từ chối
                  </Button>
                </>
              )}

            {openDialog === DIALOGS.PRESCRIPTION_DETAIL && (
              <div>
                <PrescriptionDetail
                  prescriptionId={medicalRecord.prescriptionId}
                  onClose={handleDialogClose}
                  onSwitch={handleDialogSwitch}
                />
              </div>
            )}
          </div>
        </ModalWrapper>
      </MedicalRecordDetailStyle>
    </Context.Provider>
  );
};

export default MedicalRecordDetail;
