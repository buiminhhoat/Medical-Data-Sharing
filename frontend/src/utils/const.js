export const DIALOGS = {
  LOGIN: "login",
  REGISTER: "register",
  FORGOT_PASSWORD: "forgot-password",
  REQUEST_DETAIL: "REQUEST_DETAIL",
  MEDICAL_RECORD: "MEDICAL_RECORD",
  ADD_MEDICAL_RECORD: "ADD_MEDICAL_RECORD",
  SEND_REQUEST: "SEND_REQUEST",
  ADD_MEDICATION: "ADD_MEDICATION",
  DRUG_DETAIL: "DRUG_DETAIL",
  ADD_DRUG: "ADD_DRUG",
  DRUG_LIST: "DRUG_LIST",
  PRESCRIPTION_DETAIL: "PRESCRIPTION_DETAIL",
  SELLING_PRESCRIPTION_DRUG: "SELLING_PRESCRIPTION_DRUG",
  PURCHASE_DETAIL: "PURCHASE_DETAIL",
  ADD_USER: "ADD_USER",
  QRCODE_SCANNER: "QRCODE_SCANNER",
  USER_INFO: "USER_INFO",
  SHARE_PRESCRIPTION: "SHARE_PRESCRIPTION",
  CHANGE_PASSWORD: "CHANGE_PASSWORD",
  UPDATE_INFORMATION: "UPDATE_INFORMATION",
  UPDATE_DRUG_REACTION: "UPDATE_DRUG_REACTION",
  DRUG_REACTION_DETAIL: "DRUG_REACTION_DETAIL",
};

