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
const { Option } = Select;

const Context = React.createContext({
  name: "Default",
});

const EditMedicalRecordDialogStyle = styled.div`
  overflow: auto;
`;

const EditMedicalRecordDialog = ({ values, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiEditMedicalRecord = API.DOCTOR.ADD_MEDICAL_RECORD;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const handleEditMedicalRecordFormSubmit = async (values) => {
    // values.requestId = values.requestId;
    console.log("handleEditMedicalRecordFormSubmit");
    console.log(values);
    console.log("Hashfile: ", hashFile);
    if (access_token) {
      const formData = new FormData();
      formData.append("requestId", values.requestId);
      formData.append("patientId", values.patientId);
      formData.append("patientName", values.patientName);
      formData.append("doctorId", values.doctorId);
      formData.append("doctorName", values.doctorName);
      formData.append("medicalInstitutionId", values.medicalInstitutionId);
      formData.append("medicalInstitutionName", values.medicalInstitutionName);
      formData.append("testName", values.testName);
      formData.append("details", values.details);
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
        const response = await fetch(apiEditMedicalRecord, {
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
            "Đã chỉnh sửa hồ sơ y tế thành công!",
            handleCancel
          );
          setLoading(false);
        } else {
          openNotification(
            "topRight",
            "error",
            "Thất bại",
            "Đã có lỗi xảy ra khi chỉnh sửa hồ sơ y tế!",
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
          title: medication.medicationName,
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

  return (
    <Context.Provider value={"Chỉnh sửa hồ sơ y tế"}>
      {contextHolder}
      <EditMedicalRecordDialogStyle>
        <Modal
          title="Chỉnh sửa hồ sơ y tế"
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          centered
          width={"60%"}
          loading={loading}
        >
          <Form
            name="editMedicalRecord"
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
              requestId: values.requestId,
              patientId: values.senderId,
              patientName: values.senderName,
              doctorId: values.recipientId,
              doctorName: values.recipientName,
              medicalInstitutionId: values.medicalInstitutionId,
              medicalInstitutionName: values.medicalInstitutionName,
              remember: true,
            }}
            onFinish={handleEditMedicalRecordFormSubmit}
            onFinishFailed={onFinishFailed}
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
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                label="Chi tiết xét nghiệm"
                name="details"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng điền chi tiết xét nghiệm!",
                  },
                ]}
              >
                <Input />
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
                              ]}
                            >
                              <Input
                                style={{
                                  width: "100%",
                                  marginRight: "1%",
                                }}
                                placeholder="Cách dùng"
                                value={value}
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
              <Button htmlType="submit">Chỉnh sửa hồ sơ y tế</Button>
            </div>
          </Form>
        </Modal>
      </EditMedicalRecordDialogStyle>
    </Context.Provider>
  );
};

export default EditMedicalRecordDialog;
