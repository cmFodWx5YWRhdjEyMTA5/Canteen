package com.video.aashi.canteen.login.Login;

import com.video.aashi.canteen.postclass.PutVariable;

public interface ILogin {

    void LoginError(String e);
    void LoginSuccess(String e);
    void openMainActivity();
    void showProgress();
    void dismissProgress();
    void putSharedValiables(PutVariable putVariable);
}
