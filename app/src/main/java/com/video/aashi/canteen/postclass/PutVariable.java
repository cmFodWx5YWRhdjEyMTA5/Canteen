package com.video.aashi.canteen.postclass;

public class PutVariable {

    String orgid,locationid,locationname,username;


    public PutVariable(String orgid,String locationid,String locationname,String username)
    {
        this.orgid =orgid;
        this.locationid = locationid;
        this.locationname =locationname;
        this.username= username;
    }

    public String getLocationid() {
        return locationid;
    }

    public String getLocationname() {
        return locationname;
    }

    public String getOrgid() {
        return orgid;
    }

    public String getUsername() {
        return username;
    }
}
