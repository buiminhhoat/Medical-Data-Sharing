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
import EditMedicalRecordDialog from "../EditMedicalRecordDialog/EditMedicalRecordDialog";
const { Option } = Select;

const Context = React.createContext({
  name: "Default",
});

const AddRequestDialogStyle = styled.div`
  overflow: auto;
`;

const AddRequestDialog = ({ request, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiAddRequest = API.PUBLIC.ADD_REQUEST;
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
    {
      value: "Chỉnh sửa hồ sơ y tế",
      label: "Chỉnh sửa hồ sơ y tế",
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
      console.log("medicalRecordId: ", values.medicalRecordId);
      if (requestType === "Chỉnh sửa hồ sơ y tế") {
        setValuesForm(values);
        openModal(DIALOGS.EDIT_MEDICAL_RECORD);
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
  const [buttonText, setButtionText] = useState("Tạo yêu cầu");

  function renderEditRequest() {
    return (
      <React.Fragment>
        <Form.Item label="ID hồ sơ y tế" name="medicalRecordId">
          <Input></Input>
        </Form.Item>
      </React.Fragment>
    );
  }

  useEffect(() => {
    setAdditionalFields("");
    if (requestType === "Xem hồ sơ y tế") {
      setButtionText("Tạo yêu cầu");
    }
    if (requestType === "Chỉnh sửa hồ sơ y tế") {
      setAdditionalFields(renderEditRequest());
      setButtionText("Bước tiếp theo");
    }
  }, [requestType]);

  return (
    <Context.Provider value={"Tạo yêu cầu"}>
      {contextHolder}
      <AddRequestDialogStyle>
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
              <Button htmlType="submit">{buttonText}</Button>
            </div>
          </Form>
        </Modal>

        {openDialog === DIALOGS.EDIT_MEDICAL_RECORD && (
          <div className="modal-overlay">
            <EditMedicalRecordDialog
              values={valuesForm}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )}
      </AddRequestDialogStyle>
    </Context.Provider>
  );
};

export default AddRequestDialog;
