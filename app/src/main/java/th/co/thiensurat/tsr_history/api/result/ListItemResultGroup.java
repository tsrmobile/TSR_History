package th.co.thiensurat.tsr_history.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ListItemResultGroup {

    private String status;
    private String message;
    private List<ListItemResult> data;

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

    public List<ListItemResult> getData() {
        return data;
    }

    public void setData(List<ListItemResult> data) {
        this.data = data;
    }
}
