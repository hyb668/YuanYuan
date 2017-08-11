package xyz.zimuju.reader.surface.menu;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xyz.zimuju.reader.R;
import xyz.zimuju.reader.databinding.ActivityNavHomePageBinding;
import xyz.zimuju.reader.util.ShareUtils;
import xyz.zimuju.reader.widget.statusbar.StatusBarUtil;

public class NavHomePageActivity extends AppCompatActivity {

    private ActivityNavHomePageBinding binding;

    public static void startHome(Context mContext) {
        Intent intent = new Intent(mContext, NavHomePageActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_home_page);

        binding.toolbarLayout.setTitle(getString(R.string.app_name));
        StatusBarUtil.setTranslucentForImageView(this, 0, binding.toolbar);
        binding.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.share(v.getContext(), R.string.string_share_text);
            }
        });
    }
}
