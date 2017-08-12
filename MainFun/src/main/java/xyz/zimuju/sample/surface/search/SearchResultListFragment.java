package xyz.zimuju.sample.surface.search;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.sample.engine.GankService;
import xyz.zimuju.sample.entity.HttpResult;
import xyz.zimuju.sample.entity.content.SearchResult;
import xyz.zimuju.sample.factory.ServiceFactory;
import xyz.zimuju.sample.http.subscriber.HttpResultSubscriber;
import xyz.zimuju.sample.rx.RxUtils;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;
import xyz.zimuju.sample.widget.custom.LinearDecoration;

public class SearchResultListFragment extends AbstractListFragment {
    private String keyWord = "Android";
    private String category = "all";

    public static SearchResultListFragment newInstance(String category, String keyWord) {

        SearchResultListFragment fragment = new SearchResultListFragment();
        fragment.keyWord = keyWord;
        fragment.category = category;
        return fragment;
    }

    public void refresh(String category, String keyWord) {
        this.category = category;
        this.keyWord = keyWord;
        showLoading();
        refreshData();
    }

    @Override
    protected void customConfig() {
        addItemDecoration(new LinearDecoration(getContext(), RecyclerView.VERTICAL));
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {

    }

    @Override
    public void loadData(final int pageIndex) {
        ServiceFactory.getInstance().createService(GankService.class)
                .search(category, keyWord, pageIndex)
                .compose(this.<HttpResult<List<SearchResult>>>bindToLifecycle())
                .compose(RxUtils.<HttpResult<List<SearchResult>>>defaultSchedulersSingle())
                .subscribe(new HttpResultSubscriber<List<SearchResult>>() {
                    @Override
                    public void onFailed(Throwable e) {
                        showError(new Exception(e));
                    }

                    @Override
                    public void onSuccessful(List<SearchResult> searchResults) {
                        onDataSuccessReceived(pageIndex, searchResults);
                    }


                });
    }


}
