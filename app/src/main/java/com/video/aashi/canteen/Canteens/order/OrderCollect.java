package com.video.aashi.canteen.Canteens.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.video.aashi.canteen.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderCollect extends AppCompatActivity {

    @BindView(R.id.tool_collect)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_collect);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Palanisamy (Xl-B)");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
