package xyz.zimuju.sample.surface.gank;

import android.support.v4.app.Fragment;

/**
 * Created by _SOLID
 * Date:2016/5/17
 * Time:15:49
 */
public class SortActivity extends ToolbarActivity {
    @Override
    protected String getToolbarTitle() {
        return "排序";
    }

    @Override
    protected Fragment getFragment() {
        return new SortFragment();
    }
}