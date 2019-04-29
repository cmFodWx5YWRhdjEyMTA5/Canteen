package com.video.aashi.canteen.login.Login;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Observable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.video.aashi.canteen.Interface.MainInterface;
import com.video.aashi.canteen.Interface.NetworkClient;
import com.video.aashi.canteen.postclass.LoginClass;
import com.video.aashi.canteen.postclass.PutVariable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.NetworkInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class Login implements LoginPresent
{
    ILogin login;
    SharedPreferences   sharedPreferences;
    SharedPreferences.Editor editor;
    private String TAG = "MainPresenter";
    public Login(ILogin login)
    {
        this.login = login;
    }
    @SuppressLint("CheckResult")
    @Override
    public void performLogin(String usetname, String password)
    {
        getObservable(usetname,password).subscribeWith(getObserver());
        login.showProgress();
    }
    public io.reactivex.Observable<ResponseBody> getObservable(String user, String pass)
    {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getLoginstatus(new LoginClass(user,pass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public DisposableObserver<ResponseBody> getObserver()
    {
        return new DisposableObserver<ResponseBody>() {
            String output,message,orgid,locationid,locationname,username;
            @Override
            public void onNext(@NonNull ResponseBody movieResponse)
            {
                String bodyString = null;
                try
                {
                    bodyString  = movieResponse.string();
                    JSONObject jsonObject = new JSONObject(bodyString);
                    output = jsonObject.getString("status");
                    message = jsonObject.getString("errorMessage");
                    if (output.contains("success"))
                    {
                        orgid = jsonObject.getString("orgId");
                        locationid = jsonObject.getString("locationId");
                        locationname = jsonObject.getString("locationName");
                        username=jsonObject.getString("locationName");
                        login.LoginSuccess("Login success");
                        login.openMainActivity();
                        login.dismissProgress();
                        login.putSharedValiables(new PutVariable(orgid,locationid,locationname,username));
                    }
                    else
                    {
                        login.LoginSuccess(message);
                        login.dismissProgress();
                    }
                }
                catch (JSONException | IOException e )
                {
                    e.printStackTrace();
                }
                Log.d(TAG, "OnNext" + bodyString);
            }
            @Override
            public void onError(@NonNull Throwable e)
            {
                Log.d(TAG, "MyError" + e);
                login.dismissProgress();
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {
                Log.d(TAG, "Completed");
                login.dismissProgress();
            }
        };
    }
}
