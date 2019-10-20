package org.raapp.messenger.client;

import org.raapp.messenger.client.datamodel.Request.RequestSend;
import org.raapp.messenger.client.datamodel.Responses.ResponseGetCompany;
import org.raapp.messenger.client.datamodel.Responses.ResponseSendMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IClient {

    @GET(Client.ENDPOINT_GET_COMPANY)
    Call<ResponseGetCompany> getCompanyByID(@Header(Client.AUTH_HEADER) String authToken,
                                            @Path(Client.GENERIC_ID) String companyID);

    @GET(Client.ENDPOINT_ACCEPT_INVITATION)
    Call<ResponseBody> acceptInvitation(@Header(Client.AUTH_HEADER) String authToken,
                                        @Path(Client.GENERIC_ID) String companyID,
                                        @Path(Client.GENERIC_ID_2) String invitationCode);

    @POST(Client.ENDPOINT_SEND_MESSAGE)
    Call<ResponseSendMessage> sendMessage(@Header(Client.AUTH_HEADER) String authToken,
                                          @Body RequestSend requestSend);
}
