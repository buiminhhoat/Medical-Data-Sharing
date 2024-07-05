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

const AppointmentPageStyle = styled.div`
  .fullName {
    font-size: 25px;
    font-weight: 600;
    color: ${theme.colors.green_dark};
  }
  .department {
    font-size: 20px;
    font-weight: 300;
    color: ${theme.colors.east_bay};
  }
  .hospital {
    font-size: 20px;
    font-weight: 300;
    color: ${theme.colors.east_bay};
  }

  .avatar {
    margin-right: 5%;
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
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;

  const doctorList = [
    {
      fullName: "Bùi Minh Hoạt",
      department: "Chuyên khoa tim mạch",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/240591426_104885425287974_4346565099285966094_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=sBj0EUCXkCgQ7kNvgGrb-Ba&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAXuGz-sl5aHwkmYdd29ANU9aTrFRnT_I6Ac83L7ujOsg&oe=668DFD4F",
      hospital: "Bệnh viện ABC",
    },
    {
      fullName: "Nguyễn Tiến Dũng",
      department: "Chuyên khoa răng hàm mặt",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/273035638_1409762492808967_173337536557866628_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=Q7rkwsdNcDAQ7kNvgHo4TWX&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAQ5FjV5b7jvYMimt_knvFHauubornbUItQ-5ya6Phcqg&oe=668E1E04",
      hospital: "Bệnh viện XYZ",
    },
    {
      fullName: "Bùi Minh Hoạt",
      department: "Chuyên khoa tim mạch",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/240591426_104885425287974_4346565099285966094_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=sBj0EUCXkCgQ7kNvgGrb-Ba&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAXuGz-sl5aHwkmYdd29ANU9aTrFRnT_I6Ac83L7ujOsg&oe=668DFD4F",
      hospital: "Bệnh viện ABC",
    },
    {
      fullName: "Nguyễn Tiến Dũng",
      department: "Chuyên khoa răng hàm mặt",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/273035638_1409762492808967_173337536557866628_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=Q7rkwsdNcDAQ7kNvgHo4TWX&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAQ5FjV5b7jvYMimt_knvFHauubornbUItQ-5ya6Phcqg&oe=668E1E04",
      hospital: "Bệnh viện XYZ",
    },

    {
      fullName: "Bùi Minh Hoạt",
      department: "Chuyên khoa tim mạch",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/240591426_104885425287974_4346565099285966094_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=sBj0EUCXkCgQ7kNvgGrb-Ba&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAXuGz-sl5aHwkmYdd29ANU9aTrFRnT_I6Ac83L7ujOsg&oe=668DFD4F",
      hospital: "Bệnh viện ABC",
    },
    {
      fullName: "Nguyễn Tiến Dũng",
      department: "Chuyên khoa răng hàm mặt",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/273035638_1409762492808967_173337536557866628_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=Q7rkwsdNcDAQ7kNvgHo4TWX&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAQ5FjV5b7jvYMimt_knvFHauubornbUItQ-5ya6Phcqg&oe=668E1E04",
      hospital: "Bệnh viện XYZ",
    },

    {
      fullName: "Bùi Minh Hoạt",
      department: "Chuyên khoa tim mạch",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/240591426_104885425287974_4346565099285966094_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=sBj0EUCXkCgQ7kNvgGrb-Ba&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAXuGz-sl5aHwkmYdd29ANU9aTrFRnT_I6Ac83L7ujOsg&oe=668DFD4F",
      hospital: "Bệnh viện ABC",
    },
    {
      fullName: "Nguyễn Tiến Dũng",
      department: "Chuyên khoa răng hàm mặt",
      avatar:
        "https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/273035638_1409762492808967_173337536557866628_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=Q7rkwsdNcDAQ7kNvgHo4TWX&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAQ5FjV5b7jvYMimt_knvFHauubornbUItQ-5ya6Phcqg&oe=668E1E04",
      hospital: "Bệnh viện XYZ",
    },
  ];

  console.log(doctorList);

  return (
    <AppointmentPageStyle
      style={{ backgroundColor: "rgb(250, 250, 250)", height: "100%" }}
    >
      <div className="container">
        <div style={{ marginTop: "20px" }}>
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
                        width={200}
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
                      <div className="hospital">{doctor.hospital}</div>
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
                              controlHeight: 50,
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

        {/* 
        <div className="doctorlist">
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
                    width={200}
                    src="https://scontent.fhan14-4.fna.fbcdn.net/v/t39.30808-6/240591426_104885425287974_4346565099285966094_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=sBj0EUCXkCgQ7kNvgGrb-Ba&_nc_ht=scontent.fhan14-4.fna&cb_e2o_trans=q&oh=00_AYAXuGz-sl5aHwkmYdd29ANU9aTrFRnT_I6Ac83L7ujOsg&oe=668DFD4F"
                    style={{
                      borderRadius: "50%",
                    }}
                    preview={false}
                  />
                </div>
                <div style={{ width: "100%" }}>
                  <div className="name">Bùi Minh Hoạt</div>
                  <div className="department">Chuyên khoa tim mạch</div>
                  <div className="hospital">Bệnh viện ABC</div>
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
                          controlHeight: 50,
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
                      >
                        Đặt lịch khám
                      </Button>
                    </ConfigProvider>
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </div> */}
      </div>
    </AppointmentPageStyle>
  );
};

export default AppointmentPage;
