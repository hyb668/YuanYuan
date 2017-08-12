package xyz.zimuju.sample.surface.gank;


import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import xyz.zimuju.common.basal.BasalFragment;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.surface.news.HomeFragment;
import xyz.zimuju.sample.surface.read.ReadingTabFragment;
import xyz.zimuju.sample.surface.search.SearchActivity;
import xyz.zimuju.sample.util.ViewUtils;

public class GankHomeActivity extends BaseMainActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.bottom_navigation_bnv)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.tv_search)
    TextView search;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.gank_activity_main;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    private void hideAppBar() {
        ViewGroup.LayoutParams layoutParams = mAppBarLayout.getLayoutParams();
        if (layoutParams.height == 0) return;
        layoutParams.height = 0;
        mAppBarLayout.setLayoutParams(layoutParams);
    }

    private void showAppBar() {
        ViewGroup.LayoutParams layoutParams = mAppBarLayout.getLayoutParams();
        if (layoutParams.height != 0) return;
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.app_bar_height);
        mAppBarLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
        switchFragment(0);
    }

    @Override
    protected void viewOption() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        ViewCompat.setElevation(mAppBarLayout, ViewUtils.dp2px(this, 4));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(GankHomeActivity.this);
            }
        });
    }

    private void switchFragment(int index) {
        Fragment toFragment = fragmentManager.findFragmentByTag(index + "");
        if (toFragment == null) {
            if (index == 0) {
                toFragment = ViewUtils.createFragment(HomeFragment.class);
            } else if (index == 1) {
                toFragment = ViewUtils.createFragment(ReadingTabFragment.class);
            } else {
                toFragment = ViewUtils.createFragment(HomeFragment.class);
            }
        }

        if (toFragment == currentFragment && currentFragment instanceof BasalFragment) {
            ((BasalFragment) currentFragment).refreshData();
        } else if (toFragment.isAdded()) {
            fragmentManager.beginTransaction().hide(currentFragment).show(toFragment).commit();
        } else {
            toFragment.setUserVisibleHint(true);
            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).add(R.id.frame_content, toFragment, index + "").commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.frame_content, toFragment, index + "").commit();
            }
        }
        currentFragment = toFragment;
    }

    @Override
    protected boolean beforeOnBackPressed() {
        if (currentFragment instanceof AbstractListFragment) {
            AbstractListFragment listFragment = (AbstractListFragment) currentFragment;
            return listFragment.scrollToTop();
        } else if (currentFragment instanceof ReadingTabFragment) {
            ReadingTabFragment readingTabFragment = (ReadingTabFragment) currentFragment;
            return readingTabFragment.scrollToTop();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                showAppBar();
                switchFragment(0);
                break;

            case R.id.item_reading:
                hideAppBar();
                switchFragment(1);
                break;

//            case R.id.item_collect:
//                hideAppBar();
//                switchFragment(2);
//                break;
        }
        return false;
    }
}
