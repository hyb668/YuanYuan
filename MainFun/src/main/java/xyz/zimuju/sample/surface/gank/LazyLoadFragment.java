package xyz.zimuju.sample.surface.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import xyz.zimuju.common.basal.BasalFragment;

/*
 * @description LazyLoadFragment ：懒加载Fragment
 * @author Nathaniel
 * @time 2017/8/3 - 10:18
 * @version 1.0.0
 */
public abstract class LazyLoadFragment extends BasalFragment {

    protected boolean viewCreated = false;
    protected boolean firstLoad = true;
    protected boolean needInitView = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
        if (needInitView) {
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstLoad) {
            if (viewCreated) {
                firstLoad = false;
                lazyLoad();
            } else {
                needInitView = true;
            }
        }
    }

    protected abstract void lazyLoad();
}
