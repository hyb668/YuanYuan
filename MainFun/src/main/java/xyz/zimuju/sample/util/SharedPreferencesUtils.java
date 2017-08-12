package xyz.zimuju.sample.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import xyz.zimuju.common.helper.InitializeHelper;

/**
 * apply与commit异同：
 * 1. apply没有返回值而commit返回boolean表明修改是否提交成功
 * 2. apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘, 而commit是同步的提交到硬件磁盘，
 * 因此，在多个并发的提交commit的时候，他们会等待正在处理的commit保存到磁盘后在操作，从而降低了效率。
 * 而apply只是原子的提交到内容，后面有调用apply的函数的将会直接覆盖前面的内存数据，这样从一定程度上提高了很多效率。
 * 3. apply方法不会提示任何失败的提示。
 * 由于在一个进程中，sharedPreference是单实例，一般不会出现并发冲突，
 * 如果对提交的结果不关心的话，建议使用apply，当然需要确保提交成功且有后续操作的话，还是需要用commit的。
 */
public class SharedPreferencesUtils implements InitializeHelper {
    private static final String DEFAULT_SHARED_PREFERENCES_NAME = "YuanYuan.sdf";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SharedPreferencesUtils sharedPreferencesUtils;

    public static SharedPreferencesUtils getInstance() {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SharedPreferencesUtils();
        }
        return sharedPreferencesUtils;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void initialize(Context context) {
        this.initialize(context, DEFAULT_SHARED_PREFERENCES_NAME);
    }

    @Override
    public void initialize(Context context, String sharedPreferencesName) {
        if (TextUtils.isEmpty(sharedPreferencesName)) {
            // sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GankIOApplication.getInstance());
            sharedPreferences = context.getSharedPreferences(DEFAULT_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public String getString(final String key, final String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public boolean hasKey(String key) {
        return sharedPreferences.contains(key);
    }

    public void clearPreferences(SharedPreferences sharedPreferences) {
        editor.clear();
        editor.commit();
    }
}