package com.project.zeng.bookstore.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by zeng on 2017/2/27.
 */

public class AutoScrollViewPager extends ViewPager{

    //默认间隔时间
    public static final int DEFAULT_INTERVAL = 1500;
    //默认自动播放方向
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    //当滑动在第一页和最后一页，什么都不做
    public static final int SLIDE_BORDER_MORE_NONE = 0;
    //在最后一项和第一项之间形成循环
    public static final int SLIDE_BORDER_MORE_CYCLE = 1;
    //将滑动到第一页和最后一页的事件传给父组件
    public static final int SLIDE_BORDER_MORE_TO_PARENT = 2;

    //默认播放时间间隔
    public long interval = DEFAULT_INTERVAL;
    //自动播放方向，默认向右
    private int direction = RIGHT;
    //设置是否循环播放
    private boolean isCycle = true;
    //设置当手动滑动时，停止自动播放
    private boolean stopScrollWhenTouch = true;
    //设置第一张左移和最后一张右移后跳转的页面
    private int sliderBorderMode = SLIDE_BORDER_MORE_NONE;
    //设置是否启用动画
    private boolean isBorderAnimation = true;

    private double autoScrollFactor = 1.0;
    private double swipeScrollFactor = 1.0;

    private Handler handler;
    private boolean isAutoScroll = false;
    private boolean isStopByTouch = false;
    private float touchX = 0f, downX = 0f;
    private CustomDurationScroller scroller = null;

    //当前位置
    public static final int SCROLL_WHAT = 0;

    public AutoScrollViewPager(Context context) {
        super(context);
        init();
    }

    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init(){
        //初始化Handler
        handler = new MyHandler(this);
        setViewPagerScroller();
    }

    /**
     * 设置开始自动播放
     */
    public void startAutoScroll(){
        isAutoScroll = true;
        sendScrollMessage((long)(interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
    }

    /**
     * 根据时间间隔自动播放
     * @param delayTimeInMills
     */
    public void startAutoScroll(int delayTimeInMills){
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * 停止自动播放
     */
    public void stopAutoScroll(){
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    /**
     * 设置手动滑动的动画持续时间
     * @param scrollFactor
     */
    public void setSwipeScrollDurationFactor(double scrollFactor){
        swipeScrollFactor = scrollFactor;
    }

    /**
     * 设置自动滑动的动画持续时间
     * @param scrollFactor
     */
    public void setAutoScrollDurationFactor(double scrollFactor){
        autoScrollFactor = scrollFactor;
    }

    /**
     * 设置ViewPager的Scroller滑动过渡动画
     */
    private void setViewPagerScroller(){
        try{
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator)interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 只滑动一次
     */
    public void scrollOnce(){
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if(adapter == null || (totalCount = adapter.getCount()) <= 1){
            return;
        }
        //下一张图片位置
        int nextItem = (direction == LEFT)? --currentItem : ++currentItem;
        if(nextItem < 0){
            if(isCycle){
                //若设置循环则从第一张返回最后一张
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        }else if(nextItem == totalCount){
            if(isCycle){
                //若设置循环则从最后一张重新返回第一张
                setCurrentItem(0, isBorderAnimation);
            }
        }else{
            setCurrentItem(nextItem, true);
        }
    }

    /**
     * 发送滚动的消息
     * @param delayTimeInMills
     */
    private void sendScrollMessage(long delayTimeInMills){
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        int action = MotionEventCompat.getActionMasked(motionEvent);
        if(stopScrollWhenTouch){
            if((action == MotionEvent.ACTION_DOWN) && isAutoScroll){
                //当手动滑动ViewPager时，停止自动播放
                isStopByTouch = true;
                stopAutoScroll();
            }else if(motionEvent.getAction() == MotionEvent.ACTION_UP && isStopByTouch){
                //当滑动ViewPager后离开屏幕，继续自动播放
                startAutoScroll();
            }
        }
        if(sliderBorderMode == SLIDE_BORDER_MORE_TO_PARENT
                || sliderBorderMode == SLIDE_BORDER_MORE_CYCLE){
            touchX = motionEvent.getX();
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                downX = touchX;
            }
            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int pageCount = adapter == null? 0 : adapter.getCount();
            //向右滑动
            if((currentItem == 0 && downX <= touchX)
                    || (currentItem == pageCount - 1 && downX >= touchX)){
                if(sliderBorderMode == SLIDE_BORDER_MORE_TO_PARENT){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    if(pageCount > 1){
                        setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            return super.dispatchTouchEvent(motionEvent);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionEvent);
    }

    /**
     * 获得间隔时间
     * @return
     */
    public long getInterval(){
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }

    /**
     * 设置自动滚动方向，默认向右
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否循环，默认true
     * @param cycle
     */
    public void setCycle(boolean cycle) {
        isCycle = cycle;
    }

    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }

    /**
     * 设置触摸ViewPager时是否停止自动播放，默认停止
     * @param stopScrollWhenTouch
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    public int getSliderBorderMode() {
        return sliderBorderMode;
    }

    /**
     * 设置第一张和最后一张的跳转模式
     * @param sliderBorderMode
     */
    public void setSliderBorderMode(int sliderBorderMode) {
        this.sliderBorderMode = sliderBorderMode;
    }

    public boolean isBorderAnimation() {
        return isBorderAnimation;
    }

    private static class MyHandler extends Handler{
        private final WeakReference<AutoScrollViewPager> autoScrollViewPager;

        private MyHandler(AutoScrollViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<AutoScrollViewPager>(autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case SCROLL_WHAT:{
                    AutoScrollViewPager pager = this.autoScrollViewPager.get();
                    if(pager != null){
                        pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);//设置时间间隔
                        pager.scrollOnce();
                        pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                        pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 设置自动滑动的动画
     * @param borderAnimation
     */
    public void setBorderAnimation(boolean borderAnimation) {
        isBorderAnimation = borderAnimation;
    }
}
