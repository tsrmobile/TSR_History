package th.co.thiensurat.tsr_history.api.result;

/**
 * Created by teerayut.k on 8/7/2017.
 */

public class TsrAuthenResult {

    private String displayname;
    private String userprincipalname;
    private String ad_name;

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getUserprincipalname() {
        return userprincipalname;
    }

    public void setUserprincipalname(String userprincipalname) {
        this.userprincipalname = userprincipalname;
    }
}
