import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, DatePicker, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const RegisterDialogStyle = styled.div``;
const RegisterDialog = ({ onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;
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

  const handleLoginFormSubmit = async (values) => {
    console.log(values);

    const formData = new FormData();
    formData.append("email", values.email);
    formData.append("password", values.password);
    formData.append("organization", values.organization);

    try {
      const response = await fetch(apiLoginUrl, {
        method: "POST",
        body: formData,
      });

      if (response.status === 200) {
        let jsonResponse = await response.json();
        console.log(jsonResponse);
        let access_token = jsonResponse.access_token;
        let userId = jsonResponse.userId;
        let role = jsonResponse.authorities[0].authority;

        console.log(access_token);

        const cookies = new Cookies();
        cookies.set("access_token", access_token, { path: "/" });
        cookies.set("userId", userId, { path: "/" });
        cookies.set("role", role);

        console.log(role);
        window.location.reload();
      }
    } catch (e) {
    } finally {
    }
  };

  return (
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
            name="basic"
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
            onFinish={handleLoginFormSubmit}
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
  );
};

export default RegisterDialog;
