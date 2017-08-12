package xyz.zimuju.sample.surface.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import xyz.zimuju.common.base.BaseViewBottomWindow;
import xyz.zimuju.common.entity.Entry;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.surface.content.UserActivity;

public class BottomWindow extends BaseViewBottomWindow<Entry<String, String>, DemoView> implements OnClickListener {
    private static final String TAG = "DemoBottomWindow";

    public static Intent createIntent(Context context, String title) {
        return new Intent(context, BottomWindow.class).putExtra(INTENT_TITLE, title);
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initEvent();

    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initData() {
        super.initData();

        data = new Entry<String, String>("Activity", TAG);
        data.setId(1);

        containerView.bindView(data);
    }

    @Override
    public String getTitleName() {
        return "Demo";
    }

    @Override
    public String getReturnName() {
        return null;
    }

    @Override
    public String getForwardName() {
        return null;
    }

    @Override
    protected DemoView createView() {
        return new DemoView(context, getResources());
    }

    @Override
    protected void setResult() {
        setResult(RESULT_OK, new Intent().putExtra(RESULT_DATA, TAG + " saved"));
    }

    @Override
    public void initEvent() {
        super.initEvent();

        containerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDemoViewHead:
                if (data != null) {
                    toActivity(UserActivity.createIntent(context, data.getId()));
                }
                break;
            default:
                break;
        }
    }
}