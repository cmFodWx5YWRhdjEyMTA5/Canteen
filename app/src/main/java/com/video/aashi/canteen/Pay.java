package com.video.aashi.canteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.video.aashi.canteen.Canteen.barscanner.piinview.PinView;
import com.video.aashi.canteen.Canteen.barscanner.piinview.pinPresent;
import com.video.aashi.canteen.Canteen.barscanner.piinview.pinValidate;
import com.video.aashi.canteen.Canteens.cart.Cart;
import com.video.aashi.canteen.Canteens.localDB.GsonStringConverterFactoryWrapper;
import com.video.aashi.canteen.Canteens.localDB.MyCounts;
import com.video.aashi.canteen.Canteens.localDB.databaseHelper;
import com.video.aashi.canteen.Interface.MainInterface;
import com.video.aashi.canteen.account.BlankFragment;
import com.video.aashi.canteen.fragments.Name;
import com.video.aashi.canteen.postclass.LoadJson;
import com.video.aashi.canteen.postclass.PayAmount;
import com.video.aashi.canteen.postclass.StudentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pay extends AppCompatActivity implements pinPresent,payInterfacae

{
    @BindView(R.id.pay_tool)
    Toolbar toolbar;
    public  static   ImageView qrcode;
    @BindView(R.id.totals)
    TextView totals;
    @BindView(R.id.wallet)
    TextView wallet;
    @BindView(R.id.walletRemaining)
    TextView remaining;
    @BindView(R.id.payCash)
    CardView paycash;
    @BindView(R.id.payWallet)
     CardView payWallet;
    SharedPreferences sharedPreferences,sharedPreferencess;
    String mypin;
    ProgressDialog progressDialog;
    JSONObject jsonObj;
    JSONArray jsonArray;
    PayPresent payPresent;
    JSONObject jsonObject;
    private List<Name> names;
    private databaseHelper db;
    List<LoadJson> loadJsons;
    JsonArrayRequest jsonArrayRequest;
    Retrofit retrofit;
    MainInterface mainInterface;
    JSONStringer jsonStringer = null;
    com.video.aashi.canteen.Canteen.barscanner.piinview.pinValidate pinValidate;
    String text,array;
    CircleProgressView mCircleView;
    PopupWindow popupWindow;
    MyCounts myCounts;
    String id,name,location;
    SharedPreferences getEditor;
    String orgId,canId,orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_pay);
         Bundle bundle = getIntent().getExtras();
         ButterKnife.bind(this);
        sharedPreferences =  getApplicationContext(). getSharedPreferences("PIN",MODE_PRIVATE);
        sharedPreferencess = getApplicationContext().getSharedPreferences("Student",MODE_PRIVATE);
        getEditor = getApplicationContext().getSharedPreferences("Info",MODE_PRIVATE);
        mypin = sharedPreferences.getString("mypin","");
        orgId = getEditor.getString("orgId","");
        canId =   getEditor.getString("cantenId","");
        orderId =  getEditor.getString("orderId","");
        Log.d("TAG", "Editors" + " 1 :" + orgId + " 2 :"+ canId+ " 3 : "  + orderId );
        id = sharedPreferencess.getString("id","");
        name = sharedPreferencess.getString("name","");
        location = sharedPreferencess.getString("location","");
        pinValidate = new pinValidate(Pay.this);
        payPresent = new PayPresent(Pay.this);
        db= new databaseHelper(Pay.this);
        names = new ArrayList<>();
        popupWindow = new PopupWindow(Pay.this);
        jsonObject = new JSONObject();
        loadJsons =new ArrayList<>();
        jsonArray = new JSONArray();
        myCounts = new MyCounts(getApplicationContext());
        loadNames();
         loadJsons();
         qrcode =(ImageView)findViewById(R.id.generateqr);
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         toolbar.setTitle("Checkout");
         text = getIntent().getStringExtra("amount");
         array = getIntent().getStringExtra("array");
         totals .setText(text);
         Gson gson = new Gson();
         String data = array;
         JsonParser jsonParser = new JsonParser();
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor()
                        {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException
                            {
                                okhttp3.Request request = chain.request().newBuilder()
                                        .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);
                            }
                        }).build();
        /// url  http://182.72.198.156:3035/eazied/rest/CanteenAppWS/
        retrofit =   new Retrofit.Builder().baseUrl("http://182.72.198.156:3035/eazied/rest/CanteenAppWS/").
                addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        mainInterface = retrofit.create(MainInterface.class);


        //   JsonArray jsonArray = (JsonArray) jsonParser.parse(data);
        //  mainInterface = retrofit.create()
        //  Log.i("TAG","Inputss"+Cart.jsonArray);
        paycash.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                LoadOrders();

             }
         });

        payWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = mainInterface.get();
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, retrofit2.Response response) {

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });


            }
        });
        progressDialog = new ProgressDialog(Pay.this);
         pinValidate.validatePin(mypin,location,id,name);
         MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
         try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    void LoadOrders()
    {
     showPopup();
        String ids = null;
        String names= null;
       LoadJson loadJson = null;
       retrofit2.Call<ResponseBody> call = null;
        for(int position=0;position<loadJsons.size(); position++) {

            loadJson  = loadJsons.get(position);
             ids = loadJsons.get(position).getCanId();
             names = loadJsons.get(position).getItemName();
        }

        call  = mainInterface.getLogin(new PayAmount(id,name,wallet.getText().toString().trim(),
                totals.getText().toString().trim(),
                remaining.getText().toString(),orderId,orgId,location,
                Collections.singletonList(new LoadJson(ids, loadJson.getGroupName(), loadJson.getItemId(),
                        names, loadJson.getItemPRice()
                        , loadJson.getQty(), loadJson.getTotalPrice(), loadJson.getDeliveryStatus()))
        ));
        Log.i("Tag","MyInputs"+ "orderId :" + orderId + "orgId :" +orgId +"locaId :" + location +
                "locaId :" + canId  );
        payPresent.getItems(id,name,wallet.getText().toString().trim(),totals.getText().toString().trim(),
                remaining.getText().toString().trim(),"","","", Collections.singletonList(new LoadJson(ids, loadJson.getGroupName(), loadJson.getItemId(),
                        names, loadJson.getItemPRice()
                      , loadJson.getQty(), loadJson.getTotalPrice(), loadJson.getDeliveryStatus())));

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String bodyString = null;
                try
                {
                    bodyString  = response.body().string();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                db.deleteAll();
                myCounts.deleteTables();
                Log.i("Tag","MyOutput"+ bodyString );
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        LoadJson finalLoadJson = loadJson;

    }
    void  showPopup()
    {
            Display display = getWindowManager().getDefaultDisplay();
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View mView = inflater.inflate(R.layout.progressview, null);
            popupWindow = new PopupWindow(
                    mView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            popupWindow.setFocusable(true);
            popupWindow.setAnimationStyle(R.style.popupanimation);
            popupWindow.showAtLocation(mView, Gravity.CENTER ,0, 0);
        mCircleView = (CircleProgressView)mView.findViewById(R.id.circleView);
        mCircleView.setShowTextWhileSpinning(true);
        mCircleView.setText("Ordering...");
        mCircleView.setTextMode(TextMode.TEXT);
        mCircleView.spin();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                popupWindow.dismiss();
                Toast.makeText(getApplicationContext(),"Order Confirmed",Toast.LENGTH_SHORT).show();
               // Intent i = new Intent(Pay.this,PinView.class);
              //  startActivity(i);
            }
        }, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(String s) {
    }
    @Override
    public void showProgress()
    {
        progressDialog.show();
    }
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void openActivity() {
    }
    @Override
    public void showsStudent(StudentData studentData) {
    wallet.setText(studentData.getWalletAmount());
        int remainings =  Integer.valueOf(wallet.getText().toString()) - Integer.valueOf(text.trim());
        remaining.setText(String.valueOf(remainings));
    }


    public static List<LoadJson> myJsons()
    {
        List<LoadJson> loadJsons= new ArrayList<>();



        return loadJsons;
    }

    void  loadJsons()
    {
        for(int position=0;position<names.size(); position++){

            Name name = names.get(position);
         //   LoadJson loadJson = loadJsons.get(position);
            loadJsons.add(new LoadJson(canId,names.get(position).getGroupName() ,names.get(position).getName(),
                    names.get(position).getItemName(),
                    names.get(position).getPrice(),
                    names.get(position).getQuantity(),
                    name.getTotal(),
                    "ORDERED"));
            try
            {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("canteenBillingId",loadJsons.get(position).getCanId());
                jsonObj.put("itemGroupName",loadJsons.get(position).getGroupName());
                jsonObj.put("itemId",loadJsons.get(position).getItemId());
                jsonObj.put("itemName",loadJsons.get(position).getItemName());
                jsonObj.put("itemPrice",loadJsons.get(position).getItemPRice());
                jsonObj.put("qty",loadJsons.get(position).getQty());
                jsonObj.put("totalPrice",loadJsons.get(position).getTotalPrice());
                jsonObj.put("delieveryStatus",loadJsons.get(position).getDeliveryStatus());
                jsonArray.put(jsonObj);
        /*        try {
                   jsonStringer = new JSONStringer().array().object()
                           .key("canteenBillingId").value(loadJsons.get(position).getCanId())
                           .key("itemGroupName").value(loadJsons.get(position).getGroupName())
                           .key("itemId").value(loadJsons.get(position).getItemId())
                           .key("itemName").value(loadJsons.get(position).getItemName())
                           .key("itemPrice").value(loadJsons.get(position).getItemPRice())
                           .key("qty").value(loadJsons.get(position).getQty())
                           .key("totalPrice").value(loadJsons.get(position).getTotalPrice())
                           .key("delieveryStatus").value(loadJsons.get(position).getDeliveryStatus())
                           .endObject().endArray();
                    Log.i("TAG","JSONARRAYS"+jsonStringer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showPrgress() {
        progressDialog.setMessage("Ordering..");
        progressDialog.show();
    }
    @Override
    public void hideView() {
        progressDialog.dismiss();
    }
    @Override
    public void showMessages(String e)
    {

    }
    private void loadNames()
    {

        Cursor cursor = db.getNames();
        if (cursor.moveToFirst())
        {
            do {
                Name name = new Name(cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_ID)),
                        cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndex(databaseHelper.GROUP_NAME)),
                        cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(databaseHelper.PRICE)),
                        cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_PRICE)),
                        cursor.getString(cursor.getColumnIndex(databaseHelper.COUNT))
                );
                String counts =  cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_ID));
                String counts1 =  cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_STATUS));
                String counts2 =  cursor.getString(cursor.getColumnIndex(databaseHelper.GROUP_NAME));
                String counts3 =  cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_PRICE));
                String counts4 =  cursor.getString(cursor.getColumnIndex(databaseHelper.PRICE));
                String counts5 =  cursor.getString(cursor.getColumnIndex(databaseHelper.COUNT));
                String counts6 =  cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_NAME));

                //    addMember(Integer.valueOf(counts));
                Log.i("TAG","MyItems"+counts+counts1+counts2+counts3+counts4+counts5+counts6);

                names.add(name);
            }
            while (cursor.moveToNext());
        }

    }



    void Request()
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(Pay.this);
        String u = "http://182.72.198.156:3035/eazied/rest/CanteenAppWS/createOrderOrDelieveryforCanteenInvoice";
        //Create an error listener to handle errors appropriately.
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);


                    Log.i("TAG","myresponse"+response);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, error -> Toast.makeText(Pay.this, " " + error, Toast.LENGTH_SHORT).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> para = new HashMap<String, String>();
                para.put("studentId", "799");
                para.put("studentName","MURALI VIJAYAN");
                para.put("walletAmt", wallet.getText().toString().trim());
                para.put("purchasedAmt", totals.getText().toString().trim());
                para.put("balanceAmt", remaining.getText().toString().trim());
                para.put("orderId", "1");
                para.put("orgId", "1");
                para.put("locationId", "1");
                para.put("ItemList", jsonArray.toString());
                return para;

            }
        };
        Log.i("TAG","Inputs"+wallet.getText().toString()+totals.getText().toString().trim()+
                remaining.getText().toString().trim()+jsonArray);
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
        MyRequestQueue.add(MyStringRequest);
    }


}
