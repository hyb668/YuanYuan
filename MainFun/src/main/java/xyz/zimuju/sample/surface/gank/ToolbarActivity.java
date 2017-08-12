package xyz.zimuju.sample.surface.gank;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.sample.R;

public abstract class ToolbarActivity extends BasalActivity {

    protected AppBarLayout mAppBarLayout;
    protected TextSwitcher mTextSwitcher;
    protected FragmentManager mFragmentManager;

    private void setUpToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressWarnings("deprecation")
            @Override
            public View makeView() {
                Context context = ToolbarActivity.this;
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
    }

    protected abstract String getToolbarTitle();

    @Override
    protected void initData() {
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = getFragment();
        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();
        }
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        setUpToolBar();
        if (!isHaveTitle()) {
            mAppBarLayout.setVisibility(View.GONE);
        }
        setTitle(getToolbarTitle());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gank_activity_toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTextSwitcher.setText(title);
        mTextSwitcher.setSelected(true);
    }

    protected abstract Fragment getFragment();

    protected boolean isHaveTitle() {
        return true;
    }
}
