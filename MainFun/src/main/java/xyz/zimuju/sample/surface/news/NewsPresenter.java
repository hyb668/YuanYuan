package xyz.zimuju.sample.surface.news;

import xyz.zimuju.common.basal.BasalPresenter;

public interface NewsPresenter extends BasalPresenter<NewsView> {
    void getGank(String category, int page);
}
