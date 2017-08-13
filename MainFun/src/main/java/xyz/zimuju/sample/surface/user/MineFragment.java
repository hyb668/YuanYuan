package xyz.zimuju.sample.surface.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import xyz.zimuju.common.basal.BasalFragment;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.common.util.EmptyUtil;
import xyz.zimuju.common.util.ToastUtils;
import xyz.zimuju.common.widget.roundimageview.CircleImageView;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.component.SettingCenter;
import xyz.zimuju.sample.event.LoginEvent;
import xyz.zimuju.sample.loader.ImageLoader;
import xyz.zimuju.sample.rx.RxBus;
import xyz.zimuju.sample.surface.about.AboutActivity;
import xyz.zimuju.sample.surface.gank.SubActivity;
import xyz.zimuju.sample.util.AppUtils;
import xyz.zimuju.sample.util.AuthorityUtils;
import xyz.zimuju.sample.util.SpannableStringUtils;

public class MineFragment extends BasalFragment implements View.OnClickListener {
    @BindView(R.id.mine_username_tv)
    TextView username;

    @BindView(R.id.mine_portrait_iv)
    CircleImageView portrait;

    @BindView(R.id.mine_clear_cache_tv)
    TextView clearCache;

    @BindView(R.id.mine_feedback_tv)
    TextView feedback;

    @BindView(R.id.mine_about_tv)
    TextView aboutUs;

    @BindView(R.id.mine_logout_tv)
    TextView logout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        RxBus.getInstance()
                .toObservable(LoginEvent.class)
                .compose(this.<LoginEvent>bindToLifecycle())
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(@NonNull LoginEvent loginEvent) throws Exception {
                        refreshUserInfo();
                    }
                });
        SettingCenter.countDirSizeTask(new SettingCenter.CountDirSizeListener() {
            @Override
            public void onResult(long result) {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(getString(R.string.mine_cache_clear)).append("  ");
                builder.append(SpannableStringUtils.format(getContext(), "(" + SettingCenter.formatFileSize(result) + ")", R.style.ByTextAppearance));
                clearCache.setText(builder);
            }
        });
    }

    @Override
    protected void viewOption() {

    }

    @Override
    public void refreshData() {
        refreshUserInfo();
    }

    @Override
    public void onResume() {
        refreshUserInfo();
        super.onResume();
    }

    @OnClick({R.id.mine_collect_tv, R.id.mine_username_tv, R.id.mine_feedback_tv, R.id.mine_about_tv, R.id.mine_logout_tv, R.id.mine_clear_cache_tv})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_collect_tv:
                if (!AuthorityUtils.isLogin()) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    SubActivity.start(getContext(), getString(R.string.mine_collect), SubActivity.TYPE_COLLECT);
                }
                break;

            case R.id.mine_username_tv:
                if (!AuthorityUtils.isLogin()) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    // 修改用户名
                }
                break;

            case R.id.mine_feedback_tv:
                AppUtils.feedBack(getContext(), view);
                break;

            case R.id.mine_about_tv:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;

            case R.id.mine_logout_tv:
                AppUtils.logout(getContext(), new AppUtils.OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        refreshUserInfo();
                    }
                });
                break;

            case R.id.mine_clear_cache_tv:
                AppUtils.clearCache(getContext(), new SettingCenter.ClearCacheListener() {
                    @Override
                    public void onResult() {
                        ToastUtils.showToast(getContext(), getString(R.string.cache_clear_success));
                        clearCache.setText(getString(R.string.mine_cache_clear));
                    }
                });
                break;
        }
    }

    private void refreshUserInfo() {
        if (AuthorityUtils.isLogin()) {
            username.setText(AuthorityUtils.getUserName());
            if (EmptyUtil.isNotEmpty(AuthorityUtils.getAvatar())) {
                ImageLoader.displayImage(portrait, AuthorityUtils.getAvatar());
            } else {
                portrait.setImageResource(R.mipmap.icon_portrait_default);
            }
            logout.setVisibility(View.VISIBLE);
        } else {
            username.setText(getString(R.string.mine_click_login));
            portrait.setImageResource(R.mipmap.icon_portrait_default);
            logout.setVisibility(View.GONE);
        }
    }
}
