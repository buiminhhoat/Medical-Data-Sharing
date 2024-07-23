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
  Col
} from "antd";
import { Alert, notification } from "antd";
import { MinusCircleOutlined, PlusOutlined, QrcodeOutlined, ScanOutlined } from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
import QRCodeScanner from "../../QRCodeScanner/QRCodeScanner";
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
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [loading, setLoading] = useState(true);
  const [recipientId, setRecipientId] = useState(values ? values.recipientId : "");
  
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
    {
      value: "Mua bảo hiểm",
      label: "Mua bảo hiểm",
    },
    {
      value: "Thanh toán",
      value: "Thanh toán",
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

  const [options, setOptions] = useState(null);

  useEffect(() => {
    console.log("options: ", options);
    console.log("role: ", role);
    if (options === null) {
      if (role === "Bệnh nhân") setOptions(patientOptions);
      if (role === "Bác sĩ") setOptions(doctorOptions);
      if (role === "Cửa hàng thuốc") setOptions(drugStoreOptions);
    }
  });

  const handleFormSubmit = async (values) => {
    if (access_token) {
      console.log("requestType: ", values.requestType);
      console.log(values);
      console.log("apiSendRequest: ", apiSendRequest);
      const formData = new FormData();
      console.log(values);
      for (const key in values) {
        if (key === "hashFile") continue;
        formData.append(key, values[key]);
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
      setApiSendRequest(API.DOCTOR.SEND_VIEW_REQUEST);
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

  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      recipientId: recipientId
    });
  }, [recipientId])
  console.log(recipientId);

  return (
    <Context.Provider value={"Tạo yêu cầu"}>
      {contextHolder}
      <SendRequestDialogStyle>
        <Modal
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
            onFinish={handleFormSubmit}
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

              <Form.Item
                label="ID người nhận"
                name="recipientId"
              >
                <Row gutter={10}>
                  <Col span={22}>
                    <Form.Item
                      name="recipientId"
                      noStyle
                      rules={[
                        {
                          required: true,
                          message: 'Vui lòng điền ID người nhận!',
                        },
                      ]}
                    >
                      <Input />
                    </Form.Item>
                  </Col>

                  <Col span={2} style={{display: "flex", alignItems: "right", justifyContent: "right"}}>
                    <Button onClick={onClickScan} icon={<ScanOutlined />}></Button>
                  </Col>
                </Row>
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
              <Button htmlType="submit">Tạo yêu cầu</Button>
            </div>
          </Form>
        </Modal>

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
