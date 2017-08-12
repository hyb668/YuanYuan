package xyz.zimuju.sample.surface.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.sample.engine.GankService;
import xyz.zimuju.sample.entity.HttpResult;
import xyz.zimuju.sample.entity.content.DailyList;
import xyz.zimuju.sample.entity.content.DailyTitle;
import xyz.zimuju.sample.entity.content.GanHuoData;
import xyz.zimuju.sample.factory.ServiceFactory;
import xyz.zimuju.sample.http.subscriber.HttpResultSubscriber;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;

public class RecentlyListFragment extends AbstractListFragment {


    private String date;

    public static RecentlyListFragment newInstance(int year, int month, int day) {
        RecentlyListFragment fragment = new RecentlyListFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getInt("year") + "/"
                + getArguments().getInt("month") + "/"
                + getArguments().getInt("day");
    }

    @Override
    protected void customConfig() {
        disAbleLoadMore();
        disAbleRefresh();
    }

    @Override
    protected MultiTypeAdapter getAdapter() {
        return new MultiTypeAdapter(getItems()) {
            @NonNull
            @Override
            public Class onFlattenClass(@NonNull Object item) {
                if (item instanceof GanHuoData) {
                    return GanHuoData.DailyItem.class;
                }
                return super.onFlattenClass(item);
            }
        };
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadData(final int pageIndex) {
        if (TextUtils.isEmpty(date)) {
            showError(new Exception("date is null"));
            return;
        }
        ServiceFactory.getInstance().createService(GankService.class)
                .getRecentlyGanHuo(date)
                .compose(this.<HttpResult<DailyList>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<DailyList>>defaultSchedulersSingle())
                .subscribe(new HttpResultSubscriber<DailyList>() {

                    @Override
                    public void onFailed(Throwable e) {
                        showError(new Exception(e));
                    }

                    @Override
                    public void onSuccessful(DailyList recentlyBean) {
                        List list = new ArrayList<>();

                        if (recentlyBean != null) {
                            if (recentlyBean.get休息视频() != null) {
                                list.add(new DailyTitle("休息视频"));
                                list.addAll(recentlyBean.get休息视频());
                            }
                            if (recentlyBean.getAndroid() != null) {
                                list.add(new DailyTitle("Android"));
                                list.addAll(recentlyBean.getAndroid());
                            }
                            if (recentlyBean.getIOS() != null) {
                                list.add(new DailyTitle("iOS"));
                                list.addAll(recentlyBean.getIOS());
                            }
                            if (recentlyBean.get前端() != null) {
                                list.add(new DailyTitle("前端"));
                                list.addAll(recentlyBean.get前端());
                            }
                            if (recentlyBean.getApp() != null) {
                                list.add(new DailyTitle("App"));
                                list.addAll(recentlyBean.getApp());
                            }
                            if (recentlyBean.get瞎推荐() != null) {
                                list.add(new DailyTitle("瞎推荐"));
                                list.addAll(recentlyBean.get瞎推荐());
                            }


                        }
                        onDataSuccessReceived(pageIndex, list);
                    }
                });
    }

    @NonNull
    @Override
    protected String getEmptyMsg() {
        return "今日暂无干货";
    }
}
