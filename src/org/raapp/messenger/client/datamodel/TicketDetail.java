package org.raapp.messenger.client.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketDetail {
    @SerializedName("ticket")
    @Expose
    private Ticket ticket;
    @SerializedName("events")
    @Expose
    private List<TicketEvent> events;

    //GETTERS && SETTERS
    public Ticket getTicket() {
        return ticket;
    }
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    public List<TicketEvent> getEvents() {
        return events;
    }
    public void setEvents(List<TicketEvent> events) {
        this.events = events;
    }
}