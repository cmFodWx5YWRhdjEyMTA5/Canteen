package com.video.aashi.canteen.Canteen.barscanner.piinview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.Canteens.others.PinEntryEditText;
import com.video.aashi.canteen.postclass.StudentData;

public class PinView extends AppCompatActivity implements  pinPresent {

    ProgressDialog progressDialog;
    pinValidate pinValidate;
    SharedPreferences sharedPreferences,sharedPreferencess;

    SharedPreferences studentInformation;
    SharedPreferences.Editor editor,getEditor;

    String id,name,location;
     PinEntryEditText txtPinEntry;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_view);
        txtPinEntry =
                (PinEntryEditText) findViewById(R.id.pinview );
        txtPinEntry.setFocusable(true);
        txtPinEntry.requestFocus();
        progressDialog = new ProgressDialog(PinView.this);
        pinValidate = new pinValidate(PinView.this);
        sharedPreferences = getApplicationContext().getSharedPreferences("PIN",MODE_PRIVATE);
        sharedPreferencess = getApplicationContext().getSharedPreferences("Student",MODE_PRIVATE);
        studentInformation = getApplicationContext().getSharedPreferences("Info",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        getEditor = studentInformation.edit();
        id = sharedPreferencess.getString("id","");
        name = sharedPreferencess.getString("name","");
        location = sharedPreferencess.getString("location","");
        Log.i("TAG","studentData"+id+name+location);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(txtPinEntry, InputMethodManager.SHOW_IMPLICIT);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        txtPinEntry.setFocusable(true);
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {}

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 4)
                 {
                     if(pinValidate.isNetworkAvailable(PinView.this))
                     {

                         pinValidate.validatePin(s.toString(),location,id,name);
                         editor.putString("mypin",s.toString());
                         editor.apply();
                         txtPinEntry.setText(null);
                         InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                         inputMethodManager.showSoftInput(txtPinEntry, InputMethodManager.SHOW_IMPLICIT);
                         inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                         txtPinEntry.setFocusable(false);
                     }
                     else
                     {
                         Toast.makeText(getApplicationContext(),"Check connection..!!",Toast.LENGTH_SHORT).show();
                     }

                 }

             }
        });
    }    @Override
    public void onSuccess(String s) {
        if (s.contains("Enter valid pin...!"))
        {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(txtPinEntry, InputMethodManager.SHOW_IMPLICIT);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            txtPinEntry.setFocusable(true);
        }
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showProgress() {
        progressDialog.setMessage("Validating....!");
       progressDialog.show();
    }
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void openActivity() {
     //   startActivity(new Intent(PinView.this,CanteenMain.class));
        Intent intent= new Intent(PinView.this,CanteenMain.class);
        intent.putExtra("check","1");
        startActivity(intent);

    }

    @Override
    public void showsStudent(StudentData studentData) {
        getEditor.putString("orgId",studentData.getOrgId());
        getEditor.putString("cantenId",studentData.getBillingId());
        getEditor.putString("orderId",studentData.getOrderId());
        getEditor.apply();
        Log.d("TAG", "Editorss" + " 1 :" + studentData.getOrgId()+ " 2 :"+ studentData.getBillingId()+
                " 3 : "  + studentData.getOrderId() );


        Toast.makeText(getApplicationContext(),studentData.getOrgId()+ studentData.getOrderId() ,Toast.LENGTH_SHORT).show();
    }
}
