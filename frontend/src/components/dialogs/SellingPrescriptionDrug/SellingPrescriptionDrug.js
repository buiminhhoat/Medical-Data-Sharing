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
import { Alert, notification } from "antd";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";

const { Option } = Select;

const SellingPrescriptionDrugStyle = styled.div`
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
  name: "SellingPrescriptionDrug",
});

const SellingPrescriptionDrug = ({
  prescriptionId,
  patientId,
  onClose,
  onSwitch,
}) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;

  let apiGetPrescriptionByDrugStore =
    API.DRUGSTORE.GET_PRESCRIPTION_BY_DRUG_STORE;

  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [loadingTable, setLoadingTable] = useState(true);

  const [disabled, setDisabled] = useState(false);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const fetchGetPrescriptionByPrescriptionId = async () => {
    if (access_token) {
      const formData = new FormData();
      formData.append("prescriptionId", prescriptionId);

      try {
        const response = await fetch(apiGetPrescriptionByDrugStore, {
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
    if (data != null && data.prescriptionDetailsListDto != null) {
      data.prescriptionDetailsListDto.map((prescriptionDetail) => {
        data.prescriptionDetailsListDto.options = getListDrug(
          prescriptionDetail.medicationId
        );
      });
      console.log(data);
    }
  }, [data]);

  useEffect(() => {
    if (access_token) fetchGetPrescriptionByPrescriptionId().then((r) => {});
  }, [access_token]);

  let apiGetListDrugByMedicationIdAndOwnerId =
    API.DRUGSTORE.GET_LIST_DRUG_BY_MEDICATIONID_AND_OWNERID;
  const getListDrug = async (medicationId) => {
    const formData = new FormData();
    formData.append("medicationId", medicationId);
    formData.append("ownerId", userId);

    let options = [];
    try {
      const response = await fetch(apiGetListDrugByMedicationIdAndOwnerId, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${access_token}`,
        },
        body: formData,
      });

      if (response.status === 200) {
        let data = await response.json();
        data.map((drug) => {
          options.push({
            label:
              drug.drugId.substring(0, 12) +
              "..." +
              drug.drugId.substring(drug.drugId.length - 12),
            value: drug.drugId,
          });
        });
        return options;
      }
    } catch (e) {
      console.log(e);
    }
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

  useEffect(() => {
    const fetchData = async () => {
      if (data && Array.isArray(data.prescriptionDetailsListDto)) {
        try {
          const mappedData = await Promise.all(
            data.prescriptionDetailsListDto.map(async (item) => {
              const options = await getListDrug(item.medicationId);
              return {
                key: item.medicationId,
                shortenMedicationId:
                  item.medicationId.substring(0, 8) +
                  "..." +
                  item.medicationId.substring(item.medicationId.length - 8),
                medicationId: item.medicationId,
                medicationName: item.medicationName,
                quantity: item.quantity,
                purchasedQuantity: item.purchasedQuantity,
                details: item.details,
                options: options,
                prescriptionDetailId: item.prescriptionDetailId,
              };
            })
          );
          setDataSource(mappedData);
          setLoadingTable(false);
        } catch (error) {
          console.error("Failed to fetch drug options", error);
        }
        setLoading(false);
      }
    };

    fetchData();
  }, [data]);

  const [dataSource, setDataSource] = useState("");

  const [form, setForm] = useState();
  const handleChange = (value, medicationId) => {
    // console.log("dataSource: ", dataSource);
    // console.log("value: ", value);
    // console.log("medicationId: ", medicationId);
    const newData = dataSource.map((item) => {
      if (item.medicationId === medicationId) {
        return { ...item, drugIdList: value };
      }
      return item;
    });
    console.log("newData: ", newData);
    setForm(newData);
  };

  const SellingDrug = async () => {
    console.log("form: ", form);
    console.log("dataSource: ", dataSource);
    if (access_token && !loading && !loadingTable) {
      setDisabled(true);
      let sellingPrescriptionDrug = [];
      if (form != null) {
        form.map((item) => {
          console.log("item: ", item);
          sellingPrescriptionDrug.push({
            medicationId: item.medicationId,
            prescriptionDetailId: item.prescriptionDetailId,
            drugIdList: item.drugIdList,
          });
        });
      }

      const formData = new FormData();
      formData.append(
        "sellingPrescriptionDrug",
        JSON.stringify(sellingPrescriptionDrug)
      );

      formData.append("patientId", patientId);
      formData.append("prescriptionId", prescriptionId);

      console.log("sellingPrescriptionDrug: ", sellingPrescriptionDrug);

      console.log(access_token);
      setLoading(true);
      setLoadingTable(true);
      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );

      let apiAddPurchase = API.DRUGSTORE.ADD_PURCHASE;

      try {
        console.log("***");
        const response = await fetch(apiAddPurchase, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
          console.log("data: ", data);
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
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  const columns = [
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
      title: "ID thuốc được bán",
      dataIndex: "drugList",
      key: "details",
      width: "30%",
      align: "center",
      render: (_, record) => {
        console.log("record: ", record);
        return (
          <Select
            mode="multiple"
            allowClear
            style={{ width: "100%" }}
            placeholder="Chọn ID thuốc"
            onChange={(value) => handleChange(value, record.medicationId)}
            options={record.options}
          ></Select>
        );
      },
    },
  ];

  console.log(dataSource);

  return (
    <Context.Provider value={"Bán thuốc"}>
      {contextHolder}
      <SellingPrescriptionDrugStyle>
        <ModalWrapper
          title="Bán thuốc"
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
            loading={loadingTable}
          />
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
      </SellingPrescriptionDrugStyle>
    </Context.Provider>
  );
};

export default SellingPrescriptionDrug;
