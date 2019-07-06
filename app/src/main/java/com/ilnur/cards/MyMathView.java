package com.ilnur.cards;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class MyMathView extends WebView implements NestedScrollingChild {
    private String mText;
    private String mConfig;
    private int mEngine;

    public static final String TAG = MyMathView.class.getSimpleName();
    private int mLastMotionY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedYOffset;
    private NestedScrollingChildHelper mChildHelper;
    /*private String strBody = "<html>"
            + "<head>"
            + "     <style type=\"text/css\">"
            + "     .center {"
            + "         line-height: 30;"
            + "         height: 200;"
            + "         text-align: center;"
            + "         }"
            + "     .center p {"
            + "         line-height: 1.5;"
            + "         display: inline-block;"
            + "         vertical-align: middle;"
            + "     }"
            + "     </style>"
            + ""
            + "</head>"
            + "<body> <div class=\"center\"> <p>";*/
    private String strBody = "<html>"
            + "<head>"
            + "     <style type=\"text/css\">"
            + ".center {"
            + "padding: 70px 0;"
            + "text-align: center;"
            + "}"
            + "     </style>"
            + ""
            + "</head>"
            + "<body> <div class=\"center\"> <p>";


    //private int i;

    /*private int mLastY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedOffsetY;*/


    public MyMathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        getSettings().setJavaScriptEnabled(true);

        //int i = getMeasuredHeight();
        //this.setHorizontalScrollBarEnabled(true);
        //this.setVerticalScrollBarEnabled(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDisplayZoomControls(false);

        //this.setInitialScale(1);

        //getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        //getSettings().setSupportZoom(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setDomStorageEnabled(false);


        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        setBackgroundColor(Color.TRANSPARENT);

        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try { // the order of execution of setEngine() and setText() matters
            setEngine(mTypeArray.getInteger(R.styleable.MathView_engine, 0));
            setText(mTypeArray.getString(R.styleable.MathView_text));
        } finally {
            mTypeArray.recycle();
        }
    }

    // disable touch event on MathView


    /*@Override
    public boolean performClick() {
        return super.performClick();
    }
*/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            onTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //requestDisallowInterceptTouchEvent(true);
        /*requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);*/
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
            /*case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:*/
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

        /*boolean returnValue = false;

        MotionEvent even = MotionEvent.obtain(event);
        final int action = MotionEventCompat.getActionMasked(even);
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsetY = 0;
        }
        int eventY = (int) even.getY();
        even.offsetLocation(0, mNestedOffsetY);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastY - eventY;
                // NestedPreScroll
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    mLastY = eventY - mScrollOffset[1];
                    //trackedEvent.offsetLocation(0, mScrollOffset[1]);
                    even.offsetLocation(0, -mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                }
                returnValue = super.onTouchEvent(even);

                // NestedScroll
                if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {
                    even.offsetLocation(0, mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                    mLastY -= mScrollOffset[1];
                }
                break;
            case MotionEvent.ACTION_DOWN:
                returnValue = super.onTouchEvent(even);
                mLastY = eventY;
                // start NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            *//*case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:*//*

            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                returnValue = super.onTouchEvent(even);
                // end NestedScroll

                break;
        }
        return returnValue;*/

    }

    private Chunk getChunk() {
        String TEMPLATE_KATEX = "katex";
        String TEMPLATE_MATHJAX = "mathjax";
        String template = TEMPLATE_KATEX;
        AndroidTemplates loader = new AndroidTemplates(getContext());
        switch (mEngine) {
            case Engine.KATEX:
                template = TEMPLATE_KATEX;
                break;
            case Engine.MATHJAX:
                template = TEMPLATE_MATHJAX;
                break;
        }

        return new Theme(loader).makeChunk(template);
    }

    public void setText(String text) {
        mText = text;
        Chunk chunk = getChunk();

        String TAG_FORMULA = "formula";
        String TAG_CONFIG = "config";
        chunk.set(TAG_FORMULA, mText);
        chunk.set(TAG_CONFIG, mConfig);
        String s = strBody +chunk.toString()+"</p> </div> </body></html>";
        this.loadDataWithBaseURL(null, s, "text/html", "utf-8", "about:blank");
    }

    public String getText() {
        return mText;
    }

    /**
     * Tweak the configuration of MathJax.
     * The `config` string is a call statement for MathJax.Hub.Config().
     * For example, to enable auto line breaking, you can call:
     * config.("MathJax.Hub.Config({
     * CommonHTML: { linebreaks: { automatic: true } },
     * "HTML-CSS": { linebreaks: { automatic: true } },
     * SVG: { linebreaks: { automatic: true } }
     * });");
     * <p>
     * This method should be call BEFORE setText() and AFTER setEngine().
     * PLEASE PAY ATTENTION THAT THIS METHOD IS FOR MATHJAX ONLY.
     *
     * @param config
     */
    public void config(String config) {
        if (mEngine == Engine.MATHJAX) {
            this.mConfig = config;
        }
    }

    /**
     * Set the js engine used for rendering the formulas.
     *
     * @param engine must be one of the constants in class Engine
     *               <p>
     *               This method should be call BEFORE setText().
     */
    public void setEngine(int engine) {
        switch (engine) {
            case Engine.KATEX: {
                mEngine = Engine.KATEX;
                break;
            }
            case Engine.MATHJAX: {
                mEngine = Engine.MATHJAX;
                break;
            }
            default:
                mEngine = Engine.KATEX;
        }
    }

    public static class Engine {
        final public static int KATEX = 0;
        final public static int MATHJAX = 1;
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
    }

}
