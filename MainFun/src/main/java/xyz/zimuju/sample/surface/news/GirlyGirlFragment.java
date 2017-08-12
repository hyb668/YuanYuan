package xyz.zimuju.sample.surface.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.engine.GankService;
import xyz.zimuju.sample.entity.HttpResult;
import xyz.zimuju.sample.entity.content.GanHuoData;
import xyz.zimuju.sample.factory.ServiceFactory;
import xyz.zimuju.sample.http.subscriber.HttpResultSubscriber;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;

/*
 * @description GirlyGirlFragment : 妹纸页面
 * @author Nathaniel
 * @time 2017/8/11 - 10:18
 * @version 1.0.0
 */
public class GirlyGirlFragment extends AbstractListFragment {

    public static GirlyGirlFragment newInstance() {
        Bundle args = new Bundle();
        GirlyGirlFragment fragment = new GirlyGirlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void customConfig() {
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {

    }

    @Override
    public void loadData(final int pageIndex) {
        ServiceFactory.getInstance()
                .createService(GankService.class)
                .getGanHuo("福利", pageIndex)
                .compose(this.<HttpResult<List<GanHuoData>>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<List<GanHuoData>>>defaultSchedulersSingle())
                .subscribe(new HttpResultSubscriber<List<GanHuoData>>() {
                    @Override
                    public void onFailed(Throwable e) {
                        showError(new Exception(e));
                    }

                    @Override
                    public void onSuccessful(List<GanHuoData> ganHuoDataBeen) {
                        onDataSuccessReceived(pageIndex, ganHuoDataBeen);
                    }
                });
    }

    @Override
    protected MultiTypeAdapter getAdapter() {
        return new MultiTypeAdapter(getItems()) {
            @NonNull
            @Override
            public Class onFlattenClass(@NonNull Object item) {
                return GanHuoData.Meizhi.class;
            }
        };
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void viewOption() {

    }
}
