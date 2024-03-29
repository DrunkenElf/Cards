package com.ilnur.cards;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class CustomWeb extends WebView /*implements NestedScrollingChild*/{
    public boolean several;
    public boolean first;
    public CustomWeb second;
    public String data;
    /*public NestedScrollView sv;
    private int mLastMotionY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedYOffset;
    private NestedScrollingChildHelper mChildHelper;*/


    public CustomWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setHorizontalScrollBarEnabled(true);
        /*mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);*/
    }

    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        //this.data = data;
    }

    /*@Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("scale",""+this.getScaleX());
        Log.i("scale",""+this.getWidth());
        //Log.i("scale",""+this.);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //web.getSettings().setLoadWithOverviewMode(true);
            //web.getSettings().setUseWideViewPort(true);
            //ViewGroup.LayoutParams params = web.getLayoutParams();
            //params.width = width;
            //web.
            //params.width = width;
            ///web.setLayoutParams(params);
            if (data!=null)
            loadDataWithBaseURL(null, data, "text/html", "utf-8", "auto:black");
        }
    }*/

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //Log.i("l",""+t);
        //Log.i("getHeight",""+getHeight());
        //Log.i("computeScroll",""+computeVerticalScrollRange());
        //Log.i("",""+);
        /*if (first && several) {
            if (t == computeVerticalScrollRange() - getHeight()) {
                // Trigger listener
                //this.setVisibility(GONE);
                Log.i("scroll", "BOTTOM");
                this.setVisibility(GONE);
                second.setVisibility(VISIBLE);
            }
        }
        if (!first && several) {
            if (t == 0) {
                Log.i("scroll", "TOP");
                this.setVisibility(GONE);
                second.setVisibility(VISIBLE);
            }
        }*/
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            onTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }*/

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(false);
        boolean result = false;

        MotionEvent trackedEvent = MotionEvent.obtain(event);

        final int action = MotionEventCompat.getActionMasked(trackedEvent);

        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }

        int y = (int) trackedEvent.getY();

        trackedEvent.offsetLocation(0, mNestedYOffset);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                result = super.onTouchEvent(trackedEvent);
                mLastMotionY = y;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastMotionY - y;

                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    mLastMotionY = y - mScrollOffset[1];
                    trackedEvent.offsetLocation(0, -mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                //result = super.onTouchEvent(trackedEvent);
                //mLastMotionY = y - mScrollOffset[1];


                int oldY = getScrollY();
                int newScrollY = Math.max(0, oldY + deltaY);
                int dyConsumed = newScrollY - oldY;
                int dyUnconsumed = deltaY - dyConsumed;

                if (dispatchNestedScroll(0, 0, 0, 0, mScrollOffset)) {
                    mLastMotionY -= mScrollOffset[1];
                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }

                result = super.onTouchEvent(trackedEvent);
                //trackedEvent.recycle();
                break;
            *//*case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:*//*
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                ///oldY = getScrollY();
                //Log.i("OLDY", String.valueOf(oldY));
                result = super.onTouchEvent(event);
                break;
        }
        return result;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }*/
}
