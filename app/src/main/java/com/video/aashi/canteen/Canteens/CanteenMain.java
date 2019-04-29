package com.video.aashi.canteen.Canteens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.canteen.Canteens.cart.Cart;
import com.video.aashi.canteen.Canteens.menu.MenuItems;
import com.video.aashi.canteen.MainActivity;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.account.BlankFragment;
import com.video.aashi.canteen.fragments.Homepage;
import com.video.aashi.canteen.fragments.ProductAdapter;
import com.video.aashi.canteen.login.LoginActivity;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CanteenMain extends AppCompatActivity
{

    public static   BottomNavigationView mBottomNav;
    @BindView(R.id.main_tools)
    Toolbar  toolbar;
    public  static  String check="0";
    public  static   TextView cardbadge;
    SharedPreferences sharedPreferences;
    public static String orgid,locationid,locationname,username;
    private static final String SELECTED_ITEM = "arg_selected_item";
    private  int SELECT = 0;
    private Boolean exit = false;
    private int mSelectedItem;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_main);
        ButterKnife.bind(this);

        mBottomNav =(BottomNavigationView)findViewById(R.id.navigations);
        disableShiftMode(mBottomNav);
        sharedPreferences = getApplicationContext().getSharedPreferences(LoginActivity.NAME_PREF, MODE_PRIVATE);

        orgid = sharedPreferences.getString("orgId","");
        locationid =sharedPreferences.getString("locId","");
        locationname = sharedPreferences.getString("locName","");
        username= sharedPreferences.getString("userName","");
        mBottomNav.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        toolbar = (Toolbar)findViewById(R.id.main_tools);
         // ((AppCompatActivity)getActivity()) . setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home Page");
        MenuItem selectedItem;
        //   selectedItem = mBottomNav.getMenu().findItem(R.id.action_home);
        Intent intent= getIntent();
        if (intent != null)
        {
          check =    intent.getStringExtra("check");
        }
        Log.i("TAG","Myvalues"+orgid+locationname+locationid+username);
        showfrag(new Homepage());
        SELECT = 1;
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        View views = mBottomNav.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) views;
        View cart_badge = LayoutInflater.from(CanteenMain.this)
                .inflate(R.layout.cartlayout,
                        mBottomNav, false);
        cardbadge =(TextView)cart_badge.findViewById(R.id.cart_badge);
        mBottomNav.addView(cart_badge);
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    public void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Homepage homepage= new Homepage();
                showfrag(homepage);
                SELECT = 1;
                updateToolbarText("Menu");
                break;
            case R.id.action_category:
                MenuItems menuItems=new MenuItems();
                showfrag(menuItems);
                break;
            case R.id.action_me:
                Cart cart= new Cart();
                showfrag(cart);
                SELECT =2;
                updateToolbarText("Cart");
                break;
            case R.id.action_setting:
                BlankFragment blankFragment=new BlankFragment();
                showfrag(blankFragment);
                SELECT =3;
                updateToolbarText("Account ");
                break;
        }

        mSelectedItem = item.getItemId();
        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }
    }

    public  void  showfrag(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.containers,fragment).commit();
    }

    private void updateToolbarText(CharSequence text) {
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
          toolbar.setTitle(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                showDialogue(CanteenMain.this);
                break;
            case R.id.help:
                break;
                default:
                    break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }
    private  void showDialogue(Context context)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor;
                        editor = sharedPreferences.edit();
                        editor.remove("isLoginKey");
                        editor.clear();
                        editor.apply();
                        Intent intent=new Intent(CanteenMain.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                      }
                   }
                )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
