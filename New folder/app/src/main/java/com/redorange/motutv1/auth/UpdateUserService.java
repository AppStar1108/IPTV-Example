package com.redorange.motutv1.auth;

import android.content.Context;

import com.ipmacro.CCore;
import com.redorange.motutv1.app.Motutv1Application;
import com.redorange.motutv1.utils.BackgroundExecutor;
import com.redorange.motutv1.utils.SynHtmlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class UpdateUserService
{
  public static final String CUSTOMER_NAME = "customerName";
  public static final String DRAMA_TYPES = "dramaTypes";
  public static final long HOUR = 3600L;
  public static final String ISACTIVE = "isActive";
  public static final String LIVE_CHANNEL_IDS = "liveChannelIds";
  public static final long MINUTE = 60L;
  public static final String MOVIE_TYPES = "movieTypes";
  public static final String REGION = "region";
  public static byte[] mMutex = new byte[1];
  CCore cCore = new CCore();
  long lastUpdateTime = 0L;
  Motutv1Application mApp;
  private JSONObject root;
  String sn;

  public UpdateUserService(Context paramContext)
  {
    this.mApp = ((Motutv1Application)paramContext.getApplicationContext());
    this.sn = this.mApp.getSN();
    //this.sn = "358773622";
  }
public String getSn()
{
  return this.sn;
}
  private void doTask(setIsActiveListener paramsetIsActiveListener)
  {
    synchronized(this){
    	//this.sn = "358773620";
    	String str1 = getCheckOfValidityUrl(this.cCore.getCheckExpiredInfo(Integer.parseInt(this.sn)));
        //Log.i("yy", "登录url  " + str1 + "  SN=" + this.sn);
        String str2 = SynHtmlUtil.get(str1);
        if (str2 != null){
        	this.lastUpdateTime = System.currentTimeMillis();
        	this.mApp.setConStr(str2);
            paramsetIsActiveListener.onSuccess(str2);
        }
    }
    return;
    /*          localException.printStackTrace();
          paramsetIsActiveListener.onFauil();
        }*/
    
  }

  public String getCheckOfValidityUrl(String paramString)
  {
    String str = URLEncoder.encode(paramString);
    //return "http://69.64.62.156:8080/channel/stb/checkTermOfValidity.htm?str=" + str;
    //return "http://62.210.182.14:8080//channel/stb/checkTermOfValidity.htm?str=" + str;
    return "http://extremeiptv.com:8080//channel/stb/checkTermOfValidity.htm?str=" + str;
  }

  public String getValue(String paramString)
  {
    String str1 = this.mApp.getConStr();
    if (str1 == null)
      return "";
    try
    {
      this.root = new JSONObject(str1);
      if ((this.root != null) && (this.root.has(paramString)))
      {
        String str2 = this.root.getString(paramString);
        return str2;
      }
    }
    catch (JSONException localJSONException)
    {
      //localJSONException.printStackTrace();
    }
    return null;
  }

  public void onStartCommand(setIsActiveListener paramsetIsActiveListener)
  {
	final setIsActiveListener p1 = paramsetIsActiveListener;
  //  if (System.currentTimeMillis() - this.lastUpdateTime > 600L)
      BackgroundExecutor.execute(new Runnable()
      {
        public void run()
        {
          UpdateUserService.this.doTask(p1);
        }
      });

    paramsetIsActiveListener.onFinish();
  }

  public static abstract interface setIsActiveListener
  {
    public abstract void onFauil();

    public abstract void onFinish();

    public abstract void onSuccess(String paramString);
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.auth.UpdateUserService
 * JD-Core Version:    0.6.0
 */