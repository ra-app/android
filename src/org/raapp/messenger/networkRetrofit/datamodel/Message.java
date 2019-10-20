package org.raapp.messenger.networkRetrofit.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {
    @SerializedName("attachments")
    @Expose
    private List<Object> attachments;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("preview")
    @Expose
    private List<Object> preview ;
    @SerializedName("recipients")
    @Expose
    private List<String> recipients;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("attachmentPointers")
    @Expose
    private List<Object> attachmentPointers;

    //GETTERS && SETTERS
    public List<Object> getAttachments() {
        return attachments;
    }
    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public List<Object> getPreview() {
        return preview;
    }
    public void setPreview(List<Object> preview) {
        this.preview = preview;
    }
    public List<String> getRecipients() {
        return recipients;
    }
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
    public Integer getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
    public List<Object> getAttachmentPointers() {
        return attachmentPointers;
    }
    public void setAttachmentPointers(List<Object> attachmentPointers) {
        this.attachmentPointers = attachmentPointers;
    }
}
