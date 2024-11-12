import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { useCookies } from 'react-cookie';
import {
  UserOutlined,
  CloseOutlined,
  EyeInvisibleOutlined,
  EyeTwoTone,
} from "@ant-design/icons";
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
  name: "RegisterUserContext",
});

const RegisterUserDialogStyle = styled.div`
  overflow: auto;
`;

const RegisterUserDialog = ({ onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiRegisterUser = API.ADMIN.REGISTER_USER;

  if (role === "Cơ sở y tế") {
    apiRegisterUser = API.MEDICAL_INSTITUTION.REGISTER_USER;
  }

  if (role === "Trung tâm nghiên cứu") {
    apiRegisterUser = API.RESEARCH_CENTER.REGISTER_USER;
  }

  const [isModalOpen, setIsModalOpen] = useState(true);

  const [options, setOptions] = useState();

  useEffect(() => {
    if (role === "") return;
    if (role === "Quản trị viên") {
      setOptions([
        {
          label: "Công ty sản xuất thuốc",
          value: "Công ty sản xuất thuốc",
        },
        {
          label: "Cơ sở y tế",
          value: "Cơ sở y tế",
        },
        {
          label: "Trung tâm nghiên cứu",
          value: "Trung tâm nghiên cứu",
        },
      ]);
    }

    if (role === "Cơ sở y tế") {
      setOptions([
        {
          label: "Bác sĩ",
          value: "Bác sĩ",
        },
      ]);
    }

    if (role === "Trung tâm nghiên cứu") {
      setOptions([
        {
          label: "Nhà khoa học",
          value: "Nhà khoa học",
        },
      ]);
    }
  }, [role]);

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

  const [additionalFields, setAdditionalFields] = useState(null);

  const changeRole = (value) => {
    if (
      value === "Công ty sản xuất thuốc" ||
      value === "Trung tâm nghiên cứu"
    ) {
      return (
        <Form.Item
          label="Mã số giấy phép kinh doanh"
          name="businessLicenseNumber"
          rules={[
            {
              required: true,
              message: "Vui lòng điền mã số giấy phép kinh doanh",
            },
          ]}
        >
          <Input />
        </Form.Item>
      );
    }

    if (value === "Bác sĩ") {
      return (
        <>
          <Form.Item
            label="Chuyên khoa"
            name="department"
            rules={[
              {
                required: true,
                message: "Vui lòng điền chuyên khoa",
              },
            ]}
          >
            <Input />
          </Form.Item>
        </>
      );
    }
    return "";
  };

  const onChange = (value) => {
    console.log("value: ", value);
    setAdditionalFields("");
    if (value != null) {
      setAdditionalFields(changeRole(value));
    }
  };

  const handleFormSubmit = async (values) => {
    if (access_token) {
      console.log(values);
      console.log("apiRegisterUser: ", apiRegisterUser);
      const formData = new FormData();
      console.log(values);
      for (const key in values) {
        formData.append(key, values[key]);
      }
      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu tạo tài khoản",
        "Hệ thống đã tiếp nhận yêu cầu tạo tài khoản!"
      );

      console.log("formData: ", formData);

      console.log("access_token: ", access_token);

      try {
        const response = await fetch(apiRegisterUser, {
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
            "Đã tạo tài khoản thành công!",
            handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra trong quá trình tạo tài khoản!",
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

  return (
    <Context.Provider value={"Tạo người dùng"}>
      {contextHolder}
      <RegisterUserDialogStyle>
        <ModalWrapper
          title="Tạo người dùng"
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
              span: 4,
            }}
            wrapperCol={{
              span: 19,
            }}
            style={{
              width: "100%",
              justifyContent: "center",
              alignItems: "center",
            }}
            initialValues={{
              remember: true,
            }}
            onFinish={handleFormSubmit}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
            labelWrap
          >
            <div style={{ width: "100%" }}>
              <Form.Item
                label="Họ và tên"
                name="fullName"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền họ và tên!",
                  },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                label="Email"
                name="email"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền email!",
                  },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                label="Mật khẩu"
                name="password"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền mật khẩu!",
                  },
                ]}
              >
                <Input.Password
                  placeholder="Nhập mật khẩu"
                  iconRender={(visible) =>
                    visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                  }
                />
              </Form.Item>

              <Form.Item
                label="Địa chỉ"
                name="address"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền địa chỉ!",
                  },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                label="Vai trò"
                name="role"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền vai trò!",
                  },
                ]}
              >
                <Select
                  // showSearch
                  placeholder="Chọn vai trò"
                  filterOption={(input, option) =>
                    (option?.label ?? "")
                      .toLowerCase()
                      .includes(input.toLowerCase())
                  }
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
              <Button htmlType="submit">Tạo người dùng</Button>
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
      </RegisterUserDialogStyle>
    </Context.Provider>
  );
};

export default RegisterUserDialog;
