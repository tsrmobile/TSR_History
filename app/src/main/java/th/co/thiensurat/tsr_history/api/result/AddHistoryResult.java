package th.co.thiensurat.tsr_history.api.result;

import java.util.List;

import th.co.thiensurat.tsr_history.result.item.ListItem;

/**
 * Created by teerayut.k on 7/18/2017.
 */

public class AddHistoryResult {

    private String status;
    private String message;
    private List<AddHistoryItem> data;

    public List<AddHistoryItem> getData() {
        return data;
    }

    public void setData(List<AddHistoryItem> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
