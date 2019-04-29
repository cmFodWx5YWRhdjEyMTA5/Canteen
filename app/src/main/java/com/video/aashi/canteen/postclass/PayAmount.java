package com.video.aashi.canteen.postclass;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class PayAmount {

    String studentId;
    String studentName;
    String walletAmt;
    String purchasedAmt;
    String balanceAmt;
    String orderId;
    String orgId;
    String locationId;
    List<LoadJson> ItemList;
     private JSONStringer arrays;
    String jsonArray;
    public PayAmount(String studentId, String studentName, String walletAmt, String purchasedAmt,
                     String balanceAmt, String orderId, String orgId, String locationId,List<LoadJson> ItemList)
    {
        this.studentId =studentId;
        this.studentName =studentName;
        this.walletAmt =walletAmt;
        this.purchasedAmt = purchasedAmt;
        this.balanceAmt = balanceAmt;
        this.orderId = orderId;
        this.orgId = orgId;
        this.locationId = locationId;
        this.ItemList = ItemList;

    }

}