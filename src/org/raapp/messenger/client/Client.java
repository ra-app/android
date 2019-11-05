package org.raapp.messenger.client;

import android.content.Context;

import org.raapp.messenger.client.datamodel.Request.RequestSend;
import org.raapp.messenger.client.datamodel.Responses.ResponseBase;
import org.raapp.messenger.client.datamodel.Responses.ResponseGetCompany;
import org.raapp.messenger.client.datamodel.Responses.ResponseInvitationCode;
import org.raapp.messenger.client.datamodel.Responses.ResponseSendMessage;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Client {

    public static final String BASE_URL = "https://luydm9sd26.execute-api.eu-central-1.amazonaws.com/";

    public static final String ENDPOINT_BASE_VERSION_1 = "latest/api/v1/";
    public static final String ENDPOINT_BASE_VERSION_2 = "latest/api/v2/";

    public static final String ENDPOINT_COMPANIES = "companies/";
    public static final String ENDPOINT_INBOX = "inbox/";
    public static final String ENDPOINT_BLACKBOARD = "client/blackboard/list/";

    //PARAMS && HEADERS
    public static final String GENERIC_ID = "ID";
    public static final String AUTH_HEADER = "Authorization";

    //FINAL ENDPOINTS
    public static final String ENDPOINT_GET_COMPANY = ENDPOINT_BASE_VERSION_1 + ENDPOINT_COMPANIES + "{" + GENERIC_ID + "}";
    public static final String ENDPOINT_ACCEPT_INVITATION = ENDPOINT_BASE_VERSION_1 + ENDPOINT_COMPANIES + "code/{" + GENERIC_ID + "}";
    public static final String ENDPOINT_SEND_MESSAGE = ENDPOINT_BASE_VERSION_2 + ENDPOINT_INBOX;
    public static final String ENDPOINT_GET_BLACKBOARD = ENDPOINT_BASE_VERSION_2 + ENDPOINT_BLACKBOARD + "{" + GENERIC_ID + "}";

    public static String token;
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
    public static void sendMessage(Callback<ResponseSendMessage> callback, RequestSend requestSend) {
        Call<ResponseSendMessage> sendMessageCall = iClient.sendMessage("Basic " + token, requestSend);
        sendMessageCall.enqueue(callback);
    }
    public static void getBlackboard(Callback<ResponseBase> callback, String clientId) {
        Call<ResponseBase> getBlackboardCall = iClient.getBlackboard("Basic " + token, clientId);
        getBlackboardCall.enqueue(callback);
    }
}