package com.jianwuch.giaifu.net;

import com.jianwuch.giaifu.model.BaseHttpResult;
import com.jianwuch.giaifu.model.HotWordsList;
import com.jianwuch.giaifu.model.SearchResultDataBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface GifImagerRequest {
    /**
     * search gif
     */
    @GET("/material/query")
    Observable<BaseHttpResult<SearchResultDataBean>> searchGif(@Query("query") String name,
            @Query("sortField") String sortField, @Query("start") int start,
            @Query("size") int size, @Query("iamgeType") int imageType);

    /**
     * 获取热门词
     * @return
     */
    @GET("/hotword")
    Observable<BaseHttpResult<HotWordsList>> getHotWords();


}
