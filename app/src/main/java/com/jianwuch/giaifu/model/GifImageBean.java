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
public class GifImageBean {

    @SerializedName("flag")
    public int flag;
    @SerializedName("size")
    public int size;
    @SerializedName("width")
    public int width;
    @SerializedName("text")
    public String text;
    @SerializedName("title")
    public String title;
    @SerializedName("category")
    public int category;
    @SerializedName("subText")
    public String subText;
    @SerializedName("url")
    public String url;
    @SerializedName("tags")
    public String tags;
    @SerializedName("sid")
    public String sid;
    @SerializedName("height")
    public int height;

    public static GifImageBean objectFromData(String str) {

        return new Gson().fromJson(str, GifImageBean.class);
    }
}
