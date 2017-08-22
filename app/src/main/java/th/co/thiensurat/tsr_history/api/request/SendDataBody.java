package th.co.thiensurat.tsr_history.api.request;

import java.util.List;

/**
 * Created by teerayut.k on 8/21/2017.
 */

public class SendDataBody {

    private List<dataBody> body;

    public List<dataBody> getDataBodyList() {
        return body;
    }

    public SendDataBody setDataBodyList(List<dataBody> dataBodyList) {
        this.body = dataBodyList;
        return this;
    }

    public static class dataBody {

        private String contno;
        private String name;
        private String number;
        private String moo;
        private String soi;
        private String road;
        private String village;
        private String building;
        private String province;
        private String district;
        private String subdistrict;
        private String zipcode;
        private String homephone;
        private String workphone;
        private String mobile;
        private String email;
        private String created;
        private String location;
        private String product;

        public String getCountno() {
            return contno;
        }

        public dataBody setCountno(String contno) {
            this.contno = contno;
            return this;
        }

        public String getName() {
            return name;
        }

        public dataBody setName(String name) {
            this.name = name;
            return this;
        }

        public String getNumber() {
            return number;
        }

        public dataBody setNumber(String number) {
            this.number = number;
            return this;
        }

        public String getMoo() {
            return moo;
        }

        public dataBody setMoo(String moo) {
            this.moo = moo;
            return this;
        }

        public String getSoi() {
            return soi;
        }

        public dataBody setSoi(String soi) {
            this.soi = soi;
            return this;
        }

        public String getRoad() {
            return road;
        }

        public dataBody setRoad(String road) {
            this.road = road;
            return this;
        }

        public String getVillage() {
            return village;
        }

        public dataBody setVillage(String village) {
            this.village = village;
            return this;
        }

        public String getBuilding() {
            return building;
        }

        public dataBody setBuilding(String building) {
            this.building = building;
            return this;
        }

        public String getProvince() {
            return province;
        }

        public dataBody setProvince(String province) {
            this.province = province;
            return this;
        }

        public String getDistrict() {
            return district;
        }

        public dataBody setDistrict(String district) {
            this.district = district;
            return this;
        }

        public String getSubdistrict() {
            return subdistrict;
        }

        public dataBody setSubdistrict(String subdistrict) {
            this.subdistrict = subdistrict;
            return this;
        }

        public String getZipcode() {
            return zipcode;
        }

        public dataBody setZipcode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public String getHomephone() {
            return homephone;
        }

        public dataBody setHomephone(String homephone) {
            this.homephone = homephone;
            return this;
        }

        public String getWorkphone() {
            return workphone;
        }

        public dataBody setWorkphone(String workphone) {
            this.workphone = workphone;
            return this;
        }

        public String getMobile() {
            return mobile;
        }

        public dataBody setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public dataBody setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getCreated() {
            return created;
        }

        public dataBody setCreated(String created) {
            this.created = created;
            return this;
        }

        public String getLocation() {
            return location;
        }

        public dataBody setLocation(String location) {
            this.location = location;
            return this;
        }

        public String getProduct() {
            return product;
        }

        public dataBody setProduct(String product) {
            this.product = product;
            return this;
        }
    }
}
