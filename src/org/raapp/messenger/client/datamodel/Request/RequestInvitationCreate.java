package org.raapp.messenger.client.datamodel.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInvitationCreate {
    @SerializedName("isAdminInvite")
    @Expose
    private Boolean isAdminInvite;
    @SerializedName("identifier")
    @Expose
    private String identifier;

    //CONSTRUCTOR
    public RequestInvitationCreate(Boolean isAdminInvite, String identifier) {
        this.isAdminInvite = isAdminInvite;
        this.identifier = identifier;
    }

    //GETTERS && SETTERS
    public Boolean getAdminInvite() {
        return isAdminInvite;
    }
    public void setAdminInvite(Boolean adminInvite) {
        isAdminInvite = adminInvite;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}