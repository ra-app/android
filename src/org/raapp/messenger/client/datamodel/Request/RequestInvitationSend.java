package org.raapp.messenger.client.datamodel.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInvitationSend {

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("code")
    @Expose
    private String code;

    //CONSTRUCTOR
    public RequestInvitationSend(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    //GETTERS && SETTERS
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}