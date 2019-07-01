package com.jianwuch.giaifu.module.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.jianwuch.giaifu.model.HotWordsBean;
import com.jianwuch.giaifu.module.ItemFragment;
import java.util.List;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class HotPagerAdapter extends FragmentPagerAdapter {
    private List<HotWordsBean> mData;

    public HotPagerAdapter(FragmentManager fm, List<HotWordsBean> mData) {
        super(fm);
        this.mData = mData;
    }

    @Override
    public Fragment getItem(int i) {
        return ItemFragment.newInstance(mData.get(i).query);
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position).query;
    }
}
