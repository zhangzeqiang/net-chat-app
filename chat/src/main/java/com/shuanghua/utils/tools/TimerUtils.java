package com.shuanghua.utils.tools;

import android.os.Handler;
import android.util.Log;

/**
 * 通用定时器
 * <p>1、设置超时退出(需在一定的 周期*心跳数时间 内调用{@link #reset()}}重置计数,否则会自动调用{@link TimeOutListener}}中的{@link TimeOutListener#onTimeOut()}})
 * <p>2、不超时退出,调用{@link #setNTimeOutDetect(boolean)}}则只会定时执行{@link TimeOutListener#onTimeBeat()}},而没有超时退出机制,没必要在指定时间内调用reset方法
 * <p>Copyright (c) 2015 双华科技
 * @author ZZQ
 * @version 2015-10-10 上午11:27:02
 */
public class TimerUtils {

	private static final String TAG = "TimerUtils";
	
	/** 心跳周期 */
	protected long Period = 5000;
	
	/** 3个心跳周期未检测到对方心跳信号则发送超时信号 */
	private final int DEFAULT_NTIMEOUT = 5;
	protected int NTimeOut = DEFAULT_NTIMEOUT;	
	private boolean IFTimeOutDetect = true;
	
	private TimeOutListener timeOutListener;
	
	Handler handler = new Handler();
	Runnable runnable = new Runnable(){
		public void run(){
			
			if (IFTimeOutDetect) {
				if (NTimeOut <= 0) {
					/** 超时 */
					timeOutListener.onTimeOut();
					return ;
				} else {
					/** 未超时  */
					NTimeOutDesc ();
				}
			} else {}
			
			/** 执行时间 */
			timeOutListener.onTimeBeat();
			
			handler.postDelayed(this, Period);
	    }
	};
	
	/**
	 * 设置超时心跳数
	 */
	public void setDefaultNTimeOut (int count) {
		NTimeOut = count;
	}
	
	/**
	 * 设置是否心跳检测超时退出
	 * @param ifdetect
	 */
	public void setNTimeOutDetect (boolean ifdetect) {
		IFTimeOutDetect = ifdetect;
	}
	
	/**
	 * 设置周期数
	 * @return
	 */
	public long getPeriod () {
		return Period;
	}
	
	/**
	 * NTimeOut自减1
	 */
	synchronized private void NTimeOutDesc () {
		NTimeOut = NTimeOut - 1;
	}
	
	/**
	 * NTimeOut重置
	 */
	synchronized private void NTimeOutReset () {
		NTimeOut = DEFAULT_NTIMEOUT;
	}
	
	/**
	 * 收到对方心跳信号时激活重置参数
	 */
	public void reset () {
		NTimeOutReset ();
	}
	
	public void init () {
		Log.v (TAG, "init");
		setNTimeOutDetect (true);
		this.setDefaultNTimeOut(NTimeOut);
		this.setPeriod(Period);
		
		timeOutListener = new TimeOutListener () {

			@Override
			public void onTimeOut() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTimeBeat() {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	
	/**
	 * 设置超时监听
	 */
	public void setTimeOutListener (TimeOutListener listener) {
		this.timeOutListener = listener;
	}
	
	public void setPeriod (long period) {
		Period = period;
	}
	
	public void stopSend () {
		Log.v (TAG, "stopSend");
		
		handler.removeCallbacks(runnable);  //停止Timer
	}
	
	public void sendWithTime (long delay, long period) {
		
		Log.v (TAG, "sendWithTime");
		/** 透传信号发送周期 */
		setPeriod (period);
		
		handler.postDelayed(runnable, delay); //开始timer
	}

	public static interface TimeOutListener {
		/** 检测到时间超时时执行 */
		public void onTimeOut();
		
		/** 心跳执行 */
		public void onTimeBeat();
	}
}

