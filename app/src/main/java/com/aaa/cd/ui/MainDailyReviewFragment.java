package com.aaa.cd.ui;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.ui.review.BaseCardItem;
import com.aaa.cd.ui.review.CardAdapter;
import com.aaa.cd.ui.review.CardFragment;
import com.aaa.cd.ui.review.ImageCardItem;
import com.aaa.cd.ui.review.ImageUrls;
import com.aaa.cd.ui.review.ScrollCardItem;
import com.aaa.cd.ui.review.Utils;
import com.beyondsw.lib.widget.StackCardsView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainDailyReviewFragment extends MainBaseFragment implements Handler.Callback,StackCardsView.OnCardSwipedListener
{
    private static final String TAG ="Daily Review";
    private StackCardsView mCardsView;
    private CardAdapter mAdapter;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private Handler mMainHandler;
    private static final int MSG_START_LOAD_DATA = 1;
    private static final int MSG_DATA_LOAD_DONE = 2;
    private volatile int mStartIndex;
    private static final int PAGE_COUNT = 10;
    //commit test
    private CardFragment.Callback mCallback;

    public interface Callback
    {
        void onViewPagerCbChanged(boolean checked);
    }

    public MainDailyReviewFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void initTitle(View view)
    {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_review);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainCallback.openMenu(true);
            }
        });
    }

    @Override
    public void initView(View view)
    {

//        mCallback.onViewPagerCbChanged(mCb.isChecked());
        mCardsView = Utils.findViewById(view, R.id.cards);
        mCardsView.addOnCardSwipedListener(this);
        mAdapter = new CardAdapter();
        mCardsView.setAdapter(mAdapter);
        mMainHandler = new Handler(this);
        mWorkThread = new HandlerThread("data_loader");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper(), this);
        mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();
    }

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_main_article;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCardsView.removeOnCardSwipedListener(this);
        mWorkThread.quit();
        mWorkHandler.removeMessages(MSG_START_LOAD_DATA);
        mMainHandler.removeMessages(MSG_DATA_LOAD_DONE);
        mStartIndex = 0;
    }


    @Override
    public void onCardDismiss(int direction) {
        mAdapter.remove(0);
        if (mAdapter.getCount() < 3) {
            if (!mWorkHandler.hasMessages(MSG_START_LOAD_DATA)) {
                mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();
            }
        }
    }

    @Override
    public void onCardScrolled(View view, float progress, int direction) {
        Log.d(TAG, "onCardScrolled: view=" + view.hashCode() + ", progress=" + progress + ",direction=" + direction);

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case MSG_START_LOAD_DATA:{
                List<BaseCardItem> data = loadData(mStartIndex);
                mMainHandler.obtainMessage(MSG_DATA_LOAD_DONE,data).sendToTarget();
                break;
            }
            case MSG_DATA_LOAD_DONE:{
                List<BaseCardItem> data = (List<BaseCardItem>) msg.obj;
                mAdapter.appendItems(data);
                mStartIndex += sizeOfImage(data);
                break;
            }
        }
        return true;
    }

    private int sizeOfImage(List<BaseCardItem> items){
        if(items==null){
            return 0;
        }
        int size = 0;
        for (BaseCardItem item : items) {
            if (item instanceof ImageCardItem) {
                size++;
            }
        }
        return size;
    }


    private List<BaseCardItem> loadData(int startIndex) {
        if (startIndex < ImageUrls.images.length) {
            final int endIndex = Math.min(mStartIndex + PAGE_COUNT, ImageUrls.images.length - 1);
            List<BaseCardItem> result = new ArrayList<>(endIndex - startIndex + 1);
            for (int i = startIndex; i <= endIndex; i++) {
                ImageCardItem item = new ImageCardItem(getActivity(), ImageUrls.images[i], ImageUrls.labels[i]);
                item.dismissDir = StackCardsView.SWIPE_ALL;
                item.fastDismissAllowed = true;
                result.add(item);
            }
            if (startIndex == 0) {
                ScrollCardItem item = new ScrollCardItem(getActivity());
                result.add(result.size() / 2, item);
            }
            return result;
        }
        return null;
    }

}
