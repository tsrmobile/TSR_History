package th.co.thiensurat.tsr_history.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResult;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.utils.Config;

import static th.co.thiensurat.tsr_history.api.ApiURL.BASE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ServiceManager {

    private Call<AddHistoryResult> historyResultCall;
    private Call<AuthenItemResult> requestAuthenCall;
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

    public Call<AddHistoryResult> requestAddHistory( AddHistoryBody body ){
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .addHistory( body );
    }

    public Call<AuthenItemResultGroup> requestAuthen( String value ) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getAuthen( value );
    }

    public void requestAuthentication( String value, final ServiceManagerCallback<AuthenItemResultGroup> callback) {
        requestAuthen( value ).enqueue(new Callback<AuthenItemResultGroup>() {
            @Override
            public void onResponse(Call<AuthenItemResultGroup> call, Response<AuthenItemResultGroup> response) {
                Log.e("requestAuthen", response + "");
                if( callback != null ){
                    if( authenChecker( response ) ){
                        //Log.e("Authentication", response.body().getStatus() + "");
                        callback.onSuccess( response.body() );
                    }else{
                        callback.onFailure( new Throwable( "Response authen invalid." ) );
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenItemResultGroup> call, Throwable t) {
                if( !call.isCanceled() ){
                    if( callback != null ){
                        callback.onFailure( t );
                    }
                }
            }
        });
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
                        callback.onFailure( new Throwable( "Response history invalid." ) );
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

    public void AddHistoryRequest(List<AddHistoryItem> items, final ServiceManagerCallback<AddHistoryResult> callback) {
        List<AddHistoryBody.HistoryBody> bodyList = new ArrayList<>();
        for (AddHistoryItem item : items) {
            bodyList.add( new AddHistoryBody.HistoryBody()
                    .setCustomerID(item.getCustomerID())
                    .setSaleCode(item.getSaleCode())
                    .setDateContract(item.getDateContract())
                    .setImage(item.getImage())
                    .setCreatedBy(item.getCreatedBy())
            );
        }

        AddHistoryBody body = new AddHistoryBody();
        body.setHistoryItems(bodyList);

        historyResultCall = requestAddHistory( body );
        historyResultCall.enqueue( new Callback<AddHistoryResult>(){
            @Override
            public void onResponse( Call<AddHistoryResult> call, Response<AddHistoryResult> response ){
                Log.e("AddHistoryRequest", response + "");
                if( callback != null ){
                    if( addHistoryResultChecker( response ) ){
                        callback.onSuccess( response.body() );
                        Log.e("Response", response.body().getStatus());
                    }else{
                        callback.onFailure( new Throwable( "response add invalid." ) );
                    }
                }
                historyResultCall = null;
            }

            @Override
            public void onFailure( Call<AddHistoryResult> call, Throwable t ){
                if( callback != null ){
                    callback.onFailure( t );
                }
                historyResultCall = null;
            }
        } );
    }

    private boolean authenChecker( Response<AuthenItemResultGroup> response) {
        if (response.isSuccessful()) {
            AuthenItemResultGroup resultGroup = response.body();
            return Config.SUCCESS.equals( resultGroup.getStatus() ) && resultGroup.getData() != null;
        }
        return false;
    }

    private boolean historyChecker( Response<ListItemResultGroup> response ){
        if( response.isSuccessful() ){
            ListItemResultGroup result = response.body();
            return Config.SUCCESS.equals( result.getStatus() ) && result.getData() != null;
        }
        return false;
    }

    private boolean addHistoryResultChecker( Response<AddHistoryResult> response ){
        if( response.isSuccessful() ){
            AddHistoryResult result = response.body();
            return Config.SUCCESS.equals( result.getStatus() );
        }
        return false;
    }
}
