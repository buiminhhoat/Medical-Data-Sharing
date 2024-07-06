import {memo, useState} from "react";
import styled from "styled-components";
import theme from "../../../styles/pages/theme";
import { Space, Table, Tag } from 'antd';

const HomePageStyle = styled.div`

`;

const HomePsenderName = () => {
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

    const columns = [
        {
            title: 'Mã yêu cầu',
            dataIndex: 'requestId',
            showSorterTooltip: {
                target: 'full-header',
            },
            width: "10%",
            align: "center",
            onFilter: (value, record) => record.requestId.indexOf(value) === 0,
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
            width: "15%",
            align: "center",
            sorter: (a, b) => new Date(a.dateCreated) - new Date(b.dateCreated),
            sortDirections: ["descend", "ascend"],
            defaultSortOrder: "descend"
        },
        {
            title: 'Ngày chỉnh sửa',
            dataIndex: 'dateModified',
            width: "15%",
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

    console.log(new Date("2024-07-03") - new Date("2024-07-05"));

    const data = [
        {
            key: '1',
            requestId: '7cd02...6d1a',
            senderName: "Bùi Minh Hoạt",
            recipientName: 'Nguyễn Tiến Dũng',
            dateCreated: "2024-07-03",
            dateModified: "2024-07-03",
            requestType: "Xem hồ sơ y tế",
            requestStatus: "PENDING"
        },
        {
            key: '2',
            requestId: '7cd02...6d1b',
            senderName: "Bùi Minh Hoạt",
            recipientName: 'Nguyễn Tiến Dũng',
            dateCreated: "2024-07-05",
            dateModified: "2024-07-03",
            requestType: "Đặt lịch khám",
            requestStatus: "APPROVED"
        },
        {
            key: '3',
            requestId: '7cd02...6d1c',
            senderName: "Bùi Minh Hoạt",
            recipientName: 'Nguyễn Tiến Dũng',
            dateCreated: "2024-05-05",
            dateModified: "2024-07-03",
            requestType: "Xem hồ sơ y tế",
            requestStatus: "ACCEPTED"
        },
        {
            key: '4',
            requestId: '7cd02...6d1d',
            senderName: "Bùi Minh Hoạt",
            recipientName: 'Nguyễn Tiến Dũng',
            dateCreated: "2024-03-05",
            dateModified: "2024-07-03",
            requestType: "Chỉnh sửa hồ sơ y tế",
            requestStatus: "DECLINED"
        },
    ];
    const onChange = (pagination, filters, sorter, extra) => {
        console.log('params', pagination, filters, sorter, extra);
    };

    return (
      <HomePageStyle>
          <div className="container" style={{display: "flex", marginTop: "20px", marginRight: "5%"}}>
              <div style={{width: "70%"}}>
                  <h1>Danh sách yêu cầu</h1>
                  <Table
                      columns={columns}
                      dataSource={data}
                      onChange={onChange}
                      showSorterTooltip={{
                          target: 'sorter-icon',
                      }}
                  />
              </div>
              <div style={{width: "20%"}}>
                  <h1>Lịch</h1>
              </div>
          </div>
      </HomePageStyle>
  );
};

export default memo(HomePsenderName);
