package xyz.zimuju.sample.surface.gank;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.lang.reflect.Method;

import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.application.GankIOApplication;
import xyz.zimuju.sample.util.ClipboardUtils;
import xyz.zimuju.sample.util.SystemShareUtils;

public class WebViewActivity extends BasalActivity {

    public static String WEB_URL = "webViewUrl";
    public static String TITLE = "webViewTitle";

    private Toolbar mToolbar;
    private String mUrl;
    private String mTitle;

    private FragmentManager mFragmentManager;
    private WebViewFragment mWebViewFragment;
    private TextSwitcher mTextSwitcher;

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_URL, url);
        intent.putExtra(WebViewActivity.TITLE, title);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.gank_activity_webview;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview_toolbar, menu);
        return true;
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_copy) {
            ClipboardUtils.setText(GankIOApplication.getInstance(), mUrl);
            Snackbar.make(mToolbar, "已复制到剪切板", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_share) {
            SystemShareUtils.shareText(this, "【" + mTitle + "】" + mUrl);
        } else if (id == R.id.action_browser) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getExtras().getString(WEB_URL);
        mTitle = getIntent().getExtras().getString(TITLE);
        mFragmentManager = getSupportFragmentManager();
        mWebViewFragment = WebViewFragment.newInstance(mUrl);
        mFragmentManager.beginTransaction().replace(R.id.fl_content, mWebViewFragment).commit();

        //设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//决定左上角图标的右侧是否有向左的小箭头
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressWarnings("deprecation")
            @Override
            public View makeView() {
                Context context = WebViewActivity.this;
                TextView textView = new TextView(context);
                textView.setTextAppearance(context, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        v.setSelected(!v.isSelected());
                    }
                });
                return textView;
            }
        });
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        mTextSwitcher.setText(mTitle);
        mTextSwitcher.setSelected(true);

    }

    @Override
    protected void viewOption() {

    }


    @Override
    public void onBackPressed() {
        if (mWebViewFragment.canGoBack()) {
            mWebViewFragment.goBack();
        } else {
            finish();
        }
    }


}
