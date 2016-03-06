package com.shuanghua.utils.store;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*
 * @class: 使用SharedPreference实现的数据保存基类
 */
public abstract class BaseStore {
	
	// 日志
	protected static final String ACTIVITY_TAG="BaseStore";
	
	// 实例化SharedPreferences对象
	public SharedPreferences sharedPreferences = null;
	
	// 实例化SharedPreferences.Editor对象
	private Editor editor = null;
	
	private Activity activity = null;
	
	// getInt 返回错误
	public static int ERROR = -1;
	
	/*
	 * 存储模式
	 */
	protected int storeMode = Activity.MODE_PRIVATE;
	// 存储名
	protected String storeName = "BaseStore";
	
	public BaseStore() {}
	
	/*
	 * @function: 设置存储名(需唯一)
	 */
	protected final int setStoreName(String storeName) {
		this.storeName = storeName;
		return 0;
	}

	/*
	 * @function: 设置存储mode(final表示不允许重写)
	 */
	protected final int setStoreMode (int storeMode) {
		this.storeMode = storeMode;
		return 0;
	}
	
	/*
	 * @function: 构建SharedPreferences(final表示不允许重写)
	 */
	protected final int buildSharedPreferences(Activity activity) {
		
		this.activity = activity;
		this.sharedPreferences = this.activity.getSharedPreferences(this.storeName, this.storeMode);
		this.editor = this.sharedPreferences.edit();
		return 0;
	}

	/*
	 * @function: 保存数据(final表示不允许重写)
	 */
	public final Editor putString (String key, String value) {
		return this.editor.putString(key, value);
	}
	
	/*
	 * @function: 读取数据(final表示不允许重写)
	 */
	public final String getString (String key) {
		return this.getString(key, "");  // 返回key对应的value值，不存在此关系值则返回""
	}
	
	/**
	 * 保存boolean
	 * @param key
	 * @param sDefault
	 * @return
	 */
	public final Editor putBoolean (String key, boolean value) {
		return this.editor.putBoolean(key, value);
	}
	
	/**
	 * 获取boolean
	 * @param key
	 * @param sDefault
	 * @return
	 */
	public final boolean getBoolean (String key) {
		return this.getBoolean(key, false);
	}
	
	/**
	 * 读取数据
	 * @param key
	 * @param sDefault
	 * @return
	 */
	public final boolean getBoolean (String key, boolean sDefault) {
		return this.sharedPreferences.getBoolean(key, sDefault);
	}
	
	/*
	 * @function: 读取数据(final表示不允许重写)
	 * @statement: 如果不存在关系值则以sDefault返回
	 */
	public final String getString (String key, String sDefault) {
		return this.sharedPreferences.getString(key, sDefault);
	}
	
	/*
	 * @function: 保存数据
	 */
	public final Editor putInt (String key, int value) {
		return this.editor.putInt(key, value);
	}
	
	/*
	 * @function: 读取数据
	 */
	public final int getInt (String key) {
		return this.sharedPreferences.getInt(key, ERROR);
	}
	
	/*
	 * @function: 清理数据
	 */
	public final Editor clear (String key) {
		return this.editor.clear();
	}
	
	/*
	 * @function: 提交修改和保存(final表示不允许重写)
	 */
	public final boolean commit () {
		return this.editor.commit();
	}
	
	/*
	 * @function: 提交修改和保存(final表示不允许重写)
	 */
	public final boolean save() {
		return this.commit();
	}
	
	public int buildStore (Activity activity, String storeName) {
		setStoreName(storeName);   // 设置存储文件名
		buildSharedPreferences(activity);
		return 0;
	}
}
