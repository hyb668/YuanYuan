package xyz.zimuju.common.basal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import xyz.zimuju.common.controller.ActivityController;
import xyz.zimuju.common.util.ToastUtils;

/*
 * @description BasalActivity :Activity的基类 如果不想用ButterKnife 而是用findViewById 则打开 initView()
 * @author Nathaniel-nathanwriting@126.com
 * @time 2016/9/10-16:29
 * @version v1.0.0
 */
public abstract class BasalActivity<T extends BasalPresenter> extends RxAppCompatActivity implements BasalView {
    protected T presenter;

    protected abstract int getLayoutId();

    protected abstract T initPresenter();

    protected abstract void initData();

    protected abstract void viewOption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getLayoutId());
        initialize();
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    private void initialize() {
        presenter = initPresenter();
        ActivityController.initController().addActivity(this);
        if (presenter != null) {
            presenter.attachView(this);
        }
        ButterKnife.bind(this);
        initData();
        viewOption();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        ActivityController.initController().removeActivity(this);
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showToast(getContext(), message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:

                break;
        }
    }
}
