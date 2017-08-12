package xyz.zimuju.sample.surface.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.zimuju.common.util.ToastUtils;
import xyz.zimuju.common.widget.NoneScrollViewPager;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.adapter.navigation.NavigationFragmentPagerAdapter;
import xyz.zimuju.sample.surface.news.NewsFragment;
import xyz.zimuju.sample.surface.user.MineFragment;

/*
 * @description NavigationActivity:导航界面
 * @author Nathaniel
 * @time 2017/8/1 - 10:32
 * @version 1.0.0
 */
public class NavigationActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private RadioGroup navigationGroup;
    private List<Fragment> fragmentList;
    private NoneScrollViewPager navigationContainer;
    private long exitTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        initView();
        bindView();
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        Fragment newsFragment = new NewsFragment();
        fragmentList.add(newsFragment);
        Fragment discoveryFragment = new DiscoveryFragment();
        fragmentList.add(discoveryFragment);
        Fragment mineFragment = new MineFragment();
        fragmentList.add(mineFragment);

        navigationContainer = (NoneScrollViewPager) findViewById(R.id.navigation_container_layout);
        navigationGroup = (RadioGroup) findViewById(R.id.navigation_group_layout);
    }

    private void bindView() {
        NavigationFragmentPagerAdapter navigationFragmentPagerAdapter = new NavigationFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        navigationContainer.setOffscreenPageLimit(3);
        navigationContainer.setScrollEnable(true);
        navigationContainer.setAdapter(navigationFragmentPagerAdapter);
        navigationGroup.setOnCheckedChangeListener(this);
        navigationContainer.addOnPageChangeListener(this);
        navigationContainer.setCurrentItem(0, false);
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showToast(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.navigation_news_rb:
                navigationContainer.setCurrentItem(0, false);
                break;

            case R.id.navigation_discovery_rb:
                navigationContainer.setCurrentItem(1, false);
                break;

            case R.id.navigation_mine_rb:
                navigationContainer.setCurrentItem(2, false);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                navigationGroup.check(R.id.navigation_news_rb);
                break;

            case 1:
                navigationGroup.check(R.id.navigation_discovery_rb);
                break;

            case 2:
                navigationGroup.check(R.id.navigation_mine_rb);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
