import Storage from '@Utils/Storage';
import { useNavigate } from "react-router-dom";
import { notification } from "antd";

export function useLogout(api, contextHolder) {
  const navigate = useNavigate();

  const openNotification = (placement, type, message, description, onClose) => {
    api[type]({
      message: message,
      description: description,
      placement,
      showProgress: true,
      pauseOnHover: true,
      onClose: onClose,
    });
  };

  return async () => {
    try {
      console.log("logout");

      Storage.clearData();

      navigate("/");
      if (api && contextHolder) {
        openNotification(
          "bottomRight",
          "success",
          "Thành công",
          "Đăng xuất thành công!"
        );
      }
    } catch (error) {
      if (api && contextHolder) {
        openNotification(
          "bottomRight",
          "error",
          "Thất bại",
          "Đăng xuất thất bại!"
        );
      }
    }
  };
}
