/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package xyz.zimuju.sample.surface.content;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.zimuju.common.base.BaseActivity;
import xyz.zimuju.common.defination.OnBottomDragListener;
import xyz.zimuju.common.util.CommonUtils;
import xyz.zimuju.common.util.DataKeeper;
import xyz.zimuju.common.util.ImageLoaderUtil;
import xyz.zimuju.common.util.SettingUtil;
import xyz.zimuju.common.util.StringUtils;
import xyz.zimuju.common.util.TimeUtils;
import xyz.zimuju.common.widget.AlertDialog;
import xyz.zimuju.common.widget.BottomMenuWindow;
import xyz.zimuju.common.widget.CutPictureActivity;
import xyz.zimuju.common.widget.DatePickerWindow;
import xyz.zimuju.common.widget.EditTextInfoActivity;
import xyz.zimuju.common.widget.EditTextInfoWindow;
import xyz.zimuju.common.widget.ItemDialog;
import xyz.zimuju.common.widget.PlacePickerWindow;
import xyz.zimuju.common.widget.SelectPictureActivity;
import xyz.zimuju.common.widget.ServerSettingActivity;
import xyz.zimuju.common.widget.TimePickerWindow;
import xyz.zimuju.common.widget.TopMenuWindow;
import xyz.zimuju.common.widget.WebViewActivity;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.surface.sample.BottomWindow;

public class DemoMainActivity extends BaseActivity implements View.OnClickListener, OnBottomDragListener, AlertDialog.OnDialogButtonClickListener, ItemDialog.OnDialogItemClickListener, View.OnTouchListener {
    public static final int REQUEST_TO_CAMERA_SCAN = 22;
    private static final String TAG = "DemoMainActivity";
    private static final String[] TOPBAR_COLOR_NAMES = {"灰色", "蓝色", "黄色"};
    private static final int[] TOPBAR_COLOR_RESIDS = {R.color.gray, R.color.blue, R.color.yellow};
    private static final int DIALOG_SET_TOPBAR = 1;
    private static final int REQUEST_TO_SELECT_PICTURE = 20;
    private static final int REQUEST_TO_CUT_PICTURE = 21;
    private static final int REQUEST_TO_EDIT_TEXT_INFO = 23;
    private static final int REQUEST_TO_SERVER_SETTING = 24;
    private static final int REQUEST_TO_DEMO_BOTTOM_WINDOW = 25;
    private static final int REQUEST_TO_TOP_MENU = 30;
    private static final int REQUEST_TO_BOTTOM_MENU = 31;
    private static final int REQUEST_TO_PLACE_PICKER = 32;
    private static final int REQUEST_TO_DATE_PICKER = 33;
    private static final int REQUEST_TO_TIME_PICKER = 34;
    private View rlDemoMainTopbar;
    private ImageView ivDemoMainHead;
    private TextView tvDemoMainHeadName;
    private ScrollView svDemoMain;
    private String picturePath;
    private long touchDownTime = 0;
    private int[] selectedDate = new int[]{1971, 0, 1};
    private int[] selectedTime = new int[]{12, 0, 0};

    public static Intent createIntent(Context context) {
        return new Intent(context, DemoMainActivity.class);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main_activity, this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        exitAnim = R.anim.bottom_push_out;
        rlDemoMainTopbar = findViewById(R.id.rlDemoMainTopbar);
        ivDemoMainHead = findViewById(R.id.ivDemoMainHead, this);
        tvDemoMainHeadName = findViewById(R.id.tvDemoMainHeadName, this);

        svDemoMain = (ScrollView) findViewById(R.id.svDemoMain);
    }


    private void showItemDialog() {
        new ItemDialog(context, TOPBAR_COLOR_NAMES, "选择颜色", DIALOG_SET_TOPBAR, this).show();
    }


    private void showTopMenu() {
        toActivity(TopMenuWindow.createIntent(context, new String[]{"更改导航栏颜色", "更改图片"}), REQUEST_TO_TOP_MENU, false);
    }

    private void selectPicture() {
        toActivity(SelectPictureActivity.createIntent(context), REQUEST_TO_SELECT_PICTURE, false);
    }

    private void cutPicture(String path) {
        if (StringUtils.isFilePath(path) == false) {
            Log.e(TAG, "cutPicture  StringUtil.isFilePath(path) == false >> showShortToast(找不到图片);return;");
            showShortToast("找不到图片");
            return;
        }
        this.picturePath = path;

        toActivity(CutPictureActivity.createIntent(context, path
                , DataKeeper.imagePath, "photo" + System.currentTimeMillis(), 200)
                , REQUEST_TO_CUT_PICTURE);
    }

    private void setPicture(String path) {
        if (StringUtils.isFilePath(path) == false) {
            Log.e(TAG, "setPicture  StringUtil.isFilePath(path) == false >> showShortToast(找不到图片);return;");
            showShortToast("找不到图片");
            return;
        }
        this.picturePath = path;
        svDemoMain.smoothScrollTo(0, 0);
        ImageLoaderUtil.loadImage(ivDemoMainHead, path);
    }

