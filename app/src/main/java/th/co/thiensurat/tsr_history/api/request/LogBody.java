package th.co.thiensurat.tsr_history.api.request;

import java.util.List;

import retrofit2.http.Body;

/**
 * Created by teerayut.k on 8/22/2017.
 */

public class LogBody {

    private List<logBody> body;

    public List<LogBody.logBody> getLogBody() {
        return body;
    }

    public LogBody setLogBody(List<LogBody.logBody> logBody) {
        this.body = logBody;
        return this;
    }

    public static class logBody{
        private String username;
        private String searchBy;
        private String statusValue;
        private String contNo;
        private String event;

        public String getUsername() {
            return username;
        }

        public logBody setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getSearchBy() {
            return searchBy;
        }

        public logBody setSearchBy(String searchBy) {
            this.searchBy = searchBy;
            return this;
        }

        public String getStatusValue() {
            return statusValue;
        }

        public logBody setStatusValue(String statusValue) {
            this.statusValue = statusValue;
            return this;
        }

        public String getContNo() {
            return contNo;
        }

        public logBody setContNo(String contNo) {
            this.contNo = contNo;
            return this;
        }

        public String getEvent() {
            return event;
        }

        public logBody setEvent(String event) {
            this.event = event;
            return this;
        }
    }
}
