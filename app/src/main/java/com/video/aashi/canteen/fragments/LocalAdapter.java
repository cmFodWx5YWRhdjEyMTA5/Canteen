package com.video.aashi.canteen.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.video.aashi.canteen.Canteens.cart.Cart;
import com.video.aashi.canteen.Canteens.localDB.databaseHelper;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.arrays.Counters;
import com.video.aashi.canteen.arrays.ItemList;
import com.video.aashi.canteen.arrays.Products;

import java.util.ArrayList;
import java.util.List;

public class LocalAdapter  extends BaseAdapter implements View.OnClickListener,AdapterView.OnItemClickListener
{
    public static PopupWindow editPop;
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    static List<Products> products =new ArrayList<>();
    public int mValue = 0;
    public int itemCount= 0;
    GridView gridview;
    Context context;
    int toatalAmount;
    private List<Name> names = new ArrayList<>();
    private databaseHelper db;
    boolean  check= false;
    public  static String quantity;

    View view;
    private Handler repeatUpdateHandler = new Handler();
    List<ItemList> itemLists ;
    List<Counters> counters= new ArrayList<>();
    private int[] counter = new int[10];
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    ArrayList<String> ids=new ArrayList<>();
    List<Integer> myList = new ArrayList<Integer>();
    List<Integer> myLists = new ArrayList<Integer>();
    int[] counts;
    public LocalAdapter(Context cotext, List<ItemList> arrayList, GridView gridview)
    {
        this.context = cotext;
        this.itemLists = arrayList;
        this.gridview = gridview;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolders holder = null;
        if (row == null) {
            LayoutInflater inflater =  LayoutInflater.from(context);
            row = inflater.inflate(R.layout.itemdesign, parent, false);
            holder = new ViewHolders();
            holder.   quantity = (TextView)row.findViewById(R.id.quantity);
            holder.itemname =(TextView)row.findViewById(R.id.itemName);
            holder.price =(TextView)row.findViewById(R.id.itemPrice);
            holder.item=(CardView)row.findViewById(R.id.bread);
            db = new databaseHelper(context);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolders) row.getTag();
        }
        String name = itemLists.get(position).getItemname();
        String itemnames = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();


        return row ;
    }
    class ViewHolders {
        TextView quantity, itemname, price;
        CardView item;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
