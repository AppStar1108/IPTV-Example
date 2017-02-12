package com.redorange.motutv1.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.extremeiptv.buzziptv.R;
import com.ipmacro.window.BaseWindow;
import com.redorange.motutv1.model.Channel;
import com.redorange.motutv1.model.ChannelType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelChooseWindow extends BaseWindow
{
  LayoutInflater mInflater;
  protected OnClickListener onClickListener;
  int width;
  int height;
  AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      int i = ChannelChooseWindow.this.viewPager.getCurrentItem() % ChannelChooseWindow.this.pagerList.size();
      i = getCorrectIndexBack(i);
      int j = ((ChannelType)ChannelChooseWindow.this.typeList.get(i)).getChannelList().size();
      
      Bundle localBundle = new Bundle();
      localBundle.putInt("typeId", i);
      localBundle.putInt("itemId", paramInt % j);
      if (ChannelChooseWindow.this.onClickListener != null)
        ChannelChooseWindow.this.onClickListener.onClickListener(localBundle);
    }
  };
  AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener()
  {
    @SuppressLint("NewApi")
	public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      if (ChannelChooseWindow.this.isShowing())
      {
    	  ChannelChooseWindow.this.mHandler.removeMessages(1);
    	  ChannelChooseWindow.this.mHandler.sendEmptyMessageDelayed(1, 5000L);
      }
    }

    public void onNothingSelected(AdapterView<?> paramAdapterView)
    {
    }
  };
  ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
  {
    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
      ChannelChooseWindow.this.mHandler.removeMessages(1);
      ChannelChooseWindow.this.mHandler.sendEmptyMessageDelayed(1, 5000L);
    }
  };
  ChannelPagerAdapter pagerAdapter;
  List<View> pagerList;
  List<ChannelType> typeList;
  ViewPager viewPager;

  public ChannelChooseWindow(Context paramContext,int w, int h)
  {
    super(paramContext, R.layout.live_channel_choose, w, h);
    width = w;
    height = h;
    this.parent.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        return false;
      }
    });
    this.mInflater = ((LayoutInflater)this.mContext.getSystemService("layout_inflater"));
    this.viewPager = ((ViewPager)this.parent.findViewById(R.id.viewPager));
    this.pagerAdapter = new ChannelPagerAdapter();
    this.viewPager.setAdapter(this.pagerAdapter);
    this.viewPager.setOnPageChangeListener(this.onPageChangeListener);
  }

  public void setData(Object paramObject)
  {
    Map localMap = (Map)paramObject;
    int k;
    int n;
    if (localMap.containsKey("typeList"))
    {
    	this.typeList = ((List)localMap.get("typeList"));
      	k = ((Integer)localMap.get("curTypeId")).intValue();
      	k = getCorrectIndex(k);
      	this.pagerList = new ArrayList();
      	int m = this.typeList.size();
      	//Log.i("yy", "window==" + m);
      	n = 0;
      	while (n < m)
      	{
    	  	final ChannelType localChannelType = (ChannelType)this.typeList.get(n);
    	  	final LinearLayout localLinearLayout = (LinearLayout)this.mInflater.inflate(R.layout.live_channel_choose_pager, null);
    	  	((TextView)localLinearLayout.findViewById(R.id.txtTitle)).setText(localChannelType.getName());
    	  	final ListView localListView = (ListView)localLinearLayout.findViewById(R.id.listView);
    	  	List<Channel> locallist = localChannelType.getChannelList();
          
    	  	final ImageView imgview = (ImageView)localLinearLayout.findViewById(R.id.channelchooser_scrollwheel_img);
    	  	final ImageView imgbar = (ImageView)localLinearLayout.findViewById(R.id.channelchooser_scrollbar_img);
          
          	final int count;
          	if(locallist != null)
        	  	count = locallist.size();
          	else{
        	  	count = 0;
          	}
          	if(count == 0){
          		n++;
          		continue;
          	}
          	if(count > 10){
        	  	imgview.setVisibility(0);
        	  	imgbar.setVisibility(0);
          	}else{
        	  	imgview.setVisibility(8);
        	  	imgbar.setVisibility(8);
          	}

          	localListView.setAdapter(new ChannelListAdapter(locallist, (int)((float)height*0.73)));
          	localListView.setOnItemClickListener(this.onItemClickListener);
          	localListView.setOnItemSelectedListener(this.onItemSelectedListener);
          
          	localListView.setOnScrollListener(new ListView.OnScrollListener(){
	  			@SuppressLint("NewApi")
				@Override
	  			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
	  				try{
	  					int index = localListView.getFirstVisiblePosition();
	  					int xcount = count;
	  					if(xcount > 10)
	  						xcount -= 10;
	  					else 
	  						return;
	  					float pos = (float)index / xcount;
	  					
	  					float y = pos * (imgbar.getHeight()-imgview.getHeight());
	  					imgview.setY(y);
	  				}catch(Exception exception){
	  					//exception.printStackTrace();
	  				}
	  			}
	
	  			@Override
	  			public void onScrollStateChanged(AbsListView arg0, int arg1) {
	  				// TODO Auto-generated method stub
	  				
	  			}
	  	    });
          	this.pagerList.add(localLinearLayout);
          	n++;
      	}
        this.pagerAdapter.update();
        if (this.pagerList.size() > 3)
        	this.viewPager.setCurrentItem(k + 50000 * this.pagerList.size());
        else
        	this.viewPager.setCurrentItem(k);
        return;
    }
    if ((localMap.containsKey("curTypeId")) && (localMap.containsKey("curItemId"))){
    	int i = ((Integer)localMap.get("curTypeId")).intValue();
    	i = getCorrectIndex(i);
        int j = ((Integer)localMap.get("curItemId")).intValue();
        ((ListView)((View)this.pagerList.get(i)).findViewById(R.id.listView)).requestFocus();
        ((ListView)((View)this.pagerList.get(i)).findViewById(R.id.listView)).setSelection(j);
        return;        
    }
  }

  public int getCorrectIndex(int n)
  {
	  int m = n;
	  ChannelType localChannelType = (ChannelType)this.typeList.get(n);
	  if(localChannelType == null){
		  return -1;
	  }else{
		  List<Channel> locallist = localChannelType.getChannelList();
		  if(locallist == null){
			  return -1;
		  }else if(locallist.size() == 0)
			  return -1;
	  }
	  for(int i = 0 ; i < n ; i++){
		  localChannelType = (ChannelType)this.typeList.get(i);
		  if(localChannelType == null){
			  m--;
		  }else{
			  List<Channel> locallist = localChannelType.getChannelList();
			  if(locallist == null){
				  m--;
			  }else if(locallist.size() == 0)
				  m--;
		  }
	  }
	  return m;
  }
  
  public int getCorrectIndexBack(int n)
  {
	  for(int i = 0 ; i < this.typeList.size() ; i++){
		  if(getCorrectIndex(i) == n)
			  return i;
	  }
	  return 0;
  }
  
  public void setOnClickListener(OnClickListener paramOnClickListener)
  {
    this.onClickListener = paramOnClickListener;
  }

  public void showAtLocation(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    super.showAtLocation(paramView, paramInt1, paramInt2, paramInt3);
    this.mHandler.removeMessages(1);
    this.mHandler.sendEmptyMessageDelayed(1, 5000L);
  }

  class ChannelListAdapter extends BaseAdapter
  {
    List<Channel> dataList;
    int height;
    
    public ChannelListAdapter(List<Channel> arg1)
    {
      Object localObject;
      if (arg1 != null)
        this.dataList = arg1;
    }
    
    public ChannelListAdapter(List<Channel> arg1, int h)
    {
      Object localObject;
      height = h;
      if (arg1 != null)
        this.dataList = arg1;
    }

    public int getCount()
    {
      if (this.dataList == null)
        return 0;
      return this.dataList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.dataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    @SuppressLint("NewApi")
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
    	View localView = paramView;
        ViewHolder localViewHolder;
        
        if (localView == null)
        {
    	    localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.live_channel_choose_item, null);
    	    localViewHolder = new ViewHolder();
    	    localViewHolder.imglogo = ((ImageView)localView.findViewById(R.id.live_channel_list_img));
    	    localViewHolder.txtName = ((TextView)localView.findViewById(R.id.live_channel_list_text));
    	    LinearLayout.LayoutParams m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    	m.height = (int)((float)(height-109)/10);
	    	m.width = (int)(1.2*m.height);
	    	localViewHolder.imglogo.setLayoutParams(m);
    	    localView.setTag(localViewHolder);
         }else {
        	localViewHolder = (ViewHolder)localView.getTag();    	
         }

          URL newurl = null;
          Bitmap mIcon_val = null;
          Channel localChannel = (Channel)this.dataList.get(paramInt);
          mIcon_val = BitmapFactory.decodeFile(localChannel.getLogo());
          if (mIcon_val!= null){
        	  localViewHolder.imglogo.setImageBitmap(mIcon_val);
        	  if(mIcon_val.getWidth() != mIcon_val.getHeight())
        		  localViewHolder.imglogo.setBackgroundResource(R.drawable.sort_bg);
          }else{
        	  localViewHolder.imglogo.setImageResource(R.drawable.tv_default);
          }
          localViewHolder.txtName.setText(localChannel.getSort() + ",  " + localChannel.getName());
          return localView; 
    }
  }

  class ChannelPagerAdapter extends PagerAdapter
  {
    ChannelPagerAdapter()
    {
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
    	int i = paramInt % ChannelChooseWindow.this.pagerList.size();
    	((ViewPager)paramView).removeView((View)ChannelChooseWindow.this.pagerList.get(i));
    }

    public int getCount()
    {
     int i = 0;
      if (ChannelChooseWindow.this.pagerList != null)
    	  i = ChannelChooseWindow.this.pagerList.size();
        if (i > 3)
          i = 0x7FFFFFFF;
        return i;
    }

    public Object instantiateItem(View paramView, int paramInt)
    {
    	int i = paramInt % ChannelChooseWindow.this.pagerList.size();
    	((ViewPager)paramView).addView((View)ChannelChooseWindow.this.pagerList.get(i), 0);
    	return ChannelChooseWindow.this.pagerList.get(i);
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }

    public void update()
    {
    	notifyDataSetChanged();
    }
  }

  public static abstract interface OnClickListener
  {
    public abstract void onClickListener(Object paramObject);
  }
  
  static class ViewHolder
  {
	  ImageView imglogo;
	  TextView txtName;
  }
}
