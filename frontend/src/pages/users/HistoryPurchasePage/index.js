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
import Storage from '@Utils/Storage';
import { API } from "@Const";
import { DIALOGS } from "@Const";
import PurchaseDetail from "../../../components/dialogs/PurchaseDetail/PurchaseDetail";
import SendRequestDialog from "../../../components/dialogs/SendRequest/SendRequest";
import ScanInput from "../../../components/ScanInput/ScanInput";

const HistoryPurchasePageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const HistoryPurchasePage = () => {
  const { access_token, userId, role } = Storage.getData();
  
  
  
  let apiGetListPurchase = API.PATIENT.GET_LIST_PURCHASE_BY_PATIENT_ID;

  console.log("role: ", role);
  if (role === "Bệnh nhân") {
    apiGetListPurchase = API.PATIENT.GET_LIST_PURCHASE_BY_PATIENT_ID;
  }

  if (role === "Nhà thuốc") {
    apiGetListPurchase = API.DRUGSTORE.GET_LIST_PURCHASE_BY_DRUGSTORE_ID;
  }

  const [searchPurchaseId, setSearchPurchaseId] = useState("");
  const [searchPatientId, setSearchPatientId] = useState("");
  const [searchDrugStoreId, setSearchDrugStoreId] = useState("");
  const [searchPatientName, setSearchPatientName] = useState("");
  const [searchDrugStoreName, setSearchDrugStoreName] = useState("");
  const [searchDateCreated, setSearchDateCreated] = useState(null);
  const [searchDateModified, setSearchDateModified] = useState(null);
  const [searchPrescriptionId, setSearchPrescriptionId] = useState("");

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    console.log("***");
    const prescriptionIdSet = new Set();

    json.map((record, index) => {
      record.shortenedPurchaseId =
        record.purchaseId.substring(0, 4) +
        "..." +
        record.purchaseId.substring(record.purchaseId.length - 4);
      record.shortenedPrescriptionId =
        record.prescriptionId.substring(0, 4) +
        "..." +
        record.prescriptionId.substring(record.prescriptionId.length - 4);

      record.shortenedPatientId =
        record.patientId.split('-')[1].substring(0, 4) +
        "..." +
        record.patientId.substring(record.patientId.length - 4);

      record.shortenedDrugStoreId =
        record.drugStoreId.split('-')[1].substring(0, 4) +
        "..." +
        record.drugStoreId.substring(record.drugStoreId.length - 4);

      record.key = index;
      prescriptionIdSet.add(record.prescriptionId);
    });

    setDataSource(json);
    const prescriptionIdArr = [];
    prescriptionIdSet.forEach((item) => {
      prescriptionIdArr.push({ value: item, text: item });
    });

    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchPurchaseId);
    // setLoading(true);
    const filteredData = data.filter((entry) => {
      const matchesPurchaseId = searchPurchaseId
        ? entry.purchaseId
            .toLowerCase()
            .includes(searchPurchaseId.toLowerCase())
        : true;
      const matchesPatientId = searchPatientId
        ? entry.patientId.toLowerCase().includes(searchPatientId.toLowerCase())
        : true;
      const matchesDrugStoreId = searchDrugStoreId
        ? entry.drugStoreId
            .toLowerCase()
            .includes(searchDrugStoreId.toLowerCase())
        : true;
      const matchesPatientName = searchPatientName
        ? entry.patientName
            .toLowerCase()
            .includes(searchPatientName.toLowerCase())
        : true;

      const matchesDrugStoreName = searchDrugStoreName
        ? entry.drugStoreName
            .toLowerCase()
            .includes(searchDrugStoreName.toLowerCase())
        : true;

      const matchesDateCreated = searchDateCreated
        ? entry.dateCreated
            .toLowerCase()
            .includes(searchDateCreated.toLowerCase())
        : true;

      const matchesDateModified = searchDateModified
        ? entry.dateModified
            .toLowerCase()
            .includes(searchDateModified.toLowerCase())
        : true;

      const matchesPrescriptionId = searchPrescriptionId
        ? entry.prescriptionId
            .toLowerCase()
            .includes(searchPrescriptionId.toLowerCase())
        : true;

      return (
        matchesPurchaseId &
        matchesPatientId &
        matchesDrugStoreId &
        matchesDateCreated &
        matchesPatientName &
        matchesDrugStoreName &
        matchesDateModified &
        matchesPrescriptionId
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchPurchaseId("");
    setSearchPatientId("");
    setSearchPurchaseId("");
    setSearchPatientName("");
    setSearchDrugStoreName("");
    setSearchDateCreated("");
    setSearchDateModified("");
    setSearchPrescriptionId("");
  };

  const fetchGetListPurchase = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetListPurchase, {
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
    if (access_token) fetchGetListPurchase().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [
    searchPurchaseId,
    searchPatientId,
    searchDrugStoreId,
    searchPatientName,
    searchDrugStoreName,
    searchDateCreated,
    searchDateModified,
    searchPrescriptionId,
  ]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedPurchase, setSelectedPurchase] = useState(null);

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

  const openPurchaseDetail = (purchase) => {
    console.log("openPurchaseDetail");
    console.log("purchase: ", purchase);
    openModal(DIALOGS.PURCHASE_DETAIL);
    setSelectedPurchase(purchase);
  };

  const openSendRequest = () => {
    openModal(DIALOGS.SEND_REQUEST);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const [
    highlightedTextShortenedPrescriptionId,
    setHighlightedTextShortenedPrescriptionId,
  ] = useState(null);
  const [
    highlightedTextShortenedPatientId,
    setHighlightedShortenedTextPatientId,
  ] = useState(null);
  const [
    highlightedTextShortenedDrugStoreId,
    setHighlightedTextShortenedDrugStoreId,
  ] = useState(null);
  const columns = [
    {
      title: "ID giao dịch",
      dataIndex: "shortenedPurchaseId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "10%",
      align: "center",
      onFilter: (value, record) =>
        record.shortenedPurchaseId.indexOf(value) === 0,
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.purchaseId}
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
              console.log(record.purchaseId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.purchaseId)
                .then(() => message.success("Đã sao chép " + record.purchaseId))
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
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.patientId.indexOf(value) === 0,
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
            onMouseEnter={() => setHighlightedShortenedTextPatientId(index)}
            onMouseLeave={() => setHighlightedShortenedTextPatientId(null)}
            style={{
              backgroundColor:
                highlightedTextShortenedPatientId === index ? "#ffe898" : "",
              border:
                highlightedTextShortenedPatientId === index
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
      hidden: role === "Bệnh nhân",
    },
    {
      title: "Tên bệnh nhân",
      dataIndex: "patientName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.patientName.indexOf(value) === 0,
      hidden: role === "Bệnh nhân",
    },
    {
      title: "ID cửa hàng thuốc",
      dataIndex: "shortenedDrugStoreId",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.drugStoreId.indexOf(value) === 0,
      render: (text, record, index) => (
        <Popover
          content={
            <QRCode
              type="canvas"
              value={record.drugStoreId}
              bordered={false}
              id="myqrcode"
              bgColor="#fff"
            />
          }
        >
          <span
            onMouseEnter={() => setHighlightedTextShortenedDrugStoreId(index)}
            onMouseLeave={() => setHighlightedTextShortenedDrugStoreId(null)}
            style={{
              backgroundColor:
                highlightedTextShortenedDrugStoreId === index ? "#ffe898" : "",
              border:
                highlightedTextShortenedDrugStoreId === index
                  ? "2px dashed rgb(234, 179, 8)"
                  : "none",
              borderRadius: "4px",
              padding: "2px",
              cursor: "pointer",
            }}
            onClick={() => {
              console.log(record.drugStoreId);
              console.log(index);
              console.log(dataSource);
              navigator.clipboard
                .writeText(record.drugStoreId)
                .then(() =>
                  message.success("Đã sao chép " + record.drugStoreId)
                )
                .catch((err) => message.error("Sao chép thất bại!"));
            }}
          >
            {text}
          </span>
        </Popover>
      ),
      hidden: role === "Nhà thuốc",
    },
    {
      title: "Tên cửa hàng thuốc",
      dataIndex: "drugStoreName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.drugStoreName.indexOf(value) === 0,
      hidden: role === "Nhà thuốc",
    },
    {
      title: "Ngày tạo",
      dataIndex: "dateCreated",
      width: "13%",
      align: "center",
      sorter: (a, b) => new Date(a.dateCreated) - new Date(b.dateCreated),
      sortDirections: ["descend", "ascend"],
      defaultSortOrder: "descend",
    },
    {
      title: "Ngày chỉnh sửa",
      dataIndex: "dateModified",
      width: "15%",
      align: "center",
      sorter: (a, b) => new Date(a.dateModified) - new Date(b.dateModified),
      sortDirections: ["descend", "ascend"],
      defaultSortOrder: "descend",
    },
    {
      title: "ID Đơn thuốc",
      dataIndex: "shortenedPrescriptionId",
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
            onMouseEnter={() =>
              setHighlightedTextShortenedPrescriptionId(index)
            }
            onMouseLeave={() => setHighlightedTextShortenedPrescriptionId(null)}
            style={{
              backgroundColor:
                highlightedTextShortenedPrescriptionId === index
                  ? "#ffe898"
                  : "",
              border:
                highlightedTextShortenedPrescriptionId === index
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
      title: "Hành động",
      dataIndex: "purchaseStatus",
      width: "20%",
      align: "center",
      render: (index, record) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openPurchaseDetail(record)}
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
    console.log(value.format("YYYY-MM-DD HH:mm"), mode);
  };

  console.log(searchDrugStoreId);

  return (
    <HistoryPurchasePageStyle>
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
                          value={searchPurchaseId}
                          setValue={setSearchPurchaseId}
                          placeholder="ID giao dịch"
                        />
                      </div>

                      <div style={{ width: "30%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchPatientId}
                          setValue={setSearchPatientId}
                          placeholder="Mã bệnh nhân"
                        />
                      </div>

                      <div style={{ width: "30%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchDrugStoreId}
                          setValue={setSearchDrugStoreId}
                          placeholder="Mã cửa hàng thuốc"
                        />
                      </div>
                    </div>

                    <div
                      style={{
                        width: "100%",
                        display: "flex",
                        justifyContent: "center",
                        marginBottom: "20px",
                      }}
                    >
                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD",
                        }}
                        placeholder="Ngày tạo"
                        // value={searchDateCreated}
                        onChange={(_, value) => {
                          setSearchDateCreated(value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Tên bệnh nhân"
                        value={searchPatientName}
                        onChange={(e) => {
                          setSearchPatientName(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Tên cửa hàng thuốc"
                        value={searchDrugStoreName}
                        onChange={(e) => {
                          setSearchDrugStoreName(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />
                    </div>

                    <div
                      style={{
                        width: "100%",
                        display: "flex",
                        justifyContent: "center",
                      }}
                    >
                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD",
                        }}
                        placeholder="Ngày chỉnh sửa"
                        onChange={(_, value) => {
                          setSearchDateModified(value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <div style={{ width: "62%", marginRight: "2%" }}>
                        <ScanInput
                          value={searchPrescriptionId}
                          setValue={setSearchPrescriptionId}
                          placeholder="ID đơn thuốc"
                        />
                      </div>

                      {/* <Input
                        placeholder="Trạng thái"
                        value={searchRequestStatus}
                        onChange={(e) => {
                          setSearchRequestStatus(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      /> */}
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
                <h1>Lịch sử giao dịch</h1>
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

      {openDialog === DIALOGS.PURCHASE_DETAIL && (
        <div className="modal-overlay">
          <PurchaseDetail
            purchase={selectedPurchase}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}

      {openDialog === DIALOGS.SEND_REQUEST && (
        <div className="modal-overlay">
          <SendRequestDialog
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </HistoryPurchasePageStyle>
  );
};

export default memo(HistoryPurchasePage);
