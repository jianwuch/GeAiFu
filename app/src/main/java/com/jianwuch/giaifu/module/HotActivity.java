package com.jianwuch.giaifu.module;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.jianwuch.giaifu.R;
import com.jianwuch.giaifu.SearchActivity;
import com.jianwuch.giaifu.base.BaseActivity;
import com.jianwuch.giaifu.model.BaseHttpResult;
import com.jianwuch.giaifu.model.HotWordsBean;
import com.jianwuch.giaifu.model.HotWordsList;
import com.jianwuch.giaifu.module.adapter.HotPagerAdapter;
import com.jianwuch.giaifu.net.GifImagerRequest;
import com.jianwuch.giaifu.net.RetrofitInstance;
import com.jianwuch.giaifu.util.LogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * 首页热门
 *
 * @author jove.chen
 */
public class HotActivity extends BaseActivity {
    private static final String TAG = HotActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HotPagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);

        initView();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
                .subscribe(granted -> {
                    if (granted) {
                        loadHotWords();
                    } else {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hot_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadHotWords() {
        LogUtil.d(TAG, "开始获取数据");
        RetrofitInstance.getInstance()
                .create(GifImagerRequest.class)
                .getHotWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseHttpResult<HotWordsList>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(BaseHttpResult<HotWordsList> listBaseHttpResult) {
                        onGetHotWords(listBaseHttpResult.data.list);
                        mPageAdapter = new HotPagerAdapter(getSupportFragmentManager(),
                                listBaseHttpResult.data.list);
                        viewPager.setAdapter(mPageAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG, "error");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "onComplete");
                    }
                });
    }

    private void onGetHotWords(List<HotWordsBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            LogUtil.d(TAG, list.get(i).query);
            tabLayout.addTab(tabLayout.newTab().setText(list.get(i).query));
        }
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.vp_main);

        tabLayout.setupWithViewPager(viewPager);
    }
}
