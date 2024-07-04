import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
const { Option } = Select;

const LoginDialogStyle = styled.div``;
const LoginDialog = ({ onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;

  const [isModalOpen, setIsModalOpen] = useState(true);

  const handleOk = () => {
    setIsModalOpen(false);
    onClose();
  };

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinish = (values) => {
    console.log("Success:", values);
  };
  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <LoginDialogStyle>
      <>
        <Modal
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
            onFinish={onFinish}
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
              <Select placeholder="Tổ chức">
                <Option value="Bệnh nhân">Bệnh nhân</Option>
                <Option value="Bác sĩ">Bác sĩ</Option>
                <Option value="Công ty sản xuất thuốc">
                  Công ty sản xuất thuốc
                </Option>
                <Option value="Cửa hàng thuốc">Cửa hàng thuốc</Option>
                <Option value="Trung tâm nghiên cứu">
                  Trung tâm nghiên cứu
                </Option>
              </Select>
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

            <Form.Item
              wrapperCol={{
                offset: 8,
                span: 16,
              }}
            >
              <Button type="primary" htmlType="submit">
                Submit
              </Button>
            </Form.Item>
          </Form>
        </Modal>
      </>
    </LoginDialogStyle>
  );
};

export default LoginDialog;
