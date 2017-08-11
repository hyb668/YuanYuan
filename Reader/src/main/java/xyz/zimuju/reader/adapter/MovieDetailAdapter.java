package xyz.zimuju.reader.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import xyz.zimuju.reader.R;
import xyz.zimuju.reader.base.BaseRecyclerViewAdapter;
import xyz.zimuju.reader.base.BaseRecyclerViewHolder;
import xyz.zimuju.reader.bean.movie.PersonBean;
import xyz.zimuju.reader.databinding.ItemMovieDetailPersonBinding;
import xyz.zimuju.reader.util.PerfectClickListener;
import xyz.zimuju.reader.widget.webview.WebViewActivity;


public class MovieDetailAdapter extends BaseRecyclerViewAdapter<PersonBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_movie_detail_person);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<PersonBean, ItemMovieDetailPersonBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final PersonBean bean, int position) {
            binding.setPersonBean(bean);
            binding.llItem.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (bean != null && !TextUtils.isEmpty(bean.getAlt())) {
                        WebViewActivity.loadUrl(v.getContext(), bean.getAlt(), bean.getName());
                    }
                }
            });
        }
    }
}
