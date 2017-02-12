package com.redorange.motutv1.auth;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MacTools
{
  public static final long DELAY_MILLIS = 300L;
  public static final int GET_MAC_TIMES = 100;
  public static final int MSG_GET_MAC = 1;
  public static final int STATUS_GET_MAC_FAIL = 0;
  public static final int STATUS_GET_MAC_SUCCESS = 1;
  int getMacTimes = 0;
  Context mContext;

  public MacTools(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public static String getDefaultNetMacAddress(Context p)
  {
  //  try
   // {
      //String str = loadFileAsString("/sys/class/net/eth0/address").toUpperCase().trim();
    	WifiManager manager = (WifiManager)(p.getSystemService(Context.WIFI_SERVICE));
    	WifiInfo info = manager.getConnectionInfo();
    	String str = info.getMacAddress();
      return str;
   // }
   // catch (IOException localIOException)
   // {
     // localIOException.printStackTrace();
    //}
   // return "";
  }

  public static String loadFileAsString(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer(1000);
    BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
    char[] arrayOfChar = new char[1024];
    int i = localBufferedReader.read(arrayOfChar);
    while(i!= -1)
    {
      localStringBuffer.append(String.valueOf(arrayOfChar, 0, i));
      i = localBufferedReader.read(arrayOfChar);
      
    }
   
        localBufferedReader.close();
        return localStringBuffer.toString();   
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.auth.MacTools
 * JD-Core Version:    0.6.0
 */