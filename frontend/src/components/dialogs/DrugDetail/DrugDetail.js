import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Storage from '@Utils/Storage';
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Popover, QRCode, Space, Table } from "antd";
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
  message,
} from "antd";
import { VscCommentUnresolved } from "react-icons/vsc";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
import TextWithQRCode from "../../TextWithQRCode/TextWithQRCode";
import { GATEWAY_IPFS } from "../../../utils/const";
const { Option } = Select;

const DrugDetailStyle = styled.div`
  overflow: auto;
`;

const Info = styled.div`
  display: flex;
  /* justify-content: center; */
  /* justify-items: center; */
  align-items: center;
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

const DrugDetail = ({ drug, onClose, onSwitch }) => {
  console.log("drug***: ", drug);
  const { access_token, userId, role } = Storage.getData();
  
  const drugId = drug.drugId;

  let apiGetMedication = API.PATIENT.GET_MEDICATION;

  if (role === "Bệnh nhân") {
    apiGetMedication = API.PATIENT.GET_MEDICATION;
  }

  if (role === "Nhà thuốc") {
    apiGetMedication = API.DRUGSTORE.GET_MEDICATION;
  }

  if (role === "Công ty sản xuất thuốc") {
    apiGetMedication = API.MANUFACTURER.GET_MEDICATION;
  }

  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const fetchGetMedication = async (medicationId) => {
    if (access_token) {
      const formData = new FormData();
      formData.append("medicationId", medicationId);
      console.log("medicationId: ", medicationId);
      try {
        const response = await fetch(apiGetMedication, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          const json = await response.json();
          setData(json);
          setLoading(false);
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetMedication(drug.medicationId).then((r) => {});
  }, [access_token]);

  return (
    <DrugDetailStyle>
      <ModalWrapper
        title="Chi tiết thuốc"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"60%"}
        loading={loading}
      >
        <List>
          <List.Item>
            <div style={{ width: "100%" }}>
              <Info>
                <div className="field">ID thuốc</div>
                <TextWithQRCode value={drugId}></TextWithQRCode>
              </Info>

              <Info>
                <div className="field">ID loại thuốc</div>
                <TextWithQRCode value={drug.medicationId}></TextWithQRCode>
              </Info>

              <Info>
                <div className="field">Tên loại thuốc</div>
                <div>{data?.medicationName}</div>
              </Info>

              <Info>
                <div className="field">ID công ty sản xuất thuốc</div>
                <div>{data?.manufacturerId}</div>
              </Info>

              <Info>
                <div className="field">Tên công ty sản xuất thuốc</div>
                <div>{data?.manufacturerName}</div>
              </Info>

              <Info>
                <div className="field">Đơn vị</div>
                <div>{drug.unit}</div>
              </Info>

              <Info>
                <div className="field">Ngày sản xuất</div>
                <div>{drug.manufactureDate}</div>
              </Info>

              <Info>
                <div className="field">Ngày hết hạn</div>
                <div>{drug.expirationDate}</div>
              </Info>

              <Info>
                <div className="field">Mô tả</div>
                <div>{data?.description}</div>
              </Info>

              {drug.hashFile && (
                    <Info>
                      <div className="field">File</div>
                      <a href={GATEWAY_IPFS + drug.hashFile} target="_blank">
                        {drug.hashFile}
                      </a>
                    </Info>
                  )}
            </div>
          </List.Item>
        </List>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
            marginTop: "1%",
          }}
        >
          {/* {drug.patientId === userId && (
            <>
              <Button style={{ marginRight: "3%" }}>Chia sẻ đơn thuốc</Button>
            </>
          )} */}
        </div>
      </ModalWrapper>
    </DrugDetailStyle>
  );
};

export default DrugDetail;
