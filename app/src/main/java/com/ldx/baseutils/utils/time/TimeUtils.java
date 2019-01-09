package com.ldx.baseutils.utils.time;

import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author babieta
 * @date 2018/11/28
 */

public class TimeUtils implements Runnable {
    //计时器
    private Handler mhandle = new Handler();
    //是否暂停
    private boolean isPause = false;
    //当前毫秒数
    private long currentSecond = 0;
    //不能直接run
    private boolean isRun = false;

    private TimeBack timeBack;

    public TimeUtils() {
    }

    /**
     * 计时开始
     * 传入一个时间戳
     */
    public void onStart() {
        if (isPause) {
            //证明暂停了
            isPause = false;
            run();
        } else {
            //没有暂停 and 可能第一次运行
            isRun = true;
            run();
        }
    }

    /**
     * 暂停
     */
    public void onSuspend() {
        isPause = true;
    }

    /**
     * 停止计时
     */
    public void onStop() {
        isRun = false;
        isPause = false;
        mhandle.removeCallbacks(this);
        currentSecond = 0;
        if (timeBack != null) {
            timeBack.onFormatHMS("00:00:00");
        }
    }

    /**
     * 展示时间
     * timeStamp 秒  -
     * (8 * 60 * 60) -> 转时间戳
     *
     * @param timeStamp
     */
    public void setTime(long timeStamp) {
        setCurrentSecond(timeStamp - (8 * 60 * 60));
    }

    /**
     * set 方法
     *
     * @param currentSecond
     */
    public void setCurrentSecond(long currentSecond) {
        this.currentSecond = currentSecond;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String time = formatter.format(new Date(currentSecond * 1000));
        if (timeBack != null) {
            timeBack.onFormatHMS(time);
        }
    }

    @Override
    public void run() {
        if (!isRun) {
            throw new RuntimeException("不能直接调用run 请调用start");
        }
        setCurrentSecond(currentSecond += 1);
        if (!isPause) {
            //递归调用本runable对象，实现每隔一秒一次执行任务
            mhandle.postDelayed(this, 1000);
        }
    }

    public void setTimeBack(TimeBack timeBack) {
        this.timeBack = timeBack;
    }
}
