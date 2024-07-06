import {memo, useEffect, useState} from "react";
import styled from "styled-components";
// import theme from "../../../styles/pages/theme";
import { ConfigProvider, Space, Table, Tag,  } from 'antd';
import { Calendar, theme } from 'antd';
import {useCookies} from "react-cookie";
import {API} from "@Const";

const HomePageStyle = styled.div`
    width: 100%;
    height: 100%;
`;

const HomePage = () => {
    const [cookies] = useCookies(["access_token"]);
    const access_token = cookies.access_token;
    const apiGetAllRequest = API.PUBLIC.GET_ALL_REQUEST;
    const [filtersSenderName, setFiltersSenderName] = useState([
        {
            text: 'Nguyễn Tiến Dũng',
            value: 'Nguyễn Tiến Dũng',
        },
        {
            text: 'Bùi Minh Hoạt',
            value: 'Bùi Minh Hoạt',
        },
    ]);

    const [filtersRecipientName, setFiltersRecipientName] = useState([
        {
            text: 'Nguyễn Tiến Dũng',
            value: 'Nguyễn Tiến Dũng',
        },
        {
            text: 'Bùi Minh Hoạt',
            value: 'Bùi Minh Hoạt',
        },
    ]);

    const [filtersRequestType, setfiltersRequestType] = useState([
        {
            text: 'Đặt lịch khám',
            value: 'Đặt lịch khám',
        },
        {
            text: 'Xem hồ sơ y tế',
            value: 'Xem hồ sơ y tế',
        },
    ]);

    const fetGetAllRequest = async () => {
        if (access_token) {
            const requestTypeSet = new Set();
            try {
                const response = await fetch(apiGetAllRequest, {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${access_token}`,
                    },
                });

                if (response.status === 200) {
                    const json = await response.json();
                    json.map((record, index) => {
                        record.shortenedRequestId = record.requestId.substring(0, 4) + "..." + record.requestId.substring(record.requestId.length - 4);
                        record.key = index;
                        switch (record.requestType) {
                            case "APPOINTMENT":
                                record.requestType = "Đặt lịch khám";
                                break
                            case "VIEW_RECORD":
                                record.requestType = "Xem hồ sơ y tế";
                                break;
                            case "PAYMENT":
                                record.requestType = "Thanh toán";
                                break;
                            case "PURCHASE":
                                record.requestType = "Mua hàng";
                                break;
                            case "CONFIRM_PAYMENT":
                                record.requestType = "Xác nhận thanh toán";
                                break;
                            case "EDIT_RECORD":
                                record.requestType = "Chỉnh sửa hồ sơ y tế";
                                break;
                            case "VIEW_PRESCRIPTION":
                                record.requestType = "Xem đơn thuốc";
                                break;
                            default:
                                record.requestType = "Yêu cầu không xác định"
                        }
                        requestTypeSet.add(record.requestType);
                    })

                    setData(json);
                    const requestTypeArr = [];
                    requestTypeSet.forEach((item) => {
                        requestTypeArr.push({value: item, text: item});
                    });

                    console.log(requestTypeSet);
                    console.log(requestTypeArr);
                    setfiltersRequestType(requestTypeArr);
                }
            }
            catch (e) {

            }
        }
    }

    useEffect(() => {
        if (access_token) fetGetAllRequest().then((r) => {});
    }, [access_token]);

    const columns = [
        {
            title: 'Mã yêu cầu',
            dataIndex: 'shortenedRequestId',
            showSorterTooltip: {
                target: 'full-header',
            },
            width: "10%",
            align: "center",
            onFilter: (value, record) => record.shortenedRequestId.indexOf(value) === 0,
        },
        {
            title: 'Người gửi',
            dataIndex: 'senderName',
            width: "15%",
            align: "center",
            filters: filtersSenderName,
            onFilter: (value, record) => record.senderName.indexOf(value) === 0,
        },
        {
            title: 'Người nhận',
            dataIndex: 'recipientName',
            width: "15%",
            align: "center",
            filters: filtersRecipientName,
            onFilter: (value, record) => record.recipientName.indexOf(value) === 0,
        },
        {
            title: 'Ngày tạo',
            dataIndex: 'dateCreated',
            width: "13%",
            align: "center",
            sorter: (a, b) => new Date(a.dateCreated) - new Date(b.dateCreated),
            sortDirections: ["descend", "ascend"],
            defaultSortOrder: "descend"
        },
        {
            title: 'Ngày chỉnh sửa',
            dataIndex: 'dateModified',
            width: "13%",
            align: "center",
            sorter: (a, b) => new Date(a.dateModified) - new Date(b.dateModified),
            sortDirections: ["descend", "ascend"],
            defaultSortOrder: "descend"
        },
        {
            title: 'Loại yêu cầu',
            dataIndex: 'requestType',
            width: "15%",
            align: "center",
            sorter: (a, b) => new Date(a.requestType) - new Date(b.requestType),
            filters: filtersRequestType,
            onFilter: (value, record) => record.requestType.indexOf(value) === 0,
        },
        {
            title: 'Trạng thái',
            dataIndex: 'requestStatus',
            width: "15%",
            align: "center",
            sorter: (a, b) => new Date(a.requestStatus) - new Date(b.requestStatus),
            filters: filtersRequestType,
            onFilter: (value, record) => record.requestStatus.indexOf(value) === 0,
            render: (requestStatus) => {
                let color = "white";
                if (requestStatus === "PENDING") {
                    color = "yellow";
                }
                else if (requestStatus === "DECLINED") {
                    color = "red";
                } else if (requestStatus === "APPROVED") {
                    color = "geekblue";
                }
                else if (requestStatus === "ACCEPTED") {
                    color = "green";
                }
                return (
                    <Tag color={color} key={requestStatus}>
                        {requestStatus.toUpperCase()}
                    </Tag>
                );
            }
        },
    ];

    const [data, setData] = useState([]);

    const onChange = (pagination, filters, sorter, extra) => {
        console.log('params', pagination, filters, sorter, extra);
    };

    const { token } = theme.useToken();
    const wrapperStyle = {
        width: 300,
        border: `1px solid ${token.colorBorderSecondary}`,
        borderRadius: token.borderRadiusLG,
    };

    const onPanelChange = (value, mode) => {
        console.log(value.format('YYYY-MM-DD'), mode);
    };

    return (
        <HomePageStyle>
            <div className="page">
                <div className="container" style={{display: "flex"}}>
                    <div style={{width: "100%"}}>
                        <h1>Danh sách yêu cầu</h1>
                        <ConfigProvider
                            theme={{
                                token: {
                                    borderRadius: 6
                                },
                            }}
                        >
                            <Table
                                columns={columns}
                                dataSource={data}
                                onChange={onChange}
                                showSorterTooltip={{
                                    target: 'sorter-icon',
                                }}
                                // pagination={{ pageSize: 3 }}
                            />
                        </ConfigProvider>
                    </div>
                </div>
            </div>
        </HomePageStyle>
    );
};

export default memo(HomePage);
