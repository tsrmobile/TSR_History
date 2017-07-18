package th.co.thiensurat.tsr_history.api.result;

import android.content.ClipData;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ListItemResult {

    private String ref;
    private String countno;
    private String idcard;
    private String name;
    private String lastpaystatus;
    private String customerStatus;
    private String accountStatus;
    private String paytype;
    private int periods;
    private int paylastmonth;
    private int totalPrice;
    private String productName;
    private String productModel;
    private String saleCode;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ListItemResult() {

    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCountno() {
        return countno;
    }

    public void setCountno(String countno) {
        this.countno = countno;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastpaystatus() {
        return lastpaystatus;
    }

    public void setLastpaystatus(String lastpaystatus) {
        this.lastpaystatus = lastpaystatus;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public int getPaylastmonth() {
        return paylastmonth;
    }

    public void setPaylastmonth(int paylastmonth) {
        this.paylastmonth = paylastmonth;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }
}
