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
import MedicalRecordList from "../../../components/dialogs/MedicalRecordList/MedicalRecordList";
import RegisterUserDialog from "../../../components/dialogs/RegisterUser/RegisterUser";
import UserInfo from "../../../components/dialogs/UserInfo/UserInfo";

const UserManagementPageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const UserManagementPage = () => {
  const { access_token, userId, role } = Storage.getData();
  
  

  let apiGetAllUser = API.ADMIN.GET_ALL_USER_BY_ADMIN;

  if (role === "Cơ sở y tế") {
    apiGetAllUser =
      API.MEDICAL_INSTITUTION.GET_ALL_DOCTOR_BY_MEDICAL_INSTITUTION;
  }

  if (role === "Trung tâm nghiên cứu") {
    apiGetAllUser = API.RESEARCH_CENTER.GET_ALL_SCIENTIST_BY_RESEARCH_CENTER;
  }

  const [searchUserId, setSearchUserId] = useState("");
  const [searchUserName, setSearchUserName] = useState("");
  const [searchEmail, setSearchEmail] = useState("");
  const [searchDateBirthday, setSearchDateBirthday] = useState(null);
  const [searchGender, setSearchGender] = useState(null);
  const [searchRole, setSearchAddress] = useState("");

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    json.map((user, index) => {
      user.shortenedUserId =
        user.id.substring(0, 4) + "..." + user.id.substring(user.id.length - 4);
      user.key = user.id;
    });

    setDataSource(json);

    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchUserId);
    // setLoading(true);
    const filteredData = data.filter((entry) => {
      const matchesUserId = searchUserId
        ? entry.id.toLowerCase().includes(searchUserId.toLowerCase())
        : true;
      const matchesUserName = searchUserName
        ? entry.fullName.toLowerCase().includes(searchUserName.toLowerCase())
        : true;

      const matchesEmail = searchEmail
        ? entry.email.toLowerCase().includes(searchEmail.toLowerCase())
        : true;

      const matchesDateBirthday = searchDateBirthday
        ? entry.dateBirthday
            .toLowerCase()
            .includes(searchDateBirthday.toLowerCase())
        : true;

      const matchesAddress = searchRole
        ? entry.role.toLowerCase().includes(searchRole.toLowerCase())
        : true;

      return (
        matchesUserId &
        matchesUserName &
        matchesDateBirthday &
        matchesEmail &
        matchesAddress
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchUserId("");
    setSearchUserName("");
    setSearchEmail("");
    setSearchDateBirthday("");
    setSearchGender("");
    setSearchAddress("");
  };

  const fetchGetAllUser = async () => {
    if (access_token) {
      console.log("fetchGetAllUser");
      try {
        const response = await fetch(apiGetAllUser, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        });

        if (response.status === 200) {
          const json = await response.json();
          console.log(json);

          setData(json);
          handleData(json);
        }
      } catch (e) {}
    }
  };

  useEffect(() => {
    if (access_token) fetchGetAllUser().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [
    searchUserId,
    searchUserName,
    searchEmail,
    searchDateBirthday,
    searchGender,
    searchRole,
  ]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    if (access_token) fetchGetAllUser().then((r) => {});
  }, [openDialog]);

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

  const openAddUser = () => {
    openModal(DIALOGS.ADD_USER);
  };

  const openUserInfo = (user) => {
    console.log("openUserInfo");
    console.log("user: ", user);
    setSelectedUser(user);

    openModal(DIALOGS.USER_INFO);
  };

  const [highlightedText, setHighlightedText] = useState(null);

  const [filtersRole, setFiltersRole] = useState([
    {
      text: "Bệnh nhân",
      value: "Bệnh nhân",
    },
    {
      text: "Bác sĩ",
      value: "Bác sĩ",
    },
    {
      text: "Cơ sở y tế",
      value: "Cơ sở y tế",
    },
    {
      text: "Nhà khoa học",
      value: "Nhà khoa học",
    },
    {
      text: "Trung tâm nghiên cứu",
      value: "Trung tâm nghiên cứu",
    },
    {
      text: "Nhà thuốc",
      value: "Nhà thuốc",
    },
    {
      text: "Công ty sản xuất thuốc",
      value: "Công ty sản xuất thuốc",
    },
  ]);
  const columns = [
    {
      title: "Mã người dùng",
      dataIndex: "shortenedUserId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "15%",
      align: "center",
      onFilter: (value, user) => user.shortenedUserId.indexOf(value) === 0,
      render: (text, user, index) => (
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
            console.log(user.id);
            console.log(index);
            console.log(dataSource);
            navigator.clipboard
              .writeText(user.id)
              .then(() => message.success("Đã sao chép " + user.id))
              .catch((err) => message.error("Sao chép thất bại!"));
          }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Tên người dùng",
      dataIndex: "fullName",
      width: "15%",
      align: "center",
      onFilter: (value, user) => user.fullName.indexOf(value) === 0,
    },
    {
      title: "Email",
      dataIndex: "email",
      width: "15%",
      align: "center",
      onFilter: (value, user) => user.email.indexOf(value) === 0,
    },
    {
      title: "Vai trò",
      dataIndex: "role",
      width: "15%",
      align: "center",
      filters: filtersRole,
      onFilter: (value, user) => user.role.indexOf(value) === 0,
    },
    {
      title: "Hành động",
      width: "20%",
      align: "center",
      render: (index, user) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openUserInfo(user)}
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

  return (
    <UserManagementPageStyle>
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
                        placeholder="Mã người dùng"
                        value={searchUserId}
                        onChange={(e) => {
                          setSearchUserId(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Tên người dùng"
                        value={searchUserName}
                        onChange={(e) => {
                          setSearchUserName(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <DatePicker
                        format={{
                          format: "YYYY-MM-DD",
                        }}
                        placeholder="Ngày sinh"
                        onChange={(_, value) => {
                          setSearchDateBirthday(value);
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
                      <Input
                        placeholder="Email"
                        value={searchEmail}
                        onChange={(e) => {
                          setSearchEmail(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Vai trò"
                        value={searchRole}
                        onChange={(e) => {
                          setSearchAddress(e.target.value);
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
                <h1>Danh sách người dùng</h1>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "end",
                  marginLeft: "auto",
                  marginRight: "0",
                }}
              >
                <Button onClick={() => openAddUser()}>Tạo người dùng</Button>
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

      {openDialog === DIALOGS.ADD_USER && (
        <div className="modal-overlay">
          <RegisterUserDialog
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}

      {openDialog === DIALOGS.USER_INFO && (
        <div className="modal-overlay">
          <UserInfo
            user={selectedUser}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </UserManagementPageStyle>
  );
};

export default memo(UserManagementPage);
