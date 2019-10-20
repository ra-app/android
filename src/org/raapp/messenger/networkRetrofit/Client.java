package org.raapp.messenger.networkRetrofit;

import org.raapp.messenger.networkRetrofit.datamodel.Request.RequestSend;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    public static final String BASE_URL = "https://luydm9sd26.execute-api.eu-central-1.amazonaws.com/";

    public static final String ENDPOINT_BASE_VERSION_1 = "latest/api/v1/";
    public static final String ENDPOINT_BASE_VERSION_2 = "latest/api/v2/";
    public static final String ENDPOINT_COMPANIES = "companies/";
    public static final String ENDPOINT_INBOX = "inbox/";

    //PARAMS && HEADERS
    public static final String GENERIC_ID = "ID";
    public static final String GENERIC_ID_2 = "ID2";
    public static final String AUTH_HEADER = "Authorization";

    //FINAL ENDPOINTS
    public static final String ENDPOINT_GET_COMPANY = ENDPOINT_BASE_VERSION_1 + ENDPOINT_COMPANIES + "{" + GENERIC_ID + "}";
    public static final String ENDPOINT_ACCEPT_INVITATION = ENDPOINT_BASE_VERSION_1 + ENDPOINT_COMPANIES + "code/{" + GENERIC_ID + "}/{" + GENERIC_ID_2 + "}";
    public static final String ENDPOINT_SEND_MESSAGE = ENDPOINT_BASE_VERSION_2 + ENDPOINT_INBOX;

    public static final String HARDTOKEN = "KzM0NjYwMjc5MTQ0LjE6UTdHL2FkWFZna0NkZ1ZtcThEcERpUQ==";
    /*public static void cancelAll() {
        if (retrofit != null) {
            OkHttpClient okHttpClient = (OkHttpClient) retrofit.callFactory();
            okHttpClient.dispatcher().cancelAll();
        }
    }*/

    //STATIC
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();
    private static IClient iClient = retrofit.create(IClient.class);


    //CALLS
    public static void acceptInvitation(Callback<ResponseBody> callback, String companyID, String invitationCode) {
        Call<ResponseBody> acceptInvitationCall = iClient.acceptInvitation(HARDTOKEN,companyID,invitationCode);
        acceptInvitationCall.enqueue(callback);
    }
    public static void getCompanyByID(Callback<ResponseBody> callback, String companyID) {
        Call<ResponseBody> getCompanyByIDCall = iClient.getCompanyByID(HARDTOKEN,companyID);
        getCompanyByIDCall.enqueue(callback);
    }
    public static void sendMessage(Callback<ResponseBody> callback, RequestSend requestSend) {
        Call<ResponseBody> sendMessageCall = iClient.sendMessage(HARDTOKEN,requestSend);
        sendMessageCall.enqueue(callback);
    }
}