export const API = {
  PUBLIC: {
    LOGIN_ENDPOINT: "/api/public/login",
    GET_USER_DATA: "/api/public/get-user-data",
    GET_ALL_REQUEST: "/api/public/get-all-request",
    GET_REQUEST: "/api/public/get-request",
    GET_ROLE: "/api/public/get-role",
    SEND_REQUEST: "/api/public/send-request",
    GET_ALL_DOCTOR: "/api/public/get-all-doctor",
    GET_LIST_DRUG_BY_OWNER_ID: "/api/public/get-list-drug-by-ownerId",
    DEFINE_REQUEST: "/api/public/define-request",
    GET_USER_INFO: "/api/public/get-user-info",
    CHANGE_PASSWORD: "/api/public/change-password",
    UPDATE_INFORMATION: "/api/public/update-information",
  },
  PATIENT: {
    GET_LIST_MEDICAL_RECORD:
      "/api/patient/get-list-medical-record-by-patientId",
    GET_LIST_MEDICAL_RECORD_BY_PATIENT_ID:
      "/api/patient/get-list-medical-record-by-patientId",
    GET_MEDICAL_RECORD_BY_MEDICAL_RECORD_ID:
      "/api/patient/get-medical-record-by-medicalRecordId",
    GET_PRESCRIPTION_BY_PRESCRIPTION_ID:
      "/api/patient/get-prescription-by-prescriptionId",
    SEND_APPOINTMENT_REQUEST: "/api/patient/send-appointment-request",
    GET_LIST_PURCHASE_BY_PATIENT_ID:
      "/api/patient/get-list-purchase-by-patientId",
    GET_PURCHASE_BY_PURCHASE_ID: "/api/patient/get-purchase-by-purchaseId",
    DEFINE_MEDICAL_RECORD: "/api/patient/define-medical-record",
    SHARE_PRESCRIPTION_BY_PATIENT: "/api/patient/share-prescription-by-patient",
    UPDATE_DRUG_REACTION_BY_PATIENT:
      "/api/patient/update-drug-reaction-by-patient",
  },
  DOCTOR: {
    GET_LIST_MEDICAL_RECORD: "/api/doctor/get-list-medical-record-by-patientId",
    ADD_MEDICAL_RECORD: "/api/doctor/add-medical-record",
    GET_ALL_MEDICATION: "/api/doctor/get-all-medication",
    GET_ALL_PATIENT_MANAGED_BY_DOCTOR:
      "/api/doctor/get-all-patient-managed-by-doctorId",
    SEND_VIEW_REQUEST: "/api/doctor/send-view-request",
    DEFINE_MEDICAL_RECORD: "/api/doctor/define-medical-record",
    GET_PRESCRIPTION_BY_DOCTOR: "/api/doctor/get-prescription-by-doctor",
  },
  ADMIN: {
    GET_ALL_USER_BY_ADMIN: "/api/admin/get-all-user-by-admin",
    REGISTER_USER: "/api/admin/register-user",
    GET_USER_INFO: "/api/admin/get-user-info",
  },
  MANUFACTURER: {
    GET_LIST_MEDICATION_BY_MANUFACTURERID:
      "/api/manufacturer/get-list-medication-by-manufacturerId",
    ADD_MEDICATION: "/api/manufacturer/add-medication",
    ADD_DRUG: "/api/manufacturer/add-drug",
    GET_LIST_DRUG_REACTION_BY_MANUFACTURER:
      "/api/manufacturer/get-list-drug-reaction-by-manufacturer",
    GET_PRESCRIPTION_BY_MANUFACTURER:
      "/api/manufacturer/get-prescription-by-manufacturer",
    GET_LIST_MEDICAL_RECORD:
      "/api/manufacturer/get-list-medical-record-by-manufacturerId",
    GET_ALL_PATIENT_MANAGED_BY_MANUFACTURER:
      "/api/manufacturer/get-all-patient-managed-by-manufacturerId",
    SEND_VIEW_REQUEST: "/api/manufacturer/send-view-request",
  },
  DRUGSTORE: {
    GET_PRESCRIPTION_BY_DRUG_STORE:
      "/api/drugstore/get-prescription-by-drugstore",
    SEND_VIEW_PRESCRIPTION_REQUEST:
      "/api/drugstore/send-view-prescription-request",
    GET_LIST_DRUG_BY_MEDICATIONID_AND_OWNERID:
      "/api/drugstore/get-list-drug-by-medicationId-and-ownerId",
    ADD_PURCHASE: "/api/drugstore/add-purchase",
    GET_LIST_PURCHASE_BY_DRUGSTORE_ID:
      "/api/drugstore/get-list-purchase-by-drugStoreId",
    GET_PURCHASE_BY_PURCHASE_ID: "/api/drugstore/get-purchase-by-purchaseId",
  },
  MEDICAL_INSTITUTION: {
    GET_ALL_DOCTOR_BY_MEDICAL_INSTITUTION:
      "/api/medical_institution/get-all-doctor-by-medical-institution",
    GET_USER_INFO: "/api/medical_institution/get-user-info",
    REGISTER_USER: "/api/medical_institution/register-user",
  },

  RESEARCH_CENTER: {
    GET_ALL_SCIENTIST_BY_RESEARCH_CENTER:
      "/api/research_center/get-all-scientist-by-research-center",
    GET_USER_INFO: "/api/research_center/get-user-info",
    REGISTER_USER: "/api/research_center/register-user",
  },

  SCIENTIST: {
    GET_LIST_MEDICAL_RECORD:
      "/api/scientist/get-list-medical-record-by-scientistId",
    GET_ALL_PATIENT_MANAGED_BY_SCIENTIST:
      "/api/scientist/get-all-patient-managed-by-scientistId",
    SEND_VIEW_REQUEST: "/api/scientist/send-view-request",
    GET_PRESCRIPTION_BY_SCIENTIST:
      "/api/scientist/get-prescription-by-scientist",
  },
};

export const LOGIN = {
  LOGIN: "Đăng nhập",
  EMAIL_PHONE: "Email/Số điện thoại",
  EMAIL_PHONE_PLACEHOLDER: "Nhập email hoặc số điện thoại",
  PASSWORD: "Mật khẩu",
  PASSWORD_PLACEHOLDER: "Nhập mật khẩu",
  FORGOT_PASSWORD_QUESTION: "Quên mật khẩu?",
  LOGIN_BUTTON: "Đăng nhập",
  NO_ACCOUNT_QUESTION: "Chưa có tài khoản?",
  REGISTER_HERE: "Đăng ký tại đây",
};

export const ERROR = {
  NO_TOKEN_ERROR: "Không có token.",
  SENDING_TOKEN_ERROR: "Lỗi khi gửi token.",
  SERVER_CONNECTION_ERROR: "Lỗi kết nối tới máy chủ, vui lòng thử lại sau!",
};
