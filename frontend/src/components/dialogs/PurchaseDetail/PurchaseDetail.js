import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
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
const { Option } = Select;

const PurchaseDetailStyle = styled.div`
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

const PurchaseDetail = ({ purchase, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;

  const purchaseId = purchase.purchaseId;

  let apiGetPurchaseByPurchaseId = API.PATIENT.GET_PURCHASE_BY_PURCHASE_ID;

  if (role === "Bệnh nhân") {
    apiGetPurchaseByPurchaseId = API.PATIENT.GET_PURCHASE_BY_PURCHASE_ID;
  }

  if (role === "Cửa hàng thuốc") {
    apiGetPurchaseByPurchaseId = API.DRUGSTORE.GET_PURCHASE_BY_PURCHASE_ID;
  }

  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const fetchGetPurchaseByPurchaseId = async () => {
    if (access_token) {
      const formData = new FormData();
      formData.append("purchaseId", purchaseId);
      console.log("purchaseId: ", purchaseId);
      try {
        const response = await fetch(apiGetPurchaseByPurchaseId, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
          setLoading(false);
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetPurchaseByPurchaseId().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    if (data) {
      console.log("data: ", data);
      setDataSource(
        data.purchaseDetailsList.map(
          (
            { purchaseDetailId, prescriptionDetailId, medicationId, drugId },
            index
          ) => ({
            key: index,
            purchaseDetailId: purchaseDetailId,
            shortenedPurchaseDetailId:
              purchaseDetailId.substring(0, 4) +
              "..." +
              purchaseDetailId.substring(purchaseDetailId.length - 4),
            prescriptionDetailId: prescriptionDetailId,
            shortenedPrescriptionDetailId:
              prescriptionDetailId.substring(0, 4) +
              "..." +
              prescriptionDetailId.substring(prescriptionDetailId.length - 6),
            medicationId: medicationId,
            shortenedMedicationId:
              medicationId.substring(0, 4) +
              "..." +
              medicationId.substring(medicationId.length - 4),
            drugId: drugId,
            shortenedDrugId:
              drugId.substring(0, 4) +
              "..." +
              drugId.substring(drugId.length - 4),
          })
        )
      );
      setLoading(false);
    }
  }, [data]);

  const [dataSource, setDataSource] = useState("");

  const [highlightedTextPurchaseDetailId, setHighlightedTextPurchaseDetailId] =
    useState();

  const [
    highlightedTextPrescriptionDetailId,
    setHighlightedTextPrescriptionDetailId,
  ] = useState();
  const [highlightedTextMedicationId, setHighlightedTextMedicationId] =
    useState();
  const [highlightedTextDrugId, setHighlightedTextDrugId] = useState();
  const columns = [
    {
      title: "ID chi tiết giao dịch",
      dataIndex: "shortenedPurchaseDetailId",
      key: "shortenedPurchaseDetailId",
      width: "15%",
      align: "center",
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.purchaseDetailId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedTextPurchaseDetailId(index)}
            onMouseLeave={() => setHighlightedTextPurchaseDetailId(null)}
            style={{
              backgroundColor:
                highlightedTextPurchaseDetailId === index ? "#ffe898" : "",
              border:
                highlightedTextPurchaseDetailId === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.purchaseDetailId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.purchaseDetailId)
                .then(() =>
                  message.success("Đã sao chép " + record.purchaseDetailId)
                )
                .catch((err) => message.error("Sao chép thất bại!"));
            }}
          >
            {text}
          </span>
        </Popover>
      ),
    },
    {
      title: "ID chi tiết đơn thuốc",
      dataIndex: "shortenedPrescriptionDetailId",
      key: "shortenedPrescriptionDetailId",
      width: "15%",
      align: "center",
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.prescriptionDetailId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedTextPrescriptionDetailId(index)}
            onMouseLeave={() => setHighlightedTextPrescriptionDetailId(null)}
            style={{
              backgroundColor:
                highlightedTextPrescriptionDetailId === index ? "#ffe898" : "",
              border:
                highlightedTextPrescriptionDetailId === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.prescriptionDetailId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.prescriptionDetailId)
                .then(() =>
                  message.success("Đã sao chép " + record.prescriptionDetailId)
                )
                .catch((err) => message.error("Sao chép thất bại!"));
            }}
          >
            {text}
          </span>
        </Popover>
      ),
    },
    {
      title: "ID loại thuốc",
      dataIndex: "shortenedMedicationId",
      key: "shortenedMedicationId",
      width: "15%",
      align: "center",
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.medicationId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedTextMedicationId(index)}
            onMouseLeave={() => setHighlightedTextMedicationId(null)}
            style={{
              backgroundColor:
                highlightedTextMedicationId === index ? "#ffe898" : "",
              border:
                highlightedTextMedicationId === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.medicationId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.medicationId)
                .then(() =>
                  message.success("Đã sao chép " + record.medicationId)
                )
                .catch((err) => message.error("Sao chép thất bại!"));
            }}
          >
            {text}
          </span>
        </Popover>
      ),
    },
    {
      title: "ID thuốc đã mua",
      dataIndex: "drugId",
      key: "drugId",
      width: "20%",
      align: "center",
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.drugId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedTextDrugId(index)}
            onMouseLeave={() => setHighlightedTextDrugId(null)}
            style={{
              backgroundColor: highlightedTextDrugId === index ? "#ffe898" : "",
              border:
                highlightedTextDrugId === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.drugId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.drugId)
                .then(() => message.success("Đã sao chép " + record.drugId))
                .catch((err) => message.error("Sao chép thất bại!"));
            }}
          >
            {text}
          </span>
        </Popover>
      ),
    },
  ];

  return (
    <PurchaseDetailStyle>
      <ModalWrapper
        title="Chi tiết giao dịch"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"70%"}
        loading={loading}
      >
        <List>
          <List.Item>
            <div style={{ width: "100%" }}>
              <Info>
                <div className="field">ID giao dịch</div>
                <TextWithQRCode value={purchaseId}></TextWithQRCode>
              </Info>

              <Info>
                <div className="field">ID bệnh nhân</div>
                <TextWithQRCode value={purchase.patientId}></TextWithQRCode>
              </Info>

              <Info>
                <div className="field">ID cửa hàng thuốc</div>
                <TextWithQRCode value={purchase.drugStoreId}></TextWithQRCode>
              </Info>

              <Info>
                <div className="field">Tên cửa hàng thuốc</div>
                <div>{purchase.drugStoreName}</div>
              </Info>

              <Info>
                <div className="field">ID đơn thuốc</div>
                <TextWithQRCode
                  value={purchase.prescriptionId}
                ></TextWithQRCode>
              </Info>

              <Info>
                <div className="field">Ngày tạo</div>
                <div>{purchase.dateCreated}</div>
              </Info>

              <Info>
                <div className="field">Ngày chỉnh sửa</div>
                <div>{purchase.dateModified}</div>
              </Info>
            </div>
          </List.Item>
        </List>
        <Table
          dataSource={dataSource}
          columns={columns}
          title={() => (
            <p style={{ fontWeight: "600", paddingLeft: "0" }}>
              Chi tiết giao dịch
            </p>
          )}
          loading={loading}
        />
        ;
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
            marginTop: "1%",
          }}
        >
          {/* {purchase.patientId === userId && (
            <>
              <Button style={{ marginRight: "3%" }}>Chia sẻ đơn thuốc</Button>
            </>
          )} */}
        </div>
      </ModalWrapper>
    </PurchaseDetailStyle>
  );
};

export default PurchaseDetail;
