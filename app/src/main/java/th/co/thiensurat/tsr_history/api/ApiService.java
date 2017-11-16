package th.co.thiensurat.tsr_history.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.request.FullAuthenBody;
import th.co.thiensurat.tsr_history.api.request.LogBody;
import th.co.thiensurat.tsr_history.api.request.SendDataBody;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.DataItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.TsrAuthenResult;
import th.co.thiensurat.tsr_history.utils.Config;

import static th.co.thiensurat.tsr_history.api.ApiURL.URL_AUTHEN_TSR;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_AUTH_V2;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_DATA;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_HISTORY;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_HISTORY_BY_NAME;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_SAVE;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_SAVE_INFO;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_SAVE_LOG;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public interface ApiService {

    @POST ( URL_AUTH_V2 )
    Call<AuthenItemResultGroup> getFullAuthen(@Body FullAuthenBody body);

    /*@GET( URL_AUTH_V2 )
    Call<AuthenItemResultGroup> getAuthen(@Query( "device" ) String value);*/

    @GET( URL_HISTORY )
    Call<ListItemResultGroup> getHistory(@Query( "search" ) String value );

    @GET( URL_HISTORY_BY_NAME )
    Call<ListItemResultGroup> getHistoryByName(@Query( "search" ) String value );
    
    @POST( URL_SAVE )
    Call<AddHistoryResult> addHistory(@Body AddHistoryBody body);

    @FormUrlEncoded
    @POST( URL_AUTHEN_TSR )
    Call<TsrAuthenResult> tsrAuthen(@Field("username") String username, @Field("password") String password);

    @GET ( URL_DATA )
    Call<DataItemResultGroup> getData(@Query( "data" ) String key, @Query( "code" ) String code);

    @POST ( URL_SAVE_INFO )
    Call<DataItemResultGroup> sendData(@Body SendDataBody body);

    @POST ( URL_SAVE_LOG )
    Call<DataItemResultGroup> saveLog(@Body LogBody body);

}
