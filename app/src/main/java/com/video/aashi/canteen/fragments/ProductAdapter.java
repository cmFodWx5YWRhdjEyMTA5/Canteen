package com.video.aashi.canteen.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.Canteens.cart.Cart;
import com.video.aashi.canteen.Canteens.localDB.databaseHelper;
import com.video.aashi.canteen.Canteens.others.OnSwipeTouchListener;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.arrays.Counters;
import com.video.aashi.canteen.arrays.ItemList;
import com.video.aashi.canteen.arrays.Products;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.video.aashi.canteen.fragments.Homepage.REP_DELAY;


public class ProductAdapter extends BaseAdapter implements
        View.OnClickListener,AdapterView.OnItemClickListener {


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
    public ProductAdapter(Context cotext, List<ItemList> arrayList, GridView gridview)
    {
        this.context = cotext;
        this.itemLists = arrayList;
        this.gridview = gridview;
    }

    @Override
    public int getCount()
    {
        return itemLists.size();
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
       String name = itemLists.get(position).getItemname();
       String itemnames = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
       holder.itemname.setText(itemnames);
       holder.price.setText(itemLists  .get(position).getItemprice());
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
               int total  =  a *  Integer.valueOf(itemLists.get(position).getItemprice());
               mValue = mValue -Integer.valueOf( finalHolder. quantity.getText().toString());
               //  Homepage. totalcount.setText( String.valueOf(mValue));
               //  CanteenMain.cardbadge.setText(String.valueOf(mValue));
               counter[position] = 0;
               finalHolder. quantity.setText(counter[position]+"");
               toatalAmount = toatalAmount  - total;
               // Homepage.totalprice.setText(String.valueOf(toatalAmount));
               itemLists.get(position).setValue(counter[position]);
               Toast.makeText(context,String.valueOf( a  +   total )       ,Toast.LENGTH_LONG).show();
               super.onSwipeRight();
           }
           @Override
           public void onSwipeLeft()
           {
               mValue = mValue -Integer.valueOf( finalHolder. quantity.getText().toString());
               // Homepage. totalcount.setText( String.valueOf(mValue));
               // CanteenMain.cardbadge.setText(String.valueOf(mValue));
               counter[position] = 0;
               finalHolder. quantity.setText(counter[position]+"");
               Toast.makeText(context, counter[position],Toast.LENGTH_LONG).show();
               super.onSwipeLeft();
           }
           @Override
       public void onClick() {
               counter[position] += 1;
               finalHolder. quantity.setText(counter[position]+"");
               int a= Integer.valueOf( finalHolder. quantity.getText().toString());
               int total;
               total = Integer.valueOf(itemLists.get(position).getItemprice());
               price(total);
               itemLists.get(position).setValue(counter[position]);
               int aa= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
               int totala  =  aa *  Integer.valueOf(itemLists.get(position).getItemprice());
               //   products.clear();
          //     saveNameToLocalStorage(itemLists.get(position).getItemid(),0,itemLists.get(position).getGroupname(),
                   //    itemLists.get(position).getItemname(),String.valueOf(totala),counter[position]+"");
               ids.add(itemLists.get(position).getItemid());
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   update(itemLists.get(position).getItemid(),counter[position]+"",position,totala);
               }
               increment();

            super.onClick();
       }
       @RequiresApi(api = Build.VERSION_CODES.N)
       @Override
       public void longPress()
       {
           if (counter[position] == 0)
           {
           }
           else
           {
               counter[position] -= 1;
               finalHolder. quantity.setText(counter[position]+"");
           }
           mValue--;
           if(mValue<0)
           {
               mValue=0;
           }

           int value =Integer.valueOf(finalHolder. quantity.getText().toString());
               int a = Integer.valueOf(itemLists.get(position).getItemprice());
               toatalAmount = toatalAmount - a;
               if (toatalAmount < 0)
               {
                  toatalAmount = 0;
               }
               itemLists.get(position).setValue(counter[position]);
              // Homepage.totalprice.setText(String.valueOf(toatalAmount));
           //    CanteenMain.cardbadge.setText(String.valueOf(mValue));
               int aa= Integer.valueOf( finalHolder. quantity.getText().toString());
               int totala  =  aa *  Integer.valueOf(itemLists.get(position).getItemprice());
               update(itemLists.get(position).getItemid(),counter[position]+"",position,totala);
               super.longPress();
           }
        });

         int aa= Integer.valueOf( finalHolder. quantity.getText().toString())  ;
        int totala  =  aa *  Integer.valueOf(itemLists.get(position).getItemprice());
       // products.clear();
        products.add(new Products(itemLists.get(position).getItemid(),"0",itemLists.get(position).getGroupname(),
        itemLists.get(position).getItemprice(),String.valueOf(totala)));
     holder.item.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v)
         {
         }
      });
      return row;
    }
    @Override
    public void onClick(View v) {
    }
    @Override
     public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
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
     @RequiresApi(api = Build.VERSION_CODES.N)
     private void  update(String id, String count, int position, int totala)
    {
        String string;
        Cursor cursor  = db.getId(id);

        if (cursor.moveToFirst())
        {
            do {
                  string = cursor.getString(cursor.getColumnIndex(databaseHelper.ITEM_ID));
                  db.update(count,id, String.valueOf(totala));
                   Log.i("TAG","Productss" + string + id );

            } while (cursor.moveToNext());
        }
        else
        {
            saveNameToLocalStorage(itemLists.get(position).getItemid(),0,itemLists.get(position).getGroupname(),
                    itemLists.get(position).getItemname(),String.valueOf(totala),
                    itemLists.get(position).getItemprice(),
                    counter[position]+"");
        }


        loadNames();
    }
    private void increment()
    {
        mValue++;
      //  Homepage.  totalcount.setText( String.valueOf(mValue));
        //CanteenMain.cardbadge.setText(  String.valueOf(mValue));
    }
     public void saveNameToLocalStorage(String id, int status,String groupname,String itemName,
                                        String price,String  itemprice, String count)
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
                repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
            } else if( mAutoDecrement ){
               // decrement();
                repeatUpdateHandler.postDelayed( new RptUpdater(), REP_DELAY );
            }
        }
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
    public static void addCart()
    {
        Log.i("TAG","TotalCart"+products.size());
        for (int i=0;i<products.size();i++)
        {
            if ( Integer.valueOf(products.get(i).getQuantuty()) != 0)
            {
                Log.i("TAG","TotalCarts"+products.get(i).getItemname());
            }
        }
    }
}
