package xyz.zimuju.common.basal;

/*
 * @description BasalPresenter
 * @author Nathaniel
 * @email nathanwriting@126.com
 * @time 2017/7/2 - 19:08
 * @version 1.0.0
 */
public interface BasalPresenter<T extends BasalView> {
    void attachView(T basalView);

    void detachView();
}
