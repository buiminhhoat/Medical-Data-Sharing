import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { useCookies } from 'react-cookie';
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
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const Context = React.createContext({
  name: "AddMedicationContext",
});

const AddMedicationDialogStyle = styled.div`
  overflow: auto;
`;

const AddMedicationDialog = ({ values, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  const apiAddMedication = API.MANUFACTURER.ADD_MEDICATION;
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

  const handleFormSubmit = async (values) => {
    if (access_token) {
      console.log("hashFile: ", values.hashFile);
      console.log(values);
      console.log("apiAddMedication: ", apiAddMedication);
      const formData = new FormData();
      console.log(values);
      for (const key in values) {
        if (key === "hashFile") continue;
        formData.append(key, values[key]);
      }
      openNotification(
        "topRight",
        "info",
        "Đã gửi loại thuốc",
        "Hệ thống đã tiếp nhận loại thuốc!"
      );

      console.log("formData: ", formData);

      console.log("access_token: ", access_token);

      try {
        const response = await fetch(apiAddMedication, {
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
            "Đã tạo loại thuốc thành công!",
            handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra khi tạo loại thuốc!",
            handleCancel
          );
        }
      } catch (e) {
        console.log(e);
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

  const [hashFile, setHashFile] = useState("");

  const [manufacturerId, setManufacturerId] = useState(userId);

  return (
    <Context.Provider value={"Tạo loại thuốc"}>
      {contextHolder}
      <AddMedicationDialogStyle>
        <ModalWrapper
          title="Tạo loại thuốc"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          // loading={loading}
        >
          <Form
            name="addMedicationForm"
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
              manufacturerId: manufacturerId,
              remember: true,
            }}
            onFinish={handleFormSubmit}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
          >
            <div style={{ width: "100%" }}>
              <Form.Item label="ID nhà sản xuất thuốc" name="manufacturerId">
                <Input disabled />
              </Form.Item>

              <Form.Item
                label="Tên loại thuốc"
                name="medicationName"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền tên loại thuốc!",
                  },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                label="Mô tả"
                name="description"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền mô tả!",
                  },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                label="File"
                name="hashFile"
                // rules={[
                //   {
                //     required: true,
                //     message: "Vui lòng upload file!",
                //   },
                // ]}
              >
                <FileUploader hashFile={hashFile} setHashFile={setHashFile} />
              </Form.Item>
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                justifyItems: "center",
              }}
            >
              <Button htmlType="submit">Tạo loại thuốc</Button>
            </div>
          </Form>
        </ModalWrapper>

        {/* {openDialog === DIALOGS.EDIT_MEDICAL_RECORD && (
          <div className="modal-overlay">
            <EditMedicalRecordDialog
              values={valuesForm}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )} */}
      </AddMedicationDialogStyle>
    </Context.Provider>
  );
};

export default AddMedicationDialog;
