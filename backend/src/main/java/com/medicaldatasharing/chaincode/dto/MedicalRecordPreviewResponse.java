package com.medicaldatasharing.chaincode.dto;

import com.medicaldatasharing.dto.MedicalRecordDto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MedicalRecordPreviewResponse {
    private int total;
    private List<MedicalRecordDto> medicalRecordDtoList;

    public MedicalRecordPreviewResponse(int total, List<MedicalRecordDto> medicalRecordDtoList) {
        this.total = total;
        this.medicalRecordDtoList = medicalRecordDtoList;
    }

    public static MedicalRecordPreviewResponse deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, UTF_8));

        int total = json.getInt("total");
        JSONArray jsonArray = json.getJSONArray("medicalRecordDtoList");
        List<MedicalRecordDto> medicalRecordDtoList = new ArrayList<>();
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                medicalRecordDtoList.add(MedicalRecordDto.parseMedicalRecordDto(jsonObject));
            }
        }
        return new MedicalRecordPreviewResponse(total, medicalRecordDtoList);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MedicalRecordDto> getMedicalRecordDtoList() {
        return medicalRecordDtoList;
    }

    public MedicalRecordPreviewResponse setMedicalRecordDtoList(List<MedicalRecordDto> medicalRecordDtoList) {
        this.medicalRecordDtoList = medicalRecordDtoList;
        return this;
    }
}
