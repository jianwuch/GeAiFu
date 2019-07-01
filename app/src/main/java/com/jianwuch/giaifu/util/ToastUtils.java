package com.jianwuch.giaifu.util;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.Toast;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class ToastUtils {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
