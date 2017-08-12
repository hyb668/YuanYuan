package xyz.zimuju.sample.surface.news;

import xyz.zimuju.common.basal.BasalView;
import xyz.zimuju.sample.entity.gank.GankResult;

/*
 * @description NewsView
 * @author Nathaniel
 * @email nathanwriting@126.com
 * @time 2017/8/8 - 1:16
 * @version 1.0.0
 */
public interface NewsView extends BasalView {
    void getGankResult(GankResult gankResult);
}