    private void editName(boolean toWindow) {
        if (toWindow) {
            intent = EditTextInfoWindow.createIntent(context, EditTextInfoWindow.TYPE_NICK
                    , "照片名称", StringUtils.getTrimedString(tvDemoMainHeadName), getPackageName());
        } else {
            intent = EditTextInfoActivity.createIntent(context, EditTextInfoActivity.TYPE_NICK
                    , "照片名称", StringUtils.getTrimedString(tvDemoMainHeadName));
        }

        toActivity(intent, REQUEST_TO_EDIT_TEXT_INFO, !toWindow);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

        findViewById(R.id.llDemoMainItemDialog).setOnClickListener(this);
        findViewById(R.id.llDemoMainAlertDialog).setOnClickListener(this);


        findViewById(R.id.llDemoMainScanActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainSelectPictureActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainCutPictureActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainWebViewActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainEditTextInfoActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainServerSettingActivity).setOnTouchListener(this);

        findViewById(R.id.llDemoMainDemoActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoListActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoFragmentActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoTabActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoSQLActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoTimeRefresherActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoBroadcastReceiverActivity).setOnClickListener(this);
        findViewById(R.id.llDemoMainDemoBottomWindow).setOnClickListener(this);


        findViewById(R.id.llDemoMainTopMenuWindow).setOnClickListener(this);
        findViewById(R.id.llDemoMainBottomMenuWindow).setOnClickListener(this);
        findViewById(R.id.llDemoMainEditTextInfoWindow).setOnClickListener(this);
        findViewById(R.id.llDemoMainPlacePickerWindow).setOnClickListener(this);
        findViewById(R.id.llDemoMainDatePickerWindow).setOnClickListener(this);
        findViewById(R.id.llDemoMainTimePickerWindow).setOnClickListener(this);

    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {
        if (isPositive == false) {
            return;
        }

        rlDemoMainTopbar.setBackgroundResource(R.color.red);
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        if (position < 0) {
            position = 0;
        }
        switch (requestCode) {
            case DIALOG_SET_TOPBAR:
                if (position >= TOPBAR_COLOR_RESIDS.length) {
                    position = TOPBAR_COLOR_RESIDS.length - 1;
                }
                rlDemoMainTopbar.setBackgroundResource(TOPBAR_COLOR_RESIDS[position]);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        if (rightToLeft) {
            showTopMenu();
            return;
        }

        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (v.getId() == R.id.llDemoMainServerSettingActivity) {
                    touchDownTime = System.currentTimeMillis();
                    Log.i(TAG, "onTouch MotionEvent.ACTION: touchDownTime=" + touchDownTime);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                if (v.getId() == R.id.llDemoMainServerSettingActivity) {
                    long time = System.currentTimeMillis() - touchDownTime;
                    if (time < 5000 || time > 8000) {
                        showShortToast("请长按5-8秒");
                    } else {
                        toActivity(ServerSettingActivity.createIntent(context
                                , SettingUtil.getServerAddress(false), SettingUtil.getServerAddress(true)
                                , SettingUtil.APP_SETTING, Context.MODE_PRIVATE
                                , SettingUtil.KEY_SERVER_ADDRESS_NORMAL, SettingUtil.KEY_SERVER_ADDRESS_TEST));
                        return true;
                    }
                }
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {//直接调用不会显示v被点击效果
        switch (v.getId()) {
            case R.id.ivDemoMainHead:
                selectPicture();
                break;
            case R.id.tvDemoMainHeadName:
                editName(true);
                break;


            case R.id.llDemoMainItemDialog:
                showItemDialog();
                break;
            case R.id.llDemoMainAlertDialog:
                new AlertDialog(context, "更改颜色", "确定将导航栏颜色改为红色？", true, 0, this).show();
                break;


            case R.id.llDemoMainScanActivity:
                toActivity(ScanActivity.createIntent(context), REQUEST_TO_CAMERA_SCAN);
                break;
            case R.id.llDemoMainSelectPictureActivity:
                selectPicture();
                break;
            case R.id.llDemoMainCutPictureActivity:
                cutPicture(picturePath);
                break;
            case R.id.llDemoMainWebViewActivity:
                toActivity(WebViewActivity.createIntent(context, SettingUtil.isOnTestMode ? "测试服务器" : "正式服务器"
                        , SettingUtil.getCurrentServerAddress()));
                break;
            case R.id.llDemoMainEditTextInfoActivity:
                editName(false);
                break;

            case R.id.llDemoMainDemoActivity:
                toActivity(DemoActivity.createIntent(context, 0));
                break;
            case R.id.llDemoMainDemoListActivity:
                toActivity(DemoListActivity.createIntent(context, 0));
                break;
            case R.id.llDemoMainDemoFragmentActivity:
                toActivity(DemoFragmentActivity.createIntent(context, 0));
                break;
            case R.id.llDemoMainDemoTabActivity:
                toActivity(DemoTabActivity.createIntent(context));
                break;
            case R.id.llDemoMainDemoSQLActivity:
                toActivity(DemoSQLActivity.createIntent(context));
                break;
            case R.id.llDemoMainDemoTimeRefresherActivity:
                toActivity(DemoTimeRefresherActivity.createIntent(context));
                break;
            case R.id.llDemoMainDemoBroadcastReceiverActivity:
                toActivity(DemoBroadcastReceiverActivity.createIntent(context));
                break;
            case R.id.llDemoMainDemoBottomWindow:
                toActivity(BottomWindow.createIntent(context, ""), REQUEST_TO_DEMO_BOTTOM_WINDOW, false);
                break;


            case R.id.llDemoMainTopMenuWindow:
                showTopMenu();
                break;
            case R.id.llDemoMainBottomMenuWindow:
                toActivity(BottomMenuWindow.createIntent(context, TOPBAR_COLOR_NAMES)
                        .putExtra(BottomMenuWindow.INTENT_TITLE, "选择颜色"), REQUEST_TO_BOTTOM_MENU, false);
                break;
            case R.id.llDemoMainEditTextInfoWindow:
                editName(true);
                break;
            case R.id.llDemoMainPlacePickerWindow:
                toActivity(PlacePickerWindow.createIntent(context, getPackageName(), 2), REQUEST_TO_PLACE_PICKER, false);
                break;
            case R.id.llDemoMainDatePickerWindow:
                toActivity(DatePickerWindow.createIntent(context, new int[]{1971, 0, 1}
                        , TimeUtils.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);
                break;
            case R.id.llDemoMainTimePickerWindow:
                toActivity(TimePickerWindow.createIntent(context, selectedTime), REQUEST_TO_TIME_PICKER, false);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_SELECT_PICTURE:
                if (data != null) {
                    cutPicture(data.getStringExtra(SelectPictureActivity.RESULT_PICTURE_PATH));
                }
                break;
            case REQUEST_TO_CUT_PICTURE:
                if (data != null) {
                    setPicture(data.getStringExtra(CutPictureActivity.RESULT_PICTURE_PATH));
                }
                break;
            case REQUEST_TO_CAMERA_SCAN:
                if (data != null) {
                    String result = data.getStringExtra(ScanActivity.RESULT_QRCODE_STRING);
                    CommonUtils.copyText(context, result);
//				toActivity(WebViewActivity.createIntent(context, "扫描结果", result));
                    CommonUtils.openWebSite(context, result);
                }
                break;
            case REQUEST_TO_EDIT_TEXT_INFO:
                if (data != null) {
                    svDemoMain.smoothScrollTo(0, 0);
                    tvDemoMainHeadName.setText(StringUtils.getTrimedString(
                            data.getStringExtra(EditTextInfoWindow.RESULT_VALUE)));
                }
                break;
            case REQUEST_TO_SERVER_SETTING:
                if (data != null) {
                    //TODO
                }
                break;
            case REQUEST_TO_DEMO_BOTTOM_WINDOW:
                if (data != null) {
                    showShortToast(data.getStringExtra(BottomWindow.RESULT_DATA));
                }
                break;

            case REQUEST_TO_TOP_MENU:
                if (data != null) {
                    switch (data.getIntExtra(TopMenuWindow.RESULT_POSITION, -1)) {
                        case 0:
                            showItemDialog();
                            break;
                        case 1:
                            selectPicture();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case REQUEST_TO_BOTTOM_MENU:
                if (data != null) {
                    int selectedPosition = data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1);
                    if (selectedPosition >= 0 && selectedPosition < TOPBAR_COLOR_RESIDS.length) {
                        rlDemoMainTopbar.setBackgroundResource(TOPBAR_COLOR_RESIDS[selectedPosition]);
                    }
                }
                break;

            case REQUEST_TO_PLACE_PICKER:
                if (data != null) {
                    ArrayList<String> placeList = data.getStringArrayListExtra(PlacePickerWindow.RESULT_PLACE_LIST);
                    if (placeList != null) {
                        String place = "";
                        for (String s : placeList) {
                            place += StringUtils.getTrimedString(s);
                        }
                        showShortToast("选择的地区为: " + place);
                    }
                }
                break;
            case REQUEST_TO_DATE_PICKER:
                if (data != null) {
                    ArrayList<Integer> list = data.getIntegerArrayListExtra(DatePickerWindow.RESULT_DATE_DETAIL_LIST);
                    if (list != null && list.size() >= 3) {

                        selectedDate = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            selectedDate[i] = list.get(i);
                        }

                        showShortToast("选择的日期为" + selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
                    }
                }
                break;
            case REQUEST_TO_TIME_PICKER:
                if (data != null) {
                    ArrayList<Integer> list = data.getIntegerArrayListExtra(TimePickerWindow.RESULT_TIME_DETAIL_LIST);
                    if (list != null && list.size() >= 2) {

                        selectedTime = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            selectedTime[i] = list.get(i);
                        }

                        String minute = "" + selectedTime[1];
                        if (minute.length() < 2) {
                            minute = "0" + minute;
                        }
                        showShortToast("选择的时间为" + selectedTime[0] + ":" + minute);
                    }
                }
                break;
            default:
                break;
        }
    }
}