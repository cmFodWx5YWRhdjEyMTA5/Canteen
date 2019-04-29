package com.video.aashi.canteen.postclass;

public class LoadJson {

    String canId;
    String groupName;
    String itemId;
    String itemName;
    String itemPRice;
    String qty;
    String totalPrice;
    String deliveryStatus;
    public  LoadJson(String canId,String groupName,String itemId,String itemName,String itemPRice,String qty,String totalPrice,String deliveryStatus)
    {
        this.canId =canId;
        this.groupName =groupName;
        this.itemId =itemId;
        this.itemName = itemName;
        this.itemPRice =itemPRice;
        this.qty =qty;
        this.totalPrice = totalPrice;
        this.deliveryStatus =deliveryStatus;
    }
    public String getItemName() {
        return itemName;
    }

    public String getCanId() {
        return canId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemPRice() {
        return itemPRice;
    }

    public String getQty() {
        return qty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
