package xyz.zimuju.sample.surface.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.engine.GankService;
import xyz.zimuju.sample.entity.HttpResult;
import xyz.zimuju.sample.entity.content.GanHuoData;
import xyz.zimuju.sample.factory.ServiceFactory;
import xyz.zimuju.sample.http.subscriber.HttpResultSubscriber;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;

public class CategoryListFragment extends AbstractListFragment {

    private String mType;

    public static CategoryListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type");
    }

    @Override
    protected void customConfig() {
        addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(ContextCompat.getColor(getContext(), R.color.list_divider_color))
                .sizeResId(R.dimen.list_divider_height)
                .build());
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {

    }

    @Override
    public void loadData(final int pageIndex) {
        if (TextUtils.isEmpty(mType)) {
            showError(new Exception("mType is null"));
            return;
        }
        ServiceFactory.getInstance().createService(GankService.class)
                .getGanHuo(mType, pageIndex)
                .compose(this.<HttpResult<List<GanHuoData>>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<List<GanHuoData>>>defaultSchedulersSingle())
                .subscribe(new HttpResultSubscriber<List<GanHuoData>>() {
                    @Override
                    public void onSuccessful(List<GanHuoData> list) {
                        onDataSuccessReceived(pageIndex, list);
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        showError(new Exception(e));
                    }

                });
    }

    @Override
    protected int getInitPageIndex() {
        return 1;
    }

    @Override
    protected MultiTypeAdapter getAdapter() {
        return new MultiTypeAdapter(getItems()) {
            @NonNull
            @Override
            public Class onFlattenClass(@NonNull Object item) {
                if (item instanceof GanHuoData) {
                    GanHuoData bean = (GanHuoData) item;
                    if (bean.getImages() != null && bean.getImages().size() > 0) {
                        return GanHuoData.Image.class;
                    } else {
                        return GanHuoData.Text.class;
                    }
                }

                return super.onFlattenClass(item);
            }
        };
    }
}
