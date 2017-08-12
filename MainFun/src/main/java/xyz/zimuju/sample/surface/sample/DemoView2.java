package xyz.zimuju.sample.surface.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.zimuju.common.base.BaseView;
import xyz.zimuju.common.entity.Entry;
import xyz.zimuju.common.util.StringUtils;
import xyz.zimuju.sample.R;

public class DemoView2 extends BaseView<Entry<String, String>> implements View.OnClickListener {
    private static final String TAG = "DemoView";
    public ImageView ivDemoViewHead;
    public TextView tvDemoViewName;
    public TextView tvDemoViewNumber;

    public DemoView2(Activity context, Resources resources) {
        super(context, resources);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView(LayoutInflater inflater) {
        //TODO demo_view改为你所需要的layout文件，可以根据viewType使用不同layout
        convertView = inflater.inflate(R.layout.demo_view, null);
        ivDemoViewHead = findViewById(R.id.ivDemoViewHead, this);
        tvDemoViewName = findViewById(R.id.tvDemoViewName, this);
        tvDemoViewNumber = findViewById(R.id.tvDemoViewNumber);
        return convertView;
    }


    @Override
    public void bindView(Entry<String, String> data) {
        if (data == null) {
            Log.e(TAG, "bindView data == null >> data = new Entry<>(); ");
            data = new Entry<String, String>();
        }
        this.data = data;

        tvDemoViewName.setText(StringUtils.getTrimedString(data.getKey()));
        tvDemoViewNumber.setText(StringUtils.getTrimedString(data.getValue()));
    }


    //示例代码<<<<<<<<<<<<<<<<
    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
            return;
        }
        if (data == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tvDemoViewName:
                data.setKey("New " + data.getKey());
                bindView(data);
                if (onDataChangedListener != null) {
                    onDataChangedListener.onDataChanged();
                }
                break;
            default:
                break;
        }
    }
    //示例代码>>>>>>>>>>>>>>>>


}
