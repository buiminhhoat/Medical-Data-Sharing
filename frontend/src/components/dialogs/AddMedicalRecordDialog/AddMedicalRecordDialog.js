import React, { useEffect, useState, useMemo } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
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
} from "antd";
import { Alert, notification } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
import ConfirmModal from "../ConfirmModal/ConfirmModal";
import ModalWrapper from "../../ModalWrapper/ModalWrapper";
const { Option } = Select;

const Context = React.createContext({
  name: "AddMedicalRecordDialog",
});

const AddMedicalRecordDialogStyle = styled.div`
  overflow: auto;
`;

const AddMedicalRecordDialog = ({ request, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiAddMedicalRecord = API.DOCTOR.ADD_MEDICAL_RECORD;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const handleAddMedicalRecordFormSubmit = async () => {
    if (access_token) {
      const values = valuesForm;
      console.log("handleAddMedicalRecordFormSubmit");
      console.log(values);
      console.log("Hashfile: ", hashFile);
      setIsConfirmModalOpen(false);
      setDisabledButton(true);
      const formData = new FormData();
      console.log(values);
      for (const key in values) {
        if (key === "hashFile") continue;
        formData.append(key, values[key]);
      }
      formData.append("hashFile", hashFile);
      let addPrescription = [];
      if (values.prescriptionDetailsList) {
        values.prescriptionDetailsList.map((p) => {
          const medicationId = JSON.parse(p.medicationId).medicationId;
          const quantity = String(p.quantity);
          const details = p.details;
          p = { medicationId, quantity, details };
          addPrescription.push({ medicationId, quantity, details });
        });
      }
      console.log(addPrescription);
      formData.append("addPrescription", JSON.stringify(addPrescription));
      openNotification(
        "topRight",
        "info",
        "Đã gửi yêu cầu",
        "Hệ thống đã tiếp nhận yêu cầu!"
      );

      try {
        const response = await fetch(apiAddMedicalRecord, {
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
            "Đã thêm hồ sơ y tế thành công!",
            handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra khi thêm hồ sơ y tế!",
            handleCancel
          );
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  const [medicationList, setMedicationList] = useState(null);
  const apiGetAllMedication = API.DOCTOR.GET_ALL_MEDICATION;
  const fetchGetAllMedication = async () => {
    if (access_token) {
      try {
        const response = await fetch(apiGetAllMedication, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        });

        if (response.status === 200) {
          // console.log("Hello");
          setMedicationList(await response.json());
          // setLoading(false);
        }
      } catch (e) {
        console.log(e);
      }
    }
  };

  const [treeData, setTreeData] = useState(null);

  useEffect(() => {
    if (access_token) fetchGetAllMedication();
  }, [access_token]);

  useEffect(() => {
    if (medicationList) {
      const value = medicationList.map((manufacturer) => ({
        title:
          manufacturer.manufacturerName + " | " + manufacturer.manufacturerId,
        value: JSON.stringify({
          manufacturerId: manufacturer.manufacturerId,
          manufacturerName: manufacturer.manufacturerName,
        }),
        selectable: false,
        children: manufacturer.medicationList.map((medication) => ({
          title:
            medication.medicationName +
            " | " +
            medication.medicationId.substring(0, 8) +
            "..." +
            medication.medicationId.substring(
              medication.medicationId.length - 8
            ),
          value: JSON.stringify({
            medicationId: medication.medicationId,
            medicationName: medication.medicationName,
          }),
        })),
      }));

      console.log("value");
      console.log(value);
      setTreeData(value);
      setLoading(false);
    }
  }, [medicationList]);

  const [value, setValue] = useState();
  const onChange = (newValue) => {
    console.log(newValue);
    setValue(newValue);
  };
  const onPopupScroll = (e) => {
    console.log("onPopupScroll", e);
  };

  console.log(request);

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

  const [hashFile, setHashFile] = useState("");

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [disabledButton, setDisabledButton] = useState(false);
  const [form] = Form.useForm();
  const [valuesForm, setValuesForm] = useState();
  const handleConfirm = (valuesForm) => {
    setIsConfirmModalOpen(true);
    setValuesForm(valuesForm);
  };

  const handleConfirmModalCancel = () => {
    setIsConfirmModalOpen(false);
  };

  const handleError = (valuesForm) => {
    openNotification(
      "topRight",
      "error",
      "Thất bại",
      "Dữ liệu nhập vào không hợp lệ, yêu cầu chưa được gửi đi!"
    );
    // setIsConfirmModalOpen(true);
    // setValuesForm(valuesForm);
  };

  return (
    <Context.Provider value={"Thêm hồ sơ y tế"}>
      {contextHolder}
      <AddMedicalRecordDialogStyle>
        <ModalWrapper
          title="Tạo hồ sơ y tế"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          loading={loading}
          // mask={false}
        >
          <Form
            name="basic"
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
              requestId: request.requestId,
              patientId: request.senderId,
              patientName: request.senderName,
              doctorId: request.recipientId,
              doctorName: request.recipientName,
              medicalInstitutionId: request.medicalInstitutionId,
              medicalInstitutionName: request.medicalInstitutionName,
              remember: true,
            }}
            onFinish={handleConfirm}
            onError={handleError}
            onFinishFailed={handleError}
            autoComplete="on"
          >
            <div style={{ width: "100%" }}>
              <Form.Item label="ID yêu cầu" name="requestId">
                <Input disabled />
              </Form.Item>
              <Form.Item label="ID bệnh nhân" name="patientId">
                <Input disabled />
              </Form.Item>
              <Form.Item label="Tên bệnh nhân" name="patientName">
                <Input disabled />
              </Form.Item>
              <Form.Item label="ID bác sĩ" name="doctorId">
                <Input disabled />
              </Form.Item>
              <Form.Item label="Tên bác sĩ" name="doctorName">
                <Input disabled />
              </Form.Item>
              <Form.Item label="ID cơ sở y tế" name="medicalInstitutionId">
                <Input disabled />
              </Form.Item>
              <Form.Item label="Tên cơ sở y tế" name="medicalInstitutionName">
                <Input disabled />
              </Form.Item>
              <Form.Item
                label="Tên xét nghiệm"
                name="testName"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền tên xét nghiệm!",
                  },
                  {
                    max: 100, // Giới hạn độ dài tối đa là 100 ký tự
                    message: 'Độ dài tối đa là 100 ký tự',
                  },
                ]}
              >
                <Input count={{
                  show: true,
                  max: 100,
                }}/>
              </Form.Item>

              <Form.Item
                label="Chi tiết xét nghiệm"
                name="details"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền chi tiết xét nghiệm!",
                  },
                  {
                    max: 100, // Giới hạn độ dài tối đa là 100 ký tự
                    message: 'Độ dài tối đa là 100 ký tự',
                  },
                ]}
              >
                <Input count={{
                  show: true,
                  max: 100,
                }}/>
              </Form.Item>

              <Form.Item
                label="File"
                name="hashFile"
                // rules={[
                //   {
                //     required: true,
                //     message: "Vui lòng gửi file!",
                //   },
                // ]}
              >
                <FileUploader hashFile={hashFile} setHashFile={setHashFile} />
              </Form.Item>

              <Form.Item label="Đơn thuốc" name="prescription">
                <Form.List name="prescriptionDetailsList">
                  {(fields, { add, remove }) => (
                    <div>
                      {fields.map(({ key, name, ...restField }) => (
                        <React.Fragment key={key}>
                          <div
                            // key={key}
                            style={{
                              display: "flex",
                              width: "100%",
                              // marginBottom: 8,
                              justifyContent: "center",
                              justifyItems: "center",
                            }}
                            align="baseline"
                            direction="horizon"
                          >
                            <Form.Item
                              {...restField}
                              name={[name, "medicationId"]}
                              style={{
                                width: "80%",
                              }}
                              rules={[
                                {
                                  required: true,
                                  message: "Chọn loại thuốc",
                                },
                              ]}
                            >
                              <TreeSelect
                                showSearch
                                style={{
                                  width: "99%",
                                  height: "100%",
                                  // marginRight: "1%",
                                }}
                                value={value}
                                dropdownStyle={{
                                  maxHeight: "100%",
                                  overflow: "auto",
                                }}
                                placeholder="Vui lòng chọn loại thuốc"
                                allowClear
                                treeDefaultExpandAll
                                onChange={onChange}
                                treeData={treeData}
                                onPopupScroll={onPopupScroll}
                              />
                            </Form.Item>

                            <Form.Item
                              {...restField}
                              name={[name, "quantity"]}
                              style={{
                                width: "17%",
                                marginRight: "1%",
                              }}
                              rules={[
                                {
                                  required: true,
                                  message: "Chọn số lượng",
                                },
                                {
                                  validator: (_, value) =>
                                    value > 0 && Number.isInteger(value)
                                      ? Promise.resolve()
                                      : Promise.reject(new Error('Số lượng phải là số nguyên và lớn hơn 0')),
                                }
                              ]}
                            >
                              <InputNumber
                                style={{
                                  width: "100%",
                                  marginRight: "1%",
                                }}
                                min={1}
                                placeholder="Số lượng"
                                type="number"
                              />
                            </Form.Item>

                            <MinusCircleOutlined
                              style={{
                                marginBottom: "24px",
                                display: "flex",
                                justifyContent: "center",
                                justifyItems: "center",
                                alignItems: "center",
                              }}
                              onClick={() => remove(name)}
                            />
                          </div>
                          <div key={"details" + key}>
                            <Form.Item
                              {...restField}
                              name={[name, "details"]}
                              style={{
                                width: "100%",
                                marginRight: "1%",
                              }}
                              rules={[
                                {
                                  required: true,
                                  message: "Vui lòng điền cách dùng",
                                },
                                {
                                  max: 100, // Giới hạn độ dài tối đa là 100 ký tự
                                  message: 'Độ dài tối đa là 100 ký tự',
                                },
                              ]}
                            >
                              <Input
                                style={{
                                  width: "100%",
                                  marginRight: "1%",
                                }}
                                placeholder="Cách dùng"
                                value={value}
                                count={{
                                  show: true,
                                  max: 100,
                                }}
                              />
                            </Form.Item>
                          </div>
                        </React.Fragment>
                      ))}
                      <Form.Item>
                        <Button
                          type="dashed"
                          onClick={() => add()}
                          block
                          icon={<PlusOutlined />}
                        >
                          Thêm loại thuốc
                        </Button>
                      </Form.Item>
                    </div>
                  )}
                </Form.List>
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
                Thêm hồ sơ y tế
              </Button>
            </div>
          </Form>
        </ModalWrapper>

        <ConfirmModal
          isOpen={isConfirmModalOpen}
          handleOk={handleAddMedicalRecordFormSubmit}
          handleCancel={handleConfirmModalCancel}
          title="Xác nhận"
          content="Bạn có chắc chắn không?"
        />
      </AddMedicalRecordDialogStyle>
    </Context.Provider>
  );
};

export default AddMedicalRecordDialog;
