package com.shuanghua.utils.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtils {

	private AppUtils () {	
	}
	
	/**
	 * 根据包名判断一个apk已经安装
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName)) {
			return false;
		}
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	  * 获取版本号
	  * @return 当前应用的版本号
	  */
	public static int getVersionCode(Activity activity, String pkg) {
		try {
	        PackageManager manager = activity.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(pkg, 0);
	        int version = info.versionCode;
	        return version;
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 获取一个已安装的apk文件大小
	 * @param context
	 * @param packageName
	 * @return
	 * @throws NameNotFoundException
	 */
	public static long getApkSize(Context context, String packageName)
	        throws NameNotFoundException {
	    return new File(context.getPackageManager().getApplicationInfo(
	            packageName, 0).publicSourceDir).length();
	}
	
	/**
	 * 获取未安装的apk的包名
	 * @param context
	 * @param apkUrl
	 * @return
	 */
	public static String getUninstallApkPkg (Context context, String apkUrl) {
		PackageManager pm = context.getPackageManager();  
	    PackageInfo info = pm.getPackageArchiveInfo(apkUrl, PackageManager.GET_ACTIVITIES);  
	    if (info != null) {  
	        ApplicationInfo appInfo = info.applicationInfo;  
	        return appInfo.packageName;
	    }
	    return null;
	}
	
	/**
	 * 通过app包名获取App信息块
	 * @see {@link #queryAppInfoWithIntent(Context, Intent)}}
	 * @param context
	 * @param pkg
	 * @return
	 */
	public static List<AppInfo> getAppInfoWithPkg (Context context, String pkg) {
		PackageManager pm = context.getPackageManager(); //获得PackageManager对象
		List<AppInfo> mlistAppInfo = new ArrayList<AppInfo>();
		
		Intent mainIntent = pm.getLaunchIntentForPackage(pkg);
		// 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        if (mlistAppInfo != null) {
        	mlistAppInfo.clear();
            for (ResolveInfo reInfo:resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName,
                        activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                appInfo.setActivityName(activityName);
                mlistAppInfo.add(appInfo); // 添加至列表中
                return mlistAppInfo;
            }
        }
        return null;
	}
	
	/**
	 * 根据app包名启动对应的app
	 * @return
	 */
	public static void launchWithPkg (Context context, String pkg) throws NameNotFoundException {
		PackageManager pm = context.getPackageManager(); //获得PackageManager对象
		Intent intent = pm.getLaunchIntentForPackage(pkg);
		
		context.startActivity(intent);
	}
	
	/** 
	 * 获得所有启动Activity的信息，类似于Launch界面
	 * <p>
	 * eg:<p> 
	 * Intent intent = new Intent();<p>                
           intent.setAction(Intent.ACTION_MAIN);// 添加Action属性<p>                
           intent.addCategory(Intent.CATEGORY_HOME);// 添加Category属性<p>
           List<AppInfo> mListAppInfo = queryAppInfoWithIntent(intent);
       <p>默认应用需有android.intent.category.DEFAULT才可匹配上
       @see {@link #getAppInfoWithPkg(Context, String)}}
	 */
	public static List<AppInfo> queryAppInfoWithIntent(Context context, Intent intent) {
    	List<AppInfo> mlistAppInfo = new ArrayList<AppInfo>();
    	
        PackageManager pm = context.getPackageManager(); //获得PackageManager对象
        
        // 通过查询，获得所有ResolveInfo对象.
        /*List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);*/
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();
            for (ResolveInfo reInfo:resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName,
                        activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                appInfo.setActivityName(activityName);
                mlistAppInfo.add(appInfo); // 添加至列表中
            }
        }
        
        return mlistAppInfo;
    }
	
	/**
     * install app
     * 
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }
    
    /**
	  * 获取包信息
	  * @return 当前应用的信息
	  */
	public PackageInfo getPackageInfo(Activity activity, String pkg) {
		try {
	        PackageManager manager = activity.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(pkg, 0);
	        int version = info.versionCode;
	        return info;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}
	
    /**
     * Model类 ，用来存储应用程序信息
     * <br>Copyright (c) 2015 双华科技
     * @author ZZQ
     * @version 2015-11-3 下午5:06:17
     */
    public static class AppInfo {
      
    	private String appLabel;    //应用程序标签
    	private Drawable appIcon ;  //应用程序图像
    	private Intent intent ;     //启动应用程序的Intent ，一般是Action为Main和Category为Lancher的Activity
    	private String pkgName ;    //应用程序所对应的包名
    	private String activityName;	// 应用程序启动Activity name
    	private Map<String, Object> elementMaps;
    	
    	public Map<String, Object> getMaps () {
    		return this.elementMaps;
    	}
    	
    	public Object getElement (String key) {
    		return elementMaps.get(key);
    	}
    	
    	public void setElement (String key, Object value) {
    		if (elementMaps.containsKey(key)) {
    			elementMaps.remove(key);
    		} else {
    		}
    		elementMaps.put(key, value);
    	}
    	
    	public AppInfo(){
    		elementMaps = new HashMap<String, Object>();
    	}
    	
    	public String getAppLabel() {
    		return appLabel;
    	}
    	
    	public void setAppLabel(String appName) {
    		this.appLabel = appName;
    	}
    	
    	public Drawable getAppIcon() {
    		return appIcon;
    	}
    	
    	public String getActivityName () {
    		return this.activityName;
    	}
    	
    	public void setActivityName (String activityName) {
    		this.activityName = activityName;
    	}
    	
    	public void setAppIcon(Drawable appIcon) {
    		this.appIcon = appIcon;
    	}
    	
    	public Intent getIntent() {
    		return intent;
    	}
    	
    	public void setIntent(Intent intent) {
    		this.intent = intent;
    	}
    	
    	public String getPkgName(){
    		return pkgName ;
    	}
    	
    	public void setPkgName(String pkgName){
    		this.pkgName=pkgName ;
    	}
    }

}
