package com.jianwuch.giaifu.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class SearchResultDataBean {

    @SerializedName("total")
    public int total;
    @SerializedName("resuLtTotal")
    public int resuLtTotal;
    @SerializedName("list")
    public List<GifImageBean> list;

    public static SearchResultDataBean objectFromData(String str) {

        return new Gson().fromJson(str, SearchResultDataBean.class);
    }
}
