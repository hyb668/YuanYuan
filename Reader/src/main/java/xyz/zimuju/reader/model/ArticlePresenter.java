package xyz.zimuju.reader.model;

/*
 * @description ArticlePresenter
 * @author WenTao
 * @time 2017/6/6 - 16:22
 * @version 1.0.0
 */

//public class ArticlePresenter extends RxPresenter<ArticleView> {
//    public void getArticleList(int no) {
//        GetArticleListReq req = new GetArticleListReq();
//        req.setNo(no);
//
//        rxManager.add(RetrofitUtil.getMainApis().getArticleList(req.getLoginId(), req)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new RxResponse(GetArticleListRes.class))
//                .subscribe(new RxConsumer<GetArticleListRes>() {
//                    @Override
//                    public void onNext(GetArticleListRes t) throws Exception {
//                        mvpView.getArticleListResult(t);
//                    }
//                }, new RxConsumerException() {
//                    @Override
//                    public void onError(RxError rxError) {
//                        mvpView.showToast(rxError.getMsg());
//                    }
//                }));
//    }
//}
