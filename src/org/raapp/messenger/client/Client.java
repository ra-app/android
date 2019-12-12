package org.raapp.messenger.client;

import android.content.Context;

import org.raapp.messenger.client.datamodel.Note;
import org.raapp.messenger.client.datamodel.Request.RequestInvitationCreate;
import org.raapp.messenger.client.datamodel.Request.RequestSend;
import org.raapp.messenger.client.datamodel.Request.RequestInvitationSend;
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
import org.raapp.messenger.client.datamodel.Responses.ResponseTicketListDetail;
import org.raapp.messenger.client.datamodel.Responses.ResponseTickets;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Client {

    //BASE STRINGS
    public static final String BASE_URL = "https://luydm9sd26.execute-api.eu-central-1.amazonaws.com/";
        //BASE ENVIROMENTS
        public static final String ENDPOINT_BASE_VERSION_1 = "latest/api/v1/";
        public static final String ENDPOINT_BASE_VERSION_2 = "latest/api/v2/";
        public static final String ENDPOINT_BASE_PUBLIC = "latest/public/";
        //BASE USERS
        public static final String ENDPOINT_BASE_CLIENT = "client/";
        public static final String ENDPOINT_BASE_ADMIN = "admin/";

    //ENDPOINTS
    public static final String ENDPOINT_COMPANIES = "companies/";
    public static final String ENDPOINT_CLIENT = "client/";
    public static final String ENDPOINT_INBOX = "inbox/";
    public static final String ENDPOINT_CODE = "code/";
    public static final String ENDPOINT_BLACKBOARD = "blackboard/list/";
    public static final String ENDPOINT_BLACKBOARD_UPDATE = "/blackboard/update";
    public static final String ENDPOINT_IMAGE = "img/";
    public static final String ENDPOINT_INVITE_CREATE = "/invites/create";
    public static final String ENDPOINT_INVITE_SEND= "/invites/send";
        //TICKETS
        public static final String ENDPOINT_BASE_TICKETS = "/tickets/";
        public static final String ENDPOINT_TICKETS_GET = ENDPOINT_BASE_TICKETS + "get/";
        public static final String ENDPOINT_TICKETS_DETAILS = ENDPOINT_BASE_TICKETS +"details/";
        public static final String ENDPOINT_TICKET_CLAIM = ENDPOINT_BASE_TICKETS + "claim/";
        public static final String ENDPOINT_TICKET_CLOSE = ENDPOINT_BASE_TICKETS + "close/";

    //PARAMS && HEADERS
    public static final String GENERIC_ID = "ID";
    public static final String GENERIC_ID2 = "ID2";
    public static final String AUTH_HEADER = "Authorization";

    //FINAL ENDPOINTS
    public static final String ENDPOINT_GET_COMPANY = ENDPOINT_BASE_VERSION_1 + ENDPOINT_COMPANIES + "{" + GENERIC_ID + "}";
    public static final String ENDPOINT_GET_COMPANY_LIST = ENDPOINT_BASE_VERSION_1 + ENDPOINT_CLIENT + ENDPOINT_COMPANIES;
    public static final String ENDPOINT_ACCEPT_INVITATION = ENDPOINT_BASE_VERSION_1 + ENDPOINT_COMPANIES + ENDPOINT_CODE + "{" + GENERIC_ID + "}";
    public static final String ENDPOINT_SEND_MESSAGE = ENDPOINT_BASE_VERSION_2 + ENDPOINT_INBOX;
    public static final String ENDPOINT_GET_COMPANY_IMAGE = ENDPOINT_BASE_PUBLIC + ENDPOINT_IMAGE + "{" + GENERIC_ID + "}";
        //ADMIN
    public static final String ENDPOINT_CREATE_INVITATION = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_INVITE_CREATE;
    public static final String ENDPOINT_SEND_INVITE = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_INVITE_SEND;
        //BLACKBOARD
    public static final String ENDPOINT_GET_BLACKBOARD = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_CLIENT + ENDPOINT_BLACKBOARD + "{" + GENERIC_ID + "}";
    public static final String ENDPOINT_UPDATE_BLACKBOARD = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_BLACKBOARD_UPDATE ;
        //TICKETS
    public static final String ENDPOINT_GET_TICKET = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_TICKETS_GET ;
    public static final String ENDPOINT_GET_TICKET_DETAIL = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_TICKETS_DETAILS + "{" + GENERIC_ID2 + "}";
    public static final String ENDPOINT_CLAIM_TICKET  = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_TICKET_CLAIM + "{" + GENERIC_ID2 + "}";
    public static final String ENDPOINT_CLOSE_TICKET  = ENDPOINT_BASE_VERSION_1 + ENDPOINT_BASE_ADMIN + "{" + GENERIC_ID + "}" + ENDPOINT_TICKET_CLOSE + "{" + GENERIC_ID2 + "}";


    public static String token;

    //UTIL
    /*public static void cancelAll() {
        if (retrofit != null) {
            OkHttpClient okHttpClient = (OkHttpClient) retrofit.callFactory();
            okHttpClient.dispatcher().cancelAll();
        }
    }*/
    public static void buildBase(Context context) {
        token = context.getSharedPreferences("ra-preferences", MODE_PRIVATE).getString("office_app_token", "");
    }

    //STATIC
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();
    private static IClient iClient = retrofit.create(IClient.class);

    //CALLS
    public static void acceptInvitation(Callback<ResponseInvitationCode> callback, String invitationCode) {
        Call<ResponseInvitationCode> acceptInvitationCall = iClient.acceptInvitation("Basic " + token, invitationCode);
        acceptInvitationCall.enqueue(callback);
    }
    public static void getCompanyByID(Callback<ResponseGetCompany> callback, String companyID) {
        Call<ResponseGetCompany> getCompanyByIDCall = iClient.getCompanyByID("Basic " + token, companyID);
        getCompanyByIDCall.enqueue(callback);
    }
    public static void getCompanyList(Callback<ResponseCompanyList> callback) {
        Call<ResponseCompanyList> getCompanyByIDCall = iClient.getCompanyList("Basic " + token);
        getCompanyByIDCall.enqueue(callback);
    }
    public static void getCompanyImage(Callback<ResponseBody> callback, String companyID) {
        Call<ResponseBody> getCompanyImageCall = iClient.getCompanyImage("Basic " + token, companyID);
        getCompanyImageCall.enqueue(callback);
    }
    public static void sendMessage(Callback<ResponseSendMessage> callback, RequestSend requestSend) {
        Call<ResponseSendMessage> sendMessageCall = iClient.sendMessage("Basic " + token, requestSend);
        sendMessageCall.enqueue(callback);
    }

    //ADMIN
    public static void sendInvitation(Callback<ResponseBase> callback, RequestInvitationSend requestInvitationSend, String companyID) {
        Call<ResponseBase> sendInvitationCall = iClient.sendInvitation("Basic " + token, requestInvitationSend,companyID);
        sendInvitationCall.enqueue(callback);
    }
    public static void createInvitation(Callback<ResponseInvitation> callback, RequestInvitationCreate requestInvitationCreate, String companyID) {
        Call<ResponseInvitation> createInvitationCall = iClient.createInvitation("Basic " + token, requestInvitationCreate,companyID);
        createInvitationCall.enqueue(callback);
    }

    //BLACKBOARD
    public static void getBlackboard(Callback<ResponseBlackboardList> callback, String clientId) {
        Call<ResponseBlackboardList> getBlackboardCall = iClient.getBlackboard("Basic " + token, clientId);
        getBlackboardCall.enqueue(callback);
    }
    public static void updateBlackboardNote(Callback<ResponseNote> callback, Note note, String companyID) {
        Call<ResponseNote> updateBlackboardNoteCall = iClient.updateBlackboardNote("Basic " + token, note, companyID);
        updateBlackboardNoteCall.enqueue(callback);
    }

    // TICKETS
    public static void getTickets(Callback<ResponseTickets> callback, RequestTicket requestTicket, String companyID) {
        Call<ResponseTickets> getTicketsCall = iClient.getTickets("Basic " + token, requestTicket, companyID);
        getTicketsCall.enqueue(callback);
    }
    public static void getTicketDetail(Callback<ResponseTicketDetail> callback, String companyID, String ticketId ) {
        Call<ResponseTicketDetail> getTicketDetailCall = iClient.getTicketDetail("Basic " + token, companyID, ticketId);
        getTicketDetailCall.enqueue(callback);
    }
    public static void claimTicket(Callback<ResponseTicketListDetail> callback, String companyID, String ticketId ) {
        Call<ResponseTicketListDetail> claimTicketCall = iClient.claimTicket("Basic " + token, companyID, ticketId);
        claimTicketCall.enqueue(callback);
    }
    public static void closeTicket(Callback<ResponseTicketListDetail> callback, String companyID, String ticketId ) {
        Call<ResponseTicketListDetail> closeTicketCall = iClient.closeTicket("Basic " + token, companyID, ticketId);
        closeTicketCall.enqueue(callback);
    }
}