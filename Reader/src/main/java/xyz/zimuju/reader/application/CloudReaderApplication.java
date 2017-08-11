package xyz.zimuju.reader.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import xyz.zimuju.reader.http.common.HttpUtils;
import xyz.zimuju.reader.util.DebugUtil;

public class CloudReaderApplication extends Application {

    private static CloudReaderApplication cloudReaderApplication;

    public static CloudReaderApplication getInstance() {
        return cloudReaderApplication;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        cloudReaderApplication = this;
        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);

        initTextSize();
    }

    /**
     * 使其系统更改字体大小无效
     */
    @SuppressWarnings("deprecation")
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
