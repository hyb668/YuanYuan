package xyz.zimuju.sample.surface.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.util.SnackBarUtils;

public class SearchActivity extends BasalActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.et_keyword)
    EditText mKeywordText;

    @BindView(R.id.iv_select_cate)
    ImageView mIvSelectCate;

    private String mKeyWord;
    private SearchResultListFragment mSearchResultListFragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gank_activity_search;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }


    @Override
    protected void initData() {
        mSearchResultListFragment = SearchResultListFragment.newInstance(mKeywordText.getHint().toString(), "android");
        mSearchResultListFragment.setUserVisibleHint(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_result, mSearchResultListFragment).commit();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//决定左上角图标的右侧是否有向左的小箭头
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mIvSelectCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(SearchActivity.this, mIvSelectCate);
                popupMenu.getMenuInflater()
                        .inflate(R.menu.menu_category, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mKeywordText.setHint(item.getTitle());
                        return true;
                    }
                });
            }
        });
    }

    @Override
    protected void viewOption() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mKeyWord = mKeywordText.getText().toString();
            if (!TextUtils.isEmpty(mKeyWord)) {
                mSearchResultListFragment.refresh(mKeywordText.getHint().toString(), mKeyWord);
            } else {
                SnackBarUtils.makeShort(mToolbar, getString(R.string.tips_keyword_cannot_null)).info();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
