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
const { Option } = Select;

const MedicalRecordDialogStyle = styled.div`
  overflow: auto;
`;

const Info = styled.div`
  display: flex;
  /* justify-content: center; */
  /* justify-items: center; */
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

const MedicalRecordDialog = ({ patientId, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiAllMedicalRecord = API.PATIENT.GET_LIST_MEDICAL_RECORD;
  if (role === "Bác sĩ")
    apiAllMedicalRecord = API.DOCTOR.GET_LIST_MEDICAL_RECORD;
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

  return (
    <MedicalRecordDialogStyle>
      <Modal
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

                <Info>
                  <div className="field">File</div>
                  <div>{item.hashFile}</div>
                </Info>

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
          }}
        ></div>
      </Modal>
    </MedicalRecordDialogStyle>
  );
};

export default MedicalRecordDialog;
