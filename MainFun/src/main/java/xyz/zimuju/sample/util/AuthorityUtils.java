package xyz.zimuju.sample.util;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import xyz.zimuju.sample.entity.content.WeiBo;

public class AuthorityUtils {

    public static void login(WeiBo result) {
        setUserName(result.getScreen_name());
        setDescription(result.getDescription());
        setAvatar(result.getAvatar_large());
        setLogin(true);
    }

    public static void logout() {
        setUserName("");
        setDescription("");
        setAvatar("");
        setLogin(false);
    }

    public static boolean isLogin() {
        return UserPreferencesUtils.getInstance().getBoolean("logged", false);
    }

    public static void setLogin(boolean isLogin) {
        UserPreferencesUtils.getInstance().putBoolean("logged", isLogin);
    }

    public static String getUserName() {
        return UserPreferencesUtils.getInstance().getString("username", "");
    }

    public static void setUserName(String username) {
        UserPreferencesUtils.getInstance().putString("username", username);
    }

    public static String getDescription() {
        return UserPreferencesUtils.getInstance().getString("signature", "未填写");
    }

    public static void setDescription(String desc) {
        UserPreferencesUtils.getInstance().putString("signature", desc);
    }

    public static String getAvatar() {
        return UserPreferencesUtils.getInstance().getString("portrait", "");
    }

    public static void setAvatar(String avatar) {
        UserPreferencesUtils.getInstance().putString("portrait", avatar);
    }


    //新浪微博相关

    public static String getAccessToken() {
        return SharedPreferencesUtils.getInstance().getString("accessToken", "");
    }

    public static void setAccessToken(String access_token) {
        SharedPreferencesUtils.getInstance().putString("accessToken", access_token);
    }

    public static String getUid() {
        return SharedPreferencesUtils.getInstance().getString("uid", "");
    }

    public static void setUid(String uid) {
        SharedPreferencesUtils.getInstance().putString("uid", uid);
    }

    public static String getRefreshToken() {
        return SharedPreferencesUtils.getInstance().getString("refreshToken", "");
    }

    public static void setRefreshToken(String refresh_token) {
        SharedPreferencesUtils.getInstance().putString("refreshToken", refresh_token);
    }

    public static long getExpiresIn() {
        return SharedPreferencesUtils.getInstance().getLong("expiresIn", 0);
    }

    public static void setExpiresIn(long expires_in) {
        SharedPreferencesUtils.getInstance().putLong("expiresIn", expires_in);
    }

    /*
     * @function 从 SharedPreferences 读取 Token 信息
     * @author Nathaniel-nathanwriting@126.com
     * @time 17-8-3-下午9:37
     * @return Oauth2AccessToken token
     * @version v1.0.0
     */
    public static Oauth2AccessToken readOauth2AccessToken() {
        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setUid(getUid());
        token.setToken(getAccessToken());
        token.setRefreshToken(getRefreshToken());
        token.setExpiresTime(getExpiresIn());
        return token;
    }

    /*
     * @function 保存 Token 对象到 SharedPreferences
     * @author Nathaniel-nathanwriting@126.com
     * @time 17-8-3-下午9:39
     * @parameters Oauth2AccessToken token
     * @return void
     * @version v1.0.0
     */
    public static void writeAccessToken(Oauth2AccessToken token) {
        setUid(token.getUid());
        setAccessToken(token.getToken());
        setRefreshToken(token.getRefreshToken());
        setExpiresIn(token.getExpiresTime());
    }

}
