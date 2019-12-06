package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.InviteInfo;

public class ResponseInvitation extends ResponseBase {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("inviteInfo")
    @Expose
    private InviteInfo inviteInfo;

    //GETTERS && SETTERS
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public InviteInfo getInviteInfo() {
        return inviteInfo;
    }
    public void setInviteInfo(InviteInfo inviteInfo) {
        this.inviteInfo = inviteInfo;
    }
}