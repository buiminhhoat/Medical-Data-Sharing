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
import { useCookies } from "react-cookie";
import { API } from "@Const";
import { DIALOGS } from "@Const";
import RequestDetail from "../../../components/dialogs/RequestDetail/RequestDetail";
import SendRequestDialog from "../../../components/dialogs/SendRequest/SendRequest";

const RequestPageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const RequestPage = () => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;
  const apiGetAllRequest = API.PUBLIC.GET_ALL_REQUEST;

  const [filtersRequestType, setfiltersRequestType] = useState([]);
  const [filtersRequestStatus, setfiltersRequestStatus] = useState([
    {
      value: "Chờ xử lý",
      text: "Chờ xử lý",
    },
    {
      value: "Chấp thuận",
      text: "Chấp thuận",
    },
    {
      value: "Đồng ý",
      text: "Đồng ý",
    },
    {
      value: "Từ chối",
      text: "Từ chối",
    },
  ]);

  const [searchRequestId, setSearchRequestId] = useState("");
  const [searchSenderId, setSearchSenderId] = useState("");
  const [searchRecipientId, setSearchRecipientId] = useState("");
  const [searchSenderName, setSearchSenderName] = useState("");
  const [searchRecipientName, setSearchRecipientName] = useState("");
  const [searchDateCreated, setSearchDateCreated] = useState(null);
  const [searchDateModified, setSearchDateModified] = useState(null);
  const [searchRequestType, setSearchRequestType] = useState("");
  const [searchRequestStatus, setSearchRequestStatus] = useState("");

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    console.log("*");
    const requestTypeSet = new Set();

    json.map((record, index) => {
      record.shortenedRequestId =
        record.requestId.substring(0, 4) +
        "..." +
        record.requestId.substring(record.requestId.length - 4);
      record.key = index;
      requestTypeSet.add(record.requestType);
    });

    setDataSource(json);
    const requestTypeArr = [];
    requestTypeSet.forEach((item) => {
      requestTypeArr.push({ value: item, text: item });
    });

    setfiltersRequestType(requestTypeArr);
    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchRequestId);
    // setLoading(true);
    const filteredData = data.filter((entry) => {
      const matchesRequestId = searchRequestId
        ? entry.requestId.toLowerCase().includes(searchRequestId.toLowerCase())
        : true;
      const matchesSenderId = searchSenderId
        ? entry.senderId.toLowerCase().includes(searchSenderId.toLowerCase())
        : true;
      const matchesRecipientId = searchRecipientId
        ? entry.recipientId
            .toLowerCase()
            .includes(searchRecipientId.toLowerCase())
        : true;
      const matchesSenderName = searchSenderName
        ? entry.senderName
            .toLowerCase()
            .includes(searchSenderName.toLowerCase())
        : true;

      const matchesRecipientName = searchRecipientName
        ? entry.recipientName
            .toLowerCase()
            .includes(searchRecipientName.toLowerCase())
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

      const matchesRequestType = searchRequestType
        ? entry.requestType
            .toLowerCase()
            .includes(searchRequestType.toLowerCase())
        : true;

      const matchesRequestStatus = searchRequestStatus
        ? entry.requestStatus
            .toLowerCase()
            .includes(searchRequestStatus.toLowerCase())
        : true;

      return (
        matchesRequestId &
        matchesSenderId &
        matchesRecipientId &
        matchesDateCreated &
        matchesSenderName &
        matchesRecipientName &
        matchesDateModified &
        matchesRequestType &
        matchesRequestStatus
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchRequestId("");
    setSearchSenderId("");
    setSearchRequestId("");
    setSearchSenderName("");
    setSearchRecipientName("");
    setSearchDateCreated("");
    setSearchDateModified("");
    setSearchRequestType("");
    setSearchRequestStatus("");
  };

  const fetGetAllRequest = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetAllRequest, {
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
    searchRequestId,
    searchSenderId,
    searchRecipientId,
    searchSenderName,
    searchRecipientName,
    searchDateCreated,
    searchDateModified,
    searchRequestType,
    searchRequestStatus,
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

  const openSendRequest = () => {
    openModal(DIALOGS.SEND_REQUEST);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const columns = [
    {
      title: "Mã yêu cầu",
      dataIndex: "shortenedRequestId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "10%",
      align: "center",
      onFilter: (value, record) =>
        record.shortenedRequestId.indexOf(value) === 0,
      render: (text, record, index) => (
        <span
          onMouseEnter={() => setHighlightedText(index)}
          onMouseLeave={() => setHighlightedText(null)}
          style={{
            backgroundColor: highlightedText === index ? "#ffe898" : "", // sử dụng màu để làm nổi bật văn bản
            border:
              highlightedText === index
                ? "2px dashed rgb(234, 179, 8)"
                : "none",
            borderRadius: "4px",
            padding: "2px", // Thêm padding để đường viền không dính sát vào chữ
            cursor: "pointer",
          }}
          onClick={() => {
            console.log(record.requestId);
            console.log(index);
            console.log(dataSource);
            navigator.clipboard
              .writeText(record.requestId)
              .then(() => message.success("Đã sao chép " + record.requestId))
              .catch((err) => message.error("Sao chép thất bại!"));
          }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Người gửi",
      dataIndex: "senderName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.senderName.indexOf(value) === 0,
    },
    {
      title: "Người nhận",
      dataIndex: "recipientName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.recipientName.indexOf(value) === 0,
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
      title: "Loại yêu cầu",
      dataIndex: "requestType",
      width: "15%",
      align: "center",
      filters: filtersRequestType,
      onFilter: (value, record) => record.requestType.indexOf(value) === 0,
    },
    {
      title: "Trạng thái",
      dataIndex: "requestStatus",
      width: "12%",
      align: "center",
      filters: filtersRequestStatus,
      onFilter: (value, record) => record.requestStatus.indexOf(value) === 0,
      render: (requestStatus) => {
        let color = "blue";
        if (requestStatus === "Chờ xử lý") {
          color = "yellow";
        } else if (requestStatus === "Từ chối") {
          color = "red";
        } else if (requestStatus === "Chấp thuận") {
          color = "geekblue";
        } else if (requestStatus === "Đồng ý") {
          color = "green";
        } else if (requestStatus === "Thu hồi") {
          color = "purple";
        }
        return (
          <Tag color={color} key={requestStatus}>
            {requestStatus.toUpperCase()}
          </Tag>
        );
      },
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
    console.log(value.format("YYYY-MM-DD"), mode);
  };

  console.log(searchRecipientId);

  return (
    <RequestPageStyle>
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
                      <Input
                        placeholder="Mã yêu cầu"
                        value={searchRequestId}
                        onChange={(e) => {
                          setSearchRequestId(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />
                      <Input
                        placeholder="Mã người gửi"
                        value={searchSenderId}
                        onChange={(e) => {
                          setSearchSenderId(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />
                      <Input
                        placeholder="Mã người nhận"
                        value={searchRecipientId}
                        onChange={(e) => {
                          setSearchRecipientId(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />
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
                        placeholder="Tên người gửi"
                        value={searchSenderName}
                        onChange={(e) => {
                          setSearchSenderName(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Tên người nhận"
                        value={searchRecipientName}
                        onChange={(e) => {
                          setSearchRecipientName(e.target.value);
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

                      <Input
                        placeholder="Loại yêu cầu"
                        value={searchRequestType}
                        onChange={(e) => {
                          setSearchRequestType(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Trạng thái"
                        value={searchRequestStatus}
                        onChange={(e) => {
                          setSearchRequestStatus(e.target.value);
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
                <h1>Danh sách yêu cầu</h1>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "end",
                  marginLeft: "auto",
                  marginRight: "0",
                }}
              >
                <Button onClick={() => openSendRequest()}>
                  Tạo yêu cầu mới
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

      {openDialog === DIALOGS.REQUEST_DETAIL && (
        <div className="modal-overlay">
          <RequestDetail
            request={selectedRequest}
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
    </RequestPageStyle>
  );
};

export default memo(RequestPage);
