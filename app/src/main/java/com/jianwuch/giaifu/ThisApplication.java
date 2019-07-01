package com.jianwuch.giaifu;

import android.app.Application;
import com.jianwuch.giaifu.net.RetrofitInstance;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class ThisApplication extends Application {
    private static ThisApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        RetrofitInstance.getInstance().init();
    }

    public static ThisApplication getApplication() {
        return application;
    }
}
