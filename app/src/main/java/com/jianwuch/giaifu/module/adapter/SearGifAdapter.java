package com.jianwuch.giaifu.module.adapter;

import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jianwuch.giaifu.R;
import com.jianwuch.giaifu.ThisApplication;
import com.jianwuch.giaifu.model.GifImageBean;
import java.util.Random;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class SearGifAdapter extends BaseQuickAdapter<GifImageBean, BaseViewHolder> {
    private Vibrator mVibrator;

    public SearGifAdapter(int layoutResId) {
        super(layoutResId);
    }

    public SearGifAdapter() {
        super(R.layout.search_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, GifImageBean item) {
        ImageView imageView = helper.getView(R.id.gifImage);
        Glide.with(mContext).load(item.url).into(imageView);
        helper.setBackgroundColor(R.id.gifImage, Color.parseColor(getRandColor()));
        helper.setText(R.id.textView, item.title);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "长按", Toast.LENGTH_SHORT).show();
                if (mVibrator == null) {
                    mVibrator = (Vibrator) ThisApplication.getApplication()
                            .getSystemService(Service.VIBRATOR_SERVICE);
                }
                if (mVibrator.hasVibrator()) {
                    mVibrator.cancel();
                    mVibrator.vibrate(300);
                }
                return true;
            }
        });

        helper.addOnClickListener(R.id.btnSendToWechat);
    }

    public String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }
}
