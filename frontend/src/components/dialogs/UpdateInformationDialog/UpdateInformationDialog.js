import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from 'react-cookie';
import { UserOutlined } from "@ant-design/icons";
import { Avatar, DatePicker, Space, notification } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import ConfirmModal from "../ConfirmModal/ConfirmModal";
import dayjs from "dayjs";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";

const { Option } = Select;

const UpdateInformationDialogStyle = styled.div``;

const Context = React.createContext({
  name: "Default",
});

const UpdateInformationDialog = ({ userId, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const role = cookies.role;

  let org = "";

  switch (role) {
    case "Bệnh nhân":
      org = "patient";
      break;
    case "Bác sĩ":
      org = "doctor";
      break;
    case "Cơ sở y tế":
      org = "medical_institution";
      break;
    case "Trung tâm nghiên cứu":
      org = "research_center";
      break;
    case "Nhà khoa học":
      org = "scientist";
      break;
    case "Công ty sản xuất thuốc":
      org = "manufacturer";
      break;
    case "Nhà thuốc":
      org = "drugstore";
      break;
    case "Quản trị viên":
      org = "admin";
      break;
    default:
      org = "";
  }

  const apiUpdateInformation = "/api/" + org + "/update-information";
  const [isModalOpen, setIsModalOpen] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const [data, setData] = useState("");

  const fetchGetUserInfo = async () => {
    if (access_token) {
      console.log("id: ", userId);
      const formData = new FormData();
      formData.append("id", userId);

      console.log(access_token);

      let org = "";

      switch (role) {
        case "Bệnh nhân":
          org = "patient";
          break;
        case "Bác sĩ":
          org = "doctor";
          break;
        case "Cơ sở y tế":
          org = "medical_institution";
          break;
        case "Trung tâm nghiên cứu":
          org = "research_center";
          break;
        case "Nhà khoa học":
          org = "scientist";
          break;
        case "Công ty sản xuất thuốc":
          org = "manufacturer";
          break;
        case "Nhà thuốc":
          org = "drugstore";
          break;
        case "Quản trị viên":
          org = "admin";
          break;
        default:
          org = "";
      }
    
      let apiGetUserInfo = "/api/" + org + "/get-user-info";
      
      try {
        const response = await fetch(apiGetUserInfo, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetUserInfo().then((r) => {});
  }, [access_token]);

  const [form] = Form.useForm();

  const [valuesForm, setValuesForm] = useState();

  useEffect(() => {
    if (data) {
      console.log("data: ", data);
      if (data.dateBirthday) {
        data.dateBirthday = dayjs(data.dateBirthday, "YYYY-MM-DD");
      } else {
        data.dateBirthday = null;
      }
      // for (const key in data) {
      //   const valueObj = {};
      //   valueObj[key] = data[key];
      //   form.setFieldsValue({
      //     [key]: data[key],
      //   });
      //   console.log("key: ", key, " data[key]: ", data[key]);
      // }
      const newValues = Object.keys(data).reduce((acc, key) => {
        acc[key] = data[key];
        return acc;
      }, {});
      form.setFieldsValue(newValues);
    }
  }, [data]);

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

  const handleConfirm = (valuesForm) => {
    setIsConfirmModalOpen(true);
    setValuesForm(valuesForm);
  };

  const handleConfirmModalCancel = () => {
    setIsConfirmModalOpen(false);
  };

  const handleUpdateInformationFormSubmit = async () => {
    if (access_token) {
      let org = "";

      switch (role) {
        case "Bệnh nhân":
          org = "patient";
          break;
        case "Bác sĩ":
          org = "doctor";
          break;
        case "Cơ sở y tế":
          org = "medical_institution";
          break;
        case "Trung tâm nghiên cứu":
          org = "research_center";
          break;
        case "Nhà khoa học":
          org = "scientist";
          break;
        case "Công ty sản xuất thuốc":
          org = "manufacturer";
          break;
        case "Nhà thuốc":
          org = "drugstore";
          break;
        case "Quản trị viên":
          org = "admin";
          break;
        default:
          org = "";
      }
    
      let apiGetUserInfo = "/api/" + org + "/get-user-info";

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

      for (const key in valuesForm) {
        formData.append(key, valuesForm[key]);
      }

      console.log(formData);

      try {
        const response = await fetch(apiUpdateInformation, {
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
            "Đã cập nhật thông tin thành công!",
            handleCancel
          );
        }
      } catch (e) {
        openNotification(
          "topRight",
          "error",
          "Thất bại",
          "Đã có lỗi xảy ra trong quá trình cập nhật thông tin!",
          handleCancel
        );
      }
    }
  };

  return (
    <Context.Provider value={"Cập nhật thông tin"}>
      {contextHolder}
      <UpdateInformationDialogStyle>
        <>
          <ModalWrapper
            title="Cập nhật thông tin"
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
              onFinish={handleConfirm}
              onFinishFailed={onFinishFailed}
              autoComplete="on"
              form={form}
            >
              <Form.Item label="ID người dùng" name="id">
                <Input disabled />
              </Form.Item>

              <Form.Item label="Tên người dùng" name="fullName">
                <Input />
              </Form.Item>

              <Form.Item label="Email" name="email">
                <Input disabled />
              </Form.Item>

              <Form.Item label="Địa chỉ" name="address">
                <Input />
              </Form.Item>

              {(role === "Bệnh nhân" ||
                role === "Bác sĩ" ||
                role === "Nhà khoa học") && (
                <Form.Item label="Ngày sinh" name="dateBirthday">
                  <DatePicker style={{ width: "100%" }} format={"YYYY-MM-DD"} />
                </Form.Item>
              )}

              {(role === "Bệnh nhân" ||
                role === "Bác sĩ" ||
                role === "Nhà khoa học") && (
                <Form.Item label="Giới tính" name="gender">
                  <Select
                    style={{ width: "100%" }}
                    options={[
                      { value: "Nam", label: "Nam" },
                      { value: "Nữ", label: "Nữ" },
                    ]}
                  />
                </Form.Item>
              )}

              <Form.Item label="Vai trò" name="role">
                <Input disabled />
              </Form.Item>

              {data.department && (
                <Form.Item label="Chuyên khoa" name="department">
                  <Input />
                </Form.Item>
              )}

              {(role === "Nhà thuốc" ||
                role === "Công ty sản xuất thuốc") && (
                <Form.Item
                  label="Giấy phép kinh doanh"
                  name="businessLicenseNumber"
                >
                  <Input />
                </Form.Item>
              )}

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
                  Cập nhật thông tin
                </Button>
              </div>
            </Form>
          </ModalWrapper>
        </>

        <ConfirmModal
          isOpen={isConfirmModalOpen}
          handleOk={handleUpdateInformationFormSubmit}
          handleCancel={handleConfirmModalCancel}
          title="Xác nhận"
          content="Bạn có chắc chắn không?"
        />
      </UpdateInformationDialogStyle>
    </Context.Provider>
  );
};

export default UpdateInformationDialog;
