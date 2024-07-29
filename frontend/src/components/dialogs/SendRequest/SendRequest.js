import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
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

const SendRequestDialogStyle = styled.div`
  overflow: auto;
`;

const SendRequestDialog = ({ values, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  const [apiSendRequest, setApiSendRequest] = useState("");
  const [apiGetFullName, setGetFullName] = useState(API.PUBLIC.GET_FULL_NAME);
  const [isModalOpen, setIsModalOpen] = useState(true);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

  const [loading, setLoading] = useState(true);
  const [recipientId, setRecipientId] = useState(
    values ? values.recipientId : ""
  );

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
      value: "Đặt lịch khám",
      label: "Đặt lịch khám",
    },
  ];

  const doctorOptions = [
    {
      value: "Xem hồ sơ y tế",
      label: "Xem hồ sơ y tế",
    },
  ];

  const drugStoreOptions = [
    {
      value: "Xem đơn thuốc",
      label: "Xem đơn thuốc",
    },
  ];

  const manufacturerOptions = [
    {
      value: "Xem hồ sơ y tế",
      label: "Xem hồ sơ y tế",
    },
  ];

  const scientistOptions = [
    {
      value: "Xem hồ sơ y tế",
      label: "Xem hồ sơ y tế",
    },
  ];

  const [options, setOptions] = useState(null);

  useEffect(() => {
    console.log("options: ", options);
    console.log("role: ", role);
    if (options === null) {
      if (role === "Bệnh nhân") setOptions(patientOptions);
      if (role === "Bác sĩ") setOptions(doctorOptions);
      if (role === "Cửa hàng thuốc") setOptions(drugStoreOptions);
      if (role === "Công ty sản xuất thuốc") setOptions(manufacturerOptions);
      if (role === "Nhà khoa học") setOptions(scientistOptions);
    }
  });

  const [form] = Form.useForm();

  const [valuesForm, setValuesForm] = useState();

  const handleFormSubmit = async () => {
    if (access_token) {
      setIsConfirmModalOpen(false);
      setDisabledButton(true);
      console.log("apiSendRequest: ", apiSendRequest);
      const formData = new FormData();

      console.log(valuesForm);
      for (const key in valuesForm) {
        if (key === "hashFile") continue;
        formData.append(key, valuesForm[key]);
      }

      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );

      try {
        const response = await fetch(apiSendRequest, {
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

  const fetchGetFullName = async (id) => {
    if (access_token) {
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
          let data = await response.json();
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

  const [treeData, setTreeData] = useState(null);

  const renderViewPrescriptionRequest = () => {
    return (
      <Form.Item
        label="ID đơn thuốc"
        name="prescriptionId"
        rules={[
          {
            required: true,
            message: "Vui lòng điền ID đơn thuốc",
          },
        ]}
      >
        <Input />
      </Form.Item>
    );
  };

  const changeRequestType = (value) => {
    if (value === "Đặt lịch khám") {
      setApiSendRequest(API.PATIENT.SEND_APPOINTMENT_REQUEST);
    }
    if (value === "Xem hồ sơ y tế") {
      if (role === "Bác sĩ") {
        setApiSendRequest(API.DOCTOR.SEND_VIEW_REQUEST);
      }
      if (role === "Công ty sản xuất thuốc") {
        setApiSendRequest(API.MANUFACTURER.SEND_VIEW_REQUEST);
      }
      if (role === "Nhà khoa học") {
        setApiSendRequest(API.SCIENTIST.SEND_VIEW_REQUEST);
      }
    }
    if (value === "Xem đơn thuốc") {
      setAdditionalFields(renderViewPrescriptionRequest());
      setApiSendRequest(API.DRUGSTORE.SEND_VIEW_PRESCRIPTION_REQUEST);
    }
  };

  useEffect(() => {
    if (values != null) {
      changeRequestType(values.requestType);
    }
  }, [values]);

  const onChange = (value) => {
    console.log("value: ", value);
    changeRequestType(value);
  };

  const onPopupScroll = (e) => {
    console.log("onPopupScroll", e);
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

  const [hashFile, setHashFile] = useState("");

  console.log(doctorOptions);

  const [senderId, setSenderId] = useState(userId);

  const [additionalFields, setAdditionalFields] = useState(null);

  // console.log(requestType);

  useEffect(() => {
    form.setFieldsValue({
      recipientId: recipientId,
    });
  }, [recipientId]);

  useEffect(() => {
    async function fetchAndSet() {
      if (senderId.length >= 32) {
        const senderName = await fetchGetFullName(senderId);
        form.setFieldsValue({ senderName: senderName });
      } else {
        form.setFieldsValue({ senderName: "" });
      }
      if (recipientId.length >= 32) {
        console.log("recipientName");
        const recipientName = await fetchGetFullName(recipientId);
        form.setFieldsValue({ recipientName: recipientName });
      } else {
        form.setFieldsValue({ recipientName: "" });
      }
    }

    fetchAndSet();
  }, [senderId, recipientId]);

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
    <Context.Provider value={"Tạo yêu cầu"}>
      {contextHolder}
      <SendRequestDialogStyle>
        <ModalWrapper
          title="Tạo yêu cầu"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          // loading={loading}
        >
          <Form
            name="addRequest"
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
              senderId: senderId,
              // senderName: request.senderName,
              recipientId: recipientId,
              // recipientName: values.recipientName,
              requestType: values ? values.requestType : "",
              // medicalInstitutionName: request.medicalInstitutionName,
              remember: true,
            }}
            onFinish={handleConfirm}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
            form={form}
          >
            <div style={{ width: "100%" }}>
              <Form.Item label="ID người gửi" name="senderId">
                <Input disabled />
              </Form.Item>

              <Form.Item label="Tên người gửi" name="senderName">
                <Input disabled />
              </Form.Item>

              <Form.Item label="ID người nhận" name="recipientId">
                <Form.Item
                  name="recipientId"
                  noStyle
                  rules={[
                    {
                      required: true,
                      message: "Vui lòng điền ID người nhận!",
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
                            message: "Vui lòng điền ID người nhận!",
                          },
                        ]}
                      >
                        <Input
                          onChange={(e) => setRecipientId(e.target.value)}
                        />
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

              <Form.Item label="Tên người nhận" name="recipientName">
                <Input disabled />
              </Form.Item>
              <Form.Item
                label="Loại yêu cầu"
                name="requestType"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền loại yêu cầu!",
                  },
                ]}
              >
                <Select
                  options={options}
                  onChange={(value) => {
                    onChange(value);
                  }}
                />
              </Form.Item>
              {additionalFields}
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
      </SendRequestDialogStyle>
    </Context.Provider>
  );
};

export default SendRequestDialog;
