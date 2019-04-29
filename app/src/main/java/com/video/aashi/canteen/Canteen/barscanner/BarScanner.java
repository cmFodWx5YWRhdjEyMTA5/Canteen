package com.video.aashi.canteen.Canteen.barscanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.video.aashi.canteen.Canteen.barscanner.piinview.PinView;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.login.LoginActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarScanner extends BaseScannerActivity implements ZBarScannerView.ResultHandler  {
    private ZBarScannerView mScannerView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_scanner);
        setupToolbar();
        sharedPreferences = getApplicationContext().getSharedPreferences("Student",MODE_PRIVATE);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    @SuppressLint("CommitPrefEdits")
    @Override
    public void handleResult(Result rawResult)
    {

        Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

        ArrayList<String> items =
                new  ArrayList<String>(Arrays.asList( rawResult.getContents().split("[|]")));
        Log.i("MyResult",items.size() +items.toString());
        editor = sharedPreferences.edit();

        for (int i = 0;i< items.size();i++)
        {
            editor.putString("id",items.get(0).trim() );
            editor.putString("name",items.get(1).trim() );
            editor.putString("location",items.get(5).trim());
            editor.apply();
        }
        startActivity(new Intent(BarScanner.this,PinView.class));
        //   Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarScanner.this);
            }
        }, 2000);
    }
}
