package xyz.zimuju.common.rx;


import android.content.Context;

import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.common.basal.BasalView;

public abstract class RxContract<T extends BasalView> implements BasalPresenter<T> {
    protected Context context;
    protected T basalView;
    protected RxManager rxManager = new RxManager();

    @Override
    public void attachView(T view) {
        this.basalView = view;
        context = basalView.getContext();
    }

    @Override
    public void detachView() {
        this.basalView = null;
        rxManager.clear();
    }
}
