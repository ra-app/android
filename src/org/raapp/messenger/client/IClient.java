package org.raapp.messenger.client;

import org.raapp.messenger.client.datamodel.Note;
import org.raapp.messenger.client.datamodel.Request.RequestInvitationCreate;
import org.raapp.messenger.client.datamodel.Request.RequestInvitationSend;
import org.raapp.messenger.client.datamodel.Request.RequestSend;
import org.raapp.messenger.client.datamodel.Request.RequestTicket;
import org.raapp.messenger.client.datamodel.Responses.ResponseBase;
import org.raapp.messenger.client.datamodel.Responses.ResponseBlackboardList;
import org.raapp.messenger.client.datamodel.Responses.ResponseCompanyList;
import org.raapp.messenger.client.datamodel.Responses.ResponseGetCompany;
import org.raapp.messenger.client.datamodel.Responses.ResponseInvitation;
import org.raapp.messenger.client.datamodel.Responses.ResponseInvitationCode;
import org.raapp.messenger.client.datamodel.Responses.ResponseNote;
import org.raapp.messenger.client.datamodel.Responses.ResponseSendMessage;
import org.raapp.messenger.client.datamodel.Responses.ResponseTicketDetail;
import org.raapp.messenger.client.datamodel.Responses.ResponseTickets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IClient {

    //----------------------------CALLS-------------------------------------------------------------
    @GET(Client.ENDPOINT_ACCEPT_INVITATION)
    Call<ResponseInvitationCode> acceptInvitation(@Header(Client.AUTH_HEADER) String authToken,
                                                  @Path(Client.GENERIC_ID) String invitationCode);
    @GET(Client.ENDPOINT_GET_COMPANY)
    Call<ResponseGetCompany> getCompanyByID(@Header(Client.AUTH_HEADER) String authToken,
                                            @Path(Client.GENERIC_ID) String companyID);
    @GET(Client.ENDPOINT_GET_COMPANY_LIST)
    Call<ResponseCompanyList> getCompanyList(@Header(Client.AUTH_HEADER) String authToken);

    @GET(Client.ENDPOINT_GET_COMPANY_IMAGE)
    Call<ResponseBody> getCompanyImage(@Header(Client.AUTH_HEADER) String authToken,
                                       @Path(Client.GENERIC_ID) String companyID);
    @POST(Client.ENDPOINT_SEND_MESSAGE)
    Call<ResponseSendMessage> sendMessage(@Header(Client.AUTH_HEADER) String authToken,
                                          @Body RequestSend requestSend);
    //----------------------------------------------------------------------------------------------

    //-----------------------------------BLACKBOARD-------------------------------------------------
    @GET(Client.ENDPOINT_GET_BLACKBOARD)
    Call<ResponseBlackboardList> getBlackboard(@Header(Client.AUTH_HEADER) String authToken,
                                               @Path(Client.GENERIC_ID) String clientId);
    @POST(Client.ENDPOINT_UPDATE_BLACKBOARD)
    Call<ResponseNote> updateBlackboardNote(@Header(Client.AUTH_HEADER) String authToken,
                                            @Body Note note,
                                            @Path(Client.GENERIC_ID) String companyId);
    //----------------------------------------------------------------------------------------------
    //-----------------------------------ADMIN------------------------------------------------------
    @POST(Client.ENDPOINT_CREATE_INVITATION)
    Call<ResponseInvitation> createInvitation(@Header(Client.AUTH_HEADER) String authToken,
                                              @Body RequestInvitationCreate requestInvitationCreate,
                                              @Path(Client.GENERIC_ID) String companyId);
    @POST(Client.ENDPOINT_SEND_INVITE)
    Call<ResponseBase> sendInvitation(@Header(Client.AUTH_HEADER) String authToken,
                                      @Body RequestInvitationSend requestInvitationSend,
                                      @Path(Client.GENERIC_ID) String companyId);

    //----------------------------------------------------------------------------------------------
    //-----------------------------------TICKETS----------------------------------------------------
    @POST(Client.ENDPOINT_GET_TICKET)
    Call<ResponseTickets> getTickets(@Header(Client.AUTH_HEADER) String authToken,
                                               @Body RequestTicket requestTicket,
                                               @Path(Client.GENERIC_ID) String companyId);
    @GET(Client.ENDPOINT_GET_TICKET_DETAIL)
    Call<ResponseTicketDetail> getTicketDetail(@Header(Client.AUTH_HEADER) String authToken,
                                               @Path(Client.GENERIC_ID) String companyId,
                                               @Path(Client.GENERIC_ID2) String ticketId);
    @GET(Client.ENDPOINT_CLAIM_TICKET)
    Call<ResponseTicketDetail> claimTicket(@Header(Client.AUTH_HEADER) String authToken,
                                               @Path(Client.GENERIC_ID) String companyId,
                                               @Path(Client.GENERIC_ID2) String ticketId);
    @GET(Client.ENDPOINT_CLOSE_TICKET)
    Call<ResponseTicketDetail> closeTicket(@Header(Client.AUTH_HEADER) String authToken,
                                           @Path(Client.GENERIC_ID) String companyId,
                                           @Path(Client.GENERIC_ID2) String ticketId);
    //----------------------------------------------------------------------------------------------
}