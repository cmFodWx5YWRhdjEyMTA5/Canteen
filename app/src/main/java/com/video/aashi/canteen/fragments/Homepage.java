package com.video.aashi.canteen.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.canteen.Canteen.barscanner.piinview.pinValidate;
import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.Canteens.cart.Cart;
import com.video.aashi.canteen.Canteens.localDB.ItemLocal;
import com.video.aashi.canteen.Canteens.localDB.MyCounts;
import com.video.aashi.canteen.Canteens.localDB.databaseHelper;
import com.video.aashi.canteen.Canteens.others.OnSwipeTouchListener;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.arrays.Counters;
import com.video.aashi.canteen.arrays.ItemList;
import com.video.aashi.canteen.arrays.Products;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Homepage extends Fragment implements Cartpresent
{

    @BindView(R.id.bread)
    CardView bread;
    @BindView(R.id.coffee)
    CardView coffee;
    ProductAdapters productAdapters;
    private databaseHelper db;
    private List<Name> names;
    public static int REP_DELAY =50;
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    private int[] counter = new int[1000];
    ArrayList<ItemList> itemLists;
    public int mValue;
    private Handler repeatUpdateHandler = new Handler();
    private GestureDetector gestureDetector;
    @BindView(R.id.quantity)
    TextView badge;
    @BindView(R.id.itemName)
    TextView itemName;
    @BindView(R.id.itemPrice)
    TextView price;
    @BindView(R.id.addCart)
    FloatingActionButton addCart;
    String groupName = "Veg";
    @BindView(R.id.items)
    GridView gridView;
    @BindView(R.id.addTocart)
    LinearLayout addtoCart;
    @BindView(R.id.filter)
    ImageView filter;
    List<Integer> myList = new ArrayList<Integer>();
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    ProgressDialog progressDialog;
    CartAction cartAction;
    public static TextView totalcount,totalprice;
    List<Products> products;
    ItemLocal itemLocal;
    List<ItemList> itemListList;
    MyCounts myCounts;
    int myposition;
    int mycounts;
    public List<Counters> counters;
    ProductAdapters.ViewHolder finalHolder;
    @BindView(R.id.confirm)
    TextView confirm;
    SearchView searchView;
    List<String> groupNames;
    PopupWindow filter_popup;
    boolean doubleBackToExitPressedOnce =false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);
        totalcount = (TextView)view.findViewById(R.id.totalCount);
        totalprice =(TextView)view.findViewById(R.id.totalPrice);
        products = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());
        itemLists = new ArrayList<>();
        itemLocal= new ItemLocal(getActivity());
        cartAction = new CartAction(Homepage.this,itemLists,itemLocal);
        myCounts = new MyCounts(getActivity());
        counters = new ArrayList<>();
        groupNames = new ArrayList<>();
        Cursor cursor  = myCounts.getNames();
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        getActivity().setTitle("Home Page");
        if (cursor.moveToFirst())
        {
            do {
                myposition = cursor.getInt(cursor.getColumnIndex(MyCounts.POSITION));
                mycounts = cursor.getInt(cursor.getColumnIndex(MyCounts.MYCOUNTS));
                Log.i("TAG","Mypositionss" +myposition+mycounts );
                counters.add(new Counters(myposition,mycounts));



            } while (cursor.moveToNext());

        }

        if  (cartAction.isNetworkAvailable(getActivity()))
        {

             // itemLocal.deleteAll();
              cartAction.getItems(CanteenMain.orgid,CanteenMain.locationid);
         //   Log.i("TAG","Mypositionsss" +CanteenMain.orgid+CanteenMain.locationid );
        }
        else
        {
                  loadLocalItems();
              //  Toast.makeText(getActivity(),"Check connection..!!",Toast.LENGTH_SHORT).show();

        }
        db = new databaseHelper(getActivity());
        names = new ArrayList<>();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart= new Cart();
               showfrag(    cart);
          //     updateToolbarText("Cart");
            }
        });
        gridView.setNumColumns(3);

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCounts.deleteTables();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        int string,strings;

        coffee.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         increment();
     }
     });



        return view;
    }
    @Override
    public void showMesage(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void showProgress()
    {
        progressDialog.show();
    }
    @Override
    public void addItems(List<ItemList> itemListList) {
      //  ProductAdapters productAdapter=new ProductAdapters(getActivity(),R.layout.names,itemListList);
      //  gridView.setAdapter(productAdapter);
    }
    @Override
    public void showLocal() {
        loadLocalItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout,menu);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
        .getActionView();
        searchView. setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().  getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        setSearchIcons(searchView);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(
                        android.support.v7.appcompat.R.id.search_src_text);

        searchAutoComplete.setTextColor(Color.GRAY);

        /*Code for changing the search icon */
        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_magnifier);
        // listening to search query text change

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                productAdapters.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                productAdapters.getFilter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void setSearchIcons(SearchView search) {
        try
        {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView closeBtn = (ImageView) searchField.get(search);
            closeBtn.setImageResource(R.drawable.ic_close_white_24dp );
            ImageView searchButton = (ImageView) search.
                    findViewById(android.support.v7.appcompat.R.id.search_button );
            searchButton.setImageResource(R.drawable.ic_magnifier);


        } catch (NoSuchFieldException e)
        {
            Log.e("SearchView", e.getMessage(), e);
        } catch (IllegalAccessException e)
        {
            Log.e("SearchView", e.getMessage(), e);
        }
    }
    void loadLocalItems()
    {
      //  itemLocal.deleteAlsl();
        String groupNam = null;
      //  ProductAdapter productAdapter=new ProductAdapter(getActivity(),itemListList,gridView);
       List<ItemList>  itemLists = new ArrayList<>();
       Cursor cursor = itemLocal.getNames();

        if (cursor.moveToFirst())
        {
            do
            {
                ItemList name = new ItemList(
                        cursor.getString(cursor.getColumnIndex(ItemLocal.ITEM_ID)),
                        cursor.getString(cursor.getColumnIndex(ItemLocal.GROUP_NAME)),
                        cursor.getString(cursor.getColumnIndex(ItemLocal.ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(ItemLocal.PRICE))
                );
                String counts =  cursor.getString(cursor.getColumnIndex(ItemLocal.PRICE));
                addMember(Integer.valueOf(counts));
                groupNam =  cursor.getString(cursor.getColumnIndex(ItemLocal.GROUP_NAME));
                String itemnames = groupNam.substring(0,1).toUpperCase() + groupNam.substring(1).toLowerCase();
                groupNames.add(itemnames);
                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(groupNames);
                groupNames.clear();
                groupNames.addAll(hashSet);

                itemLists.add(name);
          }
            while (cursor.moveToNext());
        }productAdapters=new ProductAdapters(getActivity(),R.layout.itemdesign,itemLists);
        gridView.setAdapter(productAdapters);
    }
    void loadFilter(String groupname)
    {
        //  itemLocal.deleteAlsl();
        String groupNam = null;
        //  ProductAdapter productAdapter=new ProductAdapter(getActivity(),itemListList,gridView);
        List<ItemList>  itemLists = new ArrayList<>();
        Cursor cursor = itemLocal.getitemBygroup(groupname);

        if (cursor.moveToFirst())
        {
            do
            {
                ItemList name = new ItemList(
                        cursor.getString(cursor.getColumnIndex(ItemLocal.ITEM_ID)),
                        cursor.getString(cursor.getColumnIndex(ItemLocal.GROUP_NAME)),
                        cursor.getString(cursor.getColumnIndex(ItemLocal.ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(ItemLocal.PRICE))
                );
                String group = cursor.getString(cursor.getColumnIndex(ItemLocal.GROUP_NAME));
                Log.i("TAG","MyGroups"+ group );
                itemLists.add(name);
            }
            while (cursor.moveToNext());
        }productAdapters=new ProductAdapters(getActivity(),R.layout.itemdesign,itemLists);
        gridView.setAdapter(productAdapters);
    }

    void showFilter()
    {

        Display display = getActivity(). getWindowManager().getDefaultDisplay();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View mView = inflater.inflate(R.layout.filterpopup, null);
        filter_popup = new PopupWindow(
                mView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        filter_popup.setFocusable(true);
        filter_popup.setAnimationStyle(R.style.popup_window_animation_phone);
        filter_popup.showAtLocation(mView, Gravity.CENTER, 0, 0);
        ListView listView= (ListView)mView.findViewById(R.id.groupSpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
               R.layout.textcenter,
                groupNames );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList =(listView.getItemAtPosition(position).toString().toUpperCase());
                loadFilter(selectedFromList);
                filter_popup.dismiss();
            }
        });

        listView.setAdapter(arrayAdapter);



        dimBehind(filter_popup);
    }
    public void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            }
            else
            {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }
    public  void  showfrag(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containers,fragment).commit();
    }

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
                            if (doubleBackToExitPressedOnce) {

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                                doubleBackToExitPressedOnce = false;
                            }
                    if (!searchView.isIconified()) {
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        doubleBackToExitPressedOnce = false;
                        searchView.setQuery("",false);
                        searchView.setIconified(true);
                     }
                     else
                     {
                        doubleBackToExitPressedOnce = true;
                        Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                     }
                     new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doubleBackToExitPressedOnce = false;

                                }
                            }, 2000);
                            return true;
                        }





              return false;
            }
        });
    }
    class RptUpdater implements Runnable {
        public void run() {
            if( mAutoIncrement ){
                increment();
                repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
            } else if( mAutoDecrement ){
            //    decrement();
                repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
            }
        }
    }
    public void decrement(){
        mValue--;
        if(mValue<0){
            mValue=0;
        }
        CanteenMain.cardbadge.setText( String.valueOf(mValue) );
        badge.setText(String.valueOf(mValue));
    }
    public void increment(){
        mValue++;
        CanteenMain.cardbadge.setText(  String.valueOf(mValue));
        badge.setText(String.valueOf(mValue));
    }
    public void setmValue()
    {
        mValue = 0;
        CanteenMain.cardbadge.setText(  String.valueOf(mValue));
        badge.setText(String.valueOf(mValue));
    }
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
     public void saveNameToLocalStorage(String id, String groupname,String itemName, String price)
     {
        itemLocal.addItems(id,groupname,itemName,price);
        ItemList itemList=new ItemList(groupname,id,itemName,price);
        itemLists.add(itemList);
     }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(getActivity(), "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(getActivity(), "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    public void addMember(Integer x) {
        myList.add(x);
    };
    public class ProductAdapters extends BaseAdapter implements View.OnClickListener,Filterable {
        PopupWindow editPop;
        private boolean mAutoIncrement = false;
        private boolean mAutoDecrement = false;
        List<Products> products =new ArrayList<>();
        public int mValue = 0;
        public int itemCount= 0;
        GridView gridview;
        Context context;
        int toatalAmount;
        private List<Name> names = new ArrayList<>();
        private databaseHelper db;
        boolean  check= false;
        public  String quantity;
        View view;
        private Handler repeatUpdateHandler = new Handler();
        List<ItemList> itemLists ;
        List<ItemList> filterList;
        // List<Counters> counters= new ArrayList<>();
        public static final int NAME_SYNCED_WITH_SERVER = 1;
        public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
        ArrayList<String> ids=new ArrayList<>();
        List<Integer> myList = new ArrayList<Integer>();
        List<Integer> myLists = new ArrayList<Integer>();
        int[] counts;
        int resourse;
        public ProductAdapters(Context cotext, int resource, List<ItemList> arrayList)
        {
            filterList = arrayList;
            this.resourse = resource;
            this.context = cotext;
            this.itemLists = arrayList;

        }

        @Override
        public int getCount()
        {
            return filterList.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ItemList itemList = filterList.get(position);

            View row = convertView;
          ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater =  LayoutInflater.from(context);
                row = inflater.inflate(R.layout.itemdesign, parent, false);
                holder = new ViewHolder();
                holder.   quantity = (TextView)row.findViewById(R.id.quantity);
                holder.itemname =(TextView)row.findViewById(R.id.itemName);
                holder.price =(TextView)row.findViewById(R.id.itemPrice);
                holder.item=(CardView)row.findViewById(R.id.bread);
                db = new databaseHelper(context);
                row.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) row.getTag();
            }
            String name = filterList.get(position).getItemname();
            String itemnames = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
            holder.itemname.setText(itemnames);
            holder.price.setText(filterList  .get(position).getItemprice());
            final ViewHolder finalHolder = holder;
            // finalHolder. quantity.setText(itemLists.get(position).getValue());
            int value =Integer.valueOf(finalHolder. quantity.getText().toString());
            holder.item.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    return super.onTouch(v, event);
                }
                @Override
                public void onSwipeTop() {
                    //    visibility();

                    super.onSwipeTop();
                }
                @Override
                public void onSwipeRight()
                {
                    if(mValue<0)
                    {
                        mValue=0;
                    }
                    if (toatalAmount<0)
                    {
                        toatalAmount = 0;
                    }
                    int a= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
                    int total  =  a *  Integer.valueOf(filterList.get(position).getItemprice());
                    mValue = mValue -Integer.valueOf( finalHolder. quantity.getText().toString());
                    //  Homepage. totalcount.setText( String.valueOf(mValue));
                    //  CanteenMain.cardbadge.setText(String.valueOf(mValue));
                    counter[Integer.valueOf(filterList.get(position).getItemid())] = 0;
                    finalHolder. quantity.setText(counter[Integer.valueOf(filterList.get(position).getItemid())]+"");
                    toatalAmount = toatalAmount  - total;
                    // Homepage.totalprice.setText(String.valueOf(toatalAmount));
                    filterList.get(position).setValue(counter[position]);
                    updatePosition(Integer.valueOf(filterList.get(position).getItemid()),
                            counter[Integer.valueOf(filterList.get(position).getItemid())]);
                    int aa= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
                    int totala  =  aa *  Integer.valueOf(filterList.get(position).getItemprice());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        update(filterList.get(position).getItemid(),

                                counter[Integer.valueOf(filterList.get(position).getItemid())] + "",
                                position,Integer.valueOf(filterList.get(position).getItemid()), totala);
                    }
                    if (counter[Integer.valueOf(filterList.get(position).getItemid())] == 0)
                    {
                        db.deleteRow(filterList.get(position).getItemid());
                    }
                  //      Toast.makeText(context,String.valueOf( a  +   total )       ,Toast.LENGTH_LONG).show();
                    super.onSwipeRight();
                }
                @Override
                public void onSwipeLeft()
                {
                    mValue = mValue -Integer.valueOf( finalHolder. quantity.getText().toString());
                    // Homepage. totalcount.setText( String.valueOf(mValue));
                    // CanteenMain.cardbadge.setText(String.valueOf(mValue));
                    counter[Integer.valueOf(filterList.get(position).getItemid())] = 0;
                    finalHolder. quantity.setText(counter[position]+"");
                    updatePosition(Integer.valueOf(filterList.get(position).getItemid()),
                            counter[Integer.valueOf(filterList.get(position).getItemid())]);
                   // Toast.makeText(context, counter[position],Toast.LENGTH_LONG).show();
                    int aa= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
                    int totala  =  aa *  Integer.valueOf(filterList.get(position).getItemprice());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        update(filterList.get(position).getItemid(), counter[Integer.valueOf(filterList.get(position).getItemid())] + "", position,
                                Integer.valueOf(filterList.get(position).getItemid()),totala);
                    }
                    if (counter[Integer.valueOf(filterList.get(position).getItemid())] == 0)
                    {
                        db.deleteRow(filterList.get(position).getItemid());
                    }
                    super.onSwipeLeft();
                }
                @Override
                public void onClick() {
                    counter[Integer.valueOf(filterList.get(position).getItemid())] += 1;
                    finalHolder. quantity.setText(counter[Integer.valueOf(filterList.get(position).getItemid())]+"");
                    int a= Integer.valueOf( finalHolder. quantity.getText().toString());
                    int total;
                    total = Integer.valueOf(filterList.get(position).getItemprice());
                    price(total);
                    filterList.get(position).setValue(counter[Integer.valueOf(filterList.get(position).getItemid())]);
                    int aa= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
                    int totala  =  aa *  Integer.valueOf(filterList.get(position).getItemprice());
                    ids.add(filterList.get(position).getItemid());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        update(filterList.get(position).getItemid(),
                                counter[Integer.valueOf(filterList.get(position).getItemid())]+"",position,
                                Integer.valueOf(filterList.get(position).getItemid()),totala);
                    }
                    increment();
                    updatePosition(Integer.valueOf(filterList.get(position).getItemid()),
                            counter[Integer.valueOf(filterList.get(position).getItemid())]);
                    Log.i("TAG","Equals" + position+counter[position] );
                    if (counter[Integer.valueOf(filterList.get(position).getItemid())] == 0)
                    {
                        db.deleteRow(filterList.get(position).getItemid());
                    }
                    remberCounts(position);

                    super.onClick();
                }
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void longPress()
                {
                    if (counter[Integer.valueOf(filterList.get(position).getItemid())] == 0)
                    {
                    }
                    else
                    {
                        counter[Integer.valueOf(filterList.get(position).getItemid())] -= 1;
                        finalHolder. quantity.setText(counter[Integer.valueOf(filterList.get(position).getItemid())]+"");
                    }
                    mValue--;
                    if(mValue<0)
                    {
                        mValue=0;
                    }
                    int value =Integer.valueOf(finalHolder. quantity.getText().toString());
                    int a = Integer.valueOf(filterList.get(position).getItemprice());
                    toatalAmount = toatalAmount - a;
                    if (toatalAmount < 0)
                    {
                        toatalAmount = 0;
                    }
                    filterList.get(position).setValue(counter[Integer.valueOf(filterList.get(position).getItemid())]);
                    int aa= Integer.valueOf( finalHolder. quantity.getText().toString());
                    int totala  =  aa *  Integer.valueOf(filterList.get(position).getItemprice());
                    update(filterList.get(position).getItemid(),counter[Integer.valueOf(filterList.get(position).getItemid())]+"",position,

                            Integer.valueOf(filterList.get(position).getItemid()), totala);
                    updatePosition(Integer.valueOf(filterList.get(position).getItemid()),counter[Integer.valueOf(filterList.get(position).getItemid())]);
                    if (counter[Integer.valueOf(filterList.get(position).getItemid())] == 0)
                    {

                        db.deleteRow(filterList.get(position).getItemid());

                    }
                    super.longPress();

                }
            });
            if (counters.size() != 0)
            {
                for (int i=0;i<counters.size();i++)
                {
                    Log.i("TAG","MyEquals" +position+counters.get(i).getPosition()+
                        counters.get(i).getCount());
                    if (counters.get(i).getCount() == Integer.valueOf(filterList.get(position).getItemid()) )
                    {
                        finalHolder.quantity.setText( counters.get(i).getPosition()+"");
                        counter[counters.get(i).getCount()]= counters.get(i).getPosition();
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                loadNames();
            }
//            finalHolder.quantity.setText(mycounts);
            int aa= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
            int totala  =  aa *  Integer.valueOf(filterList.get(position).getItemprice());
            // products.clear();
            products.add(new Products(filterList.get(position).getItemid(),"0",filterList.get(position).getGroupname(),
                    filterList.get(position).getItemprice(),String.valueOf(totala)));
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                }
            });
            for(int i=0;i<counter.length;i++)
            {
            }

            return row;
        }
        @Override
        public void onClick(View v) {
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filterList = itemLists;
                    } else {
                        ArrayList<ItemList> filteredLis = new ArrayList<>();
                        for (ItemList row : itemLists) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getItemname() .toLowerCase().contains(charString.toLowerCase())) {
                                filteredLis.add(row);
                            }
                        }
                        filterList = filteredLis;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filterList = (ArrayList<ItemList>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        class ViewHolder {
            TextView quantity, itemname, price;
            CardView item;
        }
        private   void price(int price)
        {
            if (toatalAmount<0)
            {
                toatalAmount = 0;
            }
            toatalAmount = toatalAmount+price;
            //   Homepage.totalprice.setText(String.valueOf(toatalAmount));
        }

        void updatePosition(int position,int counts)
        {
            int string;
            Cursor cursor  = myCounts.getId(position);
            if (cursor.moveToFirst())
            {
                do {
                    string = cursor.getInt(cursor.getColumnIndex(MyCounts.POSITION));
                    myCounts.updateDatas(position,counts);

                } while (cursor.moveToNext());
            }
            else
            {
                posToDatabase(position,counts);
            }

        }
        void posToDatabase(int pos,int counts)
        {
             myCounts.addDatas(pos,counts);
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void  update(String id, String count, int position,int  countPosition, int totala)
        {
            String string;
            Cursor cursor  = db.getId(id);

            if (cursor.moveToFirst())
            {
                do
               {
                    string = cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_ID));
                    db.update(count,id, String.valueOf(totala));
                    Log.i("TAG","Productss" + string + id );

                } while (cursor.moveToNext());
            }
            else
            {
                saveNameToLocalStorage(filterList.get(position).getItemid(),0,filterList.get(position).getGroupname(),
                        filterList.get(position).getItemname(),String.valueOf(totala),
                        filterList.get(position).getItemprice(),
                        counter[countPosition]+"");
            }


            loadNames();
        }


        private void increment()
        {
            mValue++;
            //  Homepage.  totalcount.setText( String.valueOf(mValue));
            //CanteenMain.cardbadge.setText(  String.valueOf(mValue));
        }
        public void saveNameToLocalStorage(String id, int status,String groupname,String itemName, String price,String  itemprice, String count)
        {
            db.addItems(id,status,groupname,itemName,price,itemprice,count);
            Name n = new Name(id,status,groupname,itemName,price,itemprice, count);
            names.add(n);

        }
        void editPop(boolean boo)
        {
            // Display display = context. getWindowManager().getDefaultDisplay();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View mView = inflater.inflate(R.layout.cart, null);
            editPop = new PopupWindow(
                    mView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editPop.setAnimationStyle(R.style.popupanimation);
            editPop.showAtLocation(mView, Gravity.BOTTOM|Gravity.LEFT, 0, 150);
            editPop.setFocusable(true);
            editPop.setOutsideTouchable(false);
        }
        class RptUpdater implements Runnable {
            public void run() {
                if( mAutoIncrement ){
                    increment();
                  //  repeatUpdateHandler.postDelayed( new com.video.aashi.canteen.fragments.ProductAdapter.RptUpdater(), REP_DELAY );
                } else if( mAutoDecrement ){
                    // decrement();
                 //   repeatUpdateHandler.postDelayed( new com.video.aashi.canteen.fragments.ProductAdapter.RptUpdater(), REP_DELAY );
                }
            }
        }

        private  void remberCounts(int position)
        {


        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void loadNames()
        {
            Log.i("Tag","MyLists"+names.size());
            Cursor cursor = db.getNames();
            myList.clear();
            myLists.clear();
            if (cursor.moveToFirst())
            {
                do {
                    String counts =  cursor.getString(cursor.getColumnIndex(databaseHelper.COUNT));
                    String countss =  cursor.getString(cursor.getColumnIndex(databaseHelper.PRICE));
                    addMember(Integer.valueOf(counts));
                    addMembers(Integer.valueOf(countss));
                    Log.i("Tag","MyList"+countss);
                }
                while (cursor.moveToNext());
            }
            String my = String.valueOf(myList.stream().mapToInt(value -> value).sum());
            String my1 = String.valueOf(myLists.stream().mapToInt(value -> value).sum());
            Log.i("TAG","Mycounts"+my);
            CanteenMain.cardbadge.setText(String.valueOf(my));
            Homepage.totalcount.setText(String.valueOf(my));
            Homepage.totalprice.setText(String.valueOf(my1));
            // listView.setAdapter(new Cart.NameAdapter(getActivity(),R.layout.names,names));
        }
        public void addMember(Integer x) {
            myList.add(x);
        };
        public void addMembers(Integer x) {
            myLists.add(x);
        };
    }
}