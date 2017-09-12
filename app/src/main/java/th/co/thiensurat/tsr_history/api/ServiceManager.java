package th.co.thiensurat.tsr_history.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.request.FullAuthenBody;
import th.co.thiensurat.tsr_history.api.request.LogBody;
import th.co.thiensurat.tsr_history.api.request.SendDataBody;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.DataItemResult;
import th.co.thiensurat.tsr_history.api.result.DataItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.TsrAuthenResult;
import th.co.thiensurat.tsr_history.utils.Config;

import static th.co.thiensurat.tsr_history.api.ApiURL.BASE_URL;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ServiceManager {

    private Call<DataItemResultGroup> dataItemResultGroupCall;
    private Call<AddHistoryResult> historyResultCall;
    private Call<TsrAuthenResult> tsrAuthenResultCall;
    private Call<AuthenItemResultGroup> requestFullAuthenCall;
    private Call<DataItemResultGroup> saveLogGroupCall;
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

    public Call<ListItemResultGroup> requestHistoryCallByName(String value ){
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getHistoryByName( value );
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

    public Call<AuthenItemResultGroup> requestFullAuthen( FullAuthenBody body ) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getFullAuthen( body );
    }

    public Call<TsrAuthenResult> requestTSR(String username, String password) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .tsrAuthen(username, password);
    }

    public Call<DataItemResultGroup> requestData(String key, String code) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .getData(key, code);
    }

    public Call<DataItemResultGroup> requestSendData(SendDataBody body) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .sendData( body );
    }

    public Call<DataItemResultGroup> requestSaveLog(LogBody body) {
        return Service.newInstance( BASE_URL )
                .getApi( api )
                .saveLog( body );
    }

    public void request(String username, String password, final ServiceManagerCallback<TsrAuthenResult> callback) {
        tsrAuthenResultCall = requestTSR(username, password);
        tsrAuthenResultCall.enqueue(new Callback<TsrAuthenResult>() {
            @Override
            public void onResponse(Call<TsrAuthenResult> call, Response<TsrAuthenResult> response) {
                Log.e("request TSR success", response + "");
                if (response.code() == 200) {
                    Log.e("request result", response.body().getDisplayname());
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure( new Throwable( "response tsr authen invalid." ) );
                }
                tsrAuthenResultCall = null;
            }

            @Override
            public void onFailure(Call<TsrAuthenResult> call, Throwable t) {
                //Log.e("request TSR failure", t.getMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
                requestFullAuthenCall = null;
            }
        });
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
                        Log.e("Authentication", response.body().getMessage() + "");
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
                        //Log.e("request history years", response.body().getMessage());
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

    public void requestHistoryByName(String value, final ServiceManagerCallback<ListItemResultGroup> callback) {
        requestHistoryCallByName( value ).enqueue(new Callback<ListItemResultGroup>() {
            @Override
            public void onResponse(Call<ListItemResultGroup> call, Response<ListItemResultGroup> response) {

            }

            @Override
            public void onFailure(Call<ListItemResultGroup> call, Throwable t) {

            }
        });
    }

    public void requestData(String key, String code, final ServiceManagerCallback<DataItemResultGroup> callback) {
        requestData(key, code).enqueue(new Callback<DataItemResultGroup>() {
            @Override
            public void onResponse(Call<DataItemResultGroup> call, Response<DataItemResultGroup> response) {
                Log.e("requestData", response + "");
                if( callback != null ){
                    if( dataChecker( response ) ){
                        callback.onSuccess( response.body() );
                    }else{
                        callback.onFailure( new Throwable( "Response data invalid." ) );
                    }
                }
            }

            @Override
            public void onFailure(Call<DataItemResultGroup> call, Throwable t) {
                if( !call.isCanceled() ){
                    if( callback != null ){
                        callback.onFailure( t );
                    }
                }
            }
        });
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
                    Log.e("Add history get msg", response.body().getMessage());
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
                Log.e("AddHistoryRequest fail", t.getMessage());
                if( callback != null ){
                    callback.onFailure( t );
                }
                historyResultCall = null;
            }
        } );
    }

    public void SendDataRequest(List<SendDataBody.dataBody> body, final ServiceManagerCallback<DataItemResultGroup> callback) {
        SendDataBody sendDataBody = new SendDataBody();
        sendDataBody.setDataBodyList(body);

        dataItemResultGroupCall = requestSendData( sendDataBody );
        dataItemResultGroupCall.enqueue(new Callback<DataItemResultGroup>() {
            @Override
            public void onResponse(Call<DataItemResultGroup> call, Response<DataItemResultGroup> response) {
                Log.e("SendDataRequest", response + "");
                if( callback != null ){
                    Log.e("Send data get MSG", response.body().getMessage());
                    if( sendDataChecker( response ) ){
                        callback.onSuccess( response.body() );
                        Log.e("Response", response.body().getStatus());
                    }else{
                        callback.onFailure( new Throwable( "response send invalid." ) );
                    }
                }
                dataItemResultGroupCall = null;
            }

            @Override
            public void onFailure(Call<DataItemResultGroup> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
                dataItemResultGroupCall = null;
            }
        });
    }

    public void saveLogRequest(List<LogBody.logBody> body, final ServiceManagerCallback<DataItemResultGroup> callback) {
        LogBody log = new LogBody();
        log.setLogBody(body);

        saveLogGroupCall = requestSaveLog( log );
        saveLogGroupCall.enqueue(new Callback<DataItemResultGroup>() {
            @Override
            public void onResponse(Call<DataItemResultGroup> call, Response<DataItemResultGroup> response) {
                Log.e("SaveLogRequest", response + "");
                if( callback != null ){
                    Log.e("Save Log get MSG", response.body().getMessage());
                    if( saveLogChecker( response ) ){
                        callback.onSuccess( response.body() );
                        Log.e("Response", response.body().getStatus());
                    }else{
                        callback.onFailure( new Throwable( "response log invalid." ) );
                    }
                }
                saveLogGroupCall = null;
            }

            @Override
            public void onFailure(Call<DataItemResultGroup> call, Throwable t) {
                if( callback != null ){
                    callback.onFailure( t );
                }
                saveLogGroupCall = null;
            }
        });
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

    private boolean dataChecker(Response<DataItemResultGroup> response) {
        if (response.isSuccessful()) {
            DataItemResultGroup resultGroup = response.body();
            return Config.SUCCESS.equals( resultGroup.getStatus() ) && resultGroup.getData() != null;
        }
        return false;
    }

    private boolean sendDataChecker( Response<DataItemResultGroup> response) {
        if (response.isSuccessful()) {
            DataItemResultGroup resultGroup = response.body();
            return  Config.SUCCESS.equals( resultGroup.getStatus());
        }
        return false;
    }

    private boolean saveLogChecker( Response<DataItemResultGroup> response) {
        if (response.isSuccessful()) {
            DataItemResultGroup resultGroup = response.body();
            return  Config.SUCCESS.equals( resultGroup.getStatus());
        }
        return false;
    }
}
