package org.raapp.messenger.client.datamodel.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RequestTicket {

    @SerializedName("limit")
    @Expose
    private int limit;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("state")
    @Expose
    private int state;

    public RequestTicket(int limit, int offset, int state) {
        this.limit = limit;
        this.offset = offset;
        this.state = state;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
