package com.video.aashi.canteen.Canteen.barscanner.piinview;

import com.video.aashi.canteen.postclass.StudentData;

public interface pinPresent {
    void onSuccess(String s);
    void showProgress();
    void hideProgress();
    void openActivity();
    void  showsStudent(StudentData studentData);

}
