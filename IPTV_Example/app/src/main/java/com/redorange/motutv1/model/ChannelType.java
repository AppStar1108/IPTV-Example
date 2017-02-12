package com.redorange.motutv1.model;

import java.util.ArrayList;
import java.util.List;

public class ChannelType
{
  List<Channel> channelList;
  int id;
  String name;

  public ChannelType(int paramInt, String paramString)
  {
    this.id = paramInt;
    this.name = paramString;
  }

  public void addChild(Channel paramChannel)
  {
    if (this.channelList == null)
      this.channelList = new ArrayList();
    this.channelList.add(paramChannel);
  }

  public List<Channel> getChannelList()
  {
    return this.channelList;
  }

  public int getId()
  {
    return this.id;
  }

  public String getName()
  {
    return this.name;
  }

  public void setChannelList(List<Channel> paramList)
  {
    this.channelList = paramList;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.model.ChannelType
 * JD-Core Version:    0.6.0
 */