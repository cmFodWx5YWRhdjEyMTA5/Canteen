package com.video.aashi.canteen.Canteens.admin.reports;
import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.video.aashi.canteen.R;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
public class DailyReport extends Fragment
{
     private int mYear, mMonth, mDay, mHour, mMinute;
     public DailyReport()
     {



     }
     EditText editText;
     DatePickerDialog datePickerDialog;
     @BindView(R.id.nextpage)
     TextView nextpage;
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
     {
        View    view = inflater.inflate(R.layout.fragment_daily_report, container, false);
        ButterKnife.bind(this,view);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.containers,new ViewReport()).
                        addToBackStack(null).commit();
            }
        });
        Spinner spinner = (Spinner)view.findViewById(R.id.spinners);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.reports, R.layout.simplespinner);
        // spinner  .setColorFilter(getResources().getColor(R.color.white1), PorterDuff.Mode.SRC_ATOP);
        adapter.setDropDownViewResource(R.layout.checkedtext);
        spinner.setAdapter(adapter);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        editText = (EditText)view. findViewById(R.id.search);
        editText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.i("Tag","Date"+ dayOfMonth+ String.valueOf(monthOfYear+1) +year);
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        return  view;
    }
}
