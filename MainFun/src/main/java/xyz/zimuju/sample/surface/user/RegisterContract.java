package xyz.zimuju.sample.surface.user;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/*
 * @description RegisterContract
 * @author Nathaniel
 * @email nathanwriting@126.com
 * @time 2017/8/6 - 12:05
 * @version 1.0.0
 */
public class RegisterContract extends SmsContract implements RegisterPresenter {


    @Override
    public void register(final String... parameters) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(parameters[0]);
        bmobUser.setPassword(parameters[1]);
        bmobUser.setMobilePhoneNumber(parameters[2]);
        bmobUser.setMobilePhoneNumberVerified(true);
        bmobUser.signUp(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    basalView.showToast("注册成功");
                    basalView.registerResult(parameters);
                } else {
                    basalView.showToast("注册失败，原因：" + e.getMessage());
                }
            }
        });
    }

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
                    basalView.showToast("登录失败，原因：" + e.getMessage());
                }
            }
        });
    }
}
