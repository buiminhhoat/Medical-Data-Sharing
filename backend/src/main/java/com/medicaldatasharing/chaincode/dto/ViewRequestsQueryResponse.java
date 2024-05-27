package com.medicaldatasharing.chaincode.dto;

import com.medicaldatasharing.dto.MedicalRecordDto;
import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ViewRequestsQueryResponse {
    @JsonProperty("total")
    private int total;

    @JsonProperty("viewRequestList")
    private List<ViewRequest> viewRequestList;

    public ViewRequestsQueryResponse(int total, List<ViewRequest> viewRequestList) {
        this.total = total;
        this.viewRequestList = viewRequestList;
    }

    public int getTotal() {
        return total;
    }

    public ViewRequestsQueryResponse setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<ViewRequest> getViewRequestList() {
        return viewRequestList;
    }

    public ViewRequestsQueryResponse setViewRequestList(List<ViewRequest> viewRequestList) {
        this.viewRequestList = viewRequestList;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static ViewRequestsQueryResponse deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, UTF_8));

        int total = json.getInt("total");
        JSONArray jsonArray = json.getJSONArray("viewRequestList");
        List<ViewRequest> viewRequestList = new ArrayList<>();
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                viewRequestList.add(new Genson().deserialize(jsonObject.toString(), ViewRequest.class));
            }
        }
        return new ViewRequestsQueryResponse(total, viewRequestList);
    }
}
