import { Routes, Route } from "react-router-dom";
import HomePage from "@Pages/users/homePage";
import { ROUTERS } from "@Utils/router";
import MasterLayout from "@Theme/masterLayout";

const renderUserRouter = () => {
  const userRouters = [
    {
      path: ROUTERS.USER.HOME,
      component: <HomePage></HomePage>,
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
