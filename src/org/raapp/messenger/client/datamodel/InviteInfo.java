package org.raapp.messenger.client.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteInfo {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("created_by_admin_uuid")
    @Expose
    private String createdByAdminUuid;
    @SerializedName("ts_created")
    @Expose
    private String tsCreated;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;
    @SerializedName("invited_admin_uuid")
    @Expose
    private Object invitedAdminUuid;
    @SerializedName("invited_client_uuid")
    @Expose
    private Object invitedClientUuid;
    @SerializedName("ts_accepted")
    @Expose
    private Object tsAccepted;
    @SerializedName("identifier")
    @Expose
    private String identifier;
}