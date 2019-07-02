package com.jianwuch.giaifu.module;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jianwuch.giaifu.R;
import com.jianwuch.giaifu.model.BaseHttpResult;
import com.jianwuch.giaifu.model.GifImageBean;
import com.jianwuch.giaifu.model.SearchResultDataBean;
import com.jianwuch.giaifu.module.adapter.SearGifAdapter;
import com.jianwuch.giaifu.net.DownloadService;
import com.jianwuch.giaifu.net.GifImagerRequest;
import com.jianwuch.giaifu.net.RetrofitInstance;
import com.jianwuch.giaifu.util.LogUtil;
import com.jianwuch.giaifu.util.ShareFileUtils;
import com.jianwuch.giaifu.util.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    private static final String KEY_SEARCH_NAME = "search_name";
    private final static String TAG = ItemFragment.class.getSimpleName();

    private final static int PAGE_COUNT = 22;
    private int currentPage = 0;
    private int start = 0;
    private int total;

    private SearGifAdapter mAdapter;
    private String searchName;
    private ViewGroup mRootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout spLayout;

    private boolean isRefleshing;
    private boolean isLoadingMore;

    public ItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ItemFragment newInstance(String name) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SEARCH_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchName = getArguments().getString(KEY_SEARCH_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_item_list, container, false);
        spLayout = mRootView.findViewById(R.id.sp_layout);
        return mRootView;
    }

    private void initView(View view) {
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        mAdapter = new SearGifAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        spLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefleshing) {
                    return;
                }
                currentPage = 0;
                start = 0;
                loadData(searchName);
                isRefleshing = true;
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isLoadingMore) {
                    return;
                }
                start = (++currentPage) * PAGE_COUNT;
                if (total != 0 && start + PAGE_COUNT >= total) {
                    mAdapter.loadMoreEnd();
                }
                loadData(searchName);
                isLoadingMore = true;
            }
        }, recyclerView);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GifImageBean bean = ((GifImageBean) adapter.getData().get(position));
                String gifUrl = bean.url;
                String title = bean.title.trim();
                Toast.makeText(context, "微信分享" + position, Toast.LENGTH_SHORT).show();
                RetrofitInstance.getInstance()
                        .create(DownloadService.class)
                        .downloadFile(gifUrl)
                        .subscribeOn(Schedulers.io())
                        .map(new Function<ResponseBody, InputStream>() {
                            @Override
                            public InputStream apply(ResponseBody responseBody) throws Exception {
                                LogUtil.d(TAG, "当先线程1"+Thread.currentThread().getName());
                                return responseBody.byteStream();
                            }
                        })
                        .observeOn(Schedulers.computation())
                        .doOnNext(new Consumer<InputStream>() {
                            @Override
                            public void accept(InputStream inputStream) throws Exception {
                                LogUtil.d(TAG, "当先线程2"+Thread.currentThread().getName());
                                String path = getActivity().getExternalFilesDir("gif_files")
                                        .getAbsolutePath();
                                File saveFile = new File(path + File.separator + title + ".gif");

                                FileOutputStream fos = new FileOutputStream(saveFile);
                                byte[] b = new byte[1024];
                                int length;

                                while ((length = inputStream.read(b)) > 0) {
                                    fos.write(b, 0, length);
                                }
                                inputStream.close();
                                fos.close();
                            }
                        })
                        .map(new Function<InputStream, File>() {
                            @Override
                            public File apply(InputStream inputStream) throws Exception {
                                LogUtil.d(TAG, "当先线程3"+Thread.currentThread().getName());
                                String path = getActivity().getExternalFilesDir("gif_files")
                                        .getAbsolutePath();

                                File saveFile = new File(path + File.separator + title + ".gif");
                                return saveFile;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) throws Exception {
                                LogUtil.d(TAG, "当先线程4"+Thread.currentThread().getName());
                                ShareFileUtils.shareImageToWeChat(getActivity(),
                                        file.getAbsolutePath());
                            }
                        });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(mRootView);
        loadData(searchName);
    }

    private void loadData(String name) {
        RetrofitInstance.getInstance()
                .create(GifImagerRequest.class)
                .searchGif(name, "timestamp_0", start, PAGE_COUNT, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseHttpResult<SearchResultDataBean>>() {

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "服务器发生错误", Toast.LENGTH_SHORT).show();
                        isRefleshing = false;
                        isLoadingMore = false;
                        mAdapter.loadMoreFail();
                    }

                    @Override
                    public void onComplete() {
                        isRefleshing = false;
                        isLoadingMore = false;
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(
                            BaseHttpResult<SearchResultDataBean> searchResultDataBeanBaseHttpResult) {
                        List<GifImageBean> data = mAdapter.getData();
                        if (isRefleshing) {
                            mAdapter.setNewData(searchResultDataBeanBaseHttpResult.data.list);
                        } else {
                            if (data == null || mAdapter.getData().size() == 0) {
                                mAdapter.setNewData(searchResultDataBeanBaseHttpResult.data.list);
                            } else {
                                mAdapter.addData(searchResultDataBeanBaseHttpResult.data.list);
                            }
                        }

                        mAdapter.loadMoreComplete();
                        mAdapter.notifyDataSetChanged();

                        spLayout.setRefreshing(false);
                        isRefleshing = false;
                        isLoadingMore = false;
                        total = searchResultDataBeanBaseHttpResult.data.resuLtTotal;
                    }
                });
    }

    private void shareToWechat(File file) {
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));  //传输图片或者文件 采用流的方式
        intent.setType("*/*");   //分享文件
        getActivity().startActivity(Intent.createChooser(intent, "分享"));
    }
}
