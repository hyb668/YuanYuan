package xyz.zimuju.sample.surface.news;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.zimuju.common.rx.RxConsumer;
import xyz.zimuju.common.rx.RxContract;
import xyz.zimuju.sample.engine.GankRetrofit;
import xyz.zimuju.sample.entity.gank.GankResult;

/*
 * @description NewsContract
 * @author Nathaniel
 * @email nathanwriting@126.com
 * @time 2017/8/8 - 1:18
 * @version 1.0.0
 */
public class NewsContract extends RxContract<NewsView> implements NewsPresenter {

    private static final int CONTENT_QUANTITY = 10;

    @Override
    public void getGank(String category, int page) {
        rxManager.add(GankRetrofit.getGankApi().getAllDate(category, CONTENT_QUANTITY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxConsumer<GankResult>() {
                    @Override
                    public void onNext(GankResult gankResult) throws Exception {
                        basalView.getGankResult(gankResult);
                    }
                }));
    }

}
