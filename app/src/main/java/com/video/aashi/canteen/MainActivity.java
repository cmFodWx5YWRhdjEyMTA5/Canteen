package com.video.aashi.canteen;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.video.aashi.canteen.Canteens.FavPage;
import com.video.aashi.canteen.Canteens.others.NavigationAdapter;
import com.video.aashi.canteen.login.LoginActivity;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    public static Toolbar toolbar;
    @BindView(R.id.navi_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.mydrawer)
    DrawerLayout drawerLayout;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ActionBarDrawerToggle drawerToggle;
    NavigationAdapter navigationAdapter;
    private static final int POS_CANTEEN = 0;
    private static final int POS_ATTENDANCE = 1;
    private static final int POS_LIBRARY = 2;
    private static final int POS_EXIT = 3;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

          Toolbar toolbar=(Toolbar)findViewById(R.id.main_tool);
        setSupportActionBar(toolbar);
               layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getSupportActionBar().setTitle("Shanthi school");
        final GestureDetector mGestureDetector = new GestureDetector(
                MainActivity.this, new GestureDetector.SimpleOnGestureListener()
        {
            @Override public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });
        String[] strings = getResources().getStringArray(R.array.ld_activityScreenTitles);
        for (String string : strings)
        {
            stringArrayList.add(string);
        }
        TypedArray imgs = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
            navigationAdapter = new NavigationAdapter(stringArrayList,imgs,MainActivity.this);
        recyclerView.setAdapter(navigationAdapter);
        drawerToggle =  new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)) {
                    drawerLayout.closeDrawers();
                    // Toast.makeText(getApplicationContext(), "The Item Clicked is: " +
                    // recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    position = recyclerView.getChildPosition(child);
                    if (position == POS_CANTEEN)
                    {

                    }
                    if (position == POS_LIBRARY)
                    {
                        FavPage favPage= new FavPage();
                        showfrag(favPage);

                    }
                    if(position == POS_ATTENDANCE) {
                        Toast.makeText(getApplicationContext(), "Canteen",Toast.LENGTH_SHORT).show();
                    }
                    if (position == POS_EXIT)
                    {

                        Toast.makeText(getApplicationContext(), "Exit",Toast.LENGTH_SHORT).show();
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
            public void onRequestDisallowInterceptTouchEvent(boolean b)
            {

            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout)
        {
            showDialogue(MainActivity.this);
        }
        else if (item.getItemId() == R.id.help)
        {
            Toast.makeText(MainActivity.this,"We can help you!!",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
    public  void  showfrag(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }
    public void updateToolbarText(CharSequence text) {
        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
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
                       // editor = sharedPreferences.edit();
                       // editor.remove("isLoginKey");
                       // editor.clear();
                      //  editor.apply();
                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
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
