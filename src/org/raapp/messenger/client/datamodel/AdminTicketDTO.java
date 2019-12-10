package org.raapp.messenger.client.datamodel;

public class AdminTicketDTO {

    private String uuid;
    private int companyID;
    private Ticket ticket;
    private long threadID;

    public AdminTicketDTO(Ticket ticket, long threadID) {
        this.ticket = ticket;
        this.threadID = threadID;
        this.uuid = ticket.getUuid();
        this.companyID = ticket.getCompanyId();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public long getThreadID() {
        return threadID;
    }

    public void setThreadID(long threadID) {
        this.threadID = threadID;
    }
}
