package xyz.zimuju.sample.surface.read;


import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import me.solidev.statusviewlayout.StatusViewLayout;
import xyz.zimuju.common.basal.BasalFragment;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.engine.XianDuService;
import xyz.zimuju.sample.entity.content.XianDuCategory;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.util.ViewUtils;

public class ReadingFragment extends BasalFragment {
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    @BindView(R.id.status_view_layout)
    StatusViewLayout status_view_layout;

    private ReadingTabAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.gank_fragment_reading_tab;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
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
                        mAdapter = new ReadingTabAdapter(getChildFragmentManager(), xianDuCategories);
                        view_pager.setAdapter(mAdapter);
                        tab_layout.setupWithViewPager(view_pager);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        status_view_layout.showError(e.getMessage());
                    }
                });
    }

    @Override
    protected void viewOption() {

    }

    public void refreshData() {
        status_view_layout = (StatusViewLayout) getRootView().findViewById(R.id.status_view_layout);
        tab_layout = (TabLayout) getRootView().findViewById(R.id.tab_layout);
        view_pager = (ViewPager) getRootView().findViewById(R.id.view_pager);
        ViewCompat.setElevation(tab_layout, ViewUtils.dp2px(getContext(), 4));
        status_view_layout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    //TODO: 点击返回时现将列表滚动到顶部
    public boolean scrollToTop() {
        return true;
    }
}
