import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
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
import SharePrescriptionDialog from "../SharePrescriptionDialog/SharePrescriptionDialog";
import UpdateDrugReactionDialog from "../UpdateDrugReactionDialog/UpdateDrugReaction";
const { Option } = Select;

const PrescriptionDetailStyle = styled.div`
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

const PrescriptionDetail = ({ prescriptionId, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;

  let apiGetPrescriptionByPrescriptionId =
    API.PATIENT.GET_PRESCRIPTION_BY_PRESCRIPTION_ID;

  if (role === "Cửa hàng thuốc") {
    apiGetPrescriptionByPrescriptionId =
      API.DRUGSTORE.GET_PRESCRIPTION_BY_DRUG_STORE;
  }

  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const [openDialog, setOpenDialog] = useState(null);
  const handleDialogSwitch = (dialogName) => {
    openModal(dialogName);
  };

  const handleDialogClose = () => {
    closeModal();
  };

  const openModal = (dialogName) => {
    setOpenDialog(dialogName);
  };

  const closeModal = () => {
    setOpenDialog(null);
  };

  const fetchGetPrescriptionByPrescriptionId = async () => {
    if (access_token) {
      const formData = new FormData();
      formData.append("prescriptionId", prescriptionId);

      try {
        const response = await fetch(apiGetPrescriptionByPrescriptionId, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
          console.log(data);
          setLoading(false);
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetPrescriptionByPrescriptionId().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    if (data) {
      setDataSource(
        data.prescriptionDetailsListDto.map(
          (
            {
              prescriptionDetailId,
              medicationId,
              medicationName,
              quantity,
              purchasedQuantity,
              details,
            },
            index
          ) => ({
            key: index,
            // prescriptionDetailId:
            //   prescriptionDetailId.substring(0, 4) +
            //   "..." +
            //   prescriptionDetailId.substring(prescriptionDetailId.length - 4),
            shortenMedicationId:
              medicationId.substring(0, 4) +
              "..." +
              medicationId.substring(medicationId.length - 4),
            medicationName: medicationName,
            quantity: quantity,
            purchasedQuantity: purchasedQuantity,
            details: details,
          })
        )
      );
      setLoading(false);
    }
  }, [data]);

  const [dataSource, setDataSource] = useState("");

  const columns = [
    // {
    //   title: "ID chi tiết đơn thuốc",
    //   dataIndex: "prescriptionDetailId",
    //   key: "prescriptionDetailId",
    //   width: "15%",
    // },
    {
      title: "ID loại thuốc",
      dataIndex: "shortenMedicationId",
      key: "shortenMedicationId",
      width: "15%",
      align: "center",
    },
    {
      title: "Tên loại thuốc",
      dataIndex: "medicationName",
      key: "medicationName",
      width: "15%",
      align: "center",
    },
    {
      title: "Số lượng được mua",
      dataIndex: "quantity",
      key: "quantity",
      width: "15%",
      align: "center",
    },
    {
      title: "Số lượng đã mua",
      dataIndex: "purchasedQuantity",
      key: "purchasedQuantity",
      width: "15%",
      align: "center",
    },
    {
      title: "Cách sử dụng",
      dataIndex: "details",
      key: "details",
      width: "30%",
      align: "center",
    },
  ];

  const onClickSharePrescription = () => {
    openModal(DIALOGS.SHARE_PRESCRIPTION);
  };

  const onClickUpdateDrugReaction = () => {
    openModal(DIALOGS.UPDATE_DRUG_REACTION);
  };

  return (
    <PrescriptionDetailStyle>
      <Modal
        title="Đơn thuốc"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"70%"}
        // loading={loading}
      >
        <List>
          <List.Item>
            <div style={{ width: "100%" }}>
              <Info>
                <div className="field">ID đơn thuốc</div>
                <div>{prescriptionId}</div>
              </Info>

              {/* <Info>
                <div className="field">ID bác sĩ</div>
                <div>{medicalRecord.doctorId}</div>
              </Info>

              <Info>
                <div className="field">Tên bác sĩ</div>
                <div>{medicalRecord.doctorName}</div>
              </Info> */}

              <Info>
                <div className="field">Phản ứng thuốc của bệnh nhân</div>
                <div>
                  {data != null && data.drugReaction != null
                    ? data.drugReaction
                    : ""}
                </div>
              </Info>
            </div>
          </List.Item>
        </List>
        <Table
          dataSource={dataSource}
          columns={columns}
          title={() => (
            <p style={{ fontWeight: "600", paddingLeft: "0" }}>
              Chi tiết đơn thuốc
            </p>
          )}
          loading={loading}
        />
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
            marginTop: "1%",
          }}
        >
          {role === "Bệnh nhân" && (
            <>
              <Button
                style={{ marginRight: "3%" }}
                onClick={onClickSharePrescription}
              >
                Chia sẻ đơn thuốc
              </Button>

              <Button
                style={{ marginRight: "3%" }}
                onClick={onClickUpdateDrugReaction}
              >
                Cập nhật phản ứng thuốc
              </Button>
            </>
          )}
        </div>
      </Modal>

      {openDialog === DIALOGS.SHARE_PRESCRIPTION && (
        <div className="modal-overlay">
          <SharePrescriptionDialog
            prescriptionId={prescriptionId}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}

      {openDialog === DIALOGS.UPDATE_DRUG_REACTION && (
        <div className="modal-overlay">
          <UpdateDrugReactionDialog
            prescription={data}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </PrescriptionDetailStyle>
  );
};

export default PrescriptionDetail;
