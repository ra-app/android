package org.raapp.messenger.client.datamodel.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.Message;


public class RequestSend {

    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("message")
    @Expose
    private Message message;

    public RequestSend(String destination, Message message) {
        this.destination = destination;
        this.message = message;
    }

    //GETTERS && SETTERS
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }
}