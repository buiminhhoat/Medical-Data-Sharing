import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
import MedicalRecordList from "../MedicalRecordList/MedicalRecordList";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
const { Option } = Select;

const RequestDetailStyle = styled.div``;

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

const RequestDetail = ({ request, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState("");
  const [loading, setLoading] = useState(true);

  const apiGetRequest = API.PUBLIC.GET_REQUEST;
  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const fetchGetRequest = async () => {
    if (access_token) {
      const formData = new FormData();
      formData.append("requestId", request.requestId);
      formData.append("requestType", request.requestType);

      console.log(access_token);

      try {
        console.log("***");
        const response = await fetch(apiGetRequest, {
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
    if (access_token) fetchGetRequest().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    if (data) {
      console.log(data);
      setLoading(false);
    }
  }, [data]);

  const [openDialog, setOpenDialog] = useState(null);
  const [patientId, setPatientId] = useState(null);

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

  const openMedicalRecord = (patientId) => {
    console.log("openMedicalRecord");
    console.log(patientId);
    openModal(DIALOGS.MEDICAL_RECORD);
    setPatientId(patientId);
  };

  const createMedicalRecord = (request) => {
    console.log("createMedicalRecord");
    console.log(request);
    openModal(DIALOGS.ADD_MEDICAL_RECORD);
  };

  return (
    <RequestDetailStyle>
      <Modal
        title="Chi tiết yêu cầu"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"55%"}
        loading={loading}
      >
        <div style={{ marginTop: "20px", marginLeft: "20px" }}>
          <Info>
            <div className="field">RequestID</div>
            <div>{data.requestId}</div>
          </Info>
          <Info>
            <div className="field">ID người gửi</div>
            <div>{data.senderId}</div>
          </Info>
          <Info>
            <div className="field">Tên người gửi</div>
            <div>{data.senderName}</div>
          </Info>

          <Info>
            <div className="field">ID người nhận</div>
            <div>{data.recipientId}</div>
          </Info>
          <Info>
            <div className="field">Tên người nhận</div>
            <div>{data.recipientName}</div>
          </Info>

          {data.requestType === "Đặt lịch khám" && (
            <>
              <Info>
                <div className="field">ID cơ sở y tế</div>
                <div>{data.medicalInstitutionId}</div>
              </Info>

              <Info>
                <div className="field">Tên cơ sở y tế</div>
                <div>{data.medicalInstitutionName}</div>
              </Info>
            </>
          )}

          <Info>
            <div className="field">Ngày tạo</div>
            <div>{data.dateCreated}</div>
          </Info>

          <Info>
            <div className="field">Ngày chỉnh sửa</div>
            <div>{data.dateModified}</div>
          </Info>

          <Info>
            <div className="field">Loại yêu cầu</div>
            <div>{data.requestType}</div>
          </Info>

          <Info>
            <div className="field">Trạng thái</div>
            <div>{data.requestStatus}</div>
          </Info>

          {data.requestType === "Xác nhận thanh toán" && (
            <Info>
              <div className="field">ID yêu cầu thanh toán</div>
              <div>{data.paymentRequestId}</div>
            </Info>
          )}

          {data.requestType === "Chỉnh sửa hồ sơ y tế" && (
            <Info>
              <div className="field">Hồ sơ y tế mới</div>
              <div>Xem</div>
            </Info>
          )}

          {data.requestType === "Thanh toán" && (
            <>
              <Info>
                <div className="field">ID hợp đồng bảo hiểm</div>
                <div>{data.insuranceContractId}</div>
              </Info>

              <Info>
                <div className="field">ID hồ sơ y tế</div>
                <div>{data.medicalRecordId}</div>
              </Info>
            </>
          )}

          {data.requestType === "Mua bảo hiểm" && (
            <>
              <Info>
                <div className="field">ID sản phẩm bảo hiểm</div>
                <div>{data.insuranceProductId}</div>
              </Info>

              <Info>
                <div className="field">Ngày bắt đầu</div>
                <div>{data.startDate}</div>
              </Info>

              <Info>
                <div className="field">Ngày kết thúc</div>
                <div>{data.endDate}</div>
              </Info>

              <Info>
                <div className="field">File hợp đồng bảo hiểm</div>
                <div>{data.hashFile}</div>
              </Info>
            </>
          )}

          {data.requestType === "Xem đơn thuốc" && (
            <>
              <Info>
                <div className="field">ID đơn thuốc</div>
                <div>{data.prescriptionId}</div>
              </Info>
            </>
          )}
        </div>

        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
          }}
        >
          {(data.requestType === "Đặt lịch khám" ||
            data.requestType === "Xem hồ sơ y tế") && (
            <>
              <Button
                style={{ marginRight: "3%" }}
                onClick={() => openMedicalRecord(data.senderId)}
              >
                Xem hồ sơ y tế
              </Button>
            </>
          )}

          {data.recipientId === userId &&
            data.requestType === "Đặt lịch khám" && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => openMedicalRecord(data.senderId)}
                >
                  Chỉnh sửa hồ sơ y tế
                </Button>
              </>
            )}

          {data.requestType === "Xem đơn thuốc" && (
            <>
              <Button style={{ marginRight: "3%" }}>Xem đơn thuốc</Button>
            </>
          )}

          {data.recipientId === userId &&
            data.requestStatus === "Chờ xử lý" &&
            (data.requestType === "Đặt lịch khám" ||
              data.requestType === "Thanh toán" ||
              data.requestType === "Mua bảo hiểm") && (
              <>
                <Button style={{ marginRight: "3%" }}>Chấp thuận</Button>
              </>
            )}

          {data.recipientId === userId &&
            data.requestStatus === "Chờ xử lý" &&
            data.requestType !== "Đặt lịch khám" && (
              <>
                <Button style={{ marginRight: "3%" }}>Đồng ý</Button>
              </>
            )}

          {(data.requestStatus === "Chờ xử lý" ||
            data.requestStatus === "Chấp thuận") && (
            <>
              <Button style={{ marginRight: "3%" }}>Từ chối</Button>
            </>
          )}

          {data.recipientId === userId &&
            data.requestType === "Đặt lịch khám" &&
            data.requestStatus !== "Đồng ý" && (
              <>
                <Button onClick={() => createMedicalRecord(data.requestId)}>
                  Tạo hồ sơ y tế
                </Button>
              </>
            )}
        </div>

        {openDialog === DIALOGS.MEDICAL_RECORD && (
          <div>
            <MedicalRecordList
              patientId={patientId}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )}

        {openDialog === DIALOGS.ADD_MEDICAL_RECORD && (
          <div>
            <AddMedicalRecordDialog
              request={data}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )}
      </Modal>
    </RequestDetailStyle>
  );
};

export default RequestDetail;
