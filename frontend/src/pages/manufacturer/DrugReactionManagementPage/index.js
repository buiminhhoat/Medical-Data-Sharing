import { memo, useEffect, useState } from "react";
import styled from "styled-components";
import styleTheme from "../../../styles/pages/theme";
import {
  Button,
  ConfigProvider,
  Space,
  Table,
  Tag,
  Input,
  Card,
  message,
  DatePicker,
  Popover,
  QRCode,
} from "antd";
import {
  InfoCircleOutlined,
  EditOutlined,
  SearchOutlined,
  ClearOutlined,
} from "@ant-design/icons";
import { Calendar, theme } from "antd";
import { useCookies } from "react-cookie";
import { API } from "@Const";
import { DIALOGS } from "@Const";
import AddDrugDialog from "../../../components/dialogs/AddDrug/AddDrug";
import ScanInput from "../../../components/ScanInput/ScanInput";
import DrugReactionDetail from "../../../components/dialogs/DrugReactionDetail/DrugReactionDetail";
// import AddDrugDialog from "../../../components/dialogs/AddDrug/AddDrug";

const DrugReactionManagementPageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const DrugReactionManagementPage = () => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const apiGetListDrugReactionByManufacturer =
    API.MANUFACTURER.GET_LIST_DRUG_REACTION_BY_MANUFACTURER;

  const [searchPrescriptionId, setSearchPrescriptionId] = useState("");
  const [searchMedicationId, setSearchMedicationId] = useState("");
  const [searchPatientId, setSearchPatientId] = useState("");

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    console.log("*");

    json.map((record, index) => {
      record.shortenedPrescriptionId =
        record.prescriptionId.substring(0, 4) +
        "..." +
        record.prescriptionId.substring(record.prescriptionId.length - 4);
      record.key = index;

      record.shortenedMedicationId =
        record.medicationId.substring(0, 4) +
        "..." +
        record.medicationId.substring(record.medicationId.length - 4);

      record.shortenedPatientId =
        record.patientId.substring(0, 4) +
        "..." +
        record.patientId.substring(record.patientId.length - 4);
    });

    setDataSource(json);
    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchPrescriptionId);
    const filteredData = data.filter((entry) => {
      const matchesPrescriptionId = searchPrescriptionId
        ? entry.prescriptionId
            .toLowerCase()
            .includes(searchPrescriptionId.toLowerCase())
        : true;
      const matchesMedicationId = searchMedicationId
        ? entry.medicationId
            .toLowerCase()
            .includes(searchMedicationId.toLowerCase())
        : true;

      const matchesPatientId = searchPatientId
        ? entry.patientId.toLowerCase().includes(searchPatientId.toLowerCase())
        : true;
      return matchesPrescriptionId & matchesMedicationId & matchesPatientId;
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchPrescriptionId("");
    setSearchMedicationId("");
  };

  console.log(apiGetListDrugReactionByManufacturer);

  const fetchGetListDrugReactionByManufacturer = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetListDrugReactionByManufacturer, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        });

        if (response.status === 200) {
          const json = await response.json();
          setData(json);
          handleData(json);
          console.log(json);
        }
      } catch (e) {}
    }
  };

  useEffect(() => {
    if (access_token) fetchGetListDrugReactionByManufacturer().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [searchPrescriptionId, searchMedicationId, searchPatientId]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedPrescription, setSelectedRequest] = useState(null);

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

  const openPrescriptionDetail = (prescription) => {
    console.log("openPrescriptionDetail");
    console.log(prescription);
    openModal(DIALOGS.DRUG_REACTION_DETAIL);
    setSelectedRequest(prescription);
  };

  const openAddDrug = () => {
    openModal(DIALOGS.ADD_DRUG);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const [highlightedTextMedicationId, setHighlightedTextMedicationId] =
    useState(null);

  const [highlightedTextPatientId, setHighlightedTextPatientId] =
    useState(null);
  const columns = [
    {
      title: "ID loại thuốc",
      dataIndex: "shortenedMedicationId",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.medicationId.indexOf(value) === 0,
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
      title: "ID đơn thuốc",
      dataIndex: "shortenedPrescriptionId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "15%",
      align: "center",
      onFilter: (value, record) =>
        record.shortenedPrescriptionId.indexOf(value) === 0,
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.prescriptionId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedText(index)}
            onMouseLeave={() => setHighlightedText(null)}
            style={{
              backgroundColor: highlightedText === index ? "#ffe898" : "",
              border:
                highlightedText === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.prescriptionId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.prescriptionId)
                .then(() =>
                  message.success("Đã sao chép " + record.prescriptionId)
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
      title: "ID bệnh nhân",
      dataIndex: "shortenedPatientId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "15%",
      align: "center",
      onFilter: (value, record) =>
        record.shortenedPatientId.indexOf(value) === 0,
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.patientId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedTextPatientId(index)}
            onMouseLeave={() => setHighlightedTextPatientId(null)}
            style={{
              backgroundColor:
                highlightedTextPatientId === index ? "#ffe898" : "",
              border:
                highlightedTextPatientId === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.patientId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.patientId)
                .then(() => message.success("Đã sao chép " + record.patientId))
                .catch((err) => message.error("Sao chép thất bại!"));
            }}
          >
            {text}
          </span>
        </Popover>
      ),
    },
    {
      title: "Phản ứng thuốc của bệnh nhân",
      dataIndex: "drugReaction",
      width: "40%",
      align: "center",
    },
    {
      title: "Hành động",
      dataIndex: "action",
      width: "10%",
      align: "center",
      render: (index, record) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openPrescriptionDetail(record)}
            >
              Chi tiết
            </Button>
          </Space>
        );
      },
    },
  ];

  const [dataSource, setDataSource] = useState([]);

  const onChange = (pagination, filters, sorter, extra) => {
    console.log("params", pagination, filters, sorter, extra);
  };

  const { token } = theme.useToken();
  const wrapperStyle = {
    width: 300,
    border: `1px solid ${token.colorBorderSecondary}`,
    borderRadius: token.borderRadiusLG,
  };

  const onPanelChange = (value, mode) => {
    console.log(value.format("YYYY-MM-DD"), mode);
  };

  return (
    <DrugReactionManagementPageStyle>
      <div className="page">
        <div className="container">
          <div style={{ marginTop: "20px" }}>
            <h1>Tìm kiếm</h1>
            <Card
              style={{
                border: "none",
                background: "white",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <div
                  style={{
                    width: "100%",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <div
                    style={{
                      width: "100%",
                      justifyContent: "center",
                      alignItems: "center",
                    }}
                  >
                    <div
                      style={{
                        width: "100%",
                        display: "flex",
                        justifyContent: "center",
                        marginBottom: "20px",
                      }}
                    >
                      <div style={{ width: "30%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchMedicationId}
                          setValue={setSearchMedicationId}
                          placeholder="ID loại thuốc"
                        />
                      </div>
                      <div style={{ width: "30%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchPrescriptionId}
                          setValue={setSearchPrescriptionId}
                          placeholder="ID đơn thuốc"
                        />
                      </div>

                      <div style={{ width: "30%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchPatientId}
                          setValue={setSearchPatientId}
                          placeholder="Mã bệnh nhân"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <div>
                  <div>
                    <ConfigProvider
                      theme={{
                        token: {},
                      }}
                    >
                      <Button
                        icon={<SearchOutlined />}
                        style={{
                          backgroundColor: `${styleTheme.colors.green}`,
                          color: "white",
                          fontWeight: 600,
                          width: "100%",
                        }}
                        onClick={handleSearch}
                      >
                        Tìm kiếm
                      </Button>
                    </ConfigProvider>
                  </div>

                  <div style={{ marginTop: "5%" }}>
                    <Button
                      icon={<ClearOutlined />}
                      style={{
                        backgroundColor: "red",
                        color: "white",
                        fontWeight: 600,
                        width: "100%",
                      }}
                      onClick={handleDelete}
                    >
                      Xóa bộ lọc
                    </Button>
                  </div>
                </div>
              </div>
            </Card>
          </div>

          <div style={{ width: "100%", marginTop: "20px" }}>
            <div style={{ display: "flex" }}>
              <div>
                <h1>Danh sách phản hồi</h1>
              </div>
            </div>
            <ConfigProvider
              theme={{
                token: {
                  borderRadius: 6,
                },
              }}
            >
              <Table
                columns={columns}
                dataSource={dataSource}
                onChange={onChange}
                loading={loading}
                showSorterTooltip={{
                  target: "sorter-icon",
                }}
              />
            </ConfigProvider>
          </div>
        </div>
      </div>

      {openDialog === DIALOGS.DRUG_REACTION_DETAIL && (
        <div className="modal-overlay">
          <DrugReactionDetail
            prescriptionId={selectedPrescription.prescriptionId}
            patientId={selectedPrescription.patientId}
            medicalRecordId={selectedPrescription.medicalRecordId}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </DrugReactionManagementPageStyle>
  );
};

export default memo(DrugReactionManagementPage);
