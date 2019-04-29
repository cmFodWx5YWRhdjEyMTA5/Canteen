package com.video.aashi.canteen.Canteen.barscanner.piinview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.video.aashi.canteen.Interface.MainInterface;
import com.video.aashi.canteen.Interface.NetworkClient;
import com.video.aashi.canteen.postclass.LoginClass;
import com.video.aashi.canteen.postclass.Pin;
import com.video.aashi.canteen.postclass.StudentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class pinValidate {
    private String TAG = "PinValide";
    pinPresent pinPresent;
    public pinValidate(pinPresent pinPresent)
    {
        this.pinPresent = pinPresent;

    }
    @SuppressLint("CheckResult")
   public void validatePin(String s,String location,String id,String name)
    {
        getObservable(id,name,location,s).subscribeWith(getObserver());
        pinPresent.showProgress();
    }
       public io.reactivex.Observable<ResponseBody> getObservable(String id, String name,String locId,String pin)
    {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getPins(new Pin(id,name,locId,pin))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }
     public DisposableObserver<ResponseBody> getObserver()
    {
        return new DisposableObserver<ResponseBody>() {
            String locationName,walletAmount,totalMount,studentname,className,canteeenId,orgId,orderId;

            @Override
            public void onNext(@NonNull ResponseBody movieResponse)
            {
                pinPresent.showProgress();
                String bodyString = null;
                try
                {
                    bodyString  = movieResponse.string();
                    // JSONArray list = jsonObject.getJSONArray("Student Canteen Information");
                    if (!bodyString.contains("{}"))
                    {
                        JSONObject  object= new JSONObject(bodyString);
                        JSONObject jsonObject = object.getJSONObject("Student Canteen Information");
                        locationName = jsonObject.getString("locationName");
                        totalMount = jsonObject.getString("totalAmt");
                        walletAmount = jsonObject.getString("walletAmt");
                        studentname = jsonObject.getString("studentName");
                        className = jsonObject.getString("className");
                        canteeenId = jsonObject.getString("canteenBillingId");
                        orgId = jsonObject.getString("orgId");
                        orderId = jsonObject.getString("orderId");
                        pinPresent.showsStudent(new StudentData(locationName,walletAmount,totalMount,
                                studentname,className,canteeenId,orgId,orderId));
                        pinPresent.onSuccess("Thank you..!");
                        pinPresent.openActivity();
                    }
                    else
                    {
                        pinPresent.onSuccess("Enter valid pin...!");
                    }
                }
                catch (JSONException | IOException e )
                {
                    e.printStackTrace();
                    pinPresent.hideProgress();
                }
                Log.d(TAG, "Editorsss" + orgId + canteeenId + orderId );
            }
            @Override
            public void onError(@NonNull Throwable e)
            {
                Log.d(TAG, "MyError" + e);
                pinPresent.hideProgress();
                pinPresent.onSuccess("Something went wrong..!!");
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {
                Log.d(TAG, "Completed");
                pinPresent.hideProgress();


            }
        };
    }
   public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
