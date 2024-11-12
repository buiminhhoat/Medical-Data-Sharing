import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Storage from '@Utils/Storage';
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import { Alert, notification } from "antd";

import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const Context = React.createContext({
  name: "LoginDialogContext",
});

const LoginDialogStyle = styled.div``;

const LoginDialog = ({ onClose, onSwitch }) => {
  const { access_token, userId, role } = Storage.getData();
  
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

  const [api, contextHolder] = notification.useNotification();
  const openNotification = (placement, type, message, description, onClose, duration) => {
    api[type]({
      message: message,
      description: description,
      placement,
      showProgress: true,
      pauseOnHover: true,
      onClose: onClose,
      duration: duration
    });
  };

  const handleLoginFormSubmit = async (values) => {

    const formData = new FormData();
    formData.append("email", values.email);
    formData.append("password", values.password);
    formData.append("organization", values.organization);

    const apiLoginUrl = "/api/" + values.organization + "/permit-all/login";

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

        Storage.setItem("access_token", access_token);
        Storage.setItem("userId", userId);
        Storage.setItem("role", role);

        console.log(role);

        // openNotification(
        //   "topRight",
        //   "success",
        //   "Đăng nhập thành công",
        //   "Đăng nhập thành công",
        //   () => {
        //     window.location.reload();
        //   },
        //   1
        // );

        window.location.reload();
      }
      else {
        openNotification(
          "topRight",
          "error",
          "Đăng nhập thất bại",
          "Vui lòng kiểm tra lại thông tin đăng nhập!",
          () => {

          },
          3
        );
      }
    } catch (e) {
      console.log(e);
      openNotification(
        "topRight",
        "error",
        "Đăng nhập thất bại",
        "Vui lòng kiểm tra lại thông tin đăng nhập!",
        () => {
            
        },
        3
      );
    } finally {
    }
  };

  return (
    <Context.Provider value={"Tạo thuốc"}>
      {contextHolder}
    <LoginDialogStyle>
      <>
        <ModalWrapper
          title="Đăng nhập"
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
              label="Tổ chức"
              name="organization"

              rules={[
                {
                  required: true,
                  message: "Vui lòng chọn tổ chức!",
                },
              ]}
            >
              <Select
                // defaultValue="patient"
                placeholder="Chọn tổ chức"

                options={[
                  { value: 'patient', label: 'Bệnh nhân' },
                  { value: 'doctor', label: 'Bác sĩ' },
                  { value: 'medical_institution', label: 'Cơ sở y tế' },
                  { value: 'research_center', label: 'Trung tâm nghiên cứu'},
                  { value: 'scientist', label: 'Nhà khoa học'},
                  { value: 'manufacturer', label: 'Công ty sản xuất thuốc'},
                  { value: 'drugstore', label: 'Nhà thuốc'},
                  { value: 'admin', label: 'Quản trị viên'},
                ]}
              />
            </Form.Item>

            <Form.Item
              name="remember"
              valuePropName="checked"
              wrapperCol={{
                offset: 8,
                span: 16,
              }}
            >
              <Checkbox>Ghi nhớ đăng nhập</Checkbox>
            </Form.Item>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                justifyItems: "center",
              }}
            >
              <div style={{ display: "flex" }}>
                <Button
                  type="primary"
                  htmlType="submit"
                  style={{ marginRight: "10%" }}
                >
                  Đăng nhập
                </Button>

                <Button
                  onClick={() => handleSwitchToOtherDialog(DIALOGS.REGISTER)}
                >
                  Đăng ký
                </Button>
              </div>
            </div>
          </Form>
        </ModalWrapper>
      </>
    </LoginDialogStyle>
  </Context.Provider>
  );
};

export default LoginDialog;
