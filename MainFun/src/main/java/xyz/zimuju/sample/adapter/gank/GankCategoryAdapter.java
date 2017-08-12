package xyz.zimuju.sample.adapter.gank;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.zimuju.common.basal.MRecyclerViewAdapter;
import xyz.zimuju.sample.R;
import xyz.zimuju.sample.entity.gank.Category;

/*
 * @description GankCategoryAdapter :
 * @author Nathaniel
 * @time 2017/8/12 - 14:02
 * @version 1.0.0
 */
public class GankCategoryAdapter extends MRecyclerViewAdapter<Category> {

    private int index = 0;
    private ViewGroup viewGroup;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewGroup = parent;
        View convertView = getView(R.layout.item_category_list, parent);
        return new CategoryViewHolder(convertView);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;
        Category category = dataList.get(position);
        if (category != null) {
            categoryViewHolder.categoryName.setText(category.getName());
        }

        if (index == position) {
            categoryViewHolder.categoryName.setTextColor(context.getResources().getColor(R.color.navigation_text_normal));
            categoryViewHolder.categoryDivide.setVisibility(View.VISIBLE);
        } else {
            categoryViewHolder.categoryName.setTextColor(context.getResources().getColor(R.color.navigation_text_pressed));
            categoryViewHolder.categoryDivide.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryName, categoryDivide;


        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.item_category_name_tv);
            categoryDivide = (TextView) itemView.findViewById(R.id.item_category_divide_tv);
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
