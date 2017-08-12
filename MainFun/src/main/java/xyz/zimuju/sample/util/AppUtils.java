package xyz.zimuju.sample.util;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.component.SettingCenter;
import xyz.zimuju.sample.entity.bomb.FeedBack;

/*
 * @description AppUtils : App 相关的工具类
 * @author Nathaniel
 * @time 2017/8/5 - 13:11
 * @version 1.0.0
 */
public class AppUtils {

    public static boolean isFirstRun() {
        return SharedPreferencesUtils.getInstance().getBoolean("isFirstRun", true);
    }

    public static void setFirstRun(boolean isFirstRun) {
        SharedPreferencesUtils.getInstance().putBoolean("isFirstRun", isFirstRun);
    }

    public static boolean shakePicture() {
        return SharedPreferencesUtils.getInstance().getBoolean("shakePicture", true);
    }

    public static void setShakePicture(boolean isEnable) {
        SharedPreferencesUtils.getInstance().putBoolean("shakePicture", isEnable);
    }


    public static void feedBack(final Context context, final View view) {
        new MaterialDialog.Builder(context)
                .title(R.string.feedback_dialog_title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.feedback_input_hint, R.string.feedback_input_prefill, new MaterialDialog.InputCallback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        if (TextUtils.isEmpty(input)) {
                            return;
                        }

                        FeedBack feedBack = new FeedBack();
                        feedBack.setContent(input.toString());
                        feedBack.setAppVersion(SystemUtils.getAppVersion(context));
                        feedBack.setDeviceName(SystemUtils.getDeviceName());
                        feedBack.setSystemVersion(SystemUtils.getSystemVersion());
                        feedBack.setUsername(AuthorityUtils.getUserName());
                        feedBack.save(new SaveListener() {
                            @Override
                            public void done(Object o, BmobException e) {
                                if (e == null) {
                                    SnackBarUtils.makeShort(view, context.getResources().getString(R.string.feedback_success)).success();
                                } else {
                                    SnackBarUtils.makeShort(view, context.getResources().getString(R.string.feedback_failed)).danger();
                                }
                            }
                        });

                    }
                }).show();
    }

    public static void logout(final Context context, final OnSuccessListener listener) {
        if (!AuthorityUtils.isLogin()) return;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title("提示")
                .content("确定注销吗？")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        AuthorityUtils.logout();
                        listener.onSuccess();
                    }
                }).negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public static void clearCache(Context context, final SettingCenter.ClearCacheListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title("温馨提示")
                .content("确定清空缓存吗？如果你使用的是移动网络不建议清空缓存哦")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        SettingCenter.clearAppCache(listener);
                    }
                }).negativeText("取消");

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public interface OnSuccessListener {
        void onSuccess();
    }
}
