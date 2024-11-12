import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { DIALOGS } from "@Const";
import styled from "styled-components";
import theme from "../../../styles/pages/theme";
import { Card, Input, Button, Image, ConfigProvider, Row, Col } from "antd";
import { SearchOutlined, CalendarOutlined } from "@ant-design/icons";
import { API } from "@Const";
import SendRequestDialog from "../../../components/dialogs/SendRequest/SendRequest";

const AppointmentPageStyle = styled.div`
  .fullName {
    font-size: 21px;
    font-weight: 600;
    color: ${theme.colors.green_dark};
  }
  .department {
    font-size: 16px;
    font-weight: 300;
    color: ${theme.colors.east_bay};
  }
  .medicalInstitutionName {
    font-size: 16px;
    font-weight: 300;
    color: ${theme.colors.east_bay};
  }

  .avatar {
    margin: 0 8% 0 4%;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40%;
  }

  .doctorlist {
    justify-content: center;
    align-items: center;
    display: flex;
    flex-wrap: wrap;
  }

  .doctorinfo {
    width: 100%;
  }
`;

const AppointmentPage = () => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;

  const [doctorList, setDoctorList] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState();

  console.log(doctorList);

  const apiGetAllDoctor = API.PUBLIC.GET_ALL_DOCTOR;

  console.log(apiGetAllDoctor);

  const fetchGetAllDoctor = async () => {
    const response = await fetch(apiGetAllDoctor, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${access_token}`,
      },
    });

    if (response.status === 200) {
      const json = await response.json();
      setDoctorList(json);
      console.log(json);
    }
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

  useEffect(() => {
    if (access_token) {
      fetchGetAllDoctor();
    }
  }, [access_token]);

  console.log(selectedDoctor);

  return (
    <AppointmentPageStyle
      style={{ backgroundColor: "rgb(250, 250, 250)", height: "100%" }}
    >
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
                <Input
                  prefix={<SearchOutlined />}
                  placeholder="Tên bác sĩ"
                  style={{ width: "25%", marginRight: "2%" }}
                />
                <Input
                  prefix={<SearchOutlined />}
                  placeholder="Tên bệnh viện"
                  style={{ width: "25%", marginRight: "2%" }}
                />
                <Input
                  prefix={<SearchOutlined />}
                  placeholder="Chuyên khoa"
                  style={{ width: "25%", marginRight: "2%" }}
                />

                <Button
                  icon={<SearchOutlined />}
                  style={{
                    backgroundColor: `${theme.colors.green}`,
                    color: "white",
                    fontWeight: 600,
                  }}
                >
                  Tìm kiếm
                </Button>
              </div>
            </Card>
          </div>
          <h1 style={{ marginTop: "20px", marginBottom: "20px" }}>
            Danh sách bác sĩ
          </h1>

          <div style={{ padding: "0 5% 0 5%" }}>
            <Row gutter={[16, 16]}>
              {doctorList?.map((doctor, index) => (
                <Col span={12} key={index}>
                  <div className="doctorinfo">
                    <Card
                      style={{
                        border: "none",
                        background: "white",
                      }}
                    >
                      <div style={{ display: "flex" }}>
                        <div className="avatar">
                          <Image
                            width={150}
                            src={doctor.avatar}
                            style={{
                              borderRadius: "50%",
                            }}
                            preview={false}
                          />
                        </div>
                        <div style={{ width: "100%" }}>
                          <div className="fullName">{doctor.fullName}</div>
                          <div className="department">{doctor.department}</div>
                          <div className="medicalInstitutionName">
                            {doctor.medicalInstitutionName}
                          </div>
                          <div
                            style={{
                              display: "flex",
                              justifyContent: "center",
                              marginTop: "10%",
                            }}
                          >
                            <ConfigProvider
                              theme={{
                                token: {
                                  controlHeight: 44,
                                },
                              }}
                            >
                              <Button
                                icon={<CalendarOutlined />}
                                style={{
                                  backgroundColor: `${theme.colors.green}`,
                                  color: "white",
                                  fontWeight: 600,
                                }}
                                onClick={(e) => {
                                  doctor.requestType = "Đặt lịch khám";
                                  doctor.recipientId = doctor.doctorId;
                                  doctor.recipientName = doctor.fullName;
                                  setSelectedDoctor(doctor);
                                  openModal(DIALOGS.SEND_REQUEST);
                                }}
                              >
                                Đặt lịch khám
                              </Button>
                            </ConfigProvider>
                          </div>
                        </div>
                      </div>
                    </Card>
                  </div>
                </Col>
              ))}
            </Row>
          </div>
        </div>
      </div>

      {openDialog === DIALOGS.SEND_REQUEST && (
        <div className="modal-overlay">
          <SendRequestDialog
            values={selectedDoctor}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </AppointmentPageStyle>
  );
};

export default AppointmentPage;
