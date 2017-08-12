package xyz.zimuju.common.rx;


import io.reactivex.functions.Consumer;

/**
 * Created by 8000m on 2017/5/2.
 */

public abstract class RxConsumer<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        onNext(t);
    }

    public abstract void onNext(T t) throws Exception;

}
