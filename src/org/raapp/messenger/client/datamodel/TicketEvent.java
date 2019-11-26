package org.raapp.messenger.client.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketEvent {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ticket_uuid")
    @Expose
    private String ticketUuid;
    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("json")
    @Expose
    private String json;

    //GETTERS && SETTERS
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTicketUuid() {
        return ticketUuid;
    }
    public void setTicketUuid(String ticketUuid) {
        this.ticketUuid = ticketUuid;
    }
    public String getTs() {
        return ts;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getJson() {
        return json;
    }
    public void setJson(String json) {
        this.json = json;
    }
}