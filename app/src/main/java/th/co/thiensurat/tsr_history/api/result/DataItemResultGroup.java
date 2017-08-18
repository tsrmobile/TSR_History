package th.co.thiensurat.tsr_history.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 8/17/2017.
 */

public class DataItemResultGroup {

    private String status;
    private String message;
    private List<DataItemResult> data;

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

    public List<DataItemResult> getData() {
        return data;
    }

    public void setData(List<DataItemResult> data) {
        this.data = data;
    }
}
