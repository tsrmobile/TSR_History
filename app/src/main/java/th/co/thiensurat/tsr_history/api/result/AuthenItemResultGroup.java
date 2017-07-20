package th.co.thiensurat.tsr_history.api.result;

import java.util.List;

/**
 * Created by teerayut.k on 7/20/2017.
 */

public class AuthenItemResultGroup {

    private String status;
    private String message;
    private List<AuthenItemResult> data;

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

    public List<AuthenItemResult> getData() {
        return data;
    }

    public void setData(List<AuthenItemResult> data) {
        this.data = data;
    }
}
