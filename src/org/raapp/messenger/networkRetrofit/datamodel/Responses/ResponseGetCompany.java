package org.raapp.messenger.networkRetrofit.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.networkRetrofit.datamodel.Company;

public class ResponseGetCompany extends ResponseBase {
    @SerializedName("company")
    @Expose
    private Company company;

    //GETTERS && SETTERS
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
}