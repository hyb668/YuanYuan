package xyz.zimuju.sample.surface.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.common.util.EmptyUtil;
import xyz.zimuju.common.util.TimeCountUtils;
import xyz.zimuju.common.widget.ClearEditText;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.util.UserPreferencesUtils;

/*
 * @description RegisterActivity
 * @author Nathaniel
 * @email nathanwriting@126.com
 * @time 2017/8/6 - 10:21
 * @version 1.0.0
 */
public class RegisterActivity extends BasalActivity<RegisterPresenter> implements View.OnClickListener, RegisterView {
    @BindView(R.id.header_back_tv)
    TextView back;

    @BindView(R.id.header_title_tv)
    TextView title;

    @BindView(R.id.login_username_cet)
    ClearEditText username;

    @BindView(R.id.login_password_cet)
    ClearEditText password;

    @BindView(R.id.login_confirm_cet)
    ClearEditText confirm;

    @BindView(R.id.login_phone_cet)
    ClearEditText phone;

    @BindView(R.id.login_obtain_tv)
    TextView obtain;

    @BindView(R.id.login_code_cet)
    ClearEditText code;

    @BindView(R.id.login_register_tv)
    TextView register;

    private TimeCountUtils timeCountUtil;
    private boolean clickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.avtivity_register;
    }

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterContract();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void viewOption() {
        title.setText("注册");
        back.setVisibility(View.VISIBLE);

        obtain.setBackgroundResource(R.drawable.shape_login_submit_pressed);
        obtain.setClickable(false);

        register.setBackgroundResource(R.drawable.shape_login_submit_pressed);
        register.setClickable(false);

        username.addTextChangedListener(new MTextWatcher(username));
        password.addTextChangedListener(new MTextWatcher(password));
        confirm.addTextChangedListener(new MTextWatcher(confirm));
        phone.addTextChangedListener(new MTextWatcher(phone));
        code.addTextChangedListener(new MTextWatcher(code));
    }

    @OnClick({R.id.login_obtain_tv, R.id.login_register_tv, R.id.header_back_tv})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_back_tv:
                finish();
                break;

            case R.id.login_obtain_tv:
                timeCountUtil = new TimeCountUtils(getContext(), 60 * 1000, 1000, obtain);
                timeCountUtil.setChangeListener(new TimeCountUtils.ChangeListener() {
                    @Override
                    public void finishListener() {
                        obtain.setClickable(true);
                        obtain.setBackgroundResource(R.drawable.shape_login_submit_normal);
                        obtain.setText("重新获取");
                    }
                }, false);
                timeCountUtil.start();
                obtain.setBackgroundResource(R.drawable.shape_login_submit_pressed);
                presenter.obtain(phone.getText().toString().trim());
                clickable = true;
                break;

            case R.id.login_register_tv:
                if (phone.getText().length() != 11) {
                    showToast("电话号码不合法");
                    return;
                }
                String phoneText = phone.getText().toString().trim();
                String codeText = code.getText().toString().trim();
                presenter.verifySmsCode(phoneText, codeText);
                break;
        }
    }

    @Override
    public void obtainResult(Integer smsId) {
        presenter.querySmsState(smsId);
    }

    @Override
    public void verifyCodeResult(boolean verified) {
        if (verified) {
            String usernameText = username.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String phoneText = phone.getText().toString().trim();
            presenter.register(usernameText, passwordText, phoneText);
        }
    }

    @Override
    public void registerResult(String... parameters) {
        presenter.login(parameters);
    }

    @Override
    public void loginResult(BmobUser bmobUser) {
        if (EmptyUtil.isEmpty(bmobUser)) {
            return;
        }
        UserPreferencesUtils.getInstance().saveUser(bmobUser);
        finish();
    }

    private class MTextWatcher implements TextWatcher {
        private EditText editText;

        MTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (editText.getId()) {
                case R.id.login_username_cet:
                    CharSequence charSequence = getContext().getString(R.string.editor_digits);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (editText.getId()) {
                case R.id.login_username_cet:
                    if (s.length() < 6) {
                        showToast("用户名长度必须是至少是6个字符");
                        return;
                    }
                    break;

                case R.id.login_password_cet:
                    if (confirm.getText().length() > 0 && password.getText().equals(confirm.getText())) {
                        showToast("两次输入的密码不一致");
                        return;
                    }
                    break;

                case R.id.login_confirm_cet:
                    if (password.getText().equals(confirm.getText())) {
                        showToast("两次输入的密码不一致");
                        return;
                    }
                    break;

                case R.id.login_phone_cet:
                    if (s.length() == 11) {
                        obtain.setClickable(true);
                        obtain.setBackgroundResource(R.drawable.selector_login_button);
                    } else {
                        obtain.setClickable(false);
                        obtain.setBackgroundResource(R.drawable.shape_login_submit_pressed);
                    }
                    break;

                case R.id.login_code_cet:
                    if (clickable && s.length() == 6) {
                        register.setClickable(true);
                        register.setBackgroundResource(R.drawable.selector_login_button);
                    } else {
                        register.setClickable(false);
                        register.setBackgroundResource(R.drawable.shape_login_submit_pressed);
                    }
            }
        }
    }
}
