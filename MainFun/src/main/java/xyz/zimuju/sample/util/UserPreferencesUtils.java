package xyz.zimuju.sample.util;

import android.content.Context;

import cn.bmob.v3.BmobUser;
import xyz.zimuju.common.util.EmptyUtil;
import xyz.zimuju.sample.entity.bomb.BombUser;

public class UserPreferencesUtils extends SharedPreferencesUtils {
    private static final String USER_SHARED_PREFERENCES_NAME = "YuanYuan_User.sdf";
    private static UserPreferencesUtils userPreferencesUtils;

    public static UserPreferencesUtils getInstance() {
        if (userPreferencesUtils == null) {
            userPreferencesUtils = new UserPreferencesUtils();
        }
        return userPreferencesUtils;
    }

    public void initialize(Context context) {
        this.initialize(context, USER_SHARED_PREFERENCES_NAME);
    }

    @Override
    public void initialize(Context context, String sharedPreferencesName) {
        SharedPreferencesUtils.getInstance().initialize(context, sharedPreferencesName);
    }

    public void saveUser(BmobUser bmobUser) {
        if (EmptyUtil.isNotEmpty(bmobUser)) {
            userPreferencesUtils.putString("username", EmptyUtil.isNotEmpty(bmobUser.getUsername()) ? bmobUser.getUsername() : "");
            userPreferencesUtils.putString("phone", EmptyUtil.isNotEmpty(bmobUser.getMobilePhoneNumber()) ? bmobUser.getMobilePhoneNumber() : "");
            userPreferencesUtils.putString("avator", EmptyUtil.isNotEmpty(bmobUser.getEmail()) ? bmobUser.getEmail() : "");
            AuthorityUtils.setLogin(true);
        }
    }

    public BmobUser getUser() {
        BombUser bombUser = new BombUser();
        bombUser.setUsername(userPreferencesUtils.getString("username", ""));
        bombUser.setMobilePhoneNumber(userPreferencesUtils.getString("phone", ""));
        bombUser.setEmail(userPreferencesUtils.getString("avator", ""));
        return bombUser;
    }
}
