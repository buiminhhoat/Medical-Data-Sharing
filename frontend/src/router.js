import React, { useEffect, useState } from "react";
import { Routes, Route } from "react-router-dom";
import HomePage from "@Pages/users/homePage";
import Appointment from "./pages/users/AppointmentPage/AppointmentPage";
import { ROUTERS } from "@Utils/router";
import MasterLayout from "./layout/masterLayout";
import RequestPage from "./pages/users/RequestPage";
import MedicalRecordManagementPage from "./pages/users/MedicalRecordManagementPage";
import { API } from "@Const";
import { useCookies } from "react-cookie";

const userRouters = [
  {
    path: ROUTERS.USER.HOME,
    component: <HomePage></HomePage>,
  },
  {
    path: ROUTERS.USER.REQUEST,
    component: <RequestPage></RequestPage>,
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
];

const doctorRouters = [];

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

const RouterCustom = () => {
  const [cookies] = useCookies(["access_token"]);
  const accessToken = cookies.access_token;

  const [role, setRole] = useState("");

  const fetchGetUserData = async () => {
    console.log(role);
    try {
      const response = await fetch(API.PUBLIC.GET_USER_DATA, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (response.status === 200) {
        const data = await response.json();
        setRole(data.role);
        console.log("hello " + role);
      } else {
        setRole("");
      }
    } catch (error) {
      setRole("");
      console.log(error);
    }
  };

  useEffect(() => {
    fetchGetUserData().then((r) => {});
  }, []);

  return (
    (role === "" && renderUserRouter()) ||
    (role === "Bệnh nhân" && renderPatientRouter()) ||
    (role === "Bác sĩ" && renderDoctorRouter())
  );
};

export default RouterCustom;
