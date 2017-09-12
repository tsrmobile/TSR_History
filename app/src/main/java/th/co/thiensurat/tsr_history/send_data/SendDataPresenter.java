package th.co.thiensurat.tsr_history.send_data;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.ConvertItem;
import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.request.SendDataBody;
import th.co.thiensurat.tsr_history.api.result.DataItemResultGroup;
import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;
import th.co.thiensurat.tsr_history.send_data.item.DataItem;
import th.co.thiensurat.tsr_history.send_data.item.DataItemGroup;
import th.co.thiensurat.tsr_history.utils.Config;

/**
 * Created by teerayut.k on 8/17/2017.
 */

public class SendDataPresenter extends BaseMvpPresenter<SendDataInterface.View> implements SendDataInterface.Presenter {

    private ServiceManager serviceManager;
    private static DataItemGroup dataItemGroup;
    private static List<DataItem> dataItemList = new ArrayList<DataItem>();
    private static List<DataItem> dataItemListDistrict = new ArrayList<DataItem>();
    private static List<DataItem> dataItemListSubDistrict = new ArrayList<DataItem>();

    public static SendDataInterface.Presenter create() {
        return new SendDataPresenter();
    }

    public SendDataPresenter(){
        serviceManager = ServiceManager.getInstance();
    }

    public void setManager( ServiceManager manager ){
        serviceManager = manager;
    }

    @Override
    public void onViewCreate() {
        RxBus.get().register( this );
    }

    @Override
    public void onViewDestroy() {
        RxBus.get().unregister( this );
    }

    @Override
    public void requestData(String key, String code) {
        getView().onLoad();
        serviceManager.requestData(key, code, new ServiceManager.ServiceManagerCallback<DataItemResultGroup>() {
            @Override
            public void onSuccess(DataItemResultGroup result) {
                if (result.getMessage().equals(Config.FAILED)) {
                    dataItemList.clear();
                } else {
                    dataItemList.clear();
                    DataItemGroup dataGroup = ConvertItem.createListDataItemGroupFromResult( result );
                    dataItemGroup = dataGroup;
                    dataItemList = ConvertItem.createListDataItemGroupFromResult( result ).getData();
                    getView().setProvince(dataItemList);
                    getView().onDismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dataItemList.clear();
                getView().onDismiss();
            }
        });
    }

    @Override
    public void requestDistrict(String key, String code) {
        getView().onLoad();
        serviceManager.requestData(key, code, new ServiceManager.ServiceManagerCallback<DataItemResultGroup>() {
            @Override
            public void onSuccess(DataItemResultGroup result) {
                if (result.getMessage().equals(Config.FAILED)) {
                    dataItemListDistrict.clear();
                } else {
                    dataItemListDistrict.clear();
                    DataItemGroup dataGroup = ConvertItem.createListDataItemGroupFromResult( result );
                    dataItemGroup = dataGroup;
                    dataItemListDistrict = ConvertItem.createListDataItemGroupFromResult( result ).getData();
                    getView().setDistrict(dataItemListDistrict);
                    getView().onDismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dataItemListDistrict.clear();
                getView().onDismiss();
            }
        });
    }

    @Override
    public void requestSubDistrict(String key, String code) {
        getView().onLoad();
        serviceManager.requestData(key, code, new ServiceManager.ServiceManagerCallback<DataItemResultGroup>() {
            @Override
            public void onSuccess(DataItemResultGroup result) {
                if (result.getMessage().equals(Config.FAILED)) {
                    dataItemListSubDistrict.clear();
                } else {
                    dataItemListSubDistrict.clear();
                    DataItemGroup dataGroup = ConvertItem.createListDataItemGroupFromResult( result );
                    dataItemGroup = dataGroup;
                    dataItemListSubDistrict = ConvertItem.createListDataItemGroupFromResult( result ).getData();
                    getView().setSubDistrict(dataItemListSubDistrict);
                    getView().onDismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dataItemListSubDistrict.clear();
                getView().onDismiss();
            }
        });
    }

    @Override
    public void setDataItemGroup(DataItemGroup dataGroup) {
        this.dataItemGroup = dataGroup;
    }

    @Override
    public DataItemGroup getDataItemGroup() {
        return dataItemGroup;
    }

    @Override
    public void onRestoreToAdapter(DataItemGroup dataGroup) {
        this.dataItemList.clear();
        this.dataItemListDistrict.clear();
        this.dataItemListSubDistrict.clear();
        if (dataItemGroup.getMessage().equals(Config.KEY_PROVINCE)) {
            this.dataItemList = dataItemGroup.getData();
            getView().setProvince(dataItemList);
        } else if (dataItemGroup.getMessage().equals(Config.KEY_DISTRICT)) {
            this.dataItemListDistrict = dataItemGroup.getData();
            getView().setDistrict(dataItemListDistrict);
        } else if (dataItemGroup.getMessage().equals(Config.KEY_SUB_DISTRICT)) {
            this.dataItemListSubDistrict = dataItemGroup.getData();
            getView().setSubDistrict(dataItemListSubDistrict);
        }
    }

    @Override
    public void sendDataToServer(final List<SendDataBody.dataBody> dataBodyList) {
        getView().onLoad();
        serviceManager.SendDataRequest(dataBodyList, new ServiceManager.ServiceManagerCallback<DataItemResultGroup>() {
            @Override
            public void onSuccess(DataItemResultGroup result) {
                if (result.getStatus().equals(Config.SUCCESS)) {
                    getView().onDismiss();
                    getView().onSuccess();
                } else if (result.getStatus().equals(Config.FAILED)) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onFail(t.getMessage());
                getView().onDismiss();
            }
        });
    }
}
