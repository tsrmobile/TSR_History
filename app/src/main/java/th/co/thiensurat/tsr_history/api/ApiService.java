package th.co.thiensurat.tsr_history.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;

import static th.co.thiensurat.tsr_history.api.ApiURL.URL_HISTORY;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public interface ApiService {

    @GET( URL_HISTORY )
    Call<ListItemResultGroup> getHistory(@Query( "search" ) String value );
}
