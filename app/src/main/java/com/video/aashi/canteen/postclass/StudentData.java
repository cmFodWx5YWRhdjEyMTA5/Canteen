package com.video.aashi.canteen.postclass;

import android.icu.text.StringSearch;

public class StudentData {

   String locationName,walletAmount,totalMount,studentname,classNam,billingId,orgId,orderId;

   public StudentData(String locationName, String walletAmount, String totalMount, String studentname,
                      String classNam,String billingId,String orgId,String orderId)
   {
       this.locationName = locationName;
       this.walletAmount = walletAmount;
       this.totalMount =totalMount;
       this.studentname = studentname;
       this.classNam = classNam;
       this.billingId = billingId;
       this.orderId = orderId;
       this.orgId = orgId;
   }

    public String getLocationName() {
        return locationName;
    }

    public String getStudentname() {
        return studentname;
    }

    public String getTotalMount() {
        return totalMount;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public String getClassNam() {
        return classNam;
    }

    public String getBillingId() {
        return billingId;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getOrderId() {
        return orderId;
    }
}
