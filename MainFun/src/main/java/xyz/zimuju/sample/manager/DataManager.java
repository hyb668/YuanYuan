package xyz.zimuju.sample.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import xyz.zimuju.common.helper.InitializeHelper;
import xyz.zimuju.common.util.StringUtils;
import xyz.zimuju.sample.entity.User;

public class DataManager implements InitializeHelper {
    private static DataManager instance;
    public final String KEY_USER = "KEY_USER";
    public final String KEY_USER_ID = "KEY_USER_ID";
    public final String KEY_USER_NAME = "KEY_USER_NAME";
    public final String KEY_USER_PHONE = "KEY_USER_PHONE";

    public final String KEY_CURRENT_USER_ID = "KEY_CURRENT_USER_ID";
    public final String KEY_LAST_USER_ID = "KEY_LAST_USER_ID";
    private final String TAG = "DataManager";
    private Context context;
    private String PATH_USER = "PATH_USER";

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    @Override
    public void initialize(Context context, String sharedPreferencesName) {
        this.context = context;
    }

    public boolean isCurrentUser(long userId) {
        return userId > 0 && userId == getCurrentUserId();
    }

    public long getCurrentUserId() {
        User user = getCurrentUser();
        return user == null ? 0 : user.getId();
    }

    public String getCurrentUserPhone() {
        User user = getCurrentUser();
        return user == null ? "" : user.getPhone();
    }

    public void setCurrentUserPhone(String phone) {
        User user = getCurrentUser();
        if (user == null) {
            user = new User();
        }
        user.setPhone(phone);
        saveUser(user);
    }

    public User getCurrentUser() {
        SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
        return sdf == null ? null : getUser(sdf.getLong(KEY_CURRENT_USER_ID, 0));
    }


    public String getLastUserPhone() {
        User user = getLastUser();
        return user == null ? "" : user.getPhone();
    }


    public User getLastUser() {
        SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
        return sdf == null ? null : getUser(sdf.getLong(KEY_LAST_USER_ID, 0));
    }


    public User getUser(long userId) {
        SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
        if (sdf == null) {
            Log.e(TAG, "get sdf == null >>  return;");
            return null;
        }
        Log.i(TAG, "getUser  userId = " + userId);
        return JSON.parseObject(sdf.getString(StringUtils.getTrimedString(userId), null), User.class);
    }


    public void saveCurrentUser(User user) {
        SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
        if (sdf == null) {
            Log.e(TAG, "saveUser sdf == null  >> return;");
            return;
        }
        if (user == null) {
            Log.w(TAG, "saveUser  user == null >>  user = new User();");
            user = new User();
        }
        SharedPreferences.Editor editor = sdf.edit();
        editor.remove(KEY_LAST_USER_ID).putLong(KEY_LAST_USER_ID, getCurrentUserId());
        editor.remove(KEY_CURRENT_USER_ID).putLong(KEY_CURRENT_USER_ID, user.getId());
        editor.commit();

        saveUser(sdf, user);
    }

    public void saveUser(User user) {
        saveUser(context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE), user);
    }

    public void saveUser(SharedPreferences sdf, User user) {
        if (sdf == null || user == null) {
            Log.e(TAG, "saveUser sdf == null || user == null >> return;");
            return;
        }
        String key = StringUtils.getTrimedString(user.getId());
        Log.i(TAG, "saveUser  key = user.getId() = " + user.getId());
        sdf.edit().remove(key).putString(key, JSON.toJSONString(user)).commit();
    }

    public void removeUser(SharedPreferences sdf, long userId) {
        if (sdf == null) {
            Log.e(TAG, "removeUser sdf == null  >> return;");
            return;
        }
        sdf.edit().remove(StringUtils.getTrimedString(userId)).commit();
    }

    public void setCurrentUserName(String name) {
        User user = getCurrentUser();
        if (user == null) {
            user = new User();
        }
        user.setUsername(name);
        saveUser(user);
    }
}
