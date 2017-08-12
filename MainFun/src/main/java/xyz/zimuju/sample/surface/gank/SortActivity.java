package xyz.zimuju.sample.surface.gank;

import android.support.v4.app.Fragment;

import xyz.zimuju.common.basal.BasalPresenter;

public class SortActivity extends ToolbarActivity {
    @Override
    protected String getToolbarTitle() {
        return "排序";
    }

    @Override
    protected Fragment getFragment() {
        return new SortFragment();
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void viewOption() {

    }
}
