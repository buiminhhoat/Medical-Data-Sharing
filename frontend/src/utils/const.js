export const DIALOGS = {
  LOGIN: "login",
  REGISTER: "register",
  FORGOT_PASSWORD: "forgot-password",
  REQUEST_DETAIL: "REQUEST_DETAIL",
  MEDICAL_RECORD: "MEDICAL_RECORD",
  ADD_MEDICAL_RECORD: "ADD_MEDICAL_RECORD",
};

export const API = {
  PUBLIC: {
    LOGIN_ENDPOINT: "/api/public/login",
    GET_USER_DATA: "/api/public/get-user-data",
    GET_ALL_REQUEST: "/api/public/get-all-request",
    GET_REQUEST: "/api/public/get-request",
    GET_ROLE: "/api/public/get-role",
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
  },
  DOCTOR: {
    GET_LIST_MEDICAL_RECORD: "/api/doctor/get-list-medical-record-by-patientId",
    ADD_MEDICAL_RECORD: "/api/doctor/add-medical-record",
    GET_ALL_MEDICATION: "/api/doctor/get-all-medication",
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
