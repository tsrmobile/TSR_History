package th.co.thiensurat.tsr_history.api.result;

/**
 * Created by teerayut.k on 7/20/2017.
 */

public class AuthenItemResult {

    private String username;
    private int loggedin;

    private AuthenItemResult() {

    }

    public int getLoggedin() {
        return loggedin;
    }

    public void setLoggedin(int loggedin) {
        this.loggedin = loggedin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
