package org.raapp.messenger.client.datamodel;

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
    private Long timestamp;
    @SerializedName("attachmentPointers")
    @Expose
    private List<Object> attachmentPointers;

    public Message(List<Object> attachments, String body, List<Object> preview, List<String> recipients, Long timestamp, List<Object> attachmentPointers) {
        this.attachments = attachments;
        this.body = body;
        this.preview = preview;
        this.recipients = recipients;
        this.timestamp = timestamp;
        this.attachmentPointers = attachmentPointers;
    }

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
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public List<Object> getAttachmentPointers() {
        return attachmentPointers;
    }
    public void setAttachmentPointers(List<Object> attachmentPointers) {
        this.attachmentPointers = attachmentPointers;
    }
}
