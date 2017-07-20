package th.co.thiensurat.tsr_history.api.event;

import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;

/**
 * Created by teerayut.k on 7/19/2017.
 */

public class AddEvent {

    private AddHistoryItem item;

    public AddEvent( AddHistoryItem item ){
        this.item = item;
    }

    public AddHistoryItem getItem(){
        return item;
    }
}
