package com.video.aashi.canteen.account;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.aashi.canteen.Canteen.barscanner.piinview.pinPresent;
import com.video.aashi.canteen.Canteen.barscanner.piinview.pinValidate;
import com.video.aashi.canteen.Canteens.CanteenMain;
import com.video.aashi.canteen.R;
import com.video.aashi.canteen.fragments.Homepage;
import com.video.aashi.canteen.postclass.StudentData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class BlankFragment extends Fragment implements pinPresent {


    SharedPreferences sharedPreferences,sharedPreferencess;
    String mypin;
    ProgressDialog progressDialog;
    pinValidate pinValidate;

    pinValidate pinValidates;

    @BindView(R.id.studentName)
    TextView studentName;
    @BindView(R.id.className)
    TextView className;
    @BindView(R.id.walletAmount)
    TextView wallet;
    String id,name,location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this,view);
        sharedPreferences = getActivity().getSharedPreferences("PIN",MODE_PRIVATE);
        sharedPreferencess = getActivity().getSharedPreferences("Student",MODE_PRIVATE);
        mypin = sharedPreferences.getString("mypin","");
        pinValidate = new pinValidate(BlankFragment.this);
        progressDialog = new ProgressDialog(getActivity());
        id = sharedPreferencess.getString("id","");
        name = sharedPreferencess.getString("name","");
        location = sharedPreferencess.getString("location","");
        pinValidate.validatePin(mypin,location,id,name);
        Log.i("TAG","Mypin"+mypin);
        getActivity().setTitle("Account");
        return view;
    }
    @Override
    public void onSuccess(String s)
    {
    }
    @Override
    public void showProgress() {
       progressDialog.setMessage("Loading..");
       progressDialog.show();
    }
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void openActivity() {
    }
    @Override
    public void showsStudent(StudentData studentData) {
      studentName.setText(studentData.getStudentname());
      className.setText(studentData.getClassNam());
      wallet.setText(studentData.getWalletAmount());
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
                    CanteenMain.mBottomNav.setSelectedItemId(R.id.action_home);
                    return true;
                }
                return false;
            }
        });
    }
    public  void  showfrag(Fragment fragment)
    {
        getActivity(). getSupportFragmentManager().beginTransaction().replace(R.id.containers,fragment).commit();
    }

}
