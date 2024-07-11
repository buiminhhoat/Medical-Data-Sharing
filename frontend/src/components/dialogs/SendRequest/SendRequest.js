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
} from "antd";
import { Alert, notification } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
const { Option } = Select;

const Context = React.createContext({
  name: "Default",
});

const SendRequestDialogStyle = styled.div`
  overflow: auto;
`;

const SendRequestDialog = ({ request, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiSendRequest = API.PUBLIC.SEND_REQUEST;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [loading, setLoading] = useState(true);

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

  const [options, setOptions] = useState(null);

  useEffect(() => {
    console.log("options", options);
    if (options === null) {
      if (role === "Bệnh nhân") setOptions(patientOptions);
      if (role === "Bác sĩ") setOptions(doctorOptions);
    }
  });

  const [valuesForm, setValuesForm] = useState();

  const handleFormSubmit = async (values) => {
    if (access_token) {
      console.log("requestType: ", values.requestType);
      console.log(values);
      console.log(apiSendRequest);
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

  const [value, setValue] = useState();
  const onChange = (newValue) => {
    console.log(newValue);
    setValue(newValue);
  };
  const onPopupScroll = (e) => {
    console.log("onPopupScroll", e);
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

  const [requestType, setRequestType] = useState(null);
  const [additionalFields, setAdditionalFields] = useState(null);

  useEffect(() => {
    setAdditionalFields("");
    if (requestType === "Xem hồ sơ y tế") {
      apiSendRequest = API.DOCTOR.SEND_VIEW_QUEST;
    }
  }, [requestType]);

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
              // recipientId: request.recipientId,
              // recipientName: request.recipientName,
              // requestType: request.requestType,
              // medicalInstitutionName: request.medicalInstitutionName,
              remember: true,
            }}
            onFinish={handleFormSubmit}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
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
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền ID người nhận!",
                  },
                ]}
              >
                <Input />
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
                  defaultValue=""
                  options={options}
                  onChange={(value) => {
                    setRequestType(value);
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
