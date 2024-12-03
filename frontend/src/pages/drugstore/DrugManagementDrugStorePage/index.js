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
} from "antd";
import {
  InfoCircleOutlined,
  EditOutlined,
  SearchOutlined,
  ClearOutlined,
} from "@ant-design/icons";
import { Calendar, theme } from "antd";
import Storage from '@Utils/Storage';
import { API } from "@Const";
import { DIALOGS } from "@Const";
import AddDrugDialog from "../../../components/dialogs/AddDrug/AddDrug";
import ScanInput from "../../../components/ScanInput/ScanInput";
import TransferDrug from "../../../components/dialogs/TransferDrug/TransferDrug";
// import AddDrugDialog from "../../../components/dialogs/AddDrug/AddDrug";

const DrugManagementDrugStorePageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const DrugManagementDrugStorePage = () => {
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

  const apiGetListDrugByOwnerId = "/api/" + org + "/get-list-drug-by-ownerId";

  const [searchDrugId, setSearchDrugId] = useState("");
  const [searchMedicationId, setSearchMedicationId] = useState("");
  const [searchManufactureDate, setSearchManufactureDate] = useState(null);
  const [searchExpirationDate, setSearchExpirationDate] = useState(null);

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

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

    setDataSource(json);
    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchDrugId);
    const filteredData = data.filter((entry) => {
      const matchesDrugId = searchDrugId
        ? entry.drugId.toLowerCase().includes(searchDrugId.toLowerCase())
        : true;
      const matchesMedicationId = searchMedicationId
        ? entry.medicationId
            .toLowerCase()
            .includes(searchMedicationId.toLowerCase())
        : true;

      const matchesManufactureDate = searchManufactureDate
        ? entry.manufactureDate
            .toLowerCase()
            .includes(searchManufactureDate.toLowerCase())
        : true;

      const matchesExpirationDate = searchExpirationDate
        ? entry.expirationDate
            .toLowerCase()
            .includes(searchExpirationDate.toLowerCase())
        : true;
      return (
        matchesDrugId &
        matchesManufactureDate &
        matchesMedicationId &
        matchesExpirationDate
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchDrugId("");
    setSearchMedicationId("");
    setSearchManufactureDate("");
    setSearchExpirationDate("");
  };

  console.log(apiGetListDrugByOwnerId);

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
          setData(json);
          handleData(json);
          console.log(json);
        }
      } catch (e) {}
    }
  };

  useEffect(() => {
    if (access_token) fetchGetListDrugByOwnerId().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [
    searchDrugId,
    searchMedicationId,
    searchManufactureDate,
    searchExpirationDate,
  ]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedMedication, setSelectedRequest] = useState(null);

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

  const openDrugDetail = (request) => {
    console.log("openDrugDetail");
    console.log(request);
    openModal(DIALOGS.DRUG_DETAIL);
    setSelectedRequest(request);
  };

  const openAddDrug = () => {
    openModal(DIALOGS.ADD_DRUG);
  };

  const openSellDrug = () => {
    console.log("SELL_DRUG");
    openModal(DIALOGS.SELL_DRUG);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const [highlightedTextMedicationId, setHighlightedTextMedicationId] =
    useState(null);
  const columns = [
    {
      title: "Mã thuốc",
      dataIndex: "shortenedDrugId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "10%",
      align: "center",
      onFilter: (value, record) => record.shortenedDrugId.indexOf(value) === 0,
      render: (text, record, index) => (
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
      ),
    },
    {
      title: "ID loại thuốc",
      dataIndex: "shortenedMedicationId",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.medicationId.indexOf(value) === 0,
      render: (text, record, index) => (
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
              .then(() => message.success("Đã sao chép " + record.medicationId))
              .catch((err) => message.error("Sao chép thất bại!"));
          }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Đơn vị",
      dataIndex: "unit",
      width: "15%",
      align: "center",
    },
    {
      title: "Ngày sản xuất",
      dataIndex: "manufactureDate",
      width: "15%",
      align: "center",
      sorter: (a, b) =>
        new Date(a.manufactureDate) - new Date(b.manufactureDate),
      sortDirections: ["descend", "ascend"],
      defaultSortOrder: "descend",
    },
    {
      title: "Ngày hết hạn",
      dataIndex: "expirationDate",
      width: "15%",
      align: "center",
      sorter: (a, b) => new Date(a.expirationDate) - new Date(b.expirationDate),
      sortDirections: ["descend", "ascend"],
      defaultSortOrder: "descend",
    },
    {
      title: "Hành động",
      dataIndex: "action",
      width: "20%",
      align: "center",
      render: (index, record) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openDrugDetail(record)}
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
    <DrugManagementDrugStorePageStyle>
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
                          value={searchDrugId}
                          setValue={setSearchDrugId}
                          placeholder="Mã thuốc"
                        />
                      </div>

                      <div style={{ width: "30%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchMedicationId}
                          setValue={setSearchMedicationId}
                          placeholder="ID loại thuốc"
                        />
                      </div>

                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD",
                        }}
                        placeholder="Ngày sản xuất"
                        // value={searchManufactureDate}
                        onChange={(_, value) => {
                          setSearchManufactureDate(value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD",
                        }}
                        placeholder="Ngày hết hạn"
                        onChange={(_, value) => {
                          setSearchExpirationDate(value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />
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
                <h1>Danh sách thuốc</h1>
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

      {/* {openDialog === DIALOGS.DRUG_DETAIL && (
        <div className="modal-overlay">
          <DrugDetail
            request={selectedMedication}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )} */}

      {openDialog === DIALOGS.ADD_DRUG && (
        <div className="modal-overlay">
          <AddDrugDialog
            values={selectedMedication}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}

      {openDialog === DIALOGS.SELL_DRUG && (
        <div className="modal-overlay">
          <TransferDrug
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </DrugManagementDrugStorePageStyle>
  );
};

export default memo(DrugManagementDrugStorePage);
