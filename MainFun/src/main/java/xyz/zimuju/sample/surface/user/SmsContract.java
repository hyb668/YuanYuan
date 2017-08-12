package xyz.zimuju.sample.surface.user;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.bean.BmobSmsState;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.QuerySMSStateListener;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import xyz.zimuju.common.rx.RxContract;
import xyz.zimuju.sample.R;

/*
 * @description SmsContract
 * @author Nathaniel
 * @email nathanwriting@126.com
 * @time 2017/8/10 - 1:15
 * @version 1.0.0
 */
public class SmsContract extends RxContract<RegisterView> implements SmsPresenter {
    @Override
    public void obtain(String phone) {
        // 请求发送验证码
        BmobSMS.requestSMSCode(context, phone, context.getString(R.string.common_sms_name), new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {
                    // 验证码发送成功
                    basalView.obtainResult(smsId);
                } else {
                    basalView.showToast("短信发送失败，原因：" + ex.getMessage());
                }
            }
        });
    }

    @Override
    public void querySmsState(Integer smsId) {
        /*
         * 注：SmsState包含两种状态：
         * 1、smsState（短信状态） :SUCCESS（发送成功）、FAIL（发送失败）、SENDING(发送中)。
         * 2、verifyState（验证状态）:true(已验证)、false(未验证)。
         */
        BmobSMS.querySmsState(context, smsId, new QuerySMSStateListener() {

            @Override
            public void done(BmobSmsState bmobSmsState, BmobException e) {
                if (e == null) {
                    switch (bmobSmsState.getSmsState()) {
                        case "SUCCESS":
                            basalView.showToast("验证码发送成功");
                            break;

                        case "FAIL":
                            basalView.showToast("验证码发送失败");
                            break;

                        case "SENDING":
                            basalView.showToast("验证码发送中");
                            break;
                    }

                    switch (bmobSmsState.getVerifyState()) {
                        case "true":
                            basalView.showToast("验证码已验证");
                            break;

                        case "false":
                            basalView.showToast("验证码未验证");
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void verifySmsCode(String phone, String code) {
        BmobSMS.verifySmsCode(context, phone, code, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                if (ex == null) {
                    // 短信验证码已验证成功
                    basalView.verifyCodeResult(true);
                } else {
                    basalView.verifyCodeResult(false);
                    basalView.showToast("验证失败，原因：" + ex.getMessage());
                }
            }
        });
    }

}
