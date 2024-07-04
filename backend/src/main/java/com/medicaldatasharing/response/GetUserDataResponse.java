package com.medicaldatasharing.response;

public class GetUserDataResponse {
    private String firstName;
    private String lastName;
    private String role;

    public GetUserDataResponse(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public GetUserDataResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public GetUserDataResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getRole() {
        return role;
    }

    public GetUserDataResponse setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "GetUserDataResponse{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
