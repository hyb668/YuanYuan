package xyz.zimuju.sample.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.entity.bomb.CollectTable;
import xyz.zimuju.sample.surface.gank.WebViewActivity;
import xyz.zimuju.sample.util.DateUtils;
import xyz.zimuju.sample.util.DialogUtils;
import xyz.zimuju.sample.util.SnackBarUtils;

public class CollectViewProvider extends ItemViewProvider<CollectTable, CollectViewProvider.ViewHolder> {

    private RecyclerView recyclerView;

    public CollectViewProvider(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.gank_item_collect, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final CollectTable collect) {
        holder.tv_time.setText(DateUtils.friendlyTime(String.valueOf(collect.getCreatedAt())));
        holder.tv_tag.setText(collect.getType());
        holder.tv_desc.setText(collect.getDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.start(holder.itemView.getContext(), collect.getDesc(), collect.getUrl());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CollectTable deleteBean = new CollectTable();
                deleteBean.setObjectId(collect.getObjectId());
                DialogUtils.showUnDoCollectDialog(v, deleteBean, new UpdateListener() {


                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            int position = getPosition(holder);
                            MultiTypeAdapter adapter = (MultiTypeAdapter) getAdapter();
                            adapter.getItems().remove(position);
                            recyclerView.getAdapter().notifyItemRemoved(position);
                            recyclerView.getAdapter().notifyItemChanged(position, adapter.getItemCount());
                        } else {
                            SnackBarUtils.makeShort(holder.itemView, "删除失败").danger();
                        }
                    }
                });
                return true;
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_people;
        TextView tv_time;
        TextView tv_tag;
        TextView tv_desc;

        ViewHolder(View itemView) {
            super(itemView);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_people = (TextView) itemView.findViewById(R.id.tv_people);
        }
    }
}