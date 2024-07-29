export const ROUTERS = {
  USER: {
    HOME: "",
    PROFILE: "/profile",
    REQUEST: "/request-management-page",
  },

  PATIENT: {
    APPOINTMENT: "/patient/appointment",
    MEDICAL_RECORD_MANAGEMENT_PAGE: "/patient/medical-record-management-page",
    HISTORY_PURCHASE: "/patient/history-purchase",
  },
  DOCTOR: {
    PATIENT_MANAGED_BY_DOCTOR_PAGE: "/doctor/patient-managed-by-doctor-page",
  },
  MANUFACTURER: {
    MEDICATION_MANAGEMENT_PAGE: "/manufacturer/medication-management-page",
    DRUG_MANAGEMENT_PAGE: "/manufacturer/drug-management-page",
    DRUG_REACTION_MANAGEMENT_PAGE:
      "/manufacturer/drug-reaction-management-page",
    PATIENT_MANAGED_BY_MANUFACTURER_PAGE:
      "/manufacturer/patient-managed-by-manufacturer-page",
  },
  ADMIN: {
    HOME: "",
    USER_MANAGEMENT: "/admin/user-management",
  },
  DRUGSTORE: {
    HISTORY_PURCHASE: "/drugstore/history-purchase",
  },
  MEDICAL_INSTITUTION: {
    DOCTOR_MANAGEMENT_PAGE: "/medical_institution/doctor-management-page",
  },
  RESEARCH_CENTER: {
    SCIENTIST_MANAGEMENT_PAGE: "/research_center/scientist-management-page",
  },
};
