package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.CompanyRole;

import java.util.List;

public class ResponseCompanyList extends ResponseBase {

    @SerializedName("client")
    @Expose
    List<CompanyRole> clientCompanies;

    @SerializedName("admin")
    @Expose
    List<CompanyRole> adminCompanies;

    public List<CompanyRole> getClientCompanies() {
        return clientCompanies;
    }

    public void setClientCompanies(List<CompanyRole> clientCompanies) {
        this.clientCompanies = clientCompanies;
    }

    public List<CompanyRole> getAdminCompanies() {
        return adminCompanies;
    }

    public void setAdminCompanies(List<CompanyRole> adminCompanies) {
        this.adminCompanies = adminCompanies;
    }
}