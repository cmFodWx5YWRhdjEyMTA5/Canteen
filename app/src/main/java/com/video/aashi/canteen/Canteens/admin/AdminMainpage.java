package com.video.aashi.canteen.Canteens.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.Canteens.admin.reports.DailyReport;
import com.video.aashi.canteen.Canteens.admin.reports.ItemReport;
import com.video.aashi.canteen.Canteens.admin.reports.Mainpage.ReportMainpage;
import com.video.aashi.canteen.Canteens.admin.reports.MonthlyReport;
import com.video.aashi.canteen.Canteens.others.AdminNavigation;
import com.video.aashi.canteen.Canteens.others.NavigationAdapter;
import com.video.aashi.canteen.MainActivity;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.account.BlankFragment;
import com.video.aashi.canteen.login.LoginActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
public class AdminMainpage extends AppCompatActivity {
    @BindView(R.id.main_tools)
    Toolbar toolbar;
    private static final String SELECTED_ITEM = "arg_selected_item";
    @BindView(R.id.navigations)
    BottomNavigationView mBottomNav;
    private int mSelectedItem;
    @BindView(R.id.admin_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.admin_drawer)
    DrawerLayout drawerLayout;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ActionBarDrawerToggle drawerToggle;
    AdminNavigation navigationAdapter;
    private static final int POS_orders = 0;
    private static final int POS_history = 1;
    private static final int POS_items = 2;
    private static final int POS_report = 3;
    private static final int POS_exit = 4;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mainpage);
        ButterKnife.bind(this);
        //  Toolbar toolbar=(Toolbar)findViewById(R.id.main_tool);
        setSupportActionBar(toolbar);
        MenuItem selectedItem;
        disableShiftMode(mBottomNav);
        getSupportActionBar().setTitle("Reports");
        mBottomNav.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        selectedItem = mBottomNav.getMenu().findItem(R.id.orderlist);
        selectFragment(selectedItem);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final GestureDetector mGestureDetector = new GestureDetector(
                AdminMainpage.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });
        String[] strings = getResources().getStringArray(R.array.admin_items);
        for (String string : strings)
        {
            stringArrayList.add(string);
        }

       //   TypedArray imgs = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        navigationAdapter = new AdminNavigation(stringArrayList, AdminMainpage.this);
        recyclerView.setAdapter(navigationAdapter);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    drawerLayout.closeDrawers();
                    // Toast.makeText(getApplicationContext(), "The Item Clicked is: " +
                    // recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    position = recyclerView.getChildPosition(child);
                    if (position == POS_orders) {
                        Orders canteenMain = new Orders();
                        showfrag(canteenMain);
                    }
                    if (position == POS_history) {
                       Completed completed= new Completed();
                       showfrag(completed);

                    }
                    if (position == POS_items) {
                        MonthlyReport monthlyReport=new MonthlyReport();
                        showfrag(monthlyReport);
                    }
                    if (position == POS_report) {
                        ReportMainpage mainpage = new ReportMainpage();
                        showfrag(mainpage);
                        updateToolbarText("Report");
                    }
                    if (position == POS_exit) {
                        ItemReport itemReport=new ItemReport();
                        showfrag(itemReport);
                    }
                    return true;
                }
                return false;
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent)
            {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {
            }
        });
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        }
        else
        {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return true;
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try
        {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                item.setChecked(item.getItemData().isChecked());

            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }
    private void selectFragment(MenuItem item) {

        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.orderlist:
                Orders homepage= new Orders();
                showfrag(homepage);
                updateToolbarText("Orders");
                break;
            case R.id.completed:
                Completed cart= new Completed();
                showfrag(cart);
                updateToolbarText("Completed");
                break;
            case R.id.accounts:
                BlankFragment blankFragment=new BlankFragment();
                showfrag(blankFragment);
                updateToolbarText("Accounts");
                break;
        }
        // update selected item
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }
    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
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
                        Intent intent=new Intent(AdminMainpage.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
