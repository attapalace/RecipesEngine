package com.examples.apps.atta.recipesengine.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonResponse {

    @SerializedName("q")
    @Expose
    private String q;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
//    @SerializedName("params")
//    @Expose
//    private Params params;
    @SerializedName("more")
    @Expose
    private Boolean more;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("hits")
    @Expose
    private ArrayList<Hit> hits = null;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

//    public Params getParams() {
//        return params;
//    }
//
//    public void setParams(Params params) {
//        this.params = params;
//    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Hit> getHits() {
        return hits;
    }

    public void setHits(ArrayList<Hit> hits) {
        this.hits = hits;
    }
}
