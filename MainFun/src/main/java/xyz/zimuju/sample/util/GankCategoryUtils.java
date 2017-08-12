package xyz.zimuju.sample.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import xyz.zimuju.sample.R;
import xyz.zimuju.sample.entity.gank.Category;

public class GankCategoryUtils {
    public static List<Category> getCategoryList(Context context) {
        List<Category> categoryList = new ArrayList<>();
        String[] categoryNames = context.getResources().getStringArray(R.array.gank_category_names);
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName(categoryNames[i]);
            categoryList.add(category);
        }
        return categoryList;
    }
}
