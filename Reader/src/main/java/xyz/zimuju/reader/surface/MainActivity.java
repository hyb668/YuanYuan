package xyz.zimuju.reader.surface;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import rx.functions.Action1;
import xyz.zimuju.reader.R;
import xyz.zimuju.reader.application.ConstantsImageUrl;
import xyz.zimuju.reader.databinding.ActivityMainBinding;
import xyz.zimuju.reader.databinding.NavHeaderMainBinding;
import xyz.zimuju.reader.http.rx.RxBus;
import xyz.zimuju.reader.http.rx.RxBusBaseMessage;
import xyz.zimuju.reader.http.rx.RxCodeConstants;
import xyz.zimuju.reader.surface.book.BookFragment;
import xyz.zimuju.reader.surface.gank.GankFragment;
import xyz.zimuju.reader.surface.menu.NavAboutActivity;
import xyz.zimuju.reader.surface.menu.NavDeedBackActivity;
import xyz.zimuju.reader.surface.menu.NavDownloadActivity;
import xyz.zimuju.reader.surface.menu.NavHomePageActivity;
import xyz.zimuju.reader.surface.one.OneFragment;
import xyz.zimuju.reader.util.CommonUtils;
import xyz.zimuju.reader.util.ImgLoadUtil;
import xyz.zimuju.reader.util.PerfectClickListener;
import xyz.zimuju.reader.util.SPUtils;
import xyz.zimuju.reader.widget.custom.MyFragmentPagerAdapter;
import xyz.zimuju.reader.widget.statusbar.StatusBarUtil;
import xyz.zimuju.reader.widget.webview.WebViewActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    NavHeaderMainBinding bind;
    private FrameLayout llTitleMenu;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private ViewPager vpContent;
    // 一定需要对应的bean
    private ActivityMainBinding mBinding;
    private ImageView llTitleGank;
    private ImageView llTitleOne;
    private ImageView llTitleDou;
    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            mBinding.drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int i = v.getId();
                    if (i == R.id.ll_nav_homepage) {
                        NavHomePageActivity.startHome(MainActivity.this);

                    } else if (i == R.id.ll_nav_scan_download) {
                        NavDownloadActivity.start(MainActivity.this);

                    } else if (i == R.id.ll_nav_deedback) {
                        NavDeedBackActivity.start(MainActivity.this);

                    } else if (i == R.id.ll_nav_about) {
                        NavAboutActivity.start(MainActivity.this);

                    } else if (i == R.id.ll_nav_login) {
                        WebViewActivity.loadUrl(v.getContext(), "https://github.com/login", "登录GitHub账号");

                    }
                }
            }, 260);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initStatusView();
        initId();
        initRxBus();

        StatusBarUtil.setColorNoTranslucentForDrawerLayout(MainActivity.this, drawerLayout,
                CommonUtils.getColor(R.color.colorTheme));
        initContentFragment();
        initDrawerLayout();
        initListener();
    }

    private void initStatusView() {
        ViewGroup.LayoutParams layoutParams = mBinding.include.viewStatus.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(this);
        mBinding.include.viewStatus.setLayoutParams(layoutParams);
    }

    private void initId() {
        drawerLayout = mBinding.drawerLayout;
        navView = mBinding.navView;
        fab = mBinding.include.fab;
        toolbar = mBinding.include.toolbar;
        llTitleMenu = mBinding.include.llTitleMenu;
        vpContent = mBinding.include.vpContent;
        fab.setVisibility(View.GONE);

        llTitleGank = mBinding.include.ivTitleGank;
        llTitleOne = mBinding.include.ivTitleOne;
        llTitleDou = mBinding.include.ivTitleDou;
    }

    private void initListener() {
        llTitleMenu.setOnClickListener(this);
        mBinding.include.ivTitleGank.setOnClickListener(this);
        mBinding.include.ivTitleDou.setOnClickListener(this);
        mBinding.include.ivTitleOne.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    /**
     * inflateHeaderView 进来的布局要宽一些
     */
    private void initDrawerLayout() {
        navView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = navView.getHeaderView(0);
        bind = DataBindingUtil.bind(headerView);
        bind.setListener(this);
        bind.dayNightSwitch.setChecked(SPUtils.getNightMode());

        ImgLoadUtil.displayCircle(bind.ivAvatar, ConstantsImageUrl.IC_AVATAR);
        bind.llNavExit.setOnClickListener(this);
        bind.ivAvatar.setOnClickListener(this);

        bind.llNavHomepage.setOnClickListener(listener);
        bind.llNavScanDownload.setOnClickListener(listener);
        bind.llNavDeedback.setOnClickListener(listener);
        bind.llNavAbout.setOnClickListener(listener);
        bind.llNavLogin.setOnClickListener(listener);
    }

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new GankFragment());
        mFragmentList.add(new OneFragment());
        mFragmentList.add(new BookFragment());
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        vpContent.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        vpContent.setOffscreenPageLimit(2);
        vpContent.addOnPageChangeListener(this);
        mBinding.include.ivTitleGank.setSelected(true);
        vpContent.setCurrentItem(0);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            drawerLayout.openDrawer(GravityCompat.START);

        } else if (i == R.id.iv_title_gank) {
            if (vpContent.getCurrentItem() != 0) {//不然cpu会有损耗
                llTitleGank.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleDou.setSelected(false);
                vpContent.setCurrentItem(0);
            }

        } else if (i == R.id.iv_title_one) {
            if (vpContent.getCurrentItem() != 1) {
                llTitleOne.setSelected(true);
                llTitleGank.setSelected(false);
                llTitleDou.setSelected(false);
                vpContent.setCurrentItem(1);
            }

        } else if (i == R.id.iv_title_dou) {
            if (vpContent.getCurrentItem() != 2) {
                llTitleDou.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleGank.setSelected(false);
                vpContent.setCurrentItem(2);
            }

        } else if (i == R.id.iv_avatar) {
            WebViewActivity.loadUrl(v.getContext(), CommonUtils.getString(R.string.string_url_cloudreader), "CloudReader");

        } else if (i == R.id.ll_nav_exit) {
            finish();

        } else {
        }
    }

    /**
     * 夜间模式待完善
     */
    public boolean getNightMode() {
        return SPUtils.getNightMode();
    }

    public void onNightModeClick(View view) {
        if (!SPUtils.getNightMode()) {
//            SkinCompatManager.getInstance().loadSkin(Constants.NIGHT_SKIN);
        } else {
            // 恢复应用默认皮肤
//            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
        SPUtils.setNightMode(!SPUtils.getNightMode());
        bind.dayNightSwitch.setChecked(SPUtils.getNightMode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            // Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                llTitleGank.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 1:
                llTitleOne.setSelected(true);
                llTitleGank.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 2:
                llTitleDou.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleGank.setSelected(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 每日推荐点击"新电影热映榜"跳转
     */
    private void initRxBus() {
        RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE_TO_ONE, RxBusBaseMessage.class)
                .subscribe(new Action1<RxBusBaseMessage>() {
                    @Override
                    public void call(RxBusBaseMessage integer) {
                        mBinding.include.vpContent.setCurrentItem(1);
                    }
                });
    }
}
