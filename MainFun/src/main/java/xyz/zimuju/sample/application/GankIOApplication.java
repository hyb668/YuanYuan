package xyz.zimuju.sample.application;

import android.app.Application;
import android.os.Environment;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import xyz.zimuju.sample.component.MultiTypeInstaller;
import xyz.zimuju.sample.constant.ConfigConstants;
import xyz.zimuju.sample.util.PrefUtils;

public class GankIOApplication extends Application {
    private static GankIOApplication gankIOApplication;

    public static GankIOApplication getInstance() {
//        if (gankIOApplication == null) {
//            gankIOApplication = new GankIOApplication();
//        }
        return gankIOApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        gankIOApplication = this;

        // 内存泄露检测
        if (ConfigConstants.debugEnable) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }

        MultiTypeInstaller.install();

        // 初始化SharedPreferences
        PrefUtils.initialize(this);

        // 初始化UserApplication
        UserApplication.getInstance().initialize(this);

        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId(ConfigConstants.BOMB_APPLICATION_ID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        BmobSMS.initialize(this, ConfigConstants.BOMB_APPLICATION_ID);
    }

    @Override
    public File getCacheDir() {
        File file = new File(Environment.getExternalStorageDirectory(), "/YuanYuan/Cache/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
