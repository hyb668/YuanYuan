package xyz.zimuju.sample.adapter.gank;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.zimuju.common.basal.MRecyclerViewAdapter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.entity.gank.Gank;
import xyz.zimuju.sample.loader.ImageLoader;

/*
 * @description GankNewsAdapter :
 * @author Nathaniel
 * @time 2017/8/12 - 14:02
 * @version 1.0.0
 */
public class GankNewsAdapter extends MRecyclerViewAdapter<Gank> {
    private ViewGroup viewGroup;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewGroup = parent;
        View convertView = getView(R.layout.item_gank_list, parent);
        return new GankViewHolder(convertView);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GankViewHolder gankViewHolder = (GankViewHolder) viewHolder;
        Gank gank = dataList.get(position);
        if (gank != null) {
            gankViewHolder.textView.setText(gank.getCreatedAt());
            ImageLoader.displayImage(gankViewHolder.imageView, gank.getUrl());
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class GankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;


        GankViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_gank_create_tv);
            imageView = (ImageView) itemView.findViewById(R.id.item_gank_image_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(viewGroup, view, getAdapterPosition());
            }
        }
    }
}
