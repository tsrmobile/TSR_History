package th.co.thiensurat.tsr_history.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.request.FullAuthenBody;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.utils.Config;

import static th.co.thiensurat.tsr_history.api.ApiURL.BASE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ServiceManager {

    private Call<AddHistoryResult> historyResultCall;
    private Call<AuthenItemResultGroup> requestFullAuthenCall;
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

    public Call<AuthenItemResultGroup> requestFullAuthen(FullAuthenBody body ) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getFullAuthen( body );
    }

    public void requestFullAuthentication (List<FullAuthenItem> items, final ServiceManagerCallback<AuthenItemResultGroup> callback) {
        List<FullAuthenBody.authenBody> bodyList = new ArrayList<>();
        for (FullAuthenItem item : items) {
            bodyList.add( new FullAuthenBody.authenBody()
                    .setUsername( item.getUsername() )
                    .setPassword( item.getPassword() )
            );
        }
        FullAuthenBody body = new FullAuthenBody();
        body.setBody(bodyList);

        requestFullAuthenCall = requestFullAuthen( body );
        requestFullAuthenCall.enqueue(new Callback<AuthenItemResultGroup>() {
            @Override
            public void onResponse(Call<AuthenItemResultGroup> call, Response<AuthenItemResultGroup> response) {
                Log.e("requestFullAuthen", response + "");
                if( callback != null ){
                    if( fullAuthenChecker( response ) ){
                        callback.onSuccess( response.body() );
                        //Log.e("Response full authen", response.body().getStatus());
                    }else{
                        callback.onFailure( new Throwable( "response full authen invalid." ) );
                    }
                }
                requestFullAuthenCall = null;
            }

            @Override
            public void onFailure(Call<AuthenItemResultGroup> call, Throwable t) {
                Log.e("requestFullAuthen", t.getMessage() + "" + call);
                if( callback != null ){
                    callback.onFailure( t );
                }
                requestFullAuthenCall = null;
            }
        });
    }

    public void requestAuthentication( String value, final ServiceManagerCallback<AuthenItemResultGroup> callback) {
        requestAuthen( value ).enqueue(new Callback<AuthenItemResultGroup>() {
            @Override
            public void onResponse(Call<AuthenItemResultGroup> call, Response<AuthenItemResultGroup> response) {
                Log.e("requestAuthen", response + "");
                if( callback != null ){
                    if( authenChecker( response ) ){
                        //Log.e("Authentication", response.body().getMessage() + "");
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

    public void AddHistoryRequest(List<AddHistoryBody.HistoryBody> items, final ServiceManagerCallback<AddHistoryResult> callback) {
        AddHistoryBody body = new AddHistoryBody();
        body.setHistoryItems(items);

        historyResultCall = requestAddHistory( body );
        historyResultCall.enqueue( new Callback<AddHistoryResult>(){
            @Override
            public void onResponse( Call<AddHistoryResult> call, Response<AddHistoryResult> response ){
                Log.e("AddHistoryRequest", response + "");
                if( callback != null ){
                    //Log.e("Add history", response.body().getData().get(0).getCustomerID());
                    Log.e("Add history get MSG", response.body().getMessage());
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
                //Log.e("AddHistoryRequest fail", t.getMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
                historyResultCall = null;
            }
        } );
    }

    private boolean fullAuthenChecker (Response<AuthenItemResultGroup> response) {
        if (response.isSuccessful()) {
            AuthenItemResultGroup resultGroup = response.body();
            return Config.SUCCESS.equals( resultGroup.getStatus() ) && resultGroup.getData() != null;
        }
        return false;
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
