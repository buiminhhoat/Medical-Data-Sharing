import { Routes, Route } from "react-router-dom";
import HomePage from "@Pages/users/homePage";
import Appointment from "./pages/users/AppointmentPage/AppointmentPage";
import { ROUTERS } from "@Utils/router";
import MasterLayout from "./layout/masterLayout";
import RequestPage from "./pages/users/RequestPage";

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
    }
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
