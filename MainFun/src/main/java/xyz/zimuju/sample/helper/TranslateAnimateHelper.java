package xyz.zimuju.sample.helper;

import android.view.View;
import android.view.animation.TranslateAnimation;

import xyz.zimuju.sample.behavior.AnimateHelper;

public class TranslateAnimateHelper implements AnimateHelper {
    public static int MODE_TITLE = 233;
    public static int MODE_BOTTOM = 2333;
    public View mTarget;
    public float mStartY;
    public int mCurrentState = STATE_SHOW;
    public int mMode = MODE_TITLE;

    private TranslateAnimateHelper(View view) {
        mTarget = view;
    }

    public static TranslateAnimateHelper get(View target) {
        return new TranslateAnimateHelper(target);
    }

    public void show() {
        if (mMode == MODE_TITLE) {
            showTitle();
        } else if (mMode == MODE_BOTTOM) {
            showBottom();
        }
    }


    private void hideTitle() {
        mTarget.setY(-mTarget.getMeasuredHeight());
        TranslateAnimation ta = new TranslateAnimation(0f, 0f, mTarget.getMeasuredHeight(), 0f);
        ta.setDuration(300);
        mTarget.startAnimation(ta);

        mCurrentState = STATE_HIDE;


    }

    private void showTitle() {

        mTarget.setY(mStartY);
        TranslateAnimation ta = new TranslateAnimation(0f, 0f, -mTarget.getMeasuredHeight(), 0);
        ta.setDuration(300);
        mTarget.startAnimation(ta);
        mCurrentState = STATE_SHOW;

    }

    public void hide() {
        if (mMode == MODE_TITLE) {
            hideTitle();
        } else if (mMode == MODE_BOTTOM) {
            hideBottom();
        }
    }

    private void showBottom() {
        mTarget.setY(mStartY);
        TranslateAnimation ta = new TranslateAnimation(0f, 0f, mTarget.getMeasuredHeight(), 0);
        ta.setDuration(300);
        mTarget.startAnimation(ta);
        mCurrentState = STATE_SHOW;

    }

    private void hideBottom() {

        mTarget.setY(mStartY + mTarget.getMeasuredHeight());

        TranslateAnimation ta = new TranslateAnimation(0f, 0f, -mTarget.getMeasuredHeight(), 0);

        mTarget.startAnimation(ta);
        ta.setDuration(300);
        mCurrentState = STATE_HIDE;
    }


    public void setStartY(float y) {
        mStartY = y;
    }

    public int getState() {
        return mCurrentState;
    }

    private void setState(int state) {
        mCurrentState = state;
    }

    public void setMode(int mode) {
        mMode = mode;
    }
}
