package org.raapp.messenger.client.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {
    @SerializedName("company_number")
    @Expose
    private Integer companyNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("business")
    @Expose
    private String business;
    @SerializedName("active")
    @Expose
    private Boolean active;

    //GETTERS && SETTERS
    public Integer getCompanyNumber() {
        return companyNumber;
    }
    public void setCompanyNumber(Integer companyNumber) {
        this.companyNumber = companyNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBusiness() {
        return business;
    }
    public void setBusiness(String business) {
        this.business = business;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}