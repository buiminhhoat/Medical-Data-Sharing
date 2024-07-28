import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space, notification } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import ConfirmModal from "../ConfirmModal/ConfirmModal";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const ChangePasswordDialogStyle = styled.div``;

const Context = React.createContext({
  name: "Default",
});

const ChangePasswordDialog = ({ userId, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;
  const apiChangePassword = API.PUBLIC.CHANGE_PASSWORD;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
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

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [disabledButton, setDisabledButton] = useState(false);

  const [form] = Form.useForm();

  const [valuesForm, setValuesForm] = useState();

  const handleConfirm = (valuesForm) => {
    setIsConfirmModalOpen(true);
    setValuesForm(valuesForm);
  };

  const handleConfirmModalCancel = () => {
    setIsConfirmModalOpen(false);
  };

  const handleChangePasswordFormSubmit = async () => {
    if (access_token) {
      setIsConfirmModalOpen(false);
      setDisabledButton(true);
      const values = valuesForm;
      console.log(values);

      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );

      const formData = new FormData();
      formData.append("oldPassword", values.oldPassword);
      formData.append("password", values.password);

      try {
        const response = await fetch(apiChangePassword, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          openNotification(
            "topRight",
            "success",
            "Thành công",
            "Đã đổi mật khẩu thành công!",
            handleCancel
          );
        }
      } catch (e) {
        openNotification(
          "topRight",
          "error",
          "Thất bại",
          "Đã có lỗi xảy ra trong quá trình đổi mật khẩu!",
          handleCancel
        );
      }
    }
  };

  return (
    <Context.Provider value={"Đổi mật khẩu"}>
      {contextHolder}
      <ChangePasswordDialogStyle>
        <>
          <ModalWrapper
            title="Đổi mật khẩu"
            open={isModalOpen}
            onCancel={handleCancel}
            footer={null}
            centered
            width={"40%"}
          >
            <Form
              name="basic"
              labelCol={{
                span: 9,
              }}
              wrapperCol={{
                span: 18,
              }}
              style={{
                marginTop: "4%",
                marginRight: "5%",
              }}
              initialValues={{}}
              onFinish={handleConfirm}
              onFinishFailed={onFinishFailed}
              autoComplete="on"
            >
              <Form.Item
                label="Mật khẩu cũ"
                name="oldPassword"
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
                label="Mật khẩu mới"
                name="password"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập mật khẩu mới!",
                  },
                  {
                    min: 8,
                    message: "Mật khẩu phải có ít nhất 8 kí tự!",
                  },
                ]}
              >
                <Input.Password />
              </Form.Item>

              <Form.Item
                label="Nhập lại mật khẩu mới"
                name="confirm_password"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập lại mật khẩu mới!",
                  },
                  {
                    min: 8,
                    message: "Mật khẩu phải có ít nhất 8 kí tự!",
                  },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue("password") === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject(
                        new Error("Mật khẩu mới bạn nhập không khớp!")
                      );
                    },
                  }),
                ]}
              >
                <Input.Password />
              </Form.Item>

              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  justifyItems: "center",
                  marginRight: "-5%",
                }}
              >
                <Button
                  type="primary"
                  htmlType="submit"
                  disabled={disabledButton}
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    justifyItems: "center",
                  }}
                >
                  Đổi mật khẩu
                </Button>
              </div>
            </Form>
          </ModalWrapper>
        </>

        <ConfirmModal
          isOpen={isConfirmModalOpen}
          handleOk={handleChangePasswordFormSubmit}
          handleCancel={handleConfirmModalCancel}
          title="Xác nhận"
          content="Bạn có chắc chắn không?"
        />
      </ChangePasswordDialogStyle>
    </Context.Provider>
  );
};

export default ChangePasswordDialog;
