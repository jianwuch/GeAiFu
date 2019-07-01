package com.jianwuch.giaifu;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jianwuch.giaifu.base.BaseActivity;
import com.jianwuch.giaifu.model.BaseHttpResult;
import com.jianwuch.giaifu.model.SearchResultDataBean;
import com.jianwuch.giaifu.module.adapter.SearGifAdapter;
import com.jianwuch.giaifu.net.GifImagerRequest;
import com.jianwuch.giaifu.net.RetrofitInstance;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jove.chen
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener,
        TextView.OnEditorActionListener {

    private RecyclerView recyclerView;
    private Button btnSearch;
    private EditText editText;
    private SearGifAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                loadData("哈哈");
            } else {
            }
        });
    }

    private void loadData(String name) {
        RetrofitInstance.getInstance()
                .create(GifImagerRequest.class)
                .searchGif(name, "timestamp_0", 0, 22, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseHttpResult<SearchResultDataBean>>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(
                            BaseHttpResult<SearchResultDataBean> searchResultDataBeanBaseHttpResult) {
                        mAdapter.setNewData(searchResultDataBeanBaseHttpResult.data.list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void findView() {
        btnSearch = findViewById(R.id.btnSearch);
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new SearGifAdapter(R.layout.search_item);
        recyclerView.setAdapter(mAdapter);

        btnSearch.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                doSearch();
                break;
        }
    }

    private void doSearch() {
        String searchStr = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            loadData(searchStr);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            doSearch();
            return true;
        }

        return false;
    }
}
