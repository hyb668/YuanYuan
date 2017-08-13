package xyz.zimuju.sample.surface.common;


import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import butterknife.BindView;
import xyz.zimuju.common.basal.BasalFragment;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.surface.news.HomeFragment;
import xyz.zimuju.sample.surface.read.ReadingFragment;
import xyz.zimuju.sample.util.ViewUtils;

public class DiscoveryFragment extends BasalFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.discovery_switcher_rg)
    RadioGroup switcher;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        fragmentManager = getActivity().getSupportFragmentManager();
        switchFragment(0);
    }

    @Override
    protected void viewOption() {
        switcher.setOnCheckedChangeListener(this);
    }

    @Override
    public void refreshData() {

    }

    private void switchFragment(int index) {
        Fragment toFragment = fragmentManager.findFragmentByTag(index + "");
        if (toFragment == null) {
            switch (index) {
                case 0:
                    toFragment = ViewUtils.createFragment(HomeFragment.class);
                    break;
                case 1:
                    toFragment = ViewUtils.createFragment(ReadingFragment.class);
                    break;
                default:
                    toFragment = ViewUtils.createFragment(HomeFragment.class);
                    break;
            }
        }

        if (toFragment == currentFragment && currentFragment instanceof BasalFragment) {
            ((BasalFragment) currentFragment).refreshData();
        } else if (toFragment.isAdded()) {
            fragmentManager.beginTransaction().hide(currentFragment).show(toFragment).commit();
        } else {
            toFragment.setUserVisibleHint(true);
            if (currentFragment != null) {
                fragmentManager.beginTransaction().hide(currentFragment).add(R.id.discovery_container_layout, toFragment, index + "").commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.discovery_container_layout, toFragment, index + "").commit();
            }
        }
        currentFragment = toFragment;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.discovery_home_rb:
                switchFragment(0);
                break;

            case R.id.discovery_reader_rb:
                switchFragment(1);
                break;
        }
    }
}
