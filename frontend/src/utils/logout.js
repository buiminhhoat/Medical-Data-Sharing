import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import { notification } from "antd";

export function useLogout(api, contextHolder) {
  const navigate = useNavigate();
  const [cookies, , removeCookie] = useCookies();

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
      // Xóa tất cả các cookies
      const cookieNames = Object.keys(cookies);
      for (const cookieName of cookieNames) {
        await removeCookie(cookieName, { path: "/" });
      }

      // Xóa toàn bộ caches
      if ("caches" in window) {
        const cacheNames = await caches.keys();
        await Promise.all(
          cacheNames.map(async function (cacheName) {
            return await caches.delete(cacheName);
          })
        );
      }
      // Redirect về trang chính của bạn (localhost:3000)
      // toast.success(MESSAGE.LOGOUT_SUCCESS);
      //   <Alert message="Đăng xuất thành công!" type="success"></Alert>;
      //   alert("Đăng xuất thành công!");
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
