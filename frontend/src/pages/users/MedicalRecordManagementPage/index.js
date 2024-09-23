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
import MedicalRecordDetail from "../../../components/dialogs/MedicalRecordDetail/MedicalRecordDetail";
import ScanInput from "../../../components/ScanInput/ScanInput";

const MedicalRecordManagementPageStyle = styled.div`
  width: 100%;
  height: 100%;
`;

const MedicalRecordManagementPage = () => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const apiGetListMedicalRecordByPatientId =
    API.PATIENT.GET_LIST_MEDICAL_RECORD_BY_PATIENT_ID;

  const [filtersTestName, setfiltersTestName] = useState([]);
  const [filtersMedicalRecordStatus, setfiltersMedicalRecordStatus] = useState([
    {
      value: "Chờ xử lý",
      text: "Chờ xử lý",
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

  const [searchMedicalRecordId, setSearchMedicalRecordId] = useState("");
  const [searchPatientId, setSearchPatientId] = useState("");
  const [searchDoctorId, setSearchDoctorId] = useState("");
  const [searchPatientName, setSearchPatientName] = useState("");
  const [searchDoctorName, setSearchDoctorName] = useState("");
  const [searchDateCreated, setSearchDateCreated] = useState(null);
  const [searchDateModified, setSearchDateModified] = useState(null);
  const [searchTestName, setSearchTestName] = useState("");
  const [searchMedicalRecordStatus, setSearchMedicalRecordStatus] =
    useState("");

  const [data, setData] = useState([]);

  const [loading, setLoading] = useState(true);

  const handleData = (json) => {
    console.log("*");
    const testNameSet = new Set();

    json.map((record, index) => {
      record.shortenedMedicalRecordId =
        record.medicalRecordId.substring(0, 4) +
        "..." +
        record.medicalRecordId.substring(record.medicalRecordId.length - 4);
      record.key = index;
      testNameSet.add(record.testName);
    });

    setDataSource(json);
    const testNameArr = [];
    testNameSet.forEach((item) => {
      testNameArr.push({ value: item, text: item });
    });

    setfiltersTestName(testNameArr);
    setLoading(false);
  };

  const handleSearch = () => {
    console.log(searchMedicalRecordId);
    // setLoading(true);
    const filteredData = data.filter((entry) => {
      const matchesMedicalRecordId = searchMedicalRecordId
        ? entry.medicalRecordId
            .toLowerCase()
            .includes(searchMedicalRecordId.toLowerCase())
        : true;
      const matchesPatientId = searchPatientId
        ? entry.patientId.toLowerCase().includes(searchPatientId.toLowerCase())
        : true;
      const matchesDoctorId = searchDoctorId
        ? entry.doctorId.toLowerCase().includes(searchDoctorId.toLowerCase())
        : true;
      const matchesPatientName = searchPatientName
        ? entry.patientName
            .toLowerCase()
            .includes(searchPatientName.toLowerCase())
        : true;

      const matchesDoctorName = searchDoctorName
        ? entry.doctorName
            .toLowerCase()
            .includes(searchDoctorName.toLowerCase())
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

      const matchesTestName = searchTestName
        ? entry.testName.toLowerCase().includes(searchTestName.toLowerCase())
        : true;

      const matchesMedicalRecordStatus = searchMedicalRecordStatus
        ? entry.medicalRecordStatus
            .toLowerCase()
            .includes(searchMedicalRecordStatus.toLowerCase())
        : true;

      return (
        matchesMedicalRecordId &
        matchesPatientId &
        matchesDoctorId &
        matchesDateCreated &
        matchesPatientName &
        matchesDoctorName &
        matchesDateModified &
        matchesTestName &
        matchesMedicalRecordStatus
      );
    });
    setDataSource(filteredData);
    // setLoading(false);
  };

  const handleDelete = () => {
    setSearchMedicalRecordId("");
    setSearchPatientId("");
    setSearchDoctorId("");
    setSearchPatientName("");
    setSearchDoctorName("");
    setSearchDateCreated("");
    setSearchDateModified("");
    setSearchTestName("");
    setSearchMedicalRecordStatus("");
  };

  const fetGetListMedicalRecordByPatientId = async () => {
    if (access_token) {
      try {
        const formData = new FormData();
        formData.append("patientId", userId);
        const response = await fetch(apiGetListMedicalRecordByPatientId, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
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
    if (access_token) fetGetListMedicalRecordByPatientId().then((r) => {});
  }, [access_token]);

  useEffect(() => {
    handleSearch();
  }, [
    searchMedicalRecordId,
    searchPatientId,
    searchDoctorId,
    searchPatientName,
    searchDoctorName,
    searchDateCreated,
    searchDateModified,
    searchTestName,
    searchMedicalRecordStatus,
  ]);

  const [openDialog, setOpenDialog] = useState(null);
  const [selectedMedicalRecord, setSelectedMedicalRecord] = useState(null);

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

  const openMedicalRecordDetail = (medicalRecord) => {
    console.log("openMedicalRecordDetail");
    console.log(medicalRecord);
    openModal(DIALOGS.MEDICAL_RECORD);
    setSelectedMedicalRecord(medicalRecord);
  };

  const [highlightedText, setHighlightedText] = useState(null);
  const columns = [
    {
      title: "Mã hồ sơ y tế",
      dataIndex: "shortenedMedicalRecordId",
      showSorterTooltip: {
        target: "full-header",
      },
      width: "15%",
      align: "center",
      onFilter: (value, record) =>
        record.shortenedMedicalRecordId.indexOf(value) === 0,
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
            console.log(record.medicalRecordId);
            console.log(index);
            console.log(dataSource);
            navigator.clipboard
              .writeText(record.medicalRecordId)
              .then(() =>
                message.success("Đã sao chép " + record.medicalRecordId)
              )
              .catch((err) => message.error("Sao chép thất bại!"));
          }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Bệnh nhân",
      dataIndex: "patientName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.patientName.indexOf(value) === 0,
    },
    {
      title: "Bác sĩ",
      dataIndex: "doctorName",
      width: "15%",
      align: "center",
      onFilter: (value, record) => record.doctorName.indexOf(value) === 0,
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
      title: "Tên xét nghiệm",
      dataIndex: "testName",
      width: "15%",
      align: "center",
      filters: filtersTestName,
      onFilter: (value, record) => record.testName.indexOf(value) === 0,
    },
    {
      title: "Trạng thái",
      dataIndex: "medicalRecordStatus",
      width: "12%",
      align: "center",
      filters: filtersMedicalRecordStatus,
      onFilter: (value, record) =>
        record.medicalRecordStatus.indexOf(value) === 0,
      render: (medicalRecordStatus) => {
        let color = "blue";
        if (medicalRecordStatus === "Chờ xử lý") {
          color = "yellow";
        } else if (medicalRecordStatus === "Từ chối") {
          color = "red";
        } else if (medicalRecordStatus === "Đồng ý") {
          color = "green";
        } else if (medicalRecordStatus === "Thu hồi") {
          color = "purple";
        }
        return (
          <Tag color={color} key={medicalRecordStatus}>
            {medicalRecordStatus.toUpperCase()}
          </Tag>
        );
      },
    },
    {
      title: "Hành động",
      width: "20%",
      align: "center",
      render: (index, record) => {
        return (
          <Space size="middle">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => openMedicalRecordDetail(record)}
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

  console.log(searchDoctorId);

  return (
    <MedicalRecordManagementPageStyle>
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
                          value={searchMedicalRecordId}
                          setValue={setSearchMedicalRecordId}
                          placeholder="Mã hồ sơ y tế"
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
                          value={searchDoctorId}
                          setValue={setSearchDoctorId}
                          placeholder="Mã bác sĩ"
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
                        placeholder="Tên bác sĩ"
                        value={searchDoctorName}
                        onChange={(e) => {
                          setSearchDoctorName(e.target.value);
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
                        placeholder="Tên xét nghiệm"
                        value={searchTestName}
                        onChange={(e) => {
                          setSearchTestName(e.target.value);
                        }}
                        style={{ width: "30%", marginRight: "2%" }}
                      />

                      <Input
                        placeholder="Trạng thái"
                        value={searchMedicalRecordStatus}
                        onChange={(e) => {
                          setSearchMedicalRecordStatus(e.target.value);
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
            <h1>Danh sách hồ sơ y tế</h1>
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
          <MedicalRecordDetail
            medicalRecord={selectedMedicalRecord}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </MedicalRecordManagementPageStyle>
  );
};

export default memo(MedicalRecordManagementPage);
