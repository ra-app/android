package org.raapp.messenger.networkRetrofit.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBase {
    @SerializedName("success")
    @Expose
    private Boolean success;

    //GETTERS && SETTERS
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
