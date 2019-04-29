package com.video.aashi.canteen.login;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.video.aashi.canteen.Canteen.barscanner.BarScanner;
import com.video.aashi.canteen.Canteen.barscanner.piinview.PinView;
import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.MainActivity;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.Canteens.admin.AdminMainpage;
import com.video.aashi.canteen.Canteens.order.OrderCollect;
import com.video.aashi.canteen.login.Login.ILogin;
import com.video.aashi.canteen.login.Login.Login;
import com.video.aashi.canteen.postclass.Pin;
import com.video.aashi.canteen.postclass.PutVariable;

public class LoginActivity extends AppCompatActivity implements ILogin
{
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    EditText mPasswordView,email;
    Login logins;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public  static final   String NAME_PREF = "pinValidate";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
          //  ComponentName componentToDisable =
          //            new ComponentName("com.video.aashi.canteen",
          //                "com.video.aashi.canteen.login.LoginActivity");
          //  getPackageManager().setComponentEnabledSetting(
          //          componentToDisable,
          //           PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
          //      PackageManager.DONT_KILL_APP);
        sharedPreferences = getApplicationContext().getSharedPreferences(NAME_PREF, MODE_PRIVATE);
        editor =  sharedPreferences.edit();
        logins = new Login(LoginActivity.this );
        email =(EditText)findViewById(R.id.username1);
        mPasswordView = (EditText) findViewById(R.id.password1);
        progressDialog = new ProgressDialog(LoginActivity.this);
        if (getSharedPreferences(NAME_PREF,0).getBoolean("isLogin",false)) {
         Intent i = new Intent(LoginActivity.this, BarScanner.class);
                // Intent i = new Intent(LoginActivity.this, CanteenMain.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });
        CardView mEmailSignInButton = (CardView) findViewById(R.id.signin);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                    logins.performLogin(email.getText().toString(),mPasswordView.getText().toString());
            /*    else if (email.getText().toString().contains("2"))



                else if (email.getText().toString().contains("3"))
                {
                    Intent intent = new Intent(LoginActivity.this, OrderCollect.class);
                    startActivity(intent);
                }
                else if (email.getText().toString().contains("4"))
                {
                    Intent intent = new Intent(LoginActivity.this, PinView.class);
                    startActivity(intent);
                }
                else if (email.getText().toString().contains("admin"))
                {
                    Intent intent = new Intent(LoginActivity.this, AdminMainpage.class);
                    startActivity(intent);
                }
                */
            }
        }
      );
    }
    @Override
    public void LoginError(String e) {

        Toast.makeText(getApplicationContext(),e,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void LoginSuccess(String e) {
        Toast.makeText(getApplicationContext(),e,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void openMainActivity()
    {

        launchActivity(BarScanner.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }
    @Override
    public void showProgress() {
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
    }
    @Override
    public void dismissProgress() {
        progressDialog.dismiss();

    }

    @Override
    public void putSharedValiables(PutVariable putVariable) {
        editor.putString("orgId",putVariable.getOrgid());
        editor.putString("locId",putVariable.getLocationid());
        editor.putString("locName",putVariable.getLocationname());
        editor.putString("userName",putVariable.getUsername());
        editor.apply();

    }
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            editor.putBoolean("isLogin",true);
            editor.apply();
            //  startActivity(intent);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
