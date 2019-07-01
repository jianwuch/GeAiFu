package com.jianwuch.giaifu.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class HotWordsBean {

    @SerializedName("id")
    public int id;

    /**
     * 名字
     */
    @SerializedName("query")
    public String query;
    @SerializedName("num")
    public int num;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("status")
    public String status;

    public static HotWordsBean objectFromData(String str) {

        return new Gson().fromJson(str, HotWordsBean.class);
    }
}
