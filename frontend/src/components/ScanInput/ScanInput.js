import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import Storage from '@Utils/Storage';
import { UserOutlined, CloseOutlined } from "@ant-design/icons";
import { Avatar, Flex, InputNumber, Space, TreeSelect } from "antd";
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

const ScanInput = ({ value, setValue, placeholder }) => {
  const [openDialog, setOpenDialog] = useState(null);
  const onClickScan = () => {
    setOpenDialog(DIALOGS.QRCODE_SCANNER);
  };

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
    console.log("value - ScanInput: ", value);
  }, [value]);

  const [isHovering, setIsHovering] = useState(false);

  const defaultColor = "#bfbfbf";
  const hoverColor = "green";

  return (
    <Space direction="vertical" size="middle" style={{ width: "100%" }}>
      <Space.Compact
        style={{
          width: "100%",
        }}
      >
        <Input
          style={{ width: "100%" }}
          onChange={(e) => {
            setValue(e.target.value);
          }}
          placeholder={placeholder}
          value={value}
          allowClear
        />

        <div>
          <Button
            onClick={onClickScan}
            icon={
              <ScanOutlined
                style={{ color: isHovering ? hoverColor : defaultColor }}
              />
            }
            onMouseEnter={() => setIsHovering(true)}
            onMouseLeave={() => setIsHovering(false)}
          ></Button>
        </div>

        {openDialog === DIALOGS.QRCODE_SCANNER && (
          <div>
            <QRCodeScanner
              value={value}
              setValue={setValue}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )}
      </Space.Compact>
    </Space>
    /* <div style={{ display: "flex" }}>
      <div style={{ width: "100%" }}>
        <Input
          style={{ width: "100%" }}
          onChange={(e) => {
            setValue(e.target.value);
          }}
          placeholder={placeholder}
          value={value}
          allowClear
        />
      </div>
      <div>
        <Button onClick={onClickScan} icon={<ScanOutlined />}></Button>
      </div>

      {openDialog === DIALOGS.QRCODE_SCANNER && (
        <div>
          <QRCodeScanner
            value={value}
            setValue={setValue}
            onClose={handleDialogClose}
            onSwitch={handleDialogSwitch}
          />
        </div>
      )}
    </div> */
  );
};

export default ScanInput;
