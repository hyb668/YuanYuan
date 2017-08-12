package xyz.zimuju.sample.surface.gank;

/*
 * @description IList ：列表接口
 * @author Nathaniel
 * @time 2017/8/3 - 10:15
 * @version 1.0.0
 */
public interface ListListener {

    void loadData(int pageIndex);

    void refreshData();

    void loadMore();

    void showError(Exception exception);

    void showLoading();

    void showEmpty(String message);

    void showContent();
}
