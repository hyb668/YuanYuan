package xyz.zimuju.sample.surface.gank;

import android.os.Bundle;
import android.widget.TextView;

import xyz.zimuju.common.basal.BasalFragment;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;

public class StringFragment extends BasalFragment {
    private String mText;
    private TextView mTvText;

    public static StringFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString("text", text);
        StringFragment fragment = new StringFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gank_fragment_string;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }


    @Override
    protected void initData() {
        if (!mText.equals("")) {
            mTvText.setText(mText);
        } else {
            mTvText.setText("暂无信息");
        }
    }

    @Override
    protected void viewOption() {

    }

    @Override
    public void refreshData() {
        mText = getArguments().getString("text");
        mTvText = (TextView) getRootView().findViewById(R.id.tv_text);
    }
}
