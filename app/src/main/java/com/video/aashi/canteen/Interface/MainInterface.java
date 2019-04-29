package com.video.aashi.canteen.Interface;

import com.google.gson.JsonArray;
import com.video.aashi.canteen.postclass.Items;
import com.video.aashi.canteen.postclass.LoginClass;
import com.video.aashi.canteen.postclass.PayAmount;
import com.video.aashi.canteen.postclass.Pin;

import org.json.JSONArray;
import org.json.JSONStringer;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MainInterface {
    @POST("appLogin")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    io.reactivex.Observable<ResponseBody> getLoginstatus(@Body LoginClass loginClass);
    @POST("validateStudentPin")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    io.reactivex.Observable<ResponseBody> getPins(@Body Pin pin);
    @POST("getCanteenItems")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    io.reactivex.Observable<ResponseBody> getItems(@Body Items pin);
    @POST("createOrderOrDelieveryforCanteenInvoice")
    @Headers({ "Content-Type: application/json","Accept: application/json"})
    io.reactivex.Observable<ResponseBody> PayCash(@Body PayAmount pin);
    @POST("createOrderOrDelieveryforCanteenInvoice")
    @Headers({ "Content-Type: application/json","Accept: application/json"})
    @FormUrlEncoded
    io.reactivex.Observable<ResponseBody> PayCashs(@Field("studentId") String studentId,@Field("studentName") String studentName,@Field("walletAmt") String walletAmt
                                                   ,@Field("purchasedAmt") String purchasedAmt,@Field("balanceAmt") String balanceAmt,  @Field("orderId") String orderId,
                                                    @Field("orgId") String orgId,
                                                  @Field("locationId") String locationId
                                               , @Field("ItemList") JSONArray deviceId);

    @POST("createOrderOrDelieveryforCanteenInvoice")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    io.reactivex.Observable<ResponseBody> PayCashss(@Body PayAmount pin);

    @POST("createOrderOrDelieveryforCanteenInvoice")
    Call<ResponseBody> getLogin(@Body PayAmount login);

    @GET("test")
    Call<ResponseBody> get();
}
