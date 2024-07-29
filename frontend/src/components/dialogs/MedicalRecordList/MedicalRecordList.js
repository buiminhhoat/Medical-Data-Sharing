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
import { Alert, notification } from "antd";
import PrescriptionDetail from "../PrescriptionDetail/PrescriptionDetail";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
import TextWithQRCode from "../../TextWithQRCode/TextWithQRCode";
const { Option } = Select;

const MedicalRecordDialogStyle = styled.div`
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

const ScrollBar = styled.div`
  overflow-y: scroll;
  max-height: 80vh;
  padding-right: 3px;

  &::-webkit-scrollbar {
    width: 10px;
  }

  &::-webkit-scrollbar-track {
    background: #fff;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(10, 101, 22, 0.5);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: rgba(10, 101, 22, 0.75);
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
  name: "MedicalRecordList",
});

const MedicalRecordList = ({ patientId, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiAllMedicalRecord = API.PATIENT.GET_LIST_MEDICAL_RECORD;
  if (role === "Bác sĩ")
    apiAllMedicalRecord = API.DOCTOR.GET_LIST_MEDICAL_RECORD;
  if (role === "Công ty sản xuất thuốc")
    apiAllMedicalRecord = API.MANUFACTURER.GET_LIST_MEDICAL_RECORD;
  if (role === "Nhà khoa học")
    apiAllMedicalRecord = API.SCIENTIST.GET_LIST_MEDICAL_RECORD;

  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const fetchAllMedicalRecord = async () => {
    if (access_token) {
      const formData = new FormData();
      formData.append("patientId", patientId);

      try {
        const response = await fetch(apiAllMedicalRecord, {
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
    if (access_token) fetchAllMedicalRecord().then((r) => {});
  }, [access_token]);

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

  const [selectedPrescriptionId, setSelectedPrescriptionId] = useState("");
  const openPrescriptionDetail = (prescriptionId) => {
    console.log("openPrescriptionDetail");
    console.log("prescriptionId: ", prescriptionId);
    setSelectedPrescriptionId(prescriptionId);
    openModal(DIALOGS.PRESCRIPTION_DETAIL);
  };

  let apiDefineMedicalRecord = API.PATIENT.DEFINE_MEDICAL_RECORD;
  if (role === "Bệnh nhân") {
    apiDefineMedicalRecord = API.PATIENT.DEFINE_MEDICAL_RECORD;
  }
  if (role === "Bác sĩ") {
    apiDefineMedicalRecord = API.DOCTOR.DEFINE_MEDICAL_RECORD;
  }

  const defineMedicalRecord = async (medicalRecordId, medicalRecordStatus) => {
    if (access_token) {
      const formData = new FormData();
      formData.append("medicalRecordId", medicalRecordId);
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

  return (
    <Context.Provider value={"Danh sách hồ sơ y tế của bệnh nhân"}>
      {contextHolder}
      <MedicalRecordDialogStyle>
        <ModalWrapper
          title="Danh sách hồ sơ y tế của bệnh nhân"
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
                    {/* <div>{item.medicalRecordId}</div> */}
                    <TextWithQRCode
                      value={item.medicalRecordId}
                    ></TextWithQRCode>
                  </Info>

                  <Info>
                    <div className="field">ID bệnh nhân</div>
                    {/* <div>{item.patientId}</div> */}
                    <TextWithQRCode value={item.patientId}></TextWithQRCode>
                  </Info>

                  <Info>
                    <div className="field">Tên bệnh nhân</div>
                    <div>{item.patientName}</div>
                  </Info>

                  <Info>
                    <div className="field">ID bác sĩ</div>
                    {/* <div>{item.doctorId}</div> */}
                    <TextWithQRCode value={item.doctorId}></TextWithQRCode>
                  </Info>

                  <Info>
                    <div className="field">Tên bác sĩ</div>
                    <div>{item.doctorName}</div>
                  </Info>

                  <Info>
                    <div className="field">ID bệnh viện</div>
                    {/* <div>{item.medicalInstitutionId}</div> */}
                    <TextWithQRCode
                      value={item.medicalInstitutionId}
                    ></TextWithQRCode>
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

                  <Info>
                    <div className="field">File</div>
                    <div>{item.hashFile}</div>
                  </Info>

                  <Info>
                    <div className="field">ID đơn thuốc</div>
                    {/* <div>{item.prescriptionId}</div> */}
                    <TextWithQRCode
                      value={item.prescriptionId}
                    ></TextWithQRCode>
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

                  <div style={{ display: "flex", justifyContent: "center" }}>
                    <Button
                      style={{ marginRight: "3%" }}
                      onClick={() =>
                        openPrescriptionDetail(item.prescriptionId)
                      }
                    >
                      Xem đơn thuốc
                    </Button>
                    {item.doctorId === userId && (
                      <Button
                        style={{ marginRight: "3%" }}
                        onClick={() =>
                          defineMedicalRecord(item.medicalRecordId, "Thu hồi")
                        }
                      >
                        Thu hồi hồ sơ y tế
                      </Button>
                    )}
                  </div>
                </div>
              </List.Item>
            )}
          />

          {openDialog === DIALOGS.PRESCRIPTION_DETAIL && (
            <div>
              <PrescriptionDetail
                prescriptionId={selectedPrescriptionId}
                onClose={handleDialogClose}
                onSwitch={handleDialogSwitch}
              />
            </div>
          )}
        </ModalWrapper>
      </MedicalRecordDialogStyle>
    </Context.Provider>
  );
};

export default MedicalRecordList;
