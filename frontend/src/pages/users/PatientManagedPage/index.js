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
  Select,
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
import dayjs from "dayjs";

import MedicalRecordList from "../../../components/dialogs/MedicalRecordList/MedicalRecordList";

const PatientManagedPageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const PatientManagedPage = () => {
  const { access_token, userId, role } = Storage.getData();
  
  
  

  let apiGetAllPatientManaged = API.DOCTOR.GET_ALL_PATIENT_MANAGED_BY_DOCTOR;

  if (role === "Công ty sản xuất thuốc")
    apiGetAllPatientManaged =
      API.MANUFACTURER.GET_ALL_PATIENT_MANAGED_BY_MANUFACTURER;

  if (role === "Nhà khoa học")
    apiGetAllPatientManaged =
      API.SCIENTIST.GET_ALL_PATIENT_MANAGED_BY_SCIENTIST;

  const [searchPatientId, setSearchPatientId] = useState("");
  const [searchPatientName, setSearchPatientName] = useState("");
  const [searchEmail, setSearchEmail] = useState("");
  const [searchDateBirthday, setSearchDateBirthday] = useState(null);
  const [searchGender, setSearchGender] = useState(null);
  const [searchAddress, setSearchAddress] = useState("");

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    json.map((patient, index) => {
      patient.shortenedPatientId =
        patient.patientId.split('-')[1].substring(0, 4) +
        "..." +
        patient.patientId.substring(patient.patientId.length - 4);
      patient.key = index;
      patient.dateBirthday = dayjs(data.dateBirthday).format('YYYY-MM-DD');
    });

    setDataSource(json);

    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchPatientId);
    // setLoading(true);
    const filteredData = data.filter((entry) => {
      const matchesPatientId = searchPatientId
        ? entry.patientId.toLowerCase().includes(searchPatientId.toLowerCase())
        : true;
      const matchesPatientName = searchPatientName
        ? entry.patientName
            .toLowerCase()
            .includes(searchPatientName.toLowerCase())
        : true;

      const matchesEmail = searchEmail
        ? entry.email.toLowerCase().includes(searchEmail.toLowerCase())
        : true;

      const matchesDateBirthday = searchDateBirthday
        ? entry.dateBirthday
            .toLowerCase()
            .includes(searchDateBirthday.toLowerCase())
        : true;

      const matchesGender = searchGender
        ? entry.gender.toLowerCase().includes(searchGender.toLowerCase())
        : true;

      const matchesAddress = searchAddress
        ? entry.address.toLowerCase().includes(searchAddress.toLowerCase())
        : true;

      return (
        matchesPatientId &
        matchesPatientName &
        matchesDateBirthday &
        matchesEmail &
        matchesGender &
        matchesAddress
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchPatientId("");
    setSearchPatientName("");
    setSearchEmail("");
    setSearchDateBirthday("");
    setSearchGender(null);
    setSearchAddress("");
  };

  const fetchGetAllPatientByDoctorId = async () => {
    if (access_token) {
      try {
        const formData = new FormData();
        const response = await fetch(apiGetAllPatientManaged, {
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
          console.log(data);
        }
      } catch (e) {}
    }
  };

  useEffect(() => {
    if (access_token) fetchGetAllPatientByDoctorId().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [
    searchPatientId,
    searchPatientName,
    searchEmail,
    searchDateBirthday,
    searchGender,
    searchAddress,
  ]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedPatient, setSelectedPatient] = useState(null);

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

  const openMedicalRecordList = (patient) => {
    console.log("openMedicalRecordList");
    console.log("patient: ", patient);
    openModal(DIALOGS.MEDICAL_RECORD);
    setSelectedPatient(patient.patientId);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const columns = [
    {
      title: "Mã bệnh nhân",
      dataIndex: "shortenedPatientId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "15%",
      align: "center",
      onFilter: (value, patient) =>
        patient.shortenedPatientId.indexOf(value) === 0,
      render: (text, patient, index) => (
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
            console.log(patient.patientId);
            console.log(index);
            console.log(dataSource);
            navigator.clipboard
              .writeText(patient.patientId)
              .then(() => message.success("Đã sao chép " + patient.patientId))
              .catch((err) => message.error("Sao chép thất bại!"));
          }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Tên bệnh nhân",
      dataIndex: "patientName",
      width: "15%",
      align: "center",
      onFilter: (value, patient) => patient.patientName.indexOf(value) === 0,
    },
    {
      title: "Email",
      dataIndex: "email",
      width: "15%",
      align: "center",
      onFilter: (value, patient) => patient.email.indexOf(value) === 0,
    },
    {
      title: "Ngày sinh",
      dataIndex: "dateBirthday",
      width: "13%",
      align: "center",
      sorter: (a, b) => new Date(a.dateBirthday) - new Date(b.dateBirthday),
      sortDirections: ["descend", "ascend"],
      defaultSortOrder: "descend",
    },
    {
      title: "Giới tính",
      dataIndex: "gender",
      width: "15%",
      align: "center",
      sorter: (a, b) => new Date(a.gender) - new Date(b.gender),
      sortDirections: ["descend", "ascend"],
      defaultSortOrder: "descend",
    },
    {
      title: "Địa chỉ",
      dataIndex: "address",
      width: "15%",
      align: "center",
      onFilter: (value, patient) => patient.address.indexOf(value) === 0,
    },
    {
      title: "Hành động",
      width: "20%",
      align: "center",
      render: (index, patient) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openMedicalRecordList(patient)}
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
    <PatientManagedPageStyle>
      <div className="page">
        <div className="container">
          <div style={{ marginTop: "20px" }}>
            <h1>Tìm kiếm</h1>
            <Card
              style={{
                border: "none",
                background: "white",
                marginTop: "20px",
                marginBottom: "25px",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  marginTop: "10px",
                  marginBottom: "10px",
                }}
              >
                <div
                  style={{
                    display: "flex",
                    width: "100%",
                    justifyContent: "center",
                    alignItems: "center",
                    justifyItems: "center",
                  }}
                >
                  <div
                    style={{
                      width: "100%",
                      justifyContent: "center",
                      alignItems: "center",
                      justifyItems: "center",
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
                        placeholder="Mã bệnh nhân"
                        value={searchPatientId}
                        onChange={(e) => {
                          setSearchPatientId(e.target.value);
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

                      <Select
                        options={[
                          {
                            value: "Nam",
                            label: "Nam",
                          },
                          {
                            value: "Nữ",
                            label: "Nữ",
                          },
                        ]}
                        placeholder="Giới tính"
                        value={searchGender}
                        onChange={(value) => {
                          setSearchGender(value);
                        }}
                        allowClear
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Địa chỉ"
                        value={searchAddress}
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
            <h1>Danh sách bệnh nhân</h1>
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

      {openDialog === DIALOGS.MEDICAL_RECORD && (
        <div className="modal-overlay">
          <MedicalRecordList
            patientId={selectedPatient}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </PatientManagedPageStyle>
  );
};

export default memo(PatientManagedPage);
