package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.Note;

import java.util.List;

public class ResponseBlackboardList extends ResponseBase {
    @SerializedName("Note")
    @Expose
    private List<Note> note = null;

    //GETTERS && SETTERS
    public List<Note> getNote() {
        return note;
    }
    public void setNote(List<Note> note) {
        this.note = note;
    }
}