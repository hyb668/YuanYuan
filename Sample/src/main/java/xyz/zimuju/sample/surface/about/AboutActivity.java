package xyz.zimuju.sample.surface.about;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import xyz.zimuju.common.basal.BasalActivity;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.util.SpannableStringUtils;

public class AboutActivity extends BasalActivity implements View.OnClickListener {
    @BindView(R.id.header_back_tv)
    TextView back;

    @BindView(R.id.header_title_tv)
    TextView title;

    @BindView(R.id.about_message_tv)
    TextView textView;

    private String titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gank_activity_about;
    }

    @Override
    protected void initData() {
        titleText = "关于";
    }

    @Override
    protected void viewOption() {
        title.setText(titleText);
        back.setVisibility(View.VISIBLE);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.about_msg));
        builder.append("\n");
        builder.append("\n");
        builder.append(SpannableStringUtils.format(this, getString(R.string.about_author), R.style.AboutItemText));
        builder.append("\n");
        builder.append(SpannableStringUtils.format(this, getString(R.string.about_github), R.style.AboutItemText));
        builder.append("\n");
        builder.append(SpannableStringUtils.format(this, getString(R.string.about_blog), R.style.AboutItemText));
        builder.append("\n");
        builder.append(SpannableStringUtils.format(this, getString(R.string.about_weibo), R.style.AboutItemText));
        builder.append("\n");
        builder.append(SpannableStringUtils.format(this, getString(R.string.about_project), R.style.AboutItemText));
        textView.setText(builder.subSequence(0, builder.length()));
    }

    @OnClick(R.id.header_back_tv)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_back_tv:
                finish();
                break;
        }
    }
}
