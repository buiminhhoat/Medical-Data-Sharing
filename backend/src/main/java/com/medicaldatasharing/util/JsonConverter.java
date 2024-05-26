package com.medicaldatasharing.util;

import org.json.JSONObject;
import java.lang.reflect.Field;

public class JsonConverter {
    public static JSONObject objectToJson(Object obj) throws IllegalAccessException {
        JSONObject jsonObject = new JSONObject();
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields(); // Lấy tất cả các trường từ lớp đối tượng

        for (Field field : fields) {
            field.setAccessible(true); // Cho phép truy cập, ngay cả đối với các trường private
            jsonObject.put(field.getName(), field.get(obj)); // Thêm tên trường và giá trị vào jsonObject
        }
        return jsonObject;
    }
}
