package com.video.aashi.canteen.Canteens.admin.reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.video.aashi.canteen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthReport extends Fragment {


    public MonthReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month_report, container, false);
    }

}
