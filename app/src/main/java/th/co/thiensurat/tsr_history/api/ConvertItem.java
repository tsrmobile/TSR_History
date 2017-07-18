package th.co.thiensurat.tsr_history.api;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.result.ListItemResult;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ConvertItem {

    public static ListItemGroup createListItemGroupFromResult(ListItemResultGroup result ){
        ListItemGroup group = new ListItemGroup();
        group.setStatus( result.getStatus() );
        group.setMessage( result.getMessage() );
        group.setData( ConvertItem.createListItemsFromResult( result.getData() ) );
        return group;
    }

    public static List<ListItem> createListItemsFromResult(List<ListItemResult> result){
        List<ListItem> items = new ArrayList<>();
        for( ListItemResult listItemResult : result ){
            ListItem item = new ListItem()
                    .setRef(listItemResult.getRef())
                    .setCountno(listItemResult.getCountno())
                    .setIdcard(listItemResult.getIdcard())
                    .setName(listItemResult.getName())
                    .setLastpaystatus(listItemResult.getLastpaystatus())
                    .setCustomerStatus(listItemResult.getCustomerStatus())
                    .setAccountStatus(listItemResult.getAccountStatus())
                    .setPaytype(listItemResult.getPaytype())
                    .setPeriods(listItemResult.getPeriods())
                    .setPaylastmonth(listItemResult.getPaylastmonth())
                    .setTotalPrice(listItemResult.getTotalPrice())
                    .setProductName(listItemResult.getProductName())
                    .setProductModel(listItemResult.getProductModel())
                    .setSaleCode(listItemResult.getSaleCode())
                    .setDate(listItemResult.getDate());
            items.add( item );
        }
        return items;
    }
}
