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
  DatePicker,
} from "antd";
import { Alert, notification } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
import DrugList from "../DrugList/DrugList";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const Context = React.createContext({
  name: "AddDrugContext",
});

const AddDrugDialogStyle = styled.div`
  overflow: auto;
`;

const AddDrugDialog = ({ values, onClose, onSwitch }) => {
  const { access_token, userId, role } = Storage.getData();
  
  
  
  const apiAddDrug = API.MANUFACTURER.ADD_DRUG;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [loading, setLoading] = useState(false);

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
    onClose();
  };

  const [data, setData] = useState([]);

  const handleFormSubmit = async (values) => {
    if (access_token) {
      console.log("apiAddDrug: ", apiAddDrug);
      const formData = new FormData();
      console.log(values);
      for (const key in values) {
        if (key === "manufactureDate" || key === "expirationDate") continue;
        formData.append(key, values[key]);
      }

      formData.append(
        "manufactureDate",
        values.manufactureDate.format("YYYY-MM-DD HH:mm")
      );
      formData.append(
        "expirationDate",
        values.expirationDate.format("YYYY-MM-DD HH:mm")
      );

      setLoading(true);
      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu tạo thuốc",
        "Hệ thống đã tiếp nhận yêu cầu tạo thuốc!"
      );

      console.log("formData: ", formData);

      console.log("access_token: ", access_token);

      try {
        const response = await fetch(apiAddDrug, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });
        if (response.status === 200) {
          console.log("data");
          let data = await response.json();
          setData(data);
          openModal(DIALOGS.DRUG_LIST);
          console.log(data);
          openNotification(
            "topRight",
            "success",
            "Thành công",
            "Đã tạo thuốc thành công!"
            // handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra khi tạo thuốc!",
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

  const [medicationId, setMedicationId] = useState(
    values != null && values.medicationId != null ? values.medicationId : ""
  );

  return (
    <Context.Provider value={"Tạo thuốc"}>
      {contextHolder}
      <AddDrugDialogStyle>
        <ModalWrapper
          title="Tạo thuốc"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          loading={loading}
        >
          <Form
            name="addDrugForm"
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
              medicationId: medicationId,
              remember: true,
            }}
            onFinish={handleFormSubmit}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
          >
            <div style={{ width: "100%" }}>
              <Form.Item
                label="ID loại thuốc"
                name="medicationId"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền ID thuốc!",
                  },
                ]}
              >
                <Input />
              </Form.Item>

              {/* <Form.Item
                label="Tên thuốc"
                name="medicationName"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền tên thuốc!",
                  },
                ]}
              >
                <Input disabled />
              </Form.Item> */}

              <Form.Item
                label="Đơn vị"
                name="unit"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền đơn vị!",
                  },
                ]}
              >
                <Select
                  options={[
                    {
                      value: "Viên",
                      label: "Viên",
                    },
                    {
                      value: "Lọ",
                      label: "Lọ",
                    },
                    {
                      value: "Hộp",
                      label: "Hộp",
                    },
                  ]}
                />
              </Form.Item>

              <Form.Item
                label="Số lượng"
                name="quantity"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền số lượng!",
                  },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                label="Ngày sản xuất"
                name="manufactureDate"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền ngày sản xuất!",
                  },
                ]}
              >
                <DatePicker
                  format={{
                    format: "YYYY-MM-DD HH:mm",
                  }}
                  placeholder="Ngày sản xuất"
                  style={{ width: "100%" }}
                />
              </Form.Item>

              <Form.Item
                label="Ngày hết hạn"
                name="expirationDate"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền ngày hết hạn!",
                  },
                ]}
              >
                <DatePicker
                  format={{
                    format: "YYYY-MM-DD HH:mm",
                  }}
                  placeholder="Ngày hết hạn"
                  style={{ width: "100%" }}
                />
              </Form.Item>
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                justifyItems: "center",
              }}
            >
              <Button htmlType="submit">Tạo thuốc</Button>
            </div>
          </Form>
        </ModalWrapper>

        {openDialog === DIALOGS.DRUG_LIST && (
          <div>
            <DrugList
              data={data}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            ></DrugList>
          </div>
        )}
      </AddDrugDialogStyle>
    </Context.Provider>
  );
};

export default AddDrugDialog;
