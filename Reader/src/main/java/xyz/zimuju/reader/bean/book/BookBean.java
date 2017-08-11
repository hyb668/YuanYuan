package xyz.zimuju.reader.bean.book;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.List;

import xyz.zimuju.reader.BR;
import xyz.zimuju.reader.http.common.ParamNames;

public class BookBean extends BaseObservable implements Serializable {
    @ParamNames("count")
    private int count;
    @ParamNames("start")
    private int start;
    @ParamNames("total")
    private int total;
    @ParamNames("books")
    private List<BooksBean> books;

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    @Bindable
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
        notifyPropertyChanged(BR.start);
    }

    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public List<BooksBean> getBooks() {
        return books;
    }

    public void setBooks(List<BooksBean> books) {
        this.books = books;
        notifyPropertyChanged(BR.books);
    }
}
