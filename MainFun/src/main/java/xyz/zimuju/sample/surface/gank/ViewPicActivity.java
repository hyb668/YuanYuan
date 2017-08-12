package xyz.zimuju.sample.surface.gank;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.common.basal.BasalPresenter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.application.GankIOApplication;
import xyz.zimuju.sample.http.subscriber.DownloadSubscriber;
import xyz.zimuju.sample.loader.ImageLoader;
import xyz.zimuju.sample.provider.ObservableProvider;
import xyz.zimuju.sample.util.FileUtils;
import xyz.zimuju.sample.util.SLog;
import xyz.zimuju.sample.util.SnackBarUtils;
import xyz.zimuju.sample.util.SystemShareUtils;

public class ViewPicActivity extends BasalActivity {
    public final static String IMG_URLS = "ImageUrls";
    public final static String IMG_INDEX = "ImageIndex";
    private ViewPager mViewPager;
    private TextView mTvIndex;
    private AppCompatImageView mIvDownload;

    private ArrayList<String> mUrlList;
    private int mCurrentIndex = 0;
    private String mSavePath;


    public static void start(Context context, View view, ArrayList<String> urls, int position) {
        Intent intent = new Intent(context, ViewPicActivity.class);
        intent.putStringArrayListExtra(IMG_URLS, urls);
        intent.putExtra(IMG_INDEX, position);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    public static void start(Context context, View view, String url) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(url);
        start(context, view, urls, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gank_activity_view_pic;
    }

    @Override
    protected BasalPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        mUrlList = getIntent().getExtras().getStringArrayList(IMG_URLS);
        mCurrentIndex = getIntent().getExtras().getInt(IMG_INDEX);

        mViewPager.setAdapter(new MyViewPager(this));
        mViewPager.setCurrentItem(mCurrentIndex);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTvIndex = (TextView) findViewById(R.id.tv_index);
        mIvDownload = (AppCompatImageView) findViewById(R.id.iv_download);
        mTvIndex.setText((mCurrentIndex + 1) + "/" + mUrlList.size());
        mIvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPicture(0);
            }
        });
        RxPermissions rxPermissions = new RxPermissions(this);
        RxView.clicks(mIvDownload)
                .compose(rxPermissions.ensureEach(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(new Consumer<com.tbruyelle.rxpermissions2.Permission>() {
                    @Override
                    public void accept(@NonNull com.tbruyelle.rxpermissions2.Permission permission) throws Exception {
                        if (permission.granted) {
                            downloadPicture(0);
                        } else if (permission.shouldShowRequestPermissionRationale) {

                        } else {
                            SnackBarUtils.makeShort(mViewPager, getString(R.string.no_permisstion_write))
                                    .info();
                        }
                    }
                });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                mTvIndex.setText((mCurrentIndex + 1) + "/" + mUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void viewOption() {

    }

    /**
     * download image
     *
     * @param action 0:save 1:share
     */
    public void downloadPicture(final int action) {
        mSavePath = FileUtils.getSaveImagePath(this) + File.separator + FileUtils.getFileName(mUrlList.get(0));
        SLog.i(this, mSavePath);
        ObservableProvider.getDefault()
                .download(mUrlList.get(0)
                        , new DownloadSubscriber(
                                FileUtils.getSaveImagePath(getApplication()),
                                FileUtils.getFileName(mUrlList.get(0))) {
                            @Override
                            public void onCompleted(File file) {
                                if (action == 0) {
                                    SnackBarUtils.makeLong(mViewPager, "已保存至相册").info();
                                    MediaScannerConnection.scanFile(GankIOApplication.getInstance(), new String[]{
                                                    mSavePath},
                                            null, null);
                                } else {
                                    SystemShareUtils.shareImage(ViewPicActivity.this, Uri.parse(file.getAbsolutePath()));
                                }
                            }

                            @Override
                            protected void onFailed(Throwable e) {
                                if (action == 0)
                                    SnackBarUtils.makeLong(mViewPager, "保存失败:" + e).danger();
                            }

                            @Override
                            public void onProgress(double progress, long downloadByte, long totalByte) {
                                // SLog.i("PicDownload", "totalByte:" + totalByte + " downloadedByte:" + downloadByte + " progress:" + progress);
                            }
                        });

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        overridePendingTransition(0, R.anim.activity_close);
    }

    class MyViewPager extends PagerAdapter {
        Context context;

        public MyViewPager(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PhotoView photoView = new PhotoView(context);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            photoView.setLayoutParams(layoutParams);

            //setUpPhotoViewAttacher(photoView);
            ImageLoader.displayImage(photoView, mUrlList.get(position));
            container.addView(photoView);

            return photoView;
        }

//        private void setUpPhotoViewAttacher(PhotoView photoView) {
//            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoView);
//            photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//                @Override
//                public void onViewTap(View view, float v, float v1) {
//                    ViewPicActivity activity = (ViewPicActivity) getContext();
//                    activity.hideOrShowToolbar();
//                }
//            });
//        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
