package com.jianwuch.giaifu.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface DownloadService {
    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
