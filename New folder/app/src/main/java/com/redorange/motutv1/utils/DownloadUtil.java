package com.redorange.motutv1.utils;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadUtil
{
  public static final String ACTION_DOWNLOAD_RECEIVER = "com.ipmacro.market.download";
  public static final int MSG_DOWNLOAD_CANCEL = 4;
  public static final int MSG_DOWNLOAD_FAIL = -1;
  public static final int MSG_DOWNLOAD_FINISH = 3;
  public static final int MSG_DOWNLOAD_ING = 2;
  public static final int MSG_DOWNLOAD_INIT = 1;
  public static final String MSG_DOWNLOAD_LENGTH = "length";
  public static final String MSG_DOWNLOAD_PATH = "path";
  private final String folder = "apk";
  private Handler handler;
  private boolean running = false;

  public DownloadUtil()
  {
  }

  public DownloadUtil(Handler paramHandler)
  {
    this.handler = paramHandler;
  }

  private void setMessage(Message paramMessage)
  {
    if (this.handler != null)
      this.handler.sendMessage(paramMessage);
  }

  public void cancel()
  {
    this.running = false;
  }

  public String download(String paramString1, String paramString2)
  {
    //Log.i("---------->", paramString1);
    this.running = true;   
    FileOutputStream localFileOutputStream;
    byte[] arrayOfByte;
    int j;
    int k;
    try
    {
        URL localURL = new URL(paramString1);
        URLConnection localURLConnection = localURL.openConnection();
        if (localURLConnection.getReadTimeout() == 5)
          return null;
        InputStream localInputStream = localURLConnection.getInputStream();
        String str1 = Environment.getExternalStorageDirectory() + "/" + "apk";
        File localFile1 = new File(str1);
        if (!localFile1.exists()){
        	localFile1.mkdir();
        }
        //Log.i("savePathString", String.valueOf(localFile1.exists()));
        String str2 = str1 + "/" + paramString2;
        //Log.i("savePathString", str2);
        File localFile2 = new File(str2);
        if (!localFile2.exists()){
        	localFile2.createNewFile();        	
        }
        Message localMessage2 = new Message();
        localFileOutputStream = new FileOutputStream(localFile2);
        arrayOfByte = new byte[4096];
        int i = localURLConnection.getContentLength();
        localMessage2.what = 1;
        Bundle localBundle1 = new Bundle();
        localBundle1.putInt("length", i);
        localMessage2.setData(localBundle1);
        setMessage(localMessage2);
        j = 0;
        k = localInputStream.read(arrayOfByte);
        while (k != -1)
        {
        	if (!this.running)
            {
              Message localMessage4 = new Message();
              localMessage4.what = 4;
              setMessage(localMessage4);
              return null;
            }
        	j += k;
            localFileOutputStream.write(arrayOfByte, 0, k);
            Message localMessage5 = new Message();
            localMessage5.what = 2;
            Bundle localBundle3 = new Bundle();
            localBundle3.putInt("length", j);
            localMessage5.setData(localBundle3);
            setMessage(localMessage5);
        }
          Message localMessage3 = new Message();
          localMessage3.what = 3;
          Bundle localBundle2 = new Bundle();
          localBundle2.putString("path", str2);
          localBundle2.putInt("length", i);
          localMessage3.setData(localBundle2);
          setMessage(localMessage3);
          localFileOutputStream.flush();
          localFileOutputStream.close();
          localInputStream.close();
          return str2;
        
      }
      catch (Exception localException)
      {
        Message localMessage1 = new Message();
        localMessage1.what = -1;
        setMessage(localMessage1);
        return null;
      }
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.utils.DownloadUtil
 * JD-Core Version:    0.6.0
 */