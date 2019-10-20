package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseSendMessage extends ResponseBase {
    @SerializedName("text")
    @Expose
    private String text;

    //GETTERS && SETTERS
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}