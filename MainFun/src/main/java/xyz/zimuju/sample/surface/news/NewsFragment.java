package xyz.zimuju.sample.surface.news;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import xyz.zimuju.common.basal.BasalFragment;
import xyz.zimuju.common.basal.MRecyclerViewAdapter;
import xyz.zimuju.common.util.EmptyUtil;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.adapter.gank.GankCategoryAdapter;
import xyz.zimuju.sample.adapter.gank.GankNewsAdapter;
import xyz.zimuju.sample.entity.gank.Category;
import xyz.zimuju.sample.entity.gank.Gank;
import xyz.zimuju.sample.entity.gank.GankResult;
import xyz.zimuju.sample.surface.content.WebActivity;
import xyz.zimuju.sample.util.GankCategoryUtils;


public class NewsFragment extends BasalFragment<NewsPresenter> implements NewsView, MRecyclerViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.news_category_recyclerview)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.news_container_recyclerview)
    RecyclerView containerRecyclerView;

    @BindView(R.id.news_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private GankCategoryAdapter gankCategoryAdapter;
    private GankNewsAdapter gankNewsAdapter;
    private List<Gank> gankList = new ArrayList<>();
    private int page = 1;
    private String categoryName = "all";
    private Category category;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected NewsPresenter initPresenter() {
        return new NewsContract();
    }

    @Override
    protected void initData() {
        gankCategoryAdapter = new GankCategoryAdapter();
        gankCategoryAdapter.setDataList(GankCategoryUtils.getCategoryList(getContext()));
        gankNewsAdapter = new GankNewsAdapter();
    }

    @Override
    protected void viewOption() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setNestedScrollingEnabled(false);
        categoryRecyclerView.setAdapter(gankCategoryAdapter);
        gankCategoryAdapter.setOnItemClickListener(this);

        presenter.getGank(categoryName, page);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        containerRecyclerView.setLayoutManager(linearLayoutManager);
        containerRecyclerView.setHasFixedSize(true);
        containerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        containerRecyclerView.setNestedScrollingEnabled(false);
        containerRecyclerView.setAdapter(gankNewsAdapter);
        gankNewsAdapter.setOnItemClickListener(this);

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);

        containerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    int itemCount = linearLayoutManager.getItemCount();
                    if (lastVisiblePosition >= itemCount - 1) {
                        page++;
                        presenter.getGank(categoryName, page);
                    }
                }
            }
        });
    }

    @Override
    public void refreshData() {
        onRefresh();
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
        if (parent.getId() == categoryRecyclerView.getId()) {
            gankCategoryAdapter.setIndex(position);
            category = GankCategoryUtils.getCategoryList(getContext()).get(position);
            categoryName = category.getName();
            if (position == 0) {
                categoryName = "all";
            }
            page = 1;
            presenter.getGank(categoryName, page);
            gankCategoryAdapter.notifyDataSetChanged();
        }

        if (parent.getId() == containerRecyclerView.getId()) {
            Gank gank = gankList.get(position);
            Intent toWebIntent = new Intent(getContext(), WebActivity.class);
            toWebIntent.putExtra("url", gank.getUrl());
            startActivity(toWebIntent);
        }
    }

    @Override
    public void getGankResult(GankResult gankResult) {
        if (EmptyUtil.isEmpty(gankResult)) {
            return;
        }

        if (page == 1) {
            gankList.clear();
        }

        List<Gank> tempList = gankResult.getResults();
        if (EmptyUtil.isNotEmpty(tempList) && tempList.size() > 0) {
            gankList.addAll(tempList);
            gankNewsAdapter.setDataList(gankList);
        }

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        gankNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.getGank(categoryName, page);
    }
}
