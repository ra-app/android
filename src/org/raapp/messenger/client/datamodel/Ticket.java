package org.raapp.messenger.client.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ticket {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("company_id")
    @Expose
    private int companyId;
    @SerializedName("client_uuid")
    @Expose
    private String clientUuid;
    @SerializedName("admin_uuid")
    @Expose
    private String adminUuid;
    @SerializedName("state")
    @Expose
    private int state;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("ts_created")
    @Expose
    private String tsCreated;
    @SerializedName("ts_claimed")
    @Expose
    private String tsClaimed;
    @SerializedName("ts_closed")
    @Expose
    private String tsClosed;


    private List<TicketEvent> ticketEvents;

    private boolean isExpanded = false;

    //GETTERS && SETTERS
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public int getCompanyId() {
        return companyId;
    }
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    public String getClientUuid() {
        return clientUuid;
    }
    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }
    public String getAdminUuid() {
        return adminUuid;
    }
    public void setAdminUuid(String adminUuid) {
        this.adminUuid = adminUuid;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getTsCreated() {
        return tsCreated;
    }
    public void setTsCreated(String tsCreated) {
        this.tsCreated = tsCreated;
    }
    public String getTsClaimed() {
        return tsClaimed;
    }
    public void setTsClaimed(String tsClaimed) {
        this.tsClaimed = tsClaimed;
    }
    public String getTsClosed() {
        return tsClosed;
    }
    public void setTsClosed(String tsClosed) {
        this.tsClosed = tsClosed;
    }

    public List<TicketEvent> getTicketEvents() {
        return ticketEvents;
    }

    public void setTicketEvents(List<TicketEvent> ticketEvents) {
        this.ticketEvents = ticketEvents;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}