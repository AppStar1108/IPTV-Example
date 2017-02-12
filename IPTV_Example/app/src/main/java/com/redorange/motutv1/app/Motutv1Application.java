package com.redorange.motutv1.app;

import android.app.Application;
import com.redorange.motutv1.auth.AuthConfig;
import com.redorange.motutv1.model.App;
import com.redorange.motutv1.model.ChannelType;
import java.util.HashMap;
import java.util.List;

public class Motutv1Application extends Application
{
  App apk;
  AuthConfig authConfig;
  int category = 1;
  String conStr = null;
  public boolean isActive = false;
  HashMap<String, Object> paramHashMap;
  List<ChannelType> typeList;

  public App getApk()
  {
    return this.apk;
  }

  public AuthConfig getAuthConfig()
  {
    return this.authConfig;
  }

  public int getCategory()
  {
    return this.category;
  }

  public String getConStr()
  {
    return this.conStr;
  }

  public Object getParamHashMap(String paramString)
  {
    return this.paramHashMap.get(paramString);
  }

  public String getSN()
  {
    return this.authConfig.getSn();
  }

  public List<ChannelType> getTypeList()
  {
    return this.typeList;
  }

  public boolean hasParamHashMap(String paramString)
  {
    return this.paramHashMap.containsKey(paramString);
  }

  public boolean isActive()
  {
    return this.isActive;
  }

  public void onCreate()
  {
    super.onCreate();
    this.authConfig = new AuthConfig(this);
    this.paramHashMap = new HashMap();
  }

  public void setActive(boolean paramBoolean)
  {
    this.isActive = paramBoolean;
  }

  public void setApk(App paramApp)
  {
    this.apk = paramApp;
  }

  public void setAuthConfig(AuthConfig paramAuthConfig)
  {
    this.authConfig = paramAuthConfig;
  }

  public void setCategory(int paramInt)
  {
    this.category = paramInt;
  }

  public void setConStr(String paramString)
  {
    this.conStr = paramString;
  }

  public void setParamHashMap(String paramString, Object paramObject)
  {
    this.paramHashMap.put(paramString, paramObject);
  }

  public void setTypeList(List<ChannelType> paramList)
  {
    this.typeList = paramList;
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.app.Motutv1Application
 * JD-Core Version:    0.6.0
 */