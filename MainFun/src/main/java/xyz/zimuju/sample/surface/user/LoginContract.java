package xyz.zimuju.sample.surface.user;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import xyz.zimuju.common.rx.RxContract;

public class LoginContract extends RxContract<LoginView> implements LoginPresenter {
    @Override
    public void login(String... parameters) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(parameters[0]);
        bmobUser.setPassword(parameters[1]);
        bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    basalView.showToast("登陆成功");
                    basalView.loginResult(bmobUser);
                } else {
                    basalView.showToast("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getMessage());
                }
            }
        });
    }
}
