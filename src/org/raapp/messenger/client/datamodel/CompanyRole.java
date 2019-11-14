package org.raapp.messenger.client.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyRole {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("company_id")
    @Expose
    private int companyId;
    @SerializedName(value = "client_uuid", alternate = "uuid")
    @Expose
    private  String clientUuid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
