package com.video.aashi.canteen.Canteens.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.Canteens.localDB.MyCounts;
import com.video.aashi.canteen.Canteens.localDB.databaseHelper;
import com.video.aashi.canteen.Pay;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.fragments.Homepage;
import com.video.aashi.canteen.fragments.Name;
import com.video.aashi.canteen.postclass.LoadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Cart extends Fragment
{

    Toolbar toolbar;


    @BindView(R.id.checkout)
    CardView checkout;
    int count = 0;
    private List<Name> names;
    private databaseHelper db;
    @BindView(R.id.listViewNames)
    ListView listView;
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.totalAmount)
    TextView totalAmount;
    List<Integer> myList = new ArrayList<Integer>();
    JSONObject jsonObj;
    JSONArray jsonArray;
    List<LoadJson> loadJsons;
    List<JSONObject> myObjects;
    @BindView(R.id.toVisible)
    LinearLayout tovisible;
    @BindView(R.id.myVivible)
    TextView myvisible;
    MyCounts myCounts;
    NameAdapter nameAdapter;
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_cart, container, false);
        ButterKnife.bind(this,view);
        names = new ArrayList<>();
        db = new databaseHelper(getActivity());
        myCounts = new MyCounts(getActivity());
        count = 0;
        loadNames();
        jsonArray=new JSONArray();
        loadJsons = new ArrayList<>();
        myObjects = new ArrayList<>();
         getActivity().setTitle("Cart");


         for(int position=0;position<names.size(); position++){

             Name name = names.get(position);

             loadJsons.add(new LoadJson("1",names.get(position).getItemName() ,"1",
                     names.get(position).getItemName(),
                     names.get(position).getPrice(),names.get(position).getQuantity(),name.getTotal(),
                     "ORDERED"));




         }
         for (int i =0 ;i<loadJsons.size();i++)
         {
             try
             {
                 JSONObject  jsonObj = new JSONObject();
                 jsonObj.put("position", i);
                 jsonObj.put("canteenBillingId",loadJsons.get(i).getCanId());
                 jsonObj.put("itemGroupName",loadJsons.get(i).getGroupName());
                 jsonObj.put("itemId",loadJsons.get(i).getItemId());
                 jsonObj.put("itemName",loadJsons.get(i).getItemName());
                 jsonObj.put("itemPrice",loadJsons.get(i).getItemPRice());
                 jsonObj.put("qty",loadJsons.get(i).getQty());
                 jsonObj.put("totalPrice",loadJsons.get(i).getTotalPrice());
                 jsonObj.put("delieveryStatus",loadJsons.get(i).getDeliveryStatus());
                 Log.i("TAG","MyJitems"+ jsonObj);
                 jsonArray.put(jsonObj);
                 Log.i("TAG","MyJsonss"+jsonArray);
                 Log.i("TAG","MyJsons"+myObjects);
                } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
        JSONObject tempArray=new JSONObject();
        totalAmount.setText(Homepage.totalprice.getText().toString());
        checkout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (names.size() == 0)
                 {
                     Toast.makeText(getActivity(),"Cart is empty..! add some items..!",Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     Intent intent=new Intent(getActivity(),Pay.class);
                    intent.putExtra("amount",totalAmount.getText().toString());
                     intent.putExtra("array", jsonArray.toString());
                     startActivity(intent);
                 }


            }
         });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCounts.deleteTables();
                db.deleteAll();
                myvisible.setVisibility(View.VISIBLE);
                 tovisible.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                refreshList();

            }
        });
        return  view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         return super.onOptionsItemSelected(item);
    }
    private void refreshList()
    {

        nameAdapter.notifyDataSetChanged();

    }
    class NameAdapter extends ArrayAdapter<Name> {

        //storing all the names in the list
        private List<Name> namess;

        //context object
        private Context context;

        //constructor
        public NameAdapter(Context context, int resource, List<Name> names) {
            super(context, resource, names);
            this.context = context;
            this.namess = names;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //getting the layoutinflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //getting listview itmes
            @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.names, null, true);
            TextView textViewName = (TextView) listViewItem.findViewById(R.id.ItemNames);
            TextView price = (TextView) listViewItem.findViewById(R.id.itemPrices);
            TextView quantity = (TextView) listViewItem.findViewById(R.id.itemQuan);
            TextView totals = (TextView) listViewItem.findViewById(R.id.totalss);
            Name name = names.get(position);


                textViewName.setText(name.getItemName());
            totals.setText(name.getPrice());
            price.setText(name.getTotal());
            quantity.setText(name.getQuantity());
            loadJsons.add(new LoadJson("1",names.get(position).getGroupName(),"1",names.get(position).getItemName(),
            names.get(position).getPrice(),names.get(position).getQuantity(),name.getTotal(),"ORDERED"));
            List<String> strings = new ArrayList<>();
            strings.add(loadJsons.get(position).getItemId());

            return listViewItem;
        }
    }
    private void loadNames()
    {
        Log.i("Tag","MyLists"+names.size() );
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


                 addMember(Integer.valueOf(counts));
                 Log.i("TAG","Mycounts"+counts);

                names.add(name);
                if (names.size() ==0)
                {
                    myvisible.setVisibility(View.VISIBLE);
                    tovisible.setVisibility(View.GONE);
                }
                else {
                    myvisible.setVisibility(View.GONE);
                    tovisible.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }
            }
            while (cursor.moveToNext());
        }
        nameAdapter =new NameAdapter(getActivity(),R.layout.names,names);
       listView.setAdapter(nameAdapter);
    }
    public void addMember(Integer x) {
        myList.add(x);
    };

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    CanteenMain.mBottomNav.setSelectedItemId(R.id.action_home);
                    return true;
                }
                return false;
            }
        });
    }
    public  void  showfrag(Fragment fragment)
    {
        getActivity(). getSupportFragmentManager().beginTransaction().replace(R.id.containers,fragment).commit();
    }

}

