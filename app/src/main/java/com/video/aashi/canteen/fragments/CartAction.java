package com.video.aashi.canteen.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.video.aashi.canteen.Canteens.localDB.ItemLocal;
import com.video.aashi.canteen.Canteens.localDB.databaseHelper;
import com.video.aashi.canteen.Interface.MainInterface;
import com.video.aashi.canteen.Interface.NetworkClient;
import com.video.aashi.canteen.arrays.ItemList;
import com.video.aashi.canteen.postclass.Items;
import com.video.aashi.canteen.postclass.LoginClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CartAction {
    private  static String TAG ="CART";

    List<ItemList> itemLists;
    Cartpresent cartpresent;
    ItemLocal itemLocal;
    public  CartAction(Cartpresent cartpresent,List<ItemList> itemLists,ItemLocal itemLocal)
    {
        this.cartpresent = cartpresent;
        this.itemLists = itemLists;
        this.itemLocal = itemLocal;

    }
    public void getItems(String usetname, String password)
    {
        getObservable(usetname,password).subscribeWith(getObserver());
        cartpresent.showProgress();
    }
    public io.reactivex.Observable<ResponseBody> getObservable(String user, String pass)
    {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getItems(new Items(user,pass))
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
                    JSONArray list = object.getJSONArray("ItemList");
                    if (list.length() != 0)
                    {
                    for(int i=0;i<list.length();i++) {
                        JSONObject jsonObject = list.getJSONObject(i);
                        output = jsonObject.getString("itemGroupName");
                        message = jsonObject.getString("itemId");
                        itemName = jsonObject.getString("itemName");
                        itemPrice = jsonObject.getString("itemPrice");
                       //itemLists.add(new ItemList(message,output,itemName,itemPrice));
                       //cartpresent.addItems(itemLists);
                       //saveNameToLocalStorage(message,output, itemName, itemPrice);
                        Log.i("TAG","MyProductss"+message+output+ itemName+itemPrice );
                        Cursor cursor = itemLocal.getId(message);
                        if (cursor.moveToFirst())
                        {
                           do {
                              String  string = cursor.getString(cursor.getColumnIndex(ItemLocal.GROUP_NAME));
                                String  string1 = cursor.getString(cursor.getColumnIndex(ItemLocal.ITEM_ID));
                                String  string2 = cursor.getString(cursor.getColumnIndex(ItemLocal.PRICE));
                                String  string3 = cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_NAME));
                                Log.i("TAG","MyProducts"+string+string1+string2+string3);
                               itemLocal.update(message,output, itemName,itemPrice );
                              //  itemLocal.updateTable();
                              cartpresent.showLocal();
                            }
                            while (cursor.moveToNext());
                        }
                        else
                        {
                            itemLocal.addItems(message,output,itemName,itemPrice);
                            cartpresent.showLocal();
                        }
                        cartpresent.dismissProgress();
                      }
                    }
                    else
                    {
                        cartpresent.showMesage("Something went wrong");
                    }
                }
                catch (JSONException | IOException e )
                {
                    e.printStackTrace();
                }
                Log.d(TAG, "OnNexts" + itemLists.size());
            }
            @Override
            public void onError(@NonNull Throwable e)
            {
                Log.d(TAG, "MyError" + e);
                cartpresent.dismissProgress();
                cartpresent.showLocal();
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {
                Log.d(TAG, "Completed");
                cartpresent.dismissProgress();
                //cartpresent.showLocal();
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
