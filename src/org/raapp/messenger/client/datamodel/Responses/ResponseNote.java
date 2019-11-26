package org.raapp.messenger.client.datamodel.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.raapp.messenger.client.datamodel.Note;

public class ResponseNote extends ResponseBase {
    @SerializedName("note")
    @Expose
    private Note note;

    //GETTERS && SETTERS
    public Note getNote() {
        return note;
    }
    public void setNote(Note note) {
        this.note = note;
    }
}