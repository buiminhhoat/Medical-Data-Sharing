import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Cookies, useCookies } from "react-cookie";
import { UserOutlined, CloseOutlined } from "@ant-design/icons";
import { Avatar, Flex, InputNumber, Space, TreeSelect } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
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
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { VscCommentUnresolved } from "react-icons/vsc";
const { Option } = Select;

const AddMedicalRecordDialogStyle = styled.div`
  overflow: auto;
`;

const Info = styled.div`
  display: flex;
  /* justify-content: center; */
  /* justify-items: center; */
  margin-bottom: 15px;
  .field {
    width: 20%;
    margin-right: 3%;
  }
`;

const StyledList = styled(List)`
  .ant-list-items > .ant-list-item:nth-child(odd) {
    background-color: rgb(246, 255, 237);
  }
  .ant-list-items > .ant-list-item:nth-child(even) {
    background-color: rgb(230, 230, 230);
  }
`;

const AddMedicalRecordDialog = ({ request, onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token", "userId"]);
  const access_token = cookies.access_token;
  const userId = cookies.userId;
  const role = cookies.role;
  let apiAddMedicalRecord = API.DOCTOR.ADD_MEDICAL_RECORD;
  const [isModalOpen, setIsModalOpen] = useState(true);

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  const handleCancel = () => {
    setIsModalOpen(false);
    onClose();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const handleAddMedicalRecordFormSubmit = async (values) => {
    // values.requestId = request.requestId;

    console.log(values);
  };
  const addMedicalRecord = async () => {
    if (access_token) {
      const formData = new FormData();

      try {
        const response = await fetch(apiAddMedicalRecord, {
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

  // useEffect(() => {
  //   if (access_token) addMedicalRecord().then((r) => {});
  // }, [access_token]);

  const treeData = [
    {
      value: "Công ty A | ID của công ty A",
      title: "Công ty A | ID của công ty A",
      selectable: false,
      children: [
        {
          value: JSON.stringify({ id: "Thuốc ABC", name: "medicationId" }),
          title: "Thuốc ABC",
        },
        {
          value: "Thuốc C | ID của thuốc C",
          title: "Thuốc C | ID của thuốc C",
        },
      ],
    },
    {
      value: "Công ty D | ID của công ty D",
      title: "Công ty D | ID của công ty D",
      selectable: false,
      children: [
        {
          value: "Thuốc E | ID của thuốc E",
          title: "Thuốc E | ID của thuốc E",
        },
        {
          value: "Thuốc F | ID của thuốc F",
          title: "Thuốc F | ID của thuốc F",
        },
      ],
    },
  ];

  const [value, setValue] = useState();
  const onChange = (newValue) => {
    console.log(newValue);
    setValue(newValue);
  };
  const onPopupScroll = (e) => {
    console.log("onPopupScroll", e);
  };

  console.log(request);

  return (
    <AddMedicalRecordDialogStyle>
      <Modal
        title="Tạo hồ sơ y tế"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        centered
        width={"60%"}
        // loading={loading}
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
          onFinish={handleAddMedicalRecordFormSubmit}
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
              name="file"
              rules={[
                {
                  required: true,
                  message: "Vui lòng gửi file!",
                },
              ]}
            >
              <Input />
            </Form.Item>

            <Form.Item label="Đơn thuốc" name="prescription">
              <Form.List name="prescriptionDetailsList">
                {(fields, { add, remove }) => (
                  <div>
                    {fields.map(({ key, name, ...restField }) => (
                      <>
                        <div
                          key={key}
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
                            name={[name, "medicationName"]}
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
                        <div>
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
                                message: "Chọn số lượng",
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
                      </>
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
            <Button htmlType="submit">Thêm hồ sơ y tế</Button>
          </div>
        </Form>
      </Modal>
    </AddMedicalRecordDialogStyle>
  );
};

export default AddMedicalRecordDialog;
