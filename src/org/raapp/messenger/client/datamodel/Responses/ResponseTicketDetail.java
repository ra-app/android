package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.TicketDetail;

public class ResponseTicketDetail extends ResponseBase {

    @SerializedName("details")
    @Expose
    private TicketDetail details;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    //GETTERS && SETTERS
    public TicketDetail getDetails() {
        return details;
    }
    public void setDetails(TicketDetail details) {
        this.details = details;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}