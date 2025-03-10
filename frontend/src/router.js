import React, { useEffect, useState } from "react";
import { Routes, Route } from "react-router-dom";
import HomePage from "@Pages/users/homePage";
import Appointment from "./pages/users/AppointmentPage/AppointmentPage";
import { ROUTERS } from "@Utils/router";
import MasterLayout from "./layout/masterLayout";
import RequestManagementPage from "./pages/users/RequestManagementPage";
import MedicalRecordManagementPage from "./pages/users/MedicalRecordManagementPage";

import { API } from "@Const";
import Storage from '@Utils/Storage';
import PatientManagedPage from "./pages/users/PatientManagedPage";
import UserManagement from "./pages/admin/UserManagement";
import MedicationManagementPage from "./pages/manufacturer/MedicationManagementPage";
import DrugManagementPage from "./pages/users/DrugManagementPage";
import HistoryPurchasePage from "./pages/users/HistoryPurchasePage";
import ProfilePage from "./pages/users/ProfilePage";
import DrugReactionManagementPage from "./pages/manufacturer/DrugReactionManagementPage";
import DrugManagementDrugStorePage from "./pages/drugstore/DrugManagementDrugStorePage";
import { useLogout } from "./utils/logout";

const userRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.USER.PROFILE,
    component: <ProfilePage></ProfilePage>,
  },
];

const patientRouters = [
  {
    path: ROUTERS.PATIENT.APPOINTMENT,
    component: <Appointment></Appointment>,
  },
  {
    path: ROUTERS.PATIENT.MEDICAL_RECORD_MANAGEMENT_PAGE,
    component: <MedicalRecordManagementPage></MedicalRecordManagementPage>,
  },
  {
    path: ROUTERS.PATIENT.HISTORY_PURCHASE,
    component: <HistoryPurchasePage></HistoryPurchasePage>,
  },
  {
    path: ROUTERS.PATIENT.DRUG_MANAGEMENT_PAGE,
    component: <DrugManagementDrugStorePage></DrugManagementDrugStorePage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestManagementPage></RequestManagementPage>,
  },
];

const doctorRouters = [
  {
    path: ROUTERS.DOCTOR.PATIENT_MANAGED_BY_DOCTOR_PAGE,
    component: <PatientManagedPage></PatientManagedPage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestManagementPage></RequestManagementPage>,
  },
];

const adminRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.ADMIN.USER_MANAGEMENT,
    component: <UserManagement></UserManagement>,
  },
];

const medicalInstitutionRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.MEDICAL_INSTITUTION.DOCTOR_MANAGEMENT_PAGE,
    component: <UserManagement></UserManagement>,
  },
];

const researchCenterRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.RESEARCH_CENTER.SCIENTIST_MANAGEMENT_PAGE,
    component: <UserManagement></UserManagement>,
  },
];

const manufacturerRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.MANUFACTURER.MEDICATION_MANAGEMENT_PAGE,
    component: <MedicationManagementPage></MedicationManagementPage>,
  },
  {
    path: ROUTERS.MANUFACTURER.DRUG_MANAGEMENT_PAGE,
    component: <DrugManagementPage></DrugManagementPage>,
  },
  {
    path: ROUTERS.MANUFACTURER.DRUG_REACTION_MANAGEMENT_PAGE,
    component: <DrugReactionManagementPage></DrugReactionManagementPage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestManagementPage></RequestManagementPage>,
  },
  {
    path: ROUTERS.MANUFACTURER.PATIENT_MANAGED_BY_MANUFACTURER_PAGE,
    component: <PatientManagedPage></PatientManagedPage>,
  },
];

const scientistRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestManagementPage></RequestManagementPage>,
  },
  {
    path: ROUTERS.SCIENTIST.PATIENT_MANAGED_BY_SCIENTIST_PAGE,
    component: <PatientManagedPage></PatientManagedPage>,
  },
];

const drugStoreRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestManagementPage></RequestManagementPage>,
  },
  {
    path: ROUTERS.DRUGSTORE.HISTORY_PURCHASE,
    component: <HistoryPurchasePage></HistoryPurchasePage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestManagementPage></RequestManagementPage>,
  },
  {
    path: ROUTERS.DRUGSTORE.DRUG_MANAGEMENT_PAGE,
    component: <DrugManagementDrugStorePage></DrugManagementDrugStorePage>,
  },
];

const renderUserRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const renderPatientRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {patientRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {/* <Route path='*' element={<NotFoundPage />} /> */}
      </Routes>
    </MasterLayout>
  );
};

const renderDoctorRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {doctorRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {/* <Route path='*' element={<NotFoundPage />} /> */}
      </Routes>
    </MasterLayout>
  );
};

const renderAdminRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {adminRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const renderMedicalInstitutionRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {medicalInstitutionRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const renderResearchCenterRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {researchCenterRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const renderManufacturerRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {manufacturerRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const renderScientistRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {scientistRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const renderDrugStoreRouter = () => {
  return (
    <MasterLayout>
      <Routes>
        {userRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
        {drugStoreRouters.map((item, key) => (
          <Route key={key} path={item.path} element={item.component} />
        ))}
      </Routes>
    </MasterLayout>
  );
};

const RouterCustom = () => {
  const { access_token, userId,  } = Storage.getData();

  const [role, setRole] = useState(Storage.getItem("role"));

  console.log("access_token: ", access_token);
  console.log("userId: ", userId);
  console.log("role: ", role);

  const logout = useLogout();

  console.log(":)", Storage.getItem("role") === null);
  const fetchGetUserData = async () => {
    if (Storage.getItem("access_token")) {
      console.log("***", Storage.getItem("role"));
      try {
        let org = "";

        switch (Storage.getItem("role")) {
          case "Bệnh nhân":
            org = "patient";
            break;
          case "Bác sĩ":
            org = "doctor";
            break;
          case "Cơ sở y tế":
            org = "medical_institution";
            break;
          case "Trung tâm nghiên cứu":
            org = "research_center";
            break;
          case "Nhà khoa học":
            org = "scientist";
            break;
          case "Công ty sản xuất thuốc":
            org = "manufacturer";
            break;
          case "Nhà thuốc":
            org = "drugstore";
            break;
          case "Quản trị viên":
            org = "admin";
            break;
          default:
            org = "";
        }
        const apiGetUserData = "/api/" + org + "/permit-all/get-user-data";
        const response = await fetch(apiGetUserData, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${Storage.getItem("access_token")}`,
          },
        });

        if (response.status === 200) {
          const data = await response.json();
          setRole(data.role);
        } else {
          logout();
        }
      } catch (error) {
        setRole("");
        console.log(error);
        logout();
      }
    }
  };

  useEffect(() => {
    fetchGetUserData().then((r) => {});
  }, []);

  return (
    (Storage.getItem("role") === null && renderUserRouter()) ||
    (Storage.getItem("role") === "" && renderUserRouter()) || 
    (Storage.getItem("role") === "Bệnh nhân" && renderPatientRouter()) ||
    (Storage.getItem("role") === "Bác sĩ" && renderDoctorRouter()) ||
    (Storage.getItem("role") === "Quản trị viên" && renderAdminRouter()) ||
    (Storage.getItem("role") === "Công ty sản xuất thuốc" && renderManufacturerRouter()) ||
    (Storage.getItem("role") === "Nhà thuốc" && renderDrugStoreRouter()) ||
    (Storage.getItem("role") === "Cơ sở y tế" && renderMedicalInstitutionRouter()) ||
    (Storage.getItem("role") === "Trung tâm nghiên cứu" && renderResearchCenterRouter()) ||
    (Storage.getItem("role") === "Nhà khoa học" && renderScientistRouter())
  );
};

export default RouterCustom;
