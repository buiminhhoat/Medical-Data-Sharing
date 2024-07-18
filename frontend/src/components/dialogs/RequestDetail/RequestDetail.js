import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import { Alert, notification } from "antd";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
import MedicalRecordList from "../MedicalRecordList/MedicalRecordList";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
import PrescriptionDetail from "../PrescriptionDetail/PrescriptionDetail";
import SellingPrescriptionDrug from "../SellingPrescriptionDrug/SellingPrescriptionDrug";
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
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState("");
  const [loading, setLoading] = useState(true);

  const apiGetRequest = API.PUBLIC.GET_REQUEST;
  const apiDefineRequest = API.PUBLIC.DEFINE_REQUEST;

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

  const Context = React.createContext({
    name: "RequestDetail",
  });

  const defineRequest = async (requestStatus) => {
    if (access_token) {
      const formData = new FormData();
      formData.append("requestId", request.requestId);
      formData.append("requestType", request.requestType);
      formData.append("requestStatus", requestStatus);

      console.log(access_token);

      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );
      try {
        console.log("***");
        const response = await fetch(apiDefineRequest, {
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

  const renderButton = () => {
    if (data.requestType === "Đặt lịch khám") {
      if (data.senderId === userId) {
        return (
          <>
            <Button
              style={{ marginRight: "3%" }}
              onClick={() => openMedicalRecord(data.senderId)}
            >
              Xem hồ sơ y tế
            </Button>

            {(data.requestStatus === "Chờ xử lý" ||
              data.requestStatus === "Chấp thuận") && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Thu hồi")}
                >
                  Thu hồi
                </Button>
              </>
            )}
          </>
        );
      }
      if (data.recipientId === userId) {
        return (
          <>
            {data.requestStatus === "Chờ xử lý" && (
              <Button
                style={{ marginRight: "3%" }}
                onClick={() => defineRequest("Chấp thuận")}
              >
                Chấp thuận
              </Button>
            )}

            {(data.requestStatus === "Chờ xử lý" ||
              data.requestStatus === "Chấp thuận") && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Từ chối")}
                >
                  Từ chối
                </Button>
              </>
            )}

            {data.requestStatus !== "Đồng ý" && (
              <>
                <Button onClick={() => createMedicalRecord(data.requestId)}>
                  Tạo hồ sơ y tế
                </Button>
              </>
            )}
          </>
        );
      }
    }

    if (data.requestType === "Xem hồ sơ y tế") {
      if (data.senderId === userId) {
        return (
          <>
            {data.requestStatus === "Đồng ý" && (
              <Button
                style={{ marginRight: "3%" }}
                onClick={() => openMedicalRecord(data.recipientId)}
              >
                Xem hồ sơ y tế
              </Button>
            )}

            {(data.requestStatus === "Chờ xử lý" ||
              data.requestStatus === "Chấp thuận") && (
              <>
                <Button style={{ marginRight: "3%" }}>Thu hồi</Button>
              </>
            )}
          </>
        );
      }
      if (data.recipientId === userId) {
        return (
          <>
            <Button
              style={{ marginRight: "3%" }}
              onClick={() => openMedicalRecord(data.recipientId)}
            >
              Xem hồ sơ y tế
            </Button>

            {data.requestStatus === "Chờ xử lý" && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Đồng ý")}
                >
                  Đồng ý
                </Button>
              </>
            )}

            {data.requestStatus === "Chờ xử lý" && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Từ chối")}
                >
                  Từ chối
                </Button>
              </>
            )}

            {data.requestStatus === "Đồng ý" && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Thu hồi")}
                >
                  Thu hồi
                </Button>
              </>
            )}
          </>
        );
      }
    }

    if (data.requestType === "Xem đơn thuốc") {
      if (data.senderId === userId) {
        return (
          <>
            {data.requestStatus === "Đồng ý" && (
              <Button
                style={{ marginRight: "3%" }}
                onClick={() => openPrescriptionDetail(data.prescriptionId)}
              >
                Xem đơn thuốc
              </Button>
            )}

            {(data.requestStatus === "Chờ xử lý" ||
              data.requestStatus === "Chấp thuận") && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Thu hồi")}
                >
                  Thu hồi
                </Button>
              </>
            )}

            {data.senderId === userId && role === "Cửa hàng thuốc" && (
              <Button
                style={{ marginRight: "3%" }}
                onClick={() => openSellingPrescriptionDrug(data.prescriptionId)}
              >
                Bán thuốc
              </Button>
            )}
          </>
        );
      }
      if (data.recipientId === userId) {
        return (
          <>
            <Button
              style={{ marginRight: "3%" }}
              onClick={() => openPrescriptionDetail(data.prescriptionId)}
            >
              Xem đơn thuốc
            </Button>

            {data.requestStatus === "Chờ xử lý" && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Đồng ý")}
                >
                  Đồng ý
                </Button>
              </>
            )}

            {data.requestStatus === "Chờ xử lý" && (
              <>
                <Button
                  style={{ marginRight: "3%" }}
                  onClick={() => defineRequest("Từ chối")}
                >
                  Từ chối
                </Button>
              </>
            )}
          </>
        );
      }
    }
  };
  useEffect(() => {
    if (data) {
      console.log(data);
      setLoading(false);
      setAdditionalFields(renderButton);
    }
  }, [data]);

  const [prescriptionId, setPrescriptionId] = useState();
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

  const openPrescriptionDetail = (prescriptionId) => {
    console.log("openPrescriptionDetail: " + prescriptionId);
    setPrescriptionId(prescriptionId);
    openModal(DIALOGS.PRESCRIPTION_DETAIL);
  };

  const openSellingPrescriptionDrug = (prescriptionId) => {
    console.log("openSellingPrescriptionDrug: " + prescriptionId);
    setPrescriptionId(prescriptionId);
    openModal(DIALOGS.SELLING_PRESCRIPTION_DRUG);
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
    <Context.Provider value={"Chi tiết y cầu"}>
      {contextHolder}
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
            {additionalFields}
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

          {openDialog === DIALOGS.PRESCRIPTION_DETAIL && (
            <div>
              <PrescriptionDetail
                prescriptionId={prescriptionId}
                onClose={handleDialogClose}
                onSwitch={handleDialogSwitch}
              ></PrescriptionDetail>
            </div>
          )}

          {openDialog === DIALOGS.SELLING_PRESCRIPTION_DRUG && (
            <div>
              <SellingPrescriptionDrug
                patientId={data.recipientId}
                prescriptionId={prescriptionId}
                onClose={handleDialogClose}
                onSwitch={handleDialogSwitch}
              ></SellingPrescriptionDrug>
            </div>
          )}
        </Modal>
      </RequestDetailStyle>
    </Context.Provider>
  );
};

export default RequestDetail;
