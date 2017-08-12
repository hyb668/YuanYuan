package xyz.zimuju.common.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 倒计时工具类
 */
public class TimeCountUtils extends CountDownTimer {
    private ChangeListener changeListener;
    private boolean clickable = true;
    private TextView textView;

    public TimeCountUtils(Context context, long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    public TimeCountUtils(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    public void setChangeListener(ChangeListener changeListener, boolean clickable) {
        this.changeListener = changeListener;
        this.clickable = clickable;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setClickable(false);
        textView.setText(millisUntilFinished / 1000 + "S");
    }

    @Override
    public void onFinish() {
        if (clickable) {
            textView.setText("重新发送");
            textView.setClickable(true);
        } else {
            textView.setText("00");
            textView.setClickable(false);
            changeListener.finishListener();
        }
    }

    public void stop() {
        super.cancel();
        textView.setText("获取验证码");
        textView.setClickable(true);
    }

    public void close() {
        super.cancel();
        textView.setText("00");
        textView.setClickable(false);
    }

    public interface ChangeListener {
        void finishListener();
    }
}
