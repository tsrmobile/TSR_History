package th.co.thiensurat.tsr_history.api;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.result.AuthenItemResult;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.DataItemResult;
import th.co.thiensurat.tsr_history.api.result.DataItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.ListItemResult;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItemGroup;
import th.co.thiensurat.tsr_history.send_data.item.DataItem;
import th.co.thiensurat.tsr_history.send_data.item.DataItemGroup;

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
                    .setDate(listItemResult.getDate())
                    .setAgingDetail(listItemResult.getAgingDetail())
                    .setStDate(listItemResult.getStDate());
            items.add( item );
        }
        return items;
    }

    public static AuthenItemGroup createListAuthenGroupFromResult(AuthenItemResultGroup result) {
        AuthenItemGroup itemGroup = new AuthenItemGroup();
        itemGroup.setStatus( result.getStatus() );
        itemGroup.setMessage( result.getMessage() );
        itemGroup.setData( ConvertItem.createListAuthenItemFromResult( result.getData() ));
        return itemGroup;
    }

    public static List<AuthenItem> createListAuthenItemFromResult(List<AuthenItemResult> results) {
        List<AuthenItem> items = new ArrayList<>();
        for (AuthenItemResult itemResult : results) {
            AuthenItem item = new AuthenItem()
                    .setLoggedin( itemResult.getLoggedin() )
                    .setUsername( itemResult.getUsername() );
            items.add(item);
        }
        return items;
    }

    public static List<ListItem> createListItemsFromItemGroup(ListItemGroup itemGroup) {
        List<ListItem> items = new ArrayList<>();
        items = itemGroup.getData();
        return items;
    }

    public static DataItemGroup createListDataItemGroupFromResult(DataItemResultGroup result ){
        DataItemGroup group = new DataItemGroup();
        group.setStatus( result.getStatus() );
        group.setMessage( result.getMessage() );
        group.setData( ConvertItem.createListDataItemsFromResult( result.getData() ) );
        return group;
    }

    public static List<DataItem> createListDataItemsFromResult(List<DataItemResult> result){
        List<DataItem> items = new ArrayList<>();
        DataItem itemHeader = new DataItem()
                .setDataId("")
                .setDataCode("header")
                .setDataName("");
        items.add( itemHeader );
        for( DataItemResult dataItemResult : result ){
            DataItem item = new DataItem()
                    .setDataId(dataItemResult.getDataId())
                    .setDataCode(dataItemResult.getDataCode())
                    .setDataName(dataItemResult.getDataName());
            items.add( item );
        }
        return items;
    }
}
