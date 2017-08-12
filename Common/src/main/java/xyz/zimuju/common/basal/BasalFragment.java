package xyz.zimuju.common.basal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import xyz.zimuju.common.util.ToastUtils;

/*
 * @description BasalFragment ： 所有fragment的基类 如果想用 findViewById 则将 initView() 打开即可
 * @author Nathaniel-nathanwriting@126.com
 * @time 16-9-24-下午11:08
 * @version v1.0.0
 */
public abstract class BasalFragment<T extends BasalPresenter> extends RxFragment implements BasalView {
    protected T presenter;
    private View rootView;

    protected abstract int getLayoutId();// 获得布局文件的id

    protected abstract T initPresenter();

    protected abstract void initData(); // 初始化数据

    protected abstract void viewOption(); // 绑定使徒控件操作

    public abstract void refreshData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        presenter = initPresenter();
        ButterKnife.bind(this, rootView);
        initData();

        if (presenter != null) {
            presenter.attachView(this);
        }

        viewOption();
        return rootView;
    }

    public Context getContext() {
        return getActivity();
    }

    protected View getRootView() {
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDestroyView();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showToast(getContext(), message);
    }
}
