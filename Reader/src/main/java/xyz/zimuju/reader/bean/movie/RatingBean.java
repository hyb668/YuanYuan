package xyz.zimuju.reader.bean.movie;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import xyz.zimuju.reader.BR;
import xyz.zimuju.reader.http.common.ParamNames;


public class RatingBean extends BaseObservable implements Serializable {
    /**
     * max : 10
     * average : 6.9
     * stars : 35
     * min : 0
     */
    @ParamNames("max")
    private int max;
    @ParamNames("average")
    private double average;
    @ParamNames("stars")
    private String stars;
    @ParamNames("min")
    private int min;

    @Bindable
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }

    @Bindable
    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
        notifyPropertyChanged(BR.average);
    }

    @Bindable
    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
        notifyPropertyChanged(BR.stars);
    }

    @Bindable
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        notifyPropertyChanged(BR.min);
    }
}
