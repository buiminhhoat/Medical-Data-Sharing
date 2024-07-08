import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import { Button, Modal, Checkbox, Form, Input, Select } from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
const { Option } = Select;

const MedicalRecordDialogStyle = styled.div``;

const Info = styled.div`
  display: flex;
  /* justify-content: center; */
  /* justify-items: center; */
  margin-bottom: 15px;
  .field {
    width: 20%;
    margin-right: 3%;
  }
`;

const MedicalRecordDialog = ({ patientId, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState("");
  const [loading, setLoading] = useState(true);

  const apiGetRequest = API.PUBLIC.GET_REQUEST;
  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const fetchGetRequest = async () => {
    if (access_token) {
      const formData = new FormData();
      // formData.append("requestId", request.requestId);
      // formData.append("requestType", request.requestType);

      console.log(access_token);

      try {
        console.log("***");
        const response = await fetch(apiGetRequest, {
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
    if (access_token) fetchGetRequest().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    if (data) {
      console.log(data);
      setLoading(false);
    }
  }, [data]);

  return (
    <MedicalRecordDialogStyle>
      <Modal
        title="Hồ sơ y tế"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"55%"}
        loading={loading}
      >
        <div style={{ marginTop: "20px", marginLeft: "20px" }}>
          <Info>
            <div className="field">Hồ sơ y tế</div>
            <div>Hồ sơ y tế</div>
          </Info>
        </div>

        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
          }}
        ></div>
      </Modal>
    </MedicalRecordDialogStyle>
  );
};

export default MedicalRecordDialog;
