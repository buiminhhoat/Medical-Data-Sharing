import { Routes, Route } from "react-router-dom";
import HomePage from "@Pages/users/homePage";
import Appointment from "./pages/users/AppointmentPage/AppointmentPage";
import { ROUTERS } from "@Utils/router";
import MasterLayout from "./layout/masterLayout";
import RequestPage from "./pages/users/RequestPage";
import MedicalRecordManagementPage from "./pages/users/MedicalRecordManagementPage";

const renderUserRouter = () => {
  const userRouters = [
    {
      path: ROUTERS.USER.HOME,
      component: <HomePage></HomePage>,
    },
    {
      path: ROUTERS.PATIENT.APPOINTMENT,
      component: <Appointment></Appointment>,
    },
    {
      path: ROUTERS.USER.REQUEST,
      component: <RequestPage></RequestPage>,
    },
    {
      path: ROUTERS.PATIENT.MEDICAL_RECORD_MANAGEMENT_PAGE,
      component: <MedicalRecordManagementPage></MedicalRecordManagementPage>,
    },
  ];

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
const RouterCustom = () => {
  return renderUserRouter();
};

export default RouterCustom;
