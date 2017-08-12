package xyz.zimuju.common.entity;

import java.util.List;

public class ResultData<T> extends StateBean {
    private T data;
    private List<T> list;
    private String json;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
