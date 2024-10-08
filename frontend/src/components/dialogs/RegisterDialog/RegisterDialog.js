import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, DatePicker, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import {
  Button,
  Modal,
  Checkbox,
  Form,
  Input,
  Select,
  notification,
} from "antd";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const RegisterDialogStyle = styled.div``;

const Context = React.createContext({
  name: "Register Dialog",
});

const RegisterDialog = ({ onClose, onSwitch }) => {
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

  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;
  const apiRegisterUrl = API.PATIENT.REGISTER;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const handleSwitchToOtherDialog = (dialogName) => {
    onSwitch(dialogName);
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const handleRegisterFormSubmit = async (values) => {
    console.log("values: ", values);

    const formData = new FormData();

    formData.append("email", values.email);
    formData.append("password", values.password);
    formData.append("fullName", values.fullName);
    formData.append("gender", values.gender);
    formData.append("dateBirthday", values.dateBirthday);

    console.log("formData: ", formData);

    try {
      const response = await fetch(apiRegisterUrl, {
        method: "POST",
        body: formData,
      });

      console.log("response: ", response);

      if (response.status === 200) {
        let jsonResponse = await response.json();
        openNotification(
          "topRight",
          "success",
          "Thành công",
          jsonResponse.messageStatus,
          window.location.reload()
        );
      } else {
        let jsonResponse = await response.json();
        openNotification(
          "topRight",
          "error",
          "Thất bại",
          jsonResponse.messageStatus,
          handleCancel
        );
      }
    } catch (e) {
      console.log("e: ", e);
    } finally {
    }
  };

  return (
    <Context.Provider value={"Cập nhật thông tin"}>
      {contextHolder}
      <RegisterDialogStyle>
        <>
          <ModalWrapper
            title="Đăng ký dành cho bệnh nhân"
            open={isModalOpen}
            onCancel={handleCancel}
            footer={null}
            centered
          >
            <Form
              name="register"
              labelCol={{
                span: 7,
              }}
              wrapperCol={{
                span: 16,
              }}
              style={{
                maxWidth: 600,
                justifyContent: "center",
                alignItems: "center",
              }}
              initialValues={{
                remember: true,
              }}
              onFinish={handleRegisterFormSubmit}
              onFinishFailed={onFinishFailed}
              autoComplete="on"
            >
              <Form.Item
                style={{ marginTop: 25 }}
                label="Họ và tên"
                name="fullName"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập họ và tên!",
                  },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                style={{ marginTop: 25 }}
                label="Email"
                name="email"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập Email!",
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
                    message: "Vui lòng nhập mật khẩu!",
                  },
                ]}
              >
                <Input.Password />
              </Form.Item>

              <Form.Item
                label="Ngày sinh"
                name="dateBirthday"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng chọn ngày sinh!",
                  },
                ]}
              >
                <DatePicker
                  style={{ width: "100%" }}
                  format={"YYYY-MM-DD"}
                  placeholder="Chọn ngày sinh"
                />
              </Form.Item>

              <Form.Item
                label="Giới tính"
                name="gender"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng chọn giới tính!",
                  },
                ]}
              >
                <Select
                  options={[
                    {
                      value: "Nam",
                      label: "Nam",
                    },
                    {
                      value: "Nữ",
                      label: "Nữ",
                    },
                  ]}
                  placeholder="Giới tính"
                  allowClear
                  style={{ width: "100%" }}
                />
              </Form.Item>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  justifyItems: "center",
                }}
              >
                <div style={{ display: "flex", marginTop: "20px" }}>
                  <Button
                    type="primary"
                    htmlType="submit"
                    style={{ marginRight: "10%" }}
                  >
                    Đăng ký dành cho bệnh nhân
                  </Button>

                  <Button
                    onClick={() => handleSwitchToOtherDialog(DIALOGS.LOGIN)}
                  >
                    Đăng nhập
                  </Button>
                </div>
              </div>
            </Form>
          </ModalWrapper>
        </>
      </RegisterDialogStyle>
    </Context.Provider>
  );
};

export default RegisterDialog;
