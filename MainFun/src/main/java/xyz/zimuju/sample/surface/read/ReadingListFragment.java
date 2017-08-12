package xyz.zimuju.sample.surface.read;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.engine.XianDuService;
import xyz.zimuju.sample.entity.content.XianDuItem;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;

public class ReadingListFragment extends AbstractListFragment {

    private String category;

    public static ReadingListFragment newInstance(String category) {

        Bundle args = new Bundle();
        args.putString("category", category);
        ReadingListFragment fragment = new ReadingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString("category");
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
        XianDuService.getData(category, pageIndex)
                .compose(RxUtils.<List<XianDuItem>>defaultSchedulersSingle())
                .subscribe(new SingleObserver<List<XianDuItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<XianDuItem> xianDuItems) {
                        onDataSuccessReceived(pageIndex, xianDuItems);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showError(new Exception(e));
                    }
                });
    }

    @Override
    protected int getInitPageIndex() {
        return 1;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void viewOption() {

    }
}
