package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.TicketDetail;

import java.util.List;

public class ResponseTicketListDetail extends ResponseBase {

    @SerializedName("details")
    @Expose
    private List<TicketDetail> details;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    //GETTERS && SETTERS
    public List<TicketDetail> getDetails() {
        return details;
    }
    public void setDetails(List<TicketDetail> details) {
        this.details = details;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}