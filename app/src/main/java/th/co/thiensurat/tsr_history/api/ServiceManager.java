package th.co.thiensurat.tsr_history.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.utils.Config;

import static th.co.thiensurat.tsr_history.api.ApiURL.BASE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ServiceManager {

    private static ServiceManager instance;
    private static ApiService api;

    public interface ServiceManagerCallback<T>{
        void onSuccess( T result );

        void onFailure( Throwable t );
    }

    public static ServiceManager getInstance(){
        if( instance == null ){
            instance = new ServiceManager();
        }
        return instance;
    }

    private ServiceManager(){
    }

    public static void setApi( ApiService mockApi ){
        api = mockApi;
    }

    public Call<ListItemResultGroup> requestHistoryCall(String value ){
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getHistory( value );
    }

    public void requestHistory( String value, final ServiceManagerCallback<ListItemResultGroup> callback ){
        requestHistoryCall( value ).enqueue( new Callback<ListItemResultGroup>(){
            @Override
            public void onResponse(Call<ListItemResultGroup> call, Response<ListItemResultGroup> response ){
                Log.e("requestHistory", response + "");
                if( callback != null ){
                    if( historyChecker( response ) ){
                        callback.onSuccess( response.body() );
                    }else{
                        callback.onFailure( new Throwable( "Response invalid." ) );
                    }
                }
            }

            @Override
            public void onFailure(Call<ListItemResultGroup> call, Throwable t ){
                if( !call.isCanceled() ){
                    if( callback != null ){
                        callback.onFailure( t );
                    }
                }
            }
        } );
    }

    private boolean historyChecker( Response<ListItemResultGroup> response ){
        if( response.isSuccessful() ){
            ListItemResultGroup result = response.body();
            return Config.SUCCESS.equals( result.getStatus() ) && result.getData() != null;
        }
        return false;
    }
}
