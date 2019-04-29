package com.video.aashi.canteen;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.video.aashi.canteen.Interface.MainInterface;
import com.video.aashi.canteen.Interface.NetworkClient;
import com.video.aashi.canteen.postclass.LoadJson;
import com.video.aashi.canteen.postclass.PayAmount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
public class PayPresent {
  public  final String TAG="TAGS";
  private payInterfacae payInterfacae;
  List<PayAmount> payAmounts = new ArrayList<>();
    public PayPresent(payInterfacae payInterfacae)
    {
        this.payInterfacae = payInterfacae;
    }
    @SuppressLint("CheckResult")
    public void getItems(String studentId, String studentName, String walletAmount, String purchadeAmount,
                         String balanceAmt, String orderId, String orgId, String locationId, List<LoadJson> itemList)
    {
        getObservable(studentId,studentName,walletAmount,purchadeAmount,balanceAmt,orderId, orgId, locationId,itemList).subscribeWith(getObserver());
        payInterfacae.showPrgress();
    }
    public io.reactivex.Observable<ResponseBody> getObservable(String studentId,String studentName,String walletAmount,
                                                               String purchadeAmount,String balanceAmt,
                 String orderId,String orgId,String locationId,List<LoadJson> itemList)
    {
        return NetworkClient.getRetrofit()
                .create(MainInterface.class)
                .PayCash (new PayAmount(studentId,studentName,walletAmount,purchadeAmount,balanceAmt,orderId,
                        orgId, locationId,itemList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public DisposableObserver<ResponseBody> getObserver()
    {
        return new DisposableObserver<ResponseBody>() {
            String output,message,itemName,itemPrice;
            String status;
            @Override
            public void onNext(@NonNull ResponseBody movieResponse)
            {
                String bodyString = null;
                try
                {
                    bodyString  = movieResponse.string();
                    JSONObject object = new JSONObject(bodyString);
                    //JSONArray list = object.getJSONArray("Item List");
                    payInterfacae.hideView();
                }
                catch (JSONException | IOException e )
                {
                    e.printStackTrace();
                }
                Log.d(TAG, "MyOrders"+  payAmounts + bodyString);
            }
            @Override
            public void onError(@NonNull Throwable e)
            {
                Log.d(TAG, "MyError" + e);
                payInterfacae.hideView();
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {
                Log.d(TAG, "Completed");
                payInterfacae.hideView();
            }
        };
    }


}
