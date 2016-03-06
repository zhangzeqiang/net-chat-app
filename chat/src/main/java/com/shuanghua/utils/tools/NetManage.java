package com.shuanghua.utils.tools;
/*
 * @class: 通用网络管理类(如网络是否连接)
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetManage {

	private static final String ACTIVITY_TAG = "NetManage";
	
	/*
	 * @function: 判断网络连接是否正常
	 * @context: 上下文
	 */
	public static boolean isNetworkConnected(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null) {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }
	    return false;  
	}
	
	/*
	 * @function: wifi下才可使用
	 */
	private static boolean isWifiOnly () {
		return true;
	}
	
	/*
	 * @function: wifi状态
	 */
	private static boolean ifWifiOn (Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        // 判断wifi是否开启 
		return wifiManager.isWifiEnabled();
	}
	
	/*
	 * @function: 只允许wifi下使用
	 * @返回true则会对wifi要求进行限制
	 */
	public static boolean wifiLimit (Context context) {
		boolean ifWifiOn = NetManage.ifWifiOn(context);
		boolean ifWifiOnly = NetManage.isWifiOnly();
		
		if (ifWifiOnly && !ifWifiOn){
			return true;
		}
		return false;
	}
	
	/**
	 * 通过wifi获取手机ip地址
	 */
	private static String getWifiIp (Context context) {
		//获取wifi服务  
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (isNetworkConnected (context) && wifiManager.isWifiEnabled()) {
        	WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
            int ipAddress = wifiInfo.getIpAddress();   
            String ip = (ipAddress & 0xFF ) + "." +
            		((ipAddress >> 8 ) & 0xFF) + "."+
            		((ipAddress >> 16 ) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF);
            return ip;
        }
        return null;
	}
	
	/**
	 * 通过GPRS获取手机ip地址
	 */
	private static String getGPRSIp (Context context) {
		if (isNetworkConnected (context) && !ifWifiOn (context)) {
			try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
	            {  
	               NetworkInterface intf = en.nextElement();  
	               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
	               {  
	                   InetAddress inetAddress = enumIpAddr.nextElement();  
	                   if (!inetAddress.isLoopbackAddress())  
	                   {  
	                       return inetAddress.getHostAddress().toString();  
	                   }  
	               }  
	           }  
	        }  
	        catch (SocketException ex) {
	            Log.e(ACTIVITY_TAG, ex.toString());
	        } 
		}
        return null;
	}
	
	/**
	 * 获取手机ip
	 */
	public static String getIp (Context context) {
		
		String ip = getWifiIp (context);
		/*if (ip != null) {
			return ip;
		}*/
		
		ip = getGPRSIp (context);
		return ip;
	}
	
}
