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
  },
  DOCTOR: {
    GET_LIST_MEDICAL_RECORD: "/api/doctor/get-list-medical-record-by-patientId",
    ADD_MEDICAL_RECORD: "/api/doctor/add-medical-record",
    GET_ALL_MEDICATION: "/api/doctor/get-all-medication",
    GET_ALL_PATIENT_MANAGED_BY_DOCTOR:
      "/api/doctor/get-all-patient-managed-by-doctorId",
    SEND_VIEW_REQUEST: "/api/doctor/send-view-request",
  },
  ADMIN: {
    GET_ALL_USER_BY_ADMIN: "/api/admin/get-all-user-by-admin",
  },
  MANUFACTURER: {
    GET_LIST_MEDICATION_BY_MANUFACTURERID:
      "/api/manufacturer/get-list-medication-by-manufacturerId",
    ADD_MEDICATION: "/api/manufacturer/add-medication",
    ADD_DRUG: "/api/manufacturer/add-drug",
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
      "/api/patient/get-list-purchase-by-drugStoreId",
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
