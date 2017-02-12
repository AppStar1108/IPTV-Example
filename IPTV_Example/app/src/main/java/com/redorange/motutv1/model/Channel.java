package com.redorange.motutv1.model;

import android.graphics.Bitmap;

public class Channel
{
  String adImage;
  String allowRegion;
  int id;
  boolean isAutoRelay;
  int isFree;
  boolean isLock;
  boolean isP2p;
  boolean isShowGlobalLogo;
  boolean isShowLogoOnBox;
  String logo;
  int mode;
  String name;
  String playUrl;
  int sort;
  int typeId;
  String typeIds;
  Bitmap bmp;
  
  public void setBitmap(Bitmap b){
	  this.bmp = b;
  }
  
  public Bitmap getBitmap(){
	  return this.bmp;
  }
  
  public String getAdImage()
  {
    return this.adImage;
  }

  public String getAllowRegion()
  {
    return this.allowRegion;
  }

  public int getId()
  {
    return this.id;
  }

  public int getIsFree()
  {
    return this.isFree;
  }

  public String getLogo()
  {
	  return this.logo;
  }

  public int getMode()
  {
    return this.mode;
  }

  public String getName()
  {
    return this.name;
  }

  public String getPlayUrl()
  {
    return this.playUrl;
  }

  public int getSort()
  {
    return this.sort;
  }

  public int getTypeId()
  {
    return this.typeId;
  }

  public String getTypeIds()
  {
    return this.typeIds;
  }

  public boolean isAutoRelay()
  {
    return this.isAutoRelay;
  }

  public boolean isLock()
  {
    return this.isLock;
  }

  public boolean isP2p()
  {
    return this.isP2p;
  }

  public boolean isShowGlobalLogo()
  {
    return this.isShowGlobalLogo;
  }

  public boolean isShowLogoOnBox()
  {
    return this.isShowLogoOnBox;
  }

  public void setAdImage(String paramString)
  {
    this.adImage = paramString;
  }

  public void setAllowRegion(String paramString)
  {
    this.allowRegion = paramString;
  }

  public void setAutoRelay(boolean paramBoolean)
  {
    this.isAutoRelay = paramBoolean;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void setIsFree(int paramInt)
  {
    this.isFree = paramInt;
  }

  public void setLock(boolean paramBoolean)
  {
    this.isLock = paramBoolean;
  }

  public void setLogo(String paramString)
  {
    this.logo = paramString;
  }

  public void setMode(int paramInt)
  {
    this.mode = paramInt;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setP2p(boolean paramBoolean)
  {
    this.isP2p = paramBoolean;
  }

  public void setPlayUrl(String paramString)
  {
    this.playUrl = paramString;
  }

  public void setShowGlobalLogo(boolean paramBoolean)
  {
    this.isShowGlobalLogo = paramBoolean;
  }

  public void setShowLogoOnBox(boolean paramBoolean)
  {
    this.isShowLogoOnBox = paramBoolean;
  }

  public void setSort(int paramInt)
  {
    this.sort = paramInt;
  }

  public void setTypeId(int paramInt)
  {
    this.typeId = paramInt;
  }

  public void setTypeIds(String paramString)
  {
    this.typeIds = paramString;
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.model.Channel
 * JD-Core Version:    0.6.0
 */