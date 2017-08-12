package xyz.zimuju.sample.surface.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.sample.constant.CategoryConstants;
import xyz.zimuju.sample.engine.GankService;
import xyz.zimuju.sample.entity.HttpResult;
import xyz.zimuju.sample.entity.content.CategoryList;
import xyz.zimuju.sample.entity.content.Daily;
import xyz.zimuju.sample.factory.ServiceFactory;
import xyz.zimuju.sample.http.subscriber.HttpResultSubscriber;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;

public class HomeFragment extends AbstractListFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserVisibleHint(true);
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {

    }

    @Override
    public void loadData(final int pageIndex) {
        final List data = new ArrayList();
        if (pageIndex == getInitPageIndex()) {
            CategoryList categoryList = new CategoryList();
            categoryList.setData(CategoryConstants.getGanHuoCateGory());
            data.add(categoryList);
        }

        ServiceFactory.getInstance()
                .createService(GankService.class)
                .getRecently(pageIndex)
                .compose(this.<HttpResult<List<Daily>>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<List<Daily>>>defaultSchedulersSingle())
                .subscribe(new HttpResultSubscriber<List<Daily>>() {
                    @Override
                    public void onSuccessful(List<Daily> dailies) {
                        data.addAll(dailies);
                        onDataSuccessReceived(pageIndex, data);
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        showError(new Exception(e));
                    }
                });
    }

    @Override
    protected void customConfig() {

    }

    @Override
    protected int getInitPageIndex() {
        return 1;
    }
}
