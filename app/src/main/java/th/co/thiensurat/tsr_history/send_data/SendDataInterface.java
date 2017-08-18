package th.co.thiensurat.tsr_history.send_data;

import java.util.List;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.send_data.item.DataItem;
import th.co.thiensurat.tsr_history.send_data.item.DataItemGroup;

/**
 * Created by teerayut.k on 8/17/2017.
 */

public class SendDataInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void setProvince(List<DataItem> dataItemList);
        void setDistrict(List<DataItem> dataItemList);
        void setSubDistrict(List<DataItem> dataItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<SendDataInterface.View> {
        void requestData(String key, String code);
        void setDataItemGroup(DataItemGroup dataGroup);
        DataItemGroup getDataItemGroup();
        void onRestoreToAdapter(DataItemGroup dataGroup);
    }
}
