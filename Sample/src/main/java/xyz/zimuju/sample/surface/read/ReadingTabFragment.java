package xyz.zimuju.sample.surface.read;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.solidev.statusviewlayout.StatusViewLayout;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.engine.service.XianDuService;
import xyz.zimuju.sample.entity.content.XianDuCategory;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.fragment.BaseFragment;
import xyz.zimuju.sample.util.ViewUtils;

/**
 * Created by _SOLID
 * Date:2016/11/29
 * Time:17:01
 * Desc:闲读
 */

public class ReadingTabFragment extends BaseFragment {

    private TabLayout tab_layout;
    private ViewPager view_pager;
    private StatusViewLayout status_view_layout;
    private XianDuTabAdapter mAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.gank_fragment_reading_tab;
    }

    @Override
    protected void initView() {
        status_view_layout = findView(R.id.status_view_layout);
        tab_layout = findView(R.id.tab_layout);
        view_pager = findView(R.id.view_pager);
        ViewCompat.setElevation(tab_layout, ViewUtils.dp2px(getContext(), 4));
        status_view_layout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        status_view_layout.showLoading();
        XianDuService.getCategorys()
                .compose(RxUtils.<List<XianDuCategory>>defaultSchedulersSingle())
                .subscribe(new SingleObserver<List<XianDuCategory>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<XianDuCategory> xianDuCategories) {
                        status_view_layout.showContent();
                        mAdapter = new XianDuTabAdapter(getChildFragmentManager(), xianDuCategories);
                        view_pager.setAdapter(mAdapter);
                        tab_layout.setupWithViewPager(view_pager);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        status_view_layout.showError(e.getMessage());
                    }
                });
    }

    //TODO: 点击返回时现将列表滚动到顶部
    public boolean scrollToTop() {

        return true;
    }
}