package xyz.zimuju.sample.surface.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.entity.bomb.CollectTable;
import xyz.zimuju.sample.provider.CollectViewProvider;
import xyz.zimuju.sample.surface.gank.AbstractListFragment;
import xyz.zimuju.sample.util.AuthorityUtils;


public class CollectFragment extends AbstractListFragment {

    public static CollectFragment newInstance() {
        Bundle args = new Bundle();
        CollectFragment fragment = new CollectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        adapter.register(CollectTable.class, new CollectViewProvider(mRecyclerView));
    }

    @Override
    protected void customConfig() {
        addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(ContextCompat.getColor(getContext(), R.color.list_divider_color))
                .sizeResId(R.dimen.list_divider_height)
                .build());
    }

    @Override
    public void loadData(final int pageIndex) {
        mPageSize = 10;
        BmobQuery<CollectTable> query = new BmobQuery<>();
        query.addWhereEqualTo("username", AuthorityUtils.getUserName());
        query.setLimit(mPageSize);
        query.setSkip(mPageSize * (pageIndex - 1));
        query.order("-createdAt");
        if (AuthorityUtils.isLogin()) {
            query.findObjects(new FindListener<CollectTable>() {
                @Override
                public void done(List<CollectTable> list, BmobException e) {
                    if (e != null) {
                        onDataSuccessReceived(pageIndex, list);
                    } else {
                        showError(new Exception(e));
                    }
                }
            });
        } else {
            showEmpty(getString(R.string.mine_no_login));
        }
    }

    @NonNull
    @Override
    protected String getEmptyMsg() {
        return getString(R.string.tips_no_collect);
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void viewOption() {

    }
}
