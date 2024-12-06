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
import RequestDetail from "../../../components/dialogs/RequestDetail/RequestDetail";
import AddMedicationDialog from "../../../components/dialogs/AddMedication/AddMedication";
import ScanInput from "../../../components/ScanInput/ScanInput";

const MedicationManagementPageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const MedicationManagementPage = () => {
  const { access_token, userId, role } = Storage.getData();
  
  const apiGetListMedicationByManufacturerId =
    API.MANUFACTURER.GET_LIST_MEDICATION_BY_MANUFACTURERID;

  const [searchMedicationId, setSearchMedicationId] = useState("");
  const [searchMedicationName, setSearchMedicationName] = useState("");
  const [searchDescription, setSearchDescription] = useState("");
  const [searchDateCreated, setSearchDateCreated] = useState(null);
  const [searchDateModified, setSearchDateModified] = useState(null);

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    console.log("*");
    const requestTypeSet = new Set();

    json.map((record, index) => {
      record.shortenedMedicationId =
        record.medicationId.substring(0, 4) +
        "..." +
        record.medicationId.substring(record.medicationId.length - 4);
      record.key = index;
      requestTypeSet.add(record.requestType);
    });

    setDataSource(json);
    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchMedicationId);
    // setLoading(true);
    const filteredData = data.filter((entry) => {
      const matchesMedicationId = searchMedicationId
        ? entry.medicationId
            .toLowerCase()
            .includes(searchMedicationId.toLowerCase())
        : true;
      const matchesMedicationName = searchMedicationName
        ? entry.medicationName
            .toLowerCase()
            .includes(searchMedicationName.toLowerCase())
        : true;

      const matchesDescription = searchDescription
        ? entry.description
            .toLowerCase()
            .includes(searchDescription.toLowerCase())
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
      return (
        matchesMedicationId &
        matchesDateCreated &
        matchesMedicationName &
        matchesDescription &
        matchesDateModified
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchMedicationId("");
    setSearchMedicationId("");
    setSearchMedicationName("");
    setSearchDescription("");
    setSearchDateCreated("");
    setSearchDateModified("");
  };

  const fetGetAllRequest = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetListMedicationByManufacturerId, {
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
    if (access_token) fetGetAllRequest().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [
    searchMedicationId,
    searchMedicationName,
    searchDescription,
    searchDateCreated,
    searchDateModified,
  ]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedRequest, setSelectedRequest] = useState(null);

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

  const openRequestDetail = (request) => {
    console.log("openRequestDetail");
    console.log(request);
    openModal(DIALOGS.REQUEST_DETAIL);
    setSelectedRequest(request);
  };

  const openAddMedication = () => {
    openModal(DIALOGS.ADD_MEDICATION);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const columns = [
    {
      title: "Mã loại thuốc",
      dataIndex: "shortenedMedicationId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "10%",
      align: "center",
      onFilter: (value, record) =>
        record.shortenedMedicationId.indexOf(value) === 0,
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
      title: "Tên loại thuốc",
      dataIndex: "medicationName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.medicationName.indexOf(value) === 0,
    },
    {
      title: "Ngày tạo",
      dataIndex: "dateCreated",
      width: "15%",
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
      title: "Mô tả",
      dataIndex: "description",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.description.indexOf(value) === 0,
    },
    {
      title: "Hành động",
      dataIndex: "requestStatus",
      width: "20%",
      align: "center",
      render: (index, record) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openRequestDetail(record)}
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

  return (
    <MedicationManagementPageStyle>
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
                          placeholder="Mã loại thuốc"
                        />
                      </div>

                      <Input
                        placeholder="Tên loại thuốc"
                        value={searchMedicationName}
                        onChange={(e) => {
                          setSearchMedicationName(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD HH:mm",
                        }}
                        placeholder="Ngày tạo"
                        // value={searchDateCreated}
                        onChange={(_, value) => {
                          setSearchDateCreated(value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD HH:mm",
                        }}
                        placeholder="Ngày chỉnh sửa"
                        onChange={(_, value) => {
                          setSearchDateModified(value);
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
                <h1>Danh sách loại thuốc</h1>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "end",
                  marginLeft: "auto",
                  marginRight: "0",
                }}
              >
                <Button onClick={() => openAddMedication()}>
                  Tạo loại thuốc mới
                </Button>
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

      {/* {openDialog === DIALOGS.REQUEST_DETAIL && (
        <div className="modal-overlay">
          <RequestDetail
            request={selectedRequest}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )} */}

      {openDialog === DIALOGS.ADD_MEDICATION && (
        <div className="modal-overlay">
          <AddMedicationDialog
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </MedicationManagementPageStyle>
  );
};

export default memo(MedicationManagementPage);
