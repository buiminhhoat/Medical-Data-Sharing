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

const RequestDetailStyle = styled.div``;

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

const RequestDetail = ({ request, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [senderID, setSenderId] = useState("");
  const [senderName, setSenderName] = useState("");
  const [recipientID, setRecipientId] = useState("");
  const [recipientName, setRecipientName] = useState("");
  const [dateCreated, setDateCreated] = useState("");
  const [dateModified, setDateModified] = useState("");
  const [requestType, setRequestType] = useState("");
  const [requestStatus, setRequestStatus] = useState("");
  const [data, setData] = useState("");

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
      formData.append("requestId", request.requestId);
      formData.append("requestType", request.requestType);

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
      setSenderId(data.senderId);
      setSenderName(data.senderName);
      setRecipientId(data.recipientId);
      setRecipientName(data.recipientName);
      setDateCreated(data.dateCreated);
      setDateModified(data.dateModified);
      setRequestType(data.requestType);
      setRequestStatus(data.requestStatus);
    }
  }, [data]);

  return (
    <RequestDetailStyle>
      <Modal
        title="Chi tiết yêu cầu"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"50%"}
      >
        <div style={{ marginTop: "20px" }}>
          <Info>
            <div className="field">RequestID</div>
            <div>{request.requestId}</div>
          </Info>
          <Info>
            <div className="field">ID người gửi</div>
            <div>{senderID}</div>
          </Info>
          <Info>
            <div className="field">Tên người gửi</div>
            <div>{senderName}</div>
          </Info>

          <Info>
            <div className="field">ID người nhận</div>
            <div>{recipientID}</div>
          </Info>
          <Info>
            <div className="field">Tên người nhận</div>
            <div>{recipientName}</div>
          </Info>
          <Info>
            <div className="field">Ngày tạo</div>
            <div>{dateCreated}</div>
          </Info>

          <Info>
            <div className="field">Ngày chỉnh sửa</div>
            <div>{dateModified}</div>
          </Info>

          <Info>
            <div className="field">Loại yêu cầu</div>
            <div>{requestType}</div>
          </Info>

          <Info>
            <div className="field">Trạng thái</div>
            <div>{requestStatus}</div>
          </Info>
        </div>

        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
          }}
        >
          <Button style={{ marginRight: "5%" }}>Xem hồ sơ bệnh nhân</Button>
          <Button style={{ marginRight: "5%" }}>Cập nhật trạng thái</Button>
          <Button>Thêm hồ sơ y tế mới</Button>
        </div>
      </Modal>
    </RequestDetailStyle>
  );
};

export default RequestDetail;
