package xyz.zimuju.common.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static xyz.zimuju.common.cache.CacheManager.Strategy.MEMORY_FIRST;

/**
 * lru 缓存在某些手机上会崩溃，暂时用preferences来做缓存
 */
public class CacheManager {
    private static final String PREF_NAME = "master_cache";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static CacheManager cacheManager;
    private CacheManager.Strategy mStrategy = MEMORY_FIRST;
    //线程池
    private ExecutorService mExecutor = null;
    //内存缓存
    private MemoryCache mMemoryCache;
    //Disk缓存
    private DiskCache mDiskCache;

    private CacheManager(Context context, Strategy strategy) {
        this.mStrategy = strategy;
        init(context);
    }

    public static synchronized CacheManager getInstance(Context context, boolean cacheEnable) {
        if (cacheEnable) {
            return getInstance(context, MEMORY_FIRST);
        } else {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
            }
            return cacheManager;
        }
    }

    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public static void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public static synchronized CacheManager getInstance(Context context, Strategy strategy) {
        if (cacheManager == null) {
            cacheManager = new CacheManager(context.getApplicationContext(), strategy);
        } else {
            cacheManager.setStrategy(strategy);
        }
        return cacheManager;
    }

    public void setStrategy(Strategy strategy) {
        this.mStrategy = strategy;
        switch (mStrategy) {
            case MEMORY_FIRST:
                if (!mMemoryCache.hasEvictedListener()) {
                    mMemoryCache.setEvictedListener(new MemoryCache.EvictedListener() {
                        @Override
                        public void handleEvictEntry(String evictKey, String evictValue) {
                            mDiskCache.put(evictKey, evictValue);
                        }
                    });
                }
                break;

            case MEMORY_ONLY:
                if (mMemoryCache.hasEvictedListener())
                    mMemoryCache.setEvictedListener(null);
                break;

            case DISK_ONLY:
                break;
        }
    }

    public String getCurStrategy() {
        return mStrategy.name();
    }

    /**
     * 初始化 DiskLruCache
     */
    private void init(Context context) {
        mExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        mDiskCache = new DiskCache(context);
        mMemoryCache = new MemoryCache();
    }

    /**
     * 从缓存中读取value
     */
    public String readCache(final String key) {
        Future<String> ret = mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result = null;
                switch (mStrategy) {
                    case MEMORY_ONLY:
                        result = mMemoryCache.get(key);
                        break;
                    case MEMORY_FIRST:
                        result = mMemoryCache.get(key);
                        if (result == null) {
                            result = mDiskCache.get(key);
                        }
                        break;
                    case DISK_ONLY:
                        result = mDiskCache.get(key);
                        break;
                }
                return result;
            }
        });
        try {
            return ret.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将value 写入到缓存中
     */
    public void writeCache(final String key, final String value) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                switch (mStrategy) {
                    case MEMORY_FIRST:
                        mMemoryCache.put(key, value);
                        mDiskCache.put(key, value);
                        break;
                    case MEMORY_ONLY:
                        mMemoryCache.put(key, value);
                        break;
                    case DISK_ONLY:
                        mDiskCache.put(key, value);
                        break;
                }
            }
        });
    }

    enum Strategy {
        MEMORY_ONLY(0), MEMORY_FIRST(1), DISK_ONLY(3);
        int id;

        Strategy(int id) {
            this.id = id;
        }
    }
}
