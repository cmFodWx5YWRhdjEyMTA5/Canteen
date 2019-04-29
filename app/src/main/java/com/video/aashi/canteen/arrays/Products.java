package com.video.aashi.canteen.arrays;

public class Products
 {
    private String id,status,groupname,itemname,quantuty;
    public Products(String id,String status,String groupname,String itemname,String quantuty)
    {
        this.id =id;
        this.status = status;
        this.groupname =groupname;
        this.itemname = itemname;
        this.quantuty =quantuty;
    }
    public String getItemname() {
        return itemname;
    }
    public String getGroupname() {
        return groupname;
    }
    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public String getQuantuty() {
        return quantuty;
     }
 }
