package com.ipmacro.ppcore;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;

public class PPCore
{
  private static boolean mInited = false;
  private static String mMac;
  public static byte[] mMutex = new byte[1];
  private static boolean mRun;
  public static int mSleepTime = 10;
  public static LinkedList<Task> mTaskList = new LinkedList();
  private static TaskThread mThread;

  static
  {
    System.loadLibrary("stlport_shared");
    System.loadLibrary("PPCoreJni");
  }

  public static void DbgMsg(String paramString)
  {
    nativeDbgMsg(paramString);
  }

  public static boolean checkCdKey(String paramString)
  {
    return nativeCheckCdKey(paramString);
  }

  private static boolean detectWifi(Context paramContext)
  {
    if ((mMac != null) && (!mMac.equals(""))) return true;
    
    WifiManager localWifiManager;
    String str;    
    localWifiManager = (WifiManager)paramContext.getSystemService("wifi");
    str = localWifiManager.getConnectionInfo().getMacAddress();    
    if ((str != null) && (!str.equals(""))) return true;
    boolean bool = localWifiManager.isWifiEnabled();
    if (!bool)
    {
      //Log.i("PPCore", "Opening Wifi");
      localWifiManager.setWifiEnabled(true);
    }
    int i = 0;
    long l1 = System.currentTimeMillis();
    while (i < 50){
    	 str = localWifiManager.getConnectionInfo().getMacAddress();
         if ((str == null) || (str.equals(""))){
        	 i++;
        	 try
             {
               Thread.sleep(100L);
             }
             catch (InterruptedException localInterruptedException)
             {
               //localInterruptedException.printStackTrace();
             }
         }
         else {
        	 long l2 = System.currentTimeMillis() - l1;
             //Log.i("PPCore", "Wifi Ready Time=" + l2 + " Count=" + i);
             break;
         }             	
    }
     
        if (!bool)
        {
          //Log.i("PPCore", "Closing Wifi");
          localWifiManager.setWifiEnabled(false);
        }
        if ((str != null) && (!str.equals(""))) return true;         
        return false;
       
      }

  public static int getExpire()
  {
    if (!mRun)
      return 0;
    return nativeGetExpire();
  }

  public static String getHttpUrl()
  {
    if (!mRun)
      return "";
    return nativeGetHttpUrl();
  }

  public static String getLetvKey(String paramString, int paramInt)
  {
    if (!mRun)
      return null;
    return nativeGetLetvKey(paramString, paramInt);
  }

  public static int getLife()
  {
    if (!mRun)
      return 0;
    return nativeGetLife();
  }

  public static String getLiveKey()
  {
    if (!mRun)
      return null;
    return nativeGetLiveKey();
  }

  public static String getPwd()
  {
    if (!mInited)
      return "";
    return nativeGetPwd();
  }

  public static String getRtspUrl()
  {
    if (!mRun)
      return "";
    return nativeGetRtspUrl();
  }

  public static int getSN()
  {
    if (!mRun)
      return 0;
    return nativeGetSN();
  }

  public static int getTimer()
  {
    return nativeGetTimer();
  }

  public static int init(Context paramContext)
  {
    int i;
    if (mInited){
      i = 0;
      return 0;
    }
    if (!detectWifi(paramContext))
        return -1;
    i = nativeInit(paramContext);    
    if (i == 0){
    	mInited = true;
    	return 0;
    }
    return i;
  }

  public static void loadLib()
  {
  }

  public static boolean login(String paramString)
  {
    if (mRun)
      return true;
    if (!nativeLogin3(paramString))
      return false;
    start();
    return true;
  }

  public static int login4(Context paramContext, int paramInt)
  {
    int i = nativeLogin4(paramContext, paramInt);
   /* if (i != 0)
      return i;*/
    start();
    return 0;
  }

  public static boolean logout()
  {
    if (!mRun)
      return false;
    //Log.i("PPCore", "Logout Pending");
    mRun = false;
   
      try {
		mThread.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
      if (mThread.isAlive())
        Log.e("PPCore", "Thread Still Running");
      mThread = null;
      return true;
    
  }

  private static native boolean nativeCheckCdKey(String paramString);

  private static native void nativeDbgMsg(String paramString);

  private static native int nativeGetExpire();

  private static native String nativeGetHttpUrl();

  private static native String nativeGetLetvKey(String paramString, int paramInt);

  private static native int nativeGetLife();

  private static native String nativeGetLiveKey();

  private static native String nativeGetPwd();

  private static native String nativeGetRtspUrl();

  private static native int nativeGetSN();

  private static native int nativeGetTimer();

  private static native int nativeInit(Context paramContext);

  private static native boolean nativeLogin3(String paramString);

  private static native int nativeLogin4(Context paramContext, int paramInt);

  private static native boolean nativeLogout();

  private static native boolean nativeOneClick();

  private static native void nativeSetLog(boolean paramBoolean);

  private static void oneClick()
  {
    nativeOneClick();
  }

  public static void setLog(boolean paramBoolean)
  {
    nativeSetLog(paramBoolean);
  }

  private static void start()
  {
    if (mRun)
      return;
    mRun = true;
    //Log.i("PPCore", "Login OK");
    //Log.i("SN", String.valueOf(getSN()));
    //Log.i("Life", String.valueOf(getLife()));
    Date localDate = new Date();
    localDate.setTime(1000L * getExpire());
    //Log.i("Expire", localDate.toString());
    //Log.i("HttpUrl", getHttpUrl());
    //Log.i("RtspUrl", getRtspUrl());
    mThread = new TaskThread();
    mThread.setName("PPCore");
    mThread.start();
  }

  public static abstract class Task
  {
    public abstract void oneClick();
  }

  private static class TaskThread extends Thread
  {
    public void run()
    {
      while(PPCore.mRun)
      {
    	  synchronized (PPCore.mMutex)
          {
            PPCore.oneClick();
            
          }
            try
            {
              sleep(PPCore.mSleepTime);
            }
            catch (InterruptedException localInterruptedException)
            {
              //localInterruptedException.printStackTrace();
            }
                    
      }
      PPCore.logout();
      //Log.i("PPCore", "Logout OK");
      return;
    }
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.ipmacro.ppcore.PPCore
 * JD-Core Version:    0.6.0
 */