package th.co.thiensurat.tsr_history.api.result;

/**
 * Created by teerayut.k on 8/17/2017.
 */

public class DataItemResult {

    private String dataId;
    private String dataCode;
    private String dataName;

    public DataItemResult() {

    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
