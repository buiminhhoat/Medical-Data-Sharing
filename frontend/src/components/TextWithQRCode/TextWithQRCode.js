import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined, CloseOutlined } from "@ant-design/icons";
import {
  Avatar,
  Flex,
  InputNumber,
  Popover,
  QRCode,
  Space,
  TreeSelect,
} from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import FileUploader from "../FileUploader/FileUploader";
import styled from "styled-components";
import { CgEnter } from "react-icons/cg";
import {
  Button,
  Modal,
  Checkbox,
  Form,
  Input,
  Select,
  List,
  Typography,
  Row,
  Col,
  message,
} from "antd";
import { Alert, notification } from "antd";
import {
  MinusCircleOutlined,
  PlusOutlined,
  QrcodeOutlined,
  ScanOutlined,
} from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
import AddMedicalRecordDialog from "../dialogs/AddMedicalRecordDialog/AddMedicalRecordDialog";
import QRCodeScanner from "../QRCodeScanner/QRCodeScanner";
import ConfirmModal from "../dialogs/ConfirmModal/ConfirmModal";
const { Option } = Select;

const TextWithQRCode = ({ value }) => {
  function doDownload(url, fileName) {
    const a = document.createElement("a");
    a.download = fileName;
    a.href = url;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }

  const downloadCanvasQRCode = () => {
    const canvas = document.getElementById("myqrcode")?.querySelector("canvas");
    if (canvas) {
      const url = canvas.toDataURL();
      doDownload(url, "QRCode.png");
    }
  };

  const copyQRCodeValue = () => {
    navigator.clipboard
      .writeText(value)
      .then(() => message.success("Đã sao chép " + value))
      .catch((err) => message.error("Sao chép thất bại!"));
  };
  return (
    <>
      <div>{value}</div>
      <div style={{ marginLeft: "1%", scale: "0.9" }}>
        <Popover content="Bấm để sao chép">
          <Popover
            content={
              <div>
                <div>
                  <QRCode
                    type="canvas"
                    value={value}
                    bordered={false}
                    id="myqrcode"
                    bgColor="#fff"
                  />
                </div>
                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    justifyItems: "center",
                    marginTop: "8%",
                  }}
                >
                  <Button type="primary" onClick={downloadCanvasQRCode}>
                    Download
                  </Button>
                </div>
              </div>
            }
          >
            <Button
              // type="primary"
              // shape="circle"
              icon={<ScanOutlined />}
              onClick={copyQRCodeValue}
            ></Button>
          </Popover>
        </Popover>
      </div>
    </>
  );
};

export default TextWithQRCode;
