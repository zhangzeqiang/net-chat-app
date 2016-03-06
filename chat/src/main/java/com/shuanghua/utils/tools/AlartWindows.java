package com.shuanghua.utils.tools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast") public class AlartWindows {
	
	private Context context = null;
	
	public AlartWindows (Context context) {
		this.context = context;
	}
	
	public static void alert(Context context, String text) {
		AlertDialog alertDiaDebugTools = new Builder(context).setMessage(
			text).create();
			Window window = alertDiaDebugTools.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			// 设置透明度为0.3
			lp.alpha = 0.6f;
			window.setAttributes(lp);
			alertDiaDebugTools.show();
	}
	
	public Toast getToast (String msg) {
		
		return Toast.makeText(context, msg, Toast.LENGTH_LONG);		// 长时间
	}
	
	public static void showText (Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();	// 短时间
	}
	
	public static void confirm (Context context, String title, String msg,
			DialogInterface.OnClickListener positionOnClickListener,
			DialogInterface.OnClickListener negativeOnClickListener) {
		
		confirmWithButton (context, title, msg, positionOnClickListener, negativeOnClickListener, "确定", "取消");
	}

	/**
	 * 确定按钮
	 * @param context
	 * @param title
	 * @param msg
	 * @param positionOnClickListener
	 * @param negativeOnClickListener
	 * @param ok
	 * @param cancle
	 */
	public static void confirmWithButton (Context context, String title, String msg,
			DialogInterface.OnClickListener positionOnClickListener,
			DialogInterface.OnClickListener negativeOnClickListener,
			String ok, String cancle) {
		new Builder(context).setTitle(title).setMessage(msg)
		.setPositiveButton(ok, positionOnClickListener)
		.setNegativeButton(cancle, negativeOnClickListener)
		.show();
	}
	
	/**
	 * 默认输入dialog
	 * @param context
	 */
	public static void inputLineWithDefault (Context context, DialogInterface.OnClickListener positionOnClickListener,
			DialogInterface.OnClickListener negativeOnClickListener) {
		
		dialogWithView (context, "请输入", android.R.drawable.ic_dialog_info, new EditText(context), positionOnClickListener, negativeOnClickListener, 
				"确定", "取消");
	}
	
	/**
	 * 输入dialog
	 */
	public static void dialogWithView (Context context, String title, int iconId, View view, 
			DialogInterface.OnClickListener positionOnClickListener,
			DialogInterface.OnClickListener negativeOnClickListener,
			String ok, String cancle) {
		
		new Builder(context).setTitle(title).setIcon(
				iconId).setView(view).setPositiveButton(ok, positionOnClickListener)
				.setNegativeButton(cancle, negativeOnClickListener).show();
	}
	
	/**
	 * 单选框
	 */
	public static void singleChoiceDialog (Context context, String title, int iconId, String[] items, DialogInterface.OnClickListener choiceEventListener, 
			DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, String ok, String cancle) {
		
		new Builder(context)
		.setTitle(title)
		.setIcon(iconId)
		.setSingleChoiceItems(
			items, 0, choiceEventListener)
		.setPositiveButton(ok, positiveListener)
		.setNegativeButton(cancle, negativeListener)
		.show();
	}
	
	/**
	 * 多选框
	 */
	public static void multiChoiceDialog () {
		
	}
}
