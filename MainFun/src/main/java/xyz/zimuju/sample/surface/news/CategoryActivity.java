package xyz.zimuju.sample.surface.news;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.surface.gank.ToolbarActivity;

public class CategoryActivity extends ToolbarActivity {

    public static void start(Context context, String type) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return getIntent().getExtras().getString("type");
    }

    @Override
    protected Fragment getFragment() {
        CategoryListFragment fragment = CategoryListFragment.newInstance(getIntent().getExtras().getString("type"));
        fragment.setUserVisibleHint(true);
        return fragment;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void viewOption() {

    }
}
