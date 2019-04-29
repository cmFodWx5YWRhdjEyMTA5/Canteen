package com.video.aashi.canteen.fragments;

/**
 * Created by Belal on 1/27/2017.
 */

public class Name {
    private String name;
    private int status;
    String itemName;
    String quantity;
    String price;
    String count;
    String groupName;
    String total;

    public Name(String id, int status,String groupname,  String itemName,String price,String total, String quantity) {

        this.name = id;

        this.status = status;
        this.groupName = groupname;
        this.itemName = itemName;
        this.quantity= quantity;
        this.price = price;
        this.total= total;

    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getCount() {
        return count;
    }

    public String getTotal() {
        return total;

    }


    public String getGroupName() {
        return groupName;
    }
}
