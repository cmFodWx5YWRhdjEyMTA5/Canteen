package com.video.aashi.canteen.arrays;

public class ItemList {

    String groupname;
    String itemid;
    String itemprice;
    String itemname;
    int value = 0;

    public  ItemList(String itemid,String groupname,String itemname, String itemprice)
    {
        this.groupname = groupname;
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemprice = itemprice;

    }
    public String getGroupname() {
        return groupname;
    }
    public String getItemid() {
        return itemid;
    }
    public String getItemname() {
        return itemname;
    }
    public String getItemprice() {
        return itemprice;
    }

    public int getValue() {
        return value;
    }


    public void setValue(int value) {
        this.value = value;
    }
}
