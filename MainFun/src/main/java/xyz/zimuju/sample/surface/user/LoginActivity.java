package xyz.zimuju.sample.surface.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.common.util.EmptyUtil;
import xyz.zimuju.common.widget.ClearEditText;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.util.UserPreferencesUtils;

/*
 * @description LoginActivity ：登陆界面
 * @author Nathaniel
 * @time 2017/8/4 - 9:36
 * @version 1.0.0
 */
public class LoginActivity extends BasalActivity<LoginPresenter> implements View.OnClickListener, LoginView {
    @BindView(R.id.header_back_tv)
    TextView back;

    @BindView(R.id.header_title_tv)
    TextView title;

    @BindView(R.id.login_username_cet)
    ClearEditText username;

    @BindView(R.id.login_password_cet)
    ClearEditText password;

    @BindView(R.id.login_register_tv)
    TextView register;

    @BindView(R.id.login_forgot_tv)
    TextView forgot;

    @BindView(R.id.login_submit_tv)
    TextView submit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginContract();
    }

    @Override
    protected void initData() {
        title.setText(getString(R.string.mine_login));
        back.setText(R.string.common_tip_back);
    }

    @Override
    protected void viewOption() {
        back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.header_back_tv, R.id.login_submit_tv, R.id.login_register_tv, R.id.login_forgot_tv})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_back_tv:
                finish();
                break;

            case R.id.login_submit_tv:
                String usernameText = username.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                presenter.login(usernameText, passwordText);
                break;

            case R.id.login_register_tv:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                finish();
                break;

            case R.id.login_forgot_tv:

                break;
        }
    }

    @Override
    public void loginResult(BmobUser bmobUser) {
        if (EmptyUtil.isEmpty(bmobUser)) {
            return;
        }
        UserPreferencesUtils.getInstance().saveUser(bmobUser);
        finish();
    }
}
