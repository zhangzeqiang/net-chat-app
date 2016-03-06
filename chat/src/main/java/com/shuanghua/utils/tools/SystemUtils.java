package com.shuanghua.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SystemUtils {

	public static class UnitUtils {
		
		/** 
	     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	     */  
	    public static int dip2px(Context context, float dpValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (dpValue * scale + 0.5f);  
	    }

	    /** 
	     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	     */
	    public static int px2dip(Context context, float pxValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (pxValue / scale + 0.5f);  
	    }  
	}
	
	/**
	 * 亮度调节
	 * @author ZZQ
	 * @version 2015年11月24日 下午3:58:59
	 */
	public static class BrightnessUtils {
		
		public static final int AUTO_MODE = 1;
		public static final int MANUL_MODE = 0;
		
		/** 
	     * 设置当前屏幕亮度的模式  
	    * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
	    * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度 
	    */  
	    public static void setScreenMode(Context context, int value) {  
	        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);  
	    }
	    
	    /** 
	     * 设置当前屏幕亮度值 0--255，并使之生效 
	     */  
		public static void setScreenBrightness(Activity activity, float value) {  
			Window mWindow = activity.getWindow();  
			WindowManager.LayoutParams mParams = mWindow.getAttributes();  
			float f = value / 255.0F;  
			mParams.screenBrightness = f;  
			mWindow.setAttributes(mParams);  
			
			// 保存设置的屏幕亮度值  
			Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);  
		}
	    
		/** 
		 * 获得当前屏幕亮度的模式  
		 * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
		 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度 
		 * @throws SettingNotFoundException 
		 */
		public static int getScreenBrightnessMode (Context context) throws SettingNotFoundException {
			 
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
		}
		
		/**
		 * 获得当前屏幕亮度值 0--255  
		 * @throws SettingNotFoundException 
		 */
		public static final int getScreenBrightness (Context context) throws SettingNotFoundException {
			return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
		}
	}
	
	/**
	 * 读取sd卡总大小以及剩余大小
	 * @return
	 */
	public static final SystemBlock readSDCard() { 
        String state = Environment.getExternalStorageState(); 
        if(Environment.MEDIA_MOUNTED.equals(state)) { 
            File sdcardDir = Environment.getExternalStorageDirectory(); 
            StatFs sf = new StatFs(sdcardDir.getPath());
            
            SystemBlock systemBlock = new SystemBlock();
            systemBlock.BlockSize = sf.getBlockSize();
            systemBlock.BlockCount = sf.getBlockCount();
            systemBlock.AvailCount = sf.getAvailableBlocks();
            return systemBlock;
        }
        return null;
    }
	
	/**
	 * 获取带单位的大小
	 * @param size
	 * @return 'size' => long, 'prefix' => String
	 */
	public static final Map<String, Object> getSizeAndPrefix (double size) {
		// 获取单位
		String prefix = "";
		if (size > 1024*1024*1024) {
			prefix = "GB";
			size = (double) size/(1024*1024*1024.0);
		} else if (size > 1024*1024) {
			prefix = "MB";
			size = size/(1024*1024.0);
		} else if (size > 1024) {
			prefix = "KB";
			size = size/1024.0;
		} else {
			prefix = "B";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("size", size);
		map.put("prefix", prefix);
		return map;
	}
	
	/**
	 * 总共存储空间totalBlock = BlockSize*BlockCount/1024;总共剩余存储空间totalAvailBlock=BlockSize*AvailCount/1024;
	 * @author ZZQ
	 * @version 2015年11月23日 下午2:06:43
	 */
	public static class SystemBlock {
		
		// 每片block大小
		public long BlockSize;
		// 总共block数量
		public long BlockCount;
		// 剩余可用的存储空间数量
		public long AvailCount;
	}
}
