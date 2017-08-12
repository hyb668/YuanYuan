package xyz.zimuju.common.basal;

import xyz.zimuju.common.util.ToastUtils;

/*
 * @description BaseMainActivity ：主界面的基类，双击back退出
 * @author Nathaniel
 * @time 2017/8/3 - 10:49
 * @version 1.0.0
 */
public abstract class BasalMainActivity extends BasalActivity {
    private static final long MAX_DOUBLE_BACK_DURATION = 1500;
    private long lastBackKeyDownTick = 0;

    @Override
    public void onBackPressed() {
        if (beforeOnBackPressed()) {
            long currentTick = System.currentTimeMillis();
            if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
                ToastUtils.showToast(getContext(), "再按一次退出");
                lastBackKeyDownTick = currentTick;
            } else {
                finish();
            }
        }
    }

    protected boolean beforeOnBackPressed() {
        return true;
    }
}
