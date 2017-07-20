package th.co.thiensurat.tsr_history.api.request;

import java.util.List;

import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;

/**
 * Created by teerayut.k on 7/19/2017.
 */

public class AddHistoryBody {
    private List<HistoryBody> body;

    public List<HistoryBody> getHistoryItems() {
        return body;
    }

    public AddHistoryBody setHistoryItems(List<HistoryBody> historyItems) {
        this.body = historyItems;
        return this;
    }

    public static class HistoryBody{

        private String customerID;
        private String saleCode;
        private String dateContract;
        private String image;
        private String createdBy;

        public String getDateContract() {
            return dateContract;
        }

        public HistoryBody setDateContract(String dateContract) {
            this.dateContract = dateContract;
            return this;
        }

        public String getImage() {
            return image;
        }

        public HistoryBody setImage(String image) {
            this.image = image;
            return this;
        }

        public String getSaleCode() {
            return saleCode;
        }

        public HistoryBody setSaleCode(String saleCode) {
            this.saleCode = saleCode;
            return this;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public HistoryBody setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public String getCustomerID() {
            return customerID;
        }

        public HistoryBody setCustomerID(String customerID) {
            this.customerID = customerID;
            return this;
        }
    }
}
