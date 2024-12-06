import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import Storage from '@Utils/Storage';
import { UserOutlined, CloseOutlined } from "@ant-design/icons";
import { Avatar, Flex, InputNumber, Space, TreeSelect } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import FileUploader from "../../FileUploader/FileUploader";
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
  Row,
  Col,
} from "antd";
import { Alert, notification } from "antd";
import {
  MinusCircleOutlined,
  PlusOutlined,
  QrcodeOutlined,
  ScanOutlined,
} from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
import QRCodeScanner from "../../QRCodeScanner/QRCodeScanner";
import ConfirmModal from "../ConfirmModal/ConfirmModal";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const Context = React.createContext({
  name: "Default",
});

const SharePrescriptionDialogStyle = styled.div`
  overflow: auto;
`;

const SharePrescriptionDialog = ({ prescriptionId, onClose, onSwitch }) => {
  const { access_token, userId, role } = Storage.getData();
  
  const [apiSharePrescription, setApiSharePrescription] = useState(
    API.PATIENT.SHARE_PRESCRIPTION_BY_PATIENT
  );
  const [isModalOpen, setIsModalOpen] = useState(true);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

  const [loading, setLoading] = useState(true);
  const [recipientId, setRecipientId] = useState("");

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

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

  const patientOptions = [
    {
      value: "Chia sẻ đơn thuốc",
      label: "Chia sẻ đơn thuốc",
    },
  ];

  const [options, setOptions] = useState(null);

  useEffect(() => {
    console.log("options: ", options);
    console.log("role: ", role);
    if (options === null) {
      if (role === "Bệnh nhân") setOptions(patientOptions);
    }
  });

  const [form] = Form.useForm();

  const [valuesForm, setValuesForm] = useState();

  const [apiGetFullName, setGetFullName] = useState("/api/user/get-full-name");
  const fetchGetFullName = async (id) => {
    if (access_token) {
      form.setFieldsValue({ recipientName: "Loading" });
      const formData = new FormData();
      formData.append("id", id);

      try {
        const response = await fetch(apiGetFullName, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          // let data = await response.json();
          let data = await response.text();
          return data;
        } else {
          return "Không tìm thấy người dùng";
        }
      } catch (e) {
        console.log(e);
        return "Không tìm thấy người dùng";
      }
    }
  };
  
  useEffect(() => {
    async function fetchAndSet() {
      console.log("recipientId: ", recipientId);
      if (recipientId != null && recipientId.split('-').slice(1).join('-').length >= 36) {
        const recipientName = await fetchGetFullName(recipientId);
        form.setFieldsValue({ recipientName: recipientName });
      } else {
        form.setFieldsValue({ recipientName: "" });
      }
    }

    fetchAndSet();
  }, [recipientId]);

  const handleFormSubmit = async () => {
    if (access_token) {
      setIsConfirmModalOpen(false);
      setDisabledButton(true);
      console.log("apiSharePrescription: ", apiSharePrescription);

      const formData = new FormData();
      console.log(valuesForm);

      formData.append("senderId", valuesForm["recipientId"]);
      formData.append("recipientId", userId);
      formData.append("prescriptionId", prescriptionId);

      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );

      try {
        const response = await fetch(apiSharePrescription, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });
        if (response.status === 200) {
          console.log("data");
          let data = await response.json();
          console.log(data);
          openNotification(
            "topRight",
            "success",
            "Thành công",
            "Đã tạo yêu cầu thành công!",
            handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra khi tạo yêu cầu!",
            handleCancel
          );
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  const onClickScan = () => {
    setOpenDialog(DIALOGS.QRCODE_SCANNER);
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

  const [senderId, setSenderId] = useState(userId);

  // console.log(requestType);

  useEffect(() => {
    form.setFieldsValue({
      recipientId: recipientId,
    });
  }, [recipientId]);
  console.log(recipientId);

  const handleConfirm = (valuesForm) => {
    setIsConfirmModalOpen(true);
    setValuesForm(valuesForm);
  };

  const handleConfirmModalCancel = () => {
    setIsConfirmModalOpen(false);
  };

  const [disabledButton, setDisabledButton] = useState(false);

  return (
    <Context.Provider value={"Chia sẻ đơn thuốc"}>
      {contextHolder}
      <SharePrescriptionDialogStyle>
        <ModalWrapper
          title="Chia sẻ đơn thuốc"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          // loading={loading}
        >
          <Form
            name="sharePrescription"
            labelCol={{
              span: 5,
            }}
            wrapperCol={{
              span: 18,
            }}
            style={{
              width: "100%",
              justifyContent: "center",
              alignItems: "center",
            }}
            initialValues={{
              // requestId: request.requestId,
              // senderName: request.senderName,
              recipientId: recipientId,
              prescriptionId: prescriptionId,
              // recipientName: values.recipientName,
              // medicalInstitutionName: request.medicalInstitutionName,
              remember: true,
            }}
            onFinish={handleConfirm}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
            form={form}
          >
            <div style={{ width: "100%" }}>
              <Form.Item label="ID đơn thuốc" name="prescriptionId">
                <Input disabled />
              </Form.Item>

              <Form.Item label="ID cửa hàng thuốc" name="recipientId">
                <Form.Item
                  name="recipientId"
                  noStyle
                  rules={[
                    {
                      required: true,
                      message: "Vui lòng điền ID cửa hàng thuốc!",
                    },
                  ]}
                >
                  <Row gutter={10}>
                    <Col span={22}>
                      <Form.Item
                        name="recipientId"
                        noStyle
                        rules={[
                          {
                            required: true,
                            message: "Vui lòng điền ID cửa hàng thuốc!",
                          },
                        ]}
                      >
                        <Input onChange={(e) => setRecipientId(e.target.value)} />
                      </Form.Item>
                    </Col>

                    <Col
                      span={2}
                      style={{
                        display: "flex",
                        alignItems: "right",
                        justifyContent: "right",
                      }}
                    >
                      <Button
                        onClick={onClickScan}
                        icon={<ScanOutlined />}
                      ></Button>
                    </Col>
                  </Row>
                </Form.Item>
              </Form.Item>

              <Form.Item label="Tên cửa hàng thuốc" name="recipientName">
                <Input disabled />
              </Form.Item>
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                justifyItems: "center",
              }}
            >
              <Button htmlType="submit" disabled={disabledButton}>
                Tạo yêu cầu
              </Button>
            </div>
          </Form>
        </ModalWrapper>

        {openDialog === DIALOGS.QRCODE_SCANNER && (
          <div>
            <QRCodeScanner
              value={recipientId}
              setValue={setRecipientId}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )}

        <ConfirmModal
          isOpen={isConfirmModalOpen}
          handleOk={handleFormSubmit}
          handleCancel={handleConfirmModalCancel}
          title="Xác nhận"
          content="Bạn có chắc chắn không?"
        />

        {/* {openDialog === DIALOGS.EDIT_MEDICAL_RECORD && (
          <div className="modal-overlay">
            <EditMedicalRecordDialog
              values={valuesForm}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )} */}
      </SharePrescriptionDialogStyle>
    </Context.Provider>
  );
};

export default SharePrescriptionDialog;
