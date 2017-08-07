package th.co.thiensurat.tsr_history.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.request.FullAuthenBody;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.TsrAuthenResult;

import static th.co.thiensurat.tsr_history.api.ApiURL.URL_AUTH;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_AUTHEN_TSR;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_FULL_AUTH;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_HISTORY;
import static th.co.thiensurat.tsr_history.api.ApiURL.URL_SAVE;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public interface ApiService {

    @POST ( URL_FULL_AUTH )
    Call<AuthenItemResultGroup> getFullAuthen(@Body FullAuthenBody body);

    @GET( URL_AUTH )
    Call<AuthenItemResultGroup> getAuthen(@Query( "device" ) String value);

    @GET( URL_HISTORY )
    Call<ListItemResultGroup> getHistory(@Query( "search" ) String value );

    @POST( URL_SAVE )
    Call<AddHistoryResult> addHistory(@Body AddHistoryBody body);

    @FormUrlEncoded
    @POST( URL_AUTHEN_TSR )
    Call<TsrAuthenResult> tsrAuthen(@Field("username") String username, @Field("password") String password);
}
