import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Space } from "antd";
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
  List,
  Typography,
} from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const DrugListStyle = styled.div`
  overflow: auto;
`;

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

const StyledList = styled(List)`
  .ant-list-items > .ant-list-item:nth-child(odd) {
    background-color: rgb(246, 255, 237);
  }
  .ant-list-items > .ant-list-item:nth-child(even) {
    background-color: rgb(230, 230, 230);
  }
`;

const DrugList = ({ data, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  return (
    <DrugListStyle>
      <ModalWrapper
        title="Danh sách thuốc được tạo ra"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"65%"}
        // loading={loading}
      >
        <StyledList
          bordered
          dataSource={data}
          renderItem={(item) => (
            <List.Item>
              <div style={{ width: "100%" }}>
                <Info>
                  <div className="field">ID thuốc</div>
                  <div>{item.drugId}</div>
                </Info>

                <Info>
                  <div className="field">ID loại thuốc</div>
                  <div>{item.medicationId}</div>
                </Info>

                <Info>
                  <div className="field">Đơn vị</div>
                  <div>{item.unit}</div>
                </Info>

                <Info>
                  <div className="field">Ngày sản xuất</div>
                  <div>{item.manufactureDate}</div>
                </Info>

                <Info>
                  <div className="field">Ngày hết hạn</div>
                  <div>{item.expirationDate}</div>
                </Info>
              </div>
            </List.Item>
          )}
        />

        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
          }}
        ></div>
      </ModalWrapper>
    </DrugListStyle>
  );
};

export default DrugList;
