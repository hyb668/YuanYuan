package xyz.zimuju.sample.engine;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import xyz.zimuju.sample.entity.gank.GankResult;

/*
 * @description GankApi :
 * @author Nathaniel
 * @time 2017/8/12 - 13:08
 * @version 1.0.0
 */
public interface GankApi {
    @GET("data/{type}/{count}/{pageIndex}")
    Observable<GankResult> getAllDate(@Path("type") String type,
                                      @Path("count") int count,
                                      @Path("pageIndex") int pageIndex
    );
}
