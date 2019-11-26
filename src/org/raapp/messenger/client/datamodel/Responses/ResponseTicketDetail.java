package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.TicketDetail;

public class ResponseTicketDetail extends ResponseBase {

    @SerializedName("details")
    @Expose
    private TicketDetail details;

    //GETTERS && SETTERS
    public TicketDetail getDetails() {
        return details;
    }
    public void setDetails(TicketDetail details) {
        this.details = details;
    }
}