import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Storage from '@Utils/Storage';
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Flex, Space, Table } from "antd";
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
import { Alert, notification } from "antd";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
import ScanInput from "../../ScanInput/ScanInput";

const { Option } = Select;

const TransferDrugStyle = styled.div`
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

const Context = React.createContext({
  name: "TransferDrug",
});

const TransferDrug = ({ onClose, onSwitch }) => {
  const { access_token, userId, role } = Storage.getData();

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

  const [data, setData] = useState([]);

  const handleData = (json) => {
    console.log("*");

    json.map((record, index) => {
      record.shortenedDrugId =
        record.drugId.substring(0, 4) +
        "..." +
        record.drugId.substring(record.drugId.length - 4);
      record.key = index;

      record.shortenedMedicationId =
        record.medicationId.substring(0, 4) +
        "..." +
        record.medicationId.substring(record.medicationId.length - 4);
    });

    setData(json);
  };

  const [drugStoreId, setDrugStoreId] = useState();
  const [isModalOpen, setIsModalOpen] = useState(true);
  const [selectedDrugIds, setSelectedDrugIds] = useState([]);
  const [disabled, setDisabled] = useState(false);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
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

  const apiGetListDrugByOwnerId = "/api/" + org + "/get-list-drug-by-ownerId";
  const fetchGetListDrugByOwnerId = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetListDrugByOwnerId, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        });

        if (response.status === 200) {
          const json = await response.json();
          // setData(json);
          handleData(json);
          console.log("data: ", data);
        }
      } catch (e) {}
    }
  };

  const SellingDrug = async () => {
    console.log("Selling Drug");
    console.log("DrugStoreID: ", drugStoreId);
    console.log("Selected DrugIds: ", selectedDrugIds);

    openNotification(
      "topRight",
      "info",
      "Đã gửi yêu cầu",
      "Hệ thống đã tiếp nhận yêu cầu!"
    );
    
    try {
        const formData = new FormData();
        formData.append("drugStoreId", drugStoreId);
        formData.append("drugIds", JSON.stringify(selectedDrugIds));
        const transferDrugUrl = "/api/" + org + "/transfer-drugs";
        const response = await fetch(transferDrugUrl, {
            method: 'POST',
            headers: {
              Authorization: `Bearer ${access_token}`,
            },
            body: formData,
        });

        if (response.ok) {
            const data = await response.json();
            console.log("Success:", data);
            openNotification(
              "topRight",
              "success",
              "Thành công",
              "Bán thuốc thành công!",
              handleCancel
            );
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra!",
            handleCancel
          );
          console.log("Error: ", response.status);
        }
    } catch (error) {
        console.error("Request failed:", error);
    }

  };

  useEffect(() => {
    fetchGetListDrugByOwnerId();
  }, [])
  return (
    <Context.Provider value={"Bán thuốc"}>
      {contextHolder}
      <TransferDrugStyle>
        <ModalWrapper
          title="Bán thuốc"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"50%"}
          // mask={false}
          // loading={loading}
        >
          <List>
            <List.Item>
              <div style={{ width: "100%" }}>
                <Info>
                  <div className="field">ID nhà thuốc</div>
                  <ScanInput
                    value={drugStoreId}
                    setValue={setDrugStoreId}
                    placeholder="ID nhà thuốc"
                  />
                </Info>

                <Info>
                  <div className="field">ID sản phẩm thuốc</div>
                  <Select
                    mode="multiple"
                    allowClear
                    style={{ width: "100%" }}
                    placeholder="Chọn ID thuốc"
                    value={selectedDrugIds}  // Bind the selected values
                    onChange={(value) => setSelectedDrugIds(value)}  // Handle the change event
                  >
                    {data.map((drug) => (
                      <Option key={drug.drugId} value={drug.drugId}>
                        {drug.shortenedDrugId}
                      </Option>
                    ))}
                  </Select>
                </Info>
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
            <Button
              style={{ marginRight: "3%" }}
              onClick={SellingDrug}
              disabled={disabled}
            >
              Bán thuốc
            </Button>
          </div>
        </ModalWrapper>
      </TransferDrugStyle>
    </Context.Provider>
  );
};

export default TransferDrug;
