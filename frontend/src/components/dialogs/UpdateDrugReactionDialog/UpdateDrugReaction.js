import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { useCookies } from 'react-cookie';
import { UserOutlined, CloseOutlined } from "@ant-design/icons";
import { Avatar, Flex, InputNumber, Space, TreeSelect } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import FileUploader from "../../FileUploader/FileUploader";
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
import AddMedicalRecordDialog from "../AddMedicalRecordDialog/AddMedicalRecordDialog";
import QRCodeScanner from "../../QRCodeScanner/QRCodeScanner";
import ConfirmModal from "../ConfirmModal/ConfirmModal";
import TextArea from "antd/es/input/TextArea";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const Context = React.createContext({
  name: "Default",
});

const UpdateDrugReactionDialogStyle = styled.div`
  overflow: auto;
`;

const UpdateDrugReactionDialog = ({ prescription, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId", "role"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  const [apiUpdateDrugReaction, setApiUpdateDrugReaction] = useState(
    API.PATIENT.UPDATE_DRUG_REACTION_BY_PATIENT
  );
  const [isModalOpen, setIsModalOpen] = useState(true);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);

  const [loading, setLoading] = useState(true);
  const [recipientId, setRecipientId] = useState();

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
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

  let apiGetPrescriptionByPrescriptionId =
    API.PATIENT.GET_PRESCRIPTION_BY_PRESCRIPTION_ID;

  const [data, setData] = useState("");

  const fetchGetPrescriptionByPrescriptionId = async () => {
    if (access_token) {
      setLoading(true);
      const formData = new FormData();
      formData.append("prescriptionId", prescription.prescriptionId);

      try {
        const response = await fetch(apiGetPrescriptionByPrescriptionId, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });

        if (response.status === 200) {
          setData(await response.json());
          console.log(data);
          setLoading(false);
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    if (access_token) fetchGetPrescriptionByPrescriptionId().then((r) => {});
  }, [access_token, openDialog]);

  const [form] = Form.useForm();

  const [valuesForm, setValuesForm] = useState();

  const handleFormSubmit = async () => {
    if (access_token) {
      setIsConfirmModalOpen(false);
      setDisabledButton(true);
      console.log("apiUpdateDrugReaction: ", apiUpdateDrugReaction);
      setLoading(true);
      const formData = new FormData();
      console.log(valuesForm);

      formData.append("prescriptionId", valuesForm["prescriptionId"]);
      formData.append("drugReaction", valuesForm["drugReaction"]);

      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );

      try {
        const response = await fetch(apiUpdateDrugReaction, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
          body: formData,
        });
        if (response.status === 200) {
          console.log("data");
          let data = await response.json();
          console.log(data);
          openNotification(
            "topRight",
            "success",
            "Thành công",
            "Đã cập nhật phản ứng thuốc thành công!",
            handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra khi cập nhật phản ứng thuốc!",
            handleCancel
          );
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  const [api, contextHolder] = notification.useNotification();
  const openNotification = (placement, type, message, description, onClose) => {
    api[type]({
      message: message,
      description: description,
      placement,
      showProgress: true,
      pauseOnHover: true,
      onClose: onClose,
    });
  };

  const [senderId, setSenderId] = useState(userId);

  // console.log(requestType);

  useEffect(() => {
    form.setFieldsValue({
      recipientId: recipientId,
    });
  }, [recipientId]);
  console.log(recipientId);

  const handleConfirm = (valuesForm) => {
    setIsConfirmModalOpen(true);
    setValuesForm(valuesForm);
  };

  const handleConfirmModalCancel = () => {
    setIsConfirmModalOpen(false);
  };

  const [disabledButton, setDisabledButton] = useState(false);

  return (
    <Context.Provider value={"Cập nhật phản ứng thuốc"}>
      {contextHolder}
      <UpdateDrugReactionDialogStyle>
        <ModalWrapper
          title="Cập nhật phản ứng thuốc"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          loading={loading}
        >
          <Form
            name="sharePrescription"
            labelCol={{
              span: 5,
            }}
            wrapperCol={{
              span: 18,
            }}
            style={{
              width: "100%",
              justifyContent: "center",
              alignItems: "center",
            }}
            initialValues={{
              prescriptionId: data.prescriptionId,
              drugReaction: data.drugReaction,
              remember: true,
            }}
            onFinish={handleConfirm}
            onFinishFailed={onFinishFailed}
            autoComplete="on"
            form={form}
          >
            <div style={{ width: "100%" }}>
              <Form.Item label="ID đơn thuốc" name="prescriptionId">
                <Input disabled />
              </Form.Item>

              <Form.Item label="Phản ứng thuốc" name="drugReaction">
                <TextArea rows={5} />
              </Form.Item>
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                justifyItems: "center",
              }}
            >
              <Button htmlType="submit" disabled={disabledButton}>
                Cập nhật
              </Button>
            </div>
          </Form>
        </ModalWrapper>

        {openDialog === DIALOGS.QRCODE_SCANNER && (
          <div>
            <QRCodeScanner
              value={recipientId}
              setValue={setRecipientId}
              onClose={handleDialogClose}
              onSwitch={handleDialogSwitch}
            />
          </div>
        )}

        <ConfirmModal
          isOpen={isConfirmModalOpen}
          handleOk={handleFormSubmit}
          handleCancel={handleConfirmModalCancel}
          title="Xác nhận"
          content="Bạn có chắc chắn không?"
        />
      </UpdateDrugReactionDialogStyle>
    </Context.Provider>
  );
};

export default UpdateDrugReactionDialog;
