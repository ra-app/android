package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.Company;

public class ResponseInvitationCode extends ResponseBase {

    @SerializedName("client_uuid")
    @Expose
    private String clientUuid;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("company")
    @Expose
    private Company company;

    //GETTERS
    public String getClientUuid() {
        return clientUuid;
    }
    public String getRole() {
        return role;
    }
    public String getCompanyId() {
        return companyId;
    }
    public Company getCompany() {
        return company;
    }
}