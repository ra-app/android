package org.raapp.messenger.client.datamodel.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.Message;

public class RequestSendInvitation {

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("code")
    @Expose
    private Message code;

    //CONSTRUCTOR
    public RequestSendInvitation(String phoneNumber, Message code) {
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
    public Message getCode() {
        return code;
    }
    public void setCode(Message code) {
        this.code = code;
    }
}