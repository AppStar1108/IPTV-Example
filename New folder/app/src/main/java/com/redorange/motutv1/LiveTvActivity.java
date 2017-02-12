package com.redorange.motutv1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.extremeiptv.buzziptv.R;
import com.redorange.motutv1.adapter.ChannelAdapter;
import com.redorange.motutv1.adapter.SearchMenuAdapter;
import com.redorange.motutv1.app.Motutv1Application;
import com.redorange.motutv1.auth.UpdateUserService;
import com.redorange.motutv1.db.EpgDatas;
import com.redorange.motutv1.db.OperationLive;
import com.redorange.motutv1.db.OperationLock;
import com.redorange.motutv1.dialog.LoadPhotoDialog;
import com.redorange.motutv1.model.Channel;
import com.redorange.motutv1.model.ChannelType;
import com.redorange.motutv1.parser.LoginInforParser;
import com.redorange.motutv1.provider.ChannelProvider;
import com.redorange.motutv1.utils.SyncImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class LiveTvActivity extends Activity implements View.OnClickListener
{
	  Button btnAddFav;
	  Channel curChannel;
	  int curItem = 0;
	  int curTag = 0;
	  int oldItem = 0;
	  LoadPhotoDialog dialog;
	  ImageView imgIcon;
	  SyncImageLoader imgLoader;
	  Context m_con;
	  LinearLayout layoutLiveType;
	  LinearLayout[] layoutTypeName;
	  //ListView listChannel;
	  GridView listChannel;
	  ChannelAdapter liveAdapter;
	  Motutv1Application mApp;
	  OperationLive operationLive;
	  OperationLock operationLock;
	  ChannelProvider provider;
	  TextView txtchannelName;
	  List<ChannelType> typeList;
	  UpdateUserService updateUser;
	  ImageView btn_fav;
	  ImageView btn_refresh;
	  ImageView btn_lock;
	  
	  //Search
	  ListView searchmenulist;
	  ImageView searchmenuscrollbar;
	  ImageView searchmenuscrollwheel;
	  SearchMenuAdapter searchmenuadapter;
	  String cursearchkey;
	  ArrayList<Channel> cursearchmenu;
	  LinearLayout searchmenulayout;
	  int numberpressed = 0;
	  LinearLayout layoutlivetvsearch;
	  
	  
	  LinearLayout searchguidelayout;
	  ImageView searchguidemenuhide;

	  public static int bIsLoadedIPTV = 0; // 0: init status, 1: unloaded, 2:loaded
	  
	  Handler mHandler = new Handler()
	  {
	    public void handleMessage(Message paramMessage)
	    {
	    	switch (paramMessage.what)
	    	{
	    	case 6:
	    		if(LiveTvActivity.this.numberpressed > 0)
	    			LiveTvActivity.this.numberpressed--;
	    		if(LiveTvActivity.this.numberpressed == 0){
	    			LiveTvActivity.this.layoutlivetvsearch.setVisibility(8);
	    			LiveTvActivity.this.cursearchkey = "";
	    		}
	    		break;
	    	case 7:
	    		LiveTvActivity.this.searchguidelayout.setVisibility(8);
	    		break;
	    	case 8:
	    		View v = LiveTvActivity.this.listChannel.getChildAt(0);
                if (v != null) {
                    v.setSelected(true);
                }
                LiveTvActivity.this.listChannel.setSelection(0);
                for(int i = 1 ; i < LiveTvActivity.this.listChannel.getChildCount() ; i++){
	    	    	((ImageView)LiveTvActivity.this.listChannel.getChildAt(i).findViewById(R.id.txt_channel_sort_border)).setImageResource(R.drawable.tv02);
	    	    }
	    	    ((ImageView)v.findViewById(R.id.txt_channel_sort_border)).setImageResource(R.drawable.tv_highlight01);
	    		break;
	/*		case 9:// connection error
				hiddenLoading();
				break;
			case 10:// connection success
				goLoadData();
				break;*/
	    	default:
		        return;	  
	    	}
	    }
	  };
	  
	  private void addLiveFavirote()
	  {
	    Channel localChannel = (Channel)((ChannelType)this.typeList.get(this.curTag)).getChannelList().get(this.curItem);
	    this.operationLive.addToDeleteLive(localChannel, new OperationLive.AddToDeleteListener()
	    {
	      public void getNumber(int paramInt1, int paramInt2)
	      {
	        if ((paramInt1 == 1) && (paramInt2 > 0)){
	          Toast.makeText(LiveTvActivity.this.getApplicationContext(), LiveTvActivity.this.getString(R.string.favorite_success), 1).show();
	          LiveTvActivity.this.refreshFavorite();
	          return;
	        }
	        if ((paramInt1 == 0) && (paramInt2 > 0)){            
	          return;
	        }
	      }
	    });
	  }
	  
	  private void addLock()
	  {
	    Channel localChannel = (Channel)((ChannelType)this.typeList.get(this.curTag)).getChannelList().get(this.curItem);
	    this.operationLock.addToDeleteLive(localChannel, new OperationLock.AddToDeleteListener()
	    {
	      public void getNumber(int paramInt1, int paramInt2)
	      {
	        if ((paramInt1 == 1) && (paramInt2 > 0)){
	         // Toast.makeText(LiveTvActivity.this.getApplicationContext(), LiveTvActivity.this.getString(2130968591), 1).show();
	          LiveTvActivity.this.refreshLock();
	          return;
	        }
	        if ((paramInt1 == 0) && (paramInt2 > 0)){            
	         // Toast.makeText(LiveTvActivity.this.getApplicationContext(), LiveTvActivity.this.getString(2130968593), 1).show();
	          return;
	        }
	      }
	    });
	  }
	  
	  public void refresh(){	  
		  loadData();
	  }
	  
	  @SuppressLint("NewApi")
	private void addTypeView()
	  {
		  Display display = getWindowManager().getDefaultDisplay();
		  DisplayMetrics outMetrics = new DisplayMetrics();
		  display.getRealMetrics(outMetrics);
		  //display.getMetrics(outMetrics);
		  int width = outMetrics.widthPixels;
		  int height = outMetrics.heightPixels;
		  
		  if ((this.typeList == null) || (this.typeList.size() < 1)) return;   
	      LayoutInflater localLayoutInflater = getLayoutInflater();
	      int i = this.typeList.size();
	      this.layoutTypeName = new LinearLayout[i];
	      this.layoutLiveType.removeAllViews();
	      for (int j = 0; j < i; j++)
	      {
	        this.layoutTypeName[j] = ((LinearLayout)localLayoutInflater.inflate(R.layout.txt_typename, null));
	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        this.layoutTypeName[j].setGravity(Gravity.CENTER);
	        lp.height = (int)((float)height*0.078-1);
	        this.layoutTypeName[j].setLayoutParams(lp);
	        
	        ImageView localImageView = (ImageView)this.layoutTypeName[j].findViewById(R.id.img_type_name);
	        
	        LinearLayout.LayoutParams lp1 =  (LinearLayout.LayoutParams)localImageView.getLayoutParams();
	        lp1.width = (int)((float)lp.height*0.8);
	        lp1.height = lp1.width;
	        localImageView.setLayoutParams(lp1);
	        
	        switch(j){
	        case 0:
	        	localImageView.setImageResource(R.drawable.iconall);
	        	this.layoutTypeName[j].findViewById(R.id.img_listseparate).setVisibility(8);
	        	break;
	        case 1:
	        	localImageView.setImageResource(R.drawable.iconfavorite);
	        	break;
	        case 2:
	        	localImageView.setImageResource(R.drawable.iconlocked);
	        	break;
	        default:
	        	localImageView.setImageResource(R.drawable.iconpic);
	        	break;
	        }
	        
	        TextView localTextView = (TextView)this.layoutTypeName[j].findViewById(R.id.txt_type_name);
	        localTextView.setText(((ChannelType)this.typeList.get(j)).getName());
	        this.layoutTypeName[j].setTag(Integer.valueOf(j));
	        this.layoutTypeName[j].setOnClickListener(this);
	        this.layoutTypeName[j].setFocusable(true);
	        final int index = j;
	        this.layoutTypeName[j].setOnFocusChangeListener(new View.OnFocusChangeListener()
		    {
			      public void onFocusChange(View paramView, boolean paramBoolean)
			      {
			    	  if(paramBoolean)
			    		  changeTypeBackground(index, LiveTvActivity.this.typeList.size());
			      }
			    });
	        this.layoutLiveType.addView(this.layoutTypeName[j]);
	      }
	  }
	
	  private void changeTypeBackground(int paramInt1, int paramInt2)
	  {
	    for(int i = 0 ; i < LiveTvActivity.this.listChannel.getChildCount() ; i++){
	    	((ImageView)LiveTvActivity.this.listChannel.getChildAt(i).findViewById(R.id.txt_channel_sort_border)).setImageResource(R.drawable.tv02);
	    }
	    int i = 0;
	    while (i < paramInt2){
	    	 if (paramInt1 == i){
	    	      this.layoutTypeName[paramInt1].findViewById(R.id.txt_back_set_layout).setBackgroundResource(R.drawable.foucos_bg);
	    	      this.layoutTypeName[paramInt1].requestFocus();
	    	 }else
	    		 this.layoutTypeName[i].findViewById(R.id.txt_back_set_layout).setBackgroundColor(0);
	    	 i++;
	    }
	    return;   
	  }
	  
	  private void changeTypeBackgroundGrey(int paramInt1, int paramInt2)
	  {
	    int i = 0;
	    while (i < paramInt2){
	    	 if (paramInt1 == i){
	    	      this.layoutTypeName[paramInt1].findViewById(R.id.txt_back_set_layout).setBackgroundResource(R.drawable.foucos_bg_grey);
	    	 }else
	    		 this.layoutTypeName[i].findViewById(R.id.txt_back_set_layout).setBackgroundColor(0);
	    	 i++;
	    }
	  }
	
	  @SuppressLint("NewApi")
	private void initData()
	  {
		  Display display = getWindowManager().getDefaultDisplay();
		  DisplayMetrics outMetrics = new DisplayMetrics();
		  display.getRealMetrics(outMetrics);
		  //display.getMetrics(outMetrics);
		  int width = outMetrics.widthPixels;
		  int height = outMetrics.heightPixels;
		  
			btn_fav = (ImageView) findViewById(R.id.btn_favourite);
			btn_lock = (ImageView) findViewById(R.id.btn_lock);
			btn_refresh = (ImageView)findViewById(R.id.btn_refresh);
		    this.provider = new ChannelProvider(this);
		    this.liveAdapter = new ChannelAdapter(false,width,height, this);
		    this.imgLoader = new SyncImageLoader(this);
		  try {
			  this.updateUser = new UpdateUserService(this);
		  }catch (Exception e){
			  e.printStackTrace();
		  }


		    this.operationLive = new OperationLive(this);
		    this.operationLock = new OperationLock(this);
		    this.dialog = new LoadPhotoDialog(this, R.style.dialog);
		    this.listChannel.setAdapter(this.liveAdapter);
		    this.listChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		    {
			      @SuppressLint("NewApi")
			      public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			      {
			    	  if(paramView == null)
			    		  return;
			    	    for(int i = 0 ; i < LiveTvActivity.this.listChannel.getChildCount() ; i++){
			    	    	((ImageView)LiveTvActivity.this.listChannel.getChildAt(i).findViewById(R.id.txt_channel_sort_border)).setImageResource(R.drawable.tv02);
			    	    }
			    	    ((ImageView)paramView.findViewById(R.id.txt_channel_sort_border)).setImageResource(R.drawable.tv_highlight01);
			    	  	findViewById(R.id.four_button_layout).setVisibility(0);
				        LiveTvActivity.this.curItem = paramInt;
				        LiveTvActivity.this.curChannel = ((Channel)((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList().get(LiveTvActivity.this.curItem));
				        try {
							if (!updateEpgInfo()) return;
						} catch (JSONException e) {
							//e.printStackTrace();
						}
				        LiveTvActivity.this.imgIcon.setImageResource(R.drawable.picture);
				        Channel localChannel = (Channel)LiveTvActivity.this.liveAdapter.getItem(paramInt);
				        Bitmap bmplogo = BitmapFactory.decodeFile(localChannel.getLogo());
				        LiveTvActivity.this.imgIcon.setBackground(null);
				        if(bmplogo != null){
				        	LiveTvActivity.this.imgIcon.setImageBitmap(getRoundedCornerBitmap(bmplogo, Color.TRANSPARENT, 16, 0, LiveTvActivity.this));
				        	//LiveTvActivity.this.imgIcon.setImageBitmap(bmplogo);
				        	if(bmplogo.getWidth() != bmplogo.getHeight())
				          		LiveTvActivity.this.imgIcon.setBackgroundResource(R.drawable.sort_bg);
				          	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
				  	  	  	chl_params.setMargins(9 , 9 , 9 , 9);
				  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
				        }else{
				        	LiveTvActivity.this.imgIcon.setImageResource(R.drawable.tv_default);
				        	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
				  	  	  	chl_params.setMargins(0 , 0 , 0 , 0);
				  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
				        }
				        LiveTvActivity.this.txtchannelName.setText(localChannel.getSort() + " " + localChannel.getName());
				        LiveTvActivity.this.changeTypeBackgroundGrey(LiveTvActivity.this.curTag, LiveTvActivity.this.typeList.size());
				        
			      }
			
			      public void onNothingSelected(AdapterView<?> paramAdapterView)
			      {
			    	  for(int i = 0 ; i < LiveTvActivity.this.listChannel.getChildCount() ; i++){
			    	    	((ImageView)LiveTvActivity.this.listChannel.getChildAt(i).findViewById(R.id.txt_channel_sort_border)).setImageResource(R.drawable.tv02);
			    	    }
			    	  if(LiveTvActivity.this.layoutLiveType.isFocused())
			    		  findViewById(R.id.four_button_layout).setVisibility(8);
			      }
		    });
		    
		    this.listChannel.setOnItemClickListener(new AdapterView.OnItemClickListener()
		    {
		      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
		      {
			    	findViewById(R.id.four_button_layout).setVisibility(0);
			    	LiveTvActivity.this.curItem = paramInt;
			        LiveTvActivity.this.curChannel = ((Channel)((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList().get(LiveTvActivity.this.curItem));
			        Channel localChannel = (Channel)LiveTvActivity.this.liveAdapter.getItem(paramInt);
			        Bitmap bmplogo = BitmapFactory.decodeFile(localChannel.getLogo());
			        try {
						if (!updateEpgInfo()) return;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
			        if(bmplogo != null)
			        	LiveTvActivity.this.imgIcon.setImageBitmap(getRoundedCornerBitmap(bmplogo, Color.TRANSPARENT, 16, 0, LiveTvActivity.this));
			        else
			        	LiveTvActivity.this.imgIcon.setImageResource(R.drawable.tv_default);
			        LiveTvActivity.this.txtchannelName.setText(localChannel.getSort() + " " + localChannel.getName());
			    		
			        Intent localIntent = new Intent();
			        localIntent.putExtra("type_id", LiveTvActivity.this.curTag);
			        localIntent.putExtra("item_id", paramInt);
			        LiveTvActivity.this.mApp.setTypeList(LiveTvActivity.this.typeList);
			        localIntent.setClass(LiveTvActivity.this, LivePlayActivity.class);
			        //Log.i("ListView","Change");
			        LiveTvActivity.this.startActivity(localIntent);
		      }
		    });
		    
		    this.listChannel.setOnScrollListener(new GridView.OnScrollListener(){
				@Override
				public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					TextView txtview = (TextView)findViewById(R.id.livetv_channel_pages);
					try{
						int index = LiveTvActivity.this.listChannel.getFirstVisiblePosition();
						int count = ((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList().size();
						int xpos = index/5;
						int xcount = count/5;
						if(count%5 != 0)
							xcount++;
						int totalpages = count/20;
						if(count % 20 != 0)
							totalpages++;
						int curpage = (int)(index/20)+1;
						if(totalpages == 0){
							curpage = 0;
						}
						txtview.setText("Page: "+ Integer.toString(curpage) + "/" + Integer.toString(totalpages));
						xcount -= 4;
						float pos = (float)xpos / xcount;
								
						ImageView imgview = (ImageView)findViewById(R.id.livetv_scrollwheel_img);
						ImageView imgbar = (ImageView)findViewById(R.id.livetv_scrollbar_img);
						float y = pos * (imgbar.getHeight()-imgview.getHeight());
						imgview.setY(y);
					}catch(Exception exception){
						txtview.setText("");
						//exception.printStackTrace();
					}
				}

				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
					// TODO Auto-generated method stub
				}
		    });
		    
		    btn_fav.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View paramView)
		      {
		        LiveTvActivity.this.addLiveFavirote();
		      }
		    });
		    
		    btn_refresh.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View paramView)
		      {
		        LiveTvActivity.this.refresh();
		      }
		    });
		    
		    btn_lock.setOnClickListener(new View.OnClickListener()
		    {
		      public void onClick(View paramView)
		      {
		        LiveTvActivity.this.addLock();
		      }
		    });
		    
		    btn_fav.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					btn_fav.setImageResource(R.drawable.tvkeygreen);
				}
			});
		    
		    btn_refresh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					btn_refresh.setImageResource(R.drawable.tvkeyred);
				}
			});
		    
		    btn_lock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					btn_lock.setImageResource(R.drawable.tvkeyyellow);
				}
			});
		    
		    btn_fav.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg1.getAction() == MotionEvent.ACTION_DOWN){				
						btn_fav.setImageResource(R.drawable.tvkeygreen);
					}
					else if (arg1.getAction() == MotionEvent.ACTION_UP){
						btn_fav.setImageResource(R.drawable.tvkeygreen);
					}
					return false;
				}
			});
		    
		    btn_refresh.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg1.getAction() == MotionEvent.ACTION_DOWN){				
						btn_refresh.setImageResource(R.drawable.tvkeyred);
					}
					else if (arg1.getAction() == MotionEvent.ACTION_UP){
						btn_refresh.setImageResource(R.drawable.tvkeyred);
					}
					return false;
				}
			});
		    
		    btn_lock.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg1.getAction() == MotionEvent.ACTION_DOWN){				
						btn_lock.setImageResource(R.drawable.tvkeyyellow);
					}
					else if (arg1.getAction() == MotionEvent.ACTION_UP){
						btn_lock.setImageResource(R.drawable.tvkeyyellow);
					}
					return false;
				}
			});
		    
		    this.layoutlivetvsearch.setVisibility(8);
		      this.searchmenuadapter = new SearchMenuAdapter(outMetrics.widthPixels, (int)((float)outMetrics.heightPixels*0.7));
		      LiveTvActivity.this.layoutlivetvsearch.setVisibility(8);
		      this.searchmenulist.setAdapter(this.searchmenuadapter);
		      this.cursearchmenu = new ArrayList<Channel>();
		      this.searchmenulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Channel item = LiveTvActivity.this.cursearchmenu.get(arg2);
					LiveTvActivity.this.curTag = 0;
					
					int curitemid = 0;
					List<Channel> list = ((ChannelType)LiveTvActivity.this.typeList.get(0)).getChannelList();
					  for(int i = 0 ; i < list.size() ; i++){
						  Channel ch = list.get(i);
						  if(ch.getSort() == item.getSort()){
							  curitemid = i;
							  break;
						  }
					  }
					  
					  LiveTvActivity.this.curChannel = item;
					  
					Intent localIntent = new Intent();
			        localIntent.putExtra("type_id", LiveTvActivity.this.curTag);
			        localIntent.putExtra("item_id", curitemid);
			        LiveTvActivity.this.layoutlivetvsearch.setVisibility(8);
			        LiveTvActivity.this.mApp.setTypeList(LiveTvActivity.this.typeList);
			        localIntent.setClass(LiveTvActivity.this, LivePlayActivity.class);
			        //Log.i("ListView","Change");
			        LiveTvActivity.this.startActivity(localIntent);
				    
				}
		      });
		      
		      this.searchmenulist.setOnScrollListener(new ListView.OnScrollListener() {
		    	  @Override
		    	  public void onScrollStateChanged(AbsListView arg0, int arg1) {
		    		  	
		    	  }
				
		    	  @Override
		   		  public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		    		  	try{
							int index = searchmenulist.getFirstVisiblePosition();
							int xcount = LiveTvActivity.this.cursearchmenu.size();
							if(xcount > 10)
								xcount -= 10;
							else 
								return;
							float pos = (float)index / xcount;
							
							float y = pos * (LiveTvActivity.this.searchmenuscrollbar.getHeight()-LiveTvActivity.this.searchmenuscrollwheel.getHeight());
							LiveTvActivity.this.searchmenuscrollwheel.setY(y);
						}catch(Exception exception){
							//exception.printStackTrace();
						}
				  }
		      });
		      
		      this.searchmenulist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					numberpressed++;
					LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(6, 5000L);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
		    	  
		      });
		      this.cursearchkey = "";
		      LiveTvActivity.this.searchguidelayout.setVisibility(8);
		      this.searchguidemenuhide.setOnClickListener(new View.OnClickListener() {
				
		    	  @Override
				  public void onClick(View arg0) {
		    		  LiveTvActivity.this.searchguidelayout.setVisibility(8);
				  }
		      });
	  }
	
	  private void initView()
	  {
	    this.layoutLiveType = ((LinearLayout)findViewById(R.id.layout_livetype));
	    this.listChannel = ((GridView)findViewById(R.id.listview_live));
	    this.imgIcon = ((ImageView)findViewById(R.id.description_logo));
	    this.txtchannelName = ((TextView)findViewById(R.id.desc_tt));
	    this.searchmenulist = (ListView)findViewById(R.id.livetv_search_menu_list);
	    this.searchmenuscrollbar = (ImageView)findViewById(R.id.livetv_searchmenu_scrollbar);
	    this.searchmenuscrollwheel = (ImageView)findViewById(R.id.livetv_searchmenu_scrollwheel);
	    this.searchmenulayout = (LinearLayout)findViewById(R.id.livetv_search_menu_layout);
	    this.layoutlivetvsearch = (LinearLayout)findViewById(R.id.layout_livetv_search);
	    this.searchguidelayout = (LinearLayout)findViewById(R.id.layout_bottom_guide);
	    this.searchguidemenuhide = (ImageView)findViewById(R.id.img_dontshow);
	    findViewById(R.id.livetv_scrollbar_img).setVisibility(View.INVISIBLE);
	    findViewById(R.id.livetv_scrollwheel_img).setVisibility(View.INVISIBLE);

		  findViewById(R.id.rly_btnback).setVisibility(View.VISIBLE);
		  findViewById(R.id.btn_sn).setVisibility(View.INVISIBLE);
		  findViewById(R.id.btn_retry).setVisibility(View.INVISIBLE);
		  findViewById(R.id.btn_exit).setVisibility(View.INVISIBLE);
		  ((Button) findViewById(R.id.btn_exit)).setEnabled(false);
		  ((Button) findViewById(R.id.btn_retry)).setEnabled(false);
	  }
	
	  private boolean isExitChannel()
	  {
	    return this.operationLive.isInsert(this.curChannel.getId());
	  }
	
	  private void loadData()
	  {
		//Toast.makeText(m_con,"LoadData...",Toast.LENGTH_SHORT).show();
	    String str = LiveTvActivity.this.updateUser.getValue("liveChannelIds");
		 LiveTvActivity.this.provider.loadChannel(str, new ChannelProvider.loadChannelTypeHandler()
		    {
		      public void onFailure(Throwable paramThrowable)
		      {
		        //paramThrowable.printStackTrace();
		        Toast.makeText(LiveTvActivity.this.getApplicationContext(), LiveTvActivity.this.getString(R.string.data_load_fail), 1).show();
				hiddenLoading();
		      }
		      public void onFinish()
		      {
//				  Toast.makeText(LiveTvActivity.this.getApplicationContext(), LiveTvActivity.this.getString(R.string.data_load_fail), 1).show();
//				  hiddenLoading();
		      }
		
		      @SuppressLint("NewApi")
			  public void onSuccess(List<ChannelType> paramList)
		      {
		    	  //Toast.makeText(m_con,"Success...",Toast.LENGTH_SHORT).show();
			        if (paramList == null) return;        
			        LiveTvActivity.this.typeList = paramList;
			        LiveTvActivity.this.addTypeView();
			        LiveTvActivity.this.refreshFavorite();        
			        if (LiveTvActivity.this.layoutTypeName == null)
			        	return;
			        LiveTvActivity.this.changeTypeBackgroundGrey(LiveTvActivity.this.curTag, LiveTvActivity.this.typeList.size());
			        LiveTvActivity.this.liveAdapter.update(((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList());          
			        if ((((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList() == null) || (((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList().isEmpty())) 
			        	return;
			          findViewById(R.id.four_button_layout).setVisibility(0);
			          LiveTvActivity.this.imgIcon.setImageResource(R.drawable.picture);
			          Channel localChannel = (Channel)LiveTvActivity.this.liveAdapter.getItem(0);
			          if(((ChannelType)paramList.get(0)).getChannelList().size() >= 20){
			        	  findViewById(R.id.livetv_scrollbar_img).setVisibility(0);
			        	  findViewById(R.id.livetv_scrollwheel_img).setVisibility(0);
			          }
			          LiveTvActivity.this.curChannel = localChannel;
			          try {
							if (!updateEpgInfo()) return;
						} catch (JSONException e) {
							//e.printStackTrace();
						  hiddenLoading();
						  return;
						}
			          Bitmap bmplogo = BitmapFactory.decodeFile(localChannel.getLogo());
			          LiveTvActivity.this.imgIcon.setBackground(null);
			          if(bmplogo != null){
			          	LiveTvActivity.this.imgIcon.setImageBitmap(getRoundedCornerBitmap(bmplogo, Color.TRANSPARENT, 16, 0, LiveTvActivity.this));
			          	if(bmplogo.getWidth() != bmplogo.getHeight())
			          		LiveTvActivity.this.imgIcon.setBackgroundResource(R.drawable.sort_bg);
			          	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
			  	  	  	chl_params.setMargins(9 , 9 , 9 , 9);
			  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
			          }else{
			          	LiveTvActivity.this.imgIcon.setImageResource(R.drawable.tv_default);
			          	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
			  	  	  	chl_params.setMargins(0 , 0 , 0 , 0);
			  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
			          }
			          findViewById(R.id.four_button_layout).setVisibility(0);
			          LiveTvActivity.this.txtchannelName.setText(localChannel.getSort() + " " + localChannel.getName());

			          LiveTvActivity.this.listChannel.requestFocus();
			          LiveTvActivity.this.searchguidelayout.setVisibility(0);
			          LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(7, 5000L);
			          LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(8, 500L);

				      findViewById(R.id.rly_btnback).setVisibility(View.INVISIBLE);
			          LiveTvActivity.this.dialog.dismiss();
		      	}
		    });
	  }
	  
	  private void refreshContent(ChannelType paramChannelType)
	  {
	    this.liveAdapter.update(paramChannelType.getChannelList());
	  }
	
	  @SuppressLint("NewApi")
	public void onClick(View paramView)
	  {
		  if (paramView.getId()==R.id.btn_retry)		  {
			  // Retry Connection
			  Start_Proc();
			  return;
		  }
		  if (paramView.getId()==R.id.btn_exit)
		  {
			  //Application Exit
			  android.os.Process.killProcess(android.os.Process.myPid());
			  return;
		  }
		  this.curTag = ((Integer)paramView.getTag()).intValue();
	    changeTypeBackground(this.curTag, this.typeList.size());
	    ChannelType temp = (ChannelType)this.typeList.get(this.curTag);
	    ((TextView)findViewById(R.id.livetv_type_heading)).setText(temp.getName());
	    
	    ((ImageView)findViewById(R.id.description_logo_border)).setVisibility(8);
	    LiveTvActivity.this.imgIcon.setBackground(null);
	    LiveTvActivity.this.imgIcon.setImageBitmap(null);
        LiveTvActivity.this.txtchannelName.setText("");
        ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(8);
		((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
		((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);
        ((TextView)findViewById(R.id.livetv_epg_time_1)).setText("");
		((TextView)findViewById(R.id.livetv_epg_info_1)).setText("");
		((TextView)findViewById(R.id.livetv_epg_time_2)).setText("");
		((TextView)findViewById(R.id.livetv_epg_info_2)).setText("");
		((TextView)findViewById(R.id.livetv_epg_time_3)).setText("");
		((TextView)findViewById(R.id.livetv_epg_info_3)).setText("");
		
		//listChannel
		this.listChannel.setAdapter(null);
	    this.liveAdapter.update(((ChannelType)this.typeList.get(this.curTag)).getChannelList());
	    this.listChannel.setAdapter(this.liveAdapter);
	    this.curItem = 0;
	    if (((ChannelType)this.typeList.get(this.curTag)).getChannelList() != null)
	    {
	    	if (this.typeList.get(this.curTag).getChannelList().size() == 0){
	    		findViewById(R.id.livetv_scrollbar_img).setVisibility(8);
	    		findViewById(R.id.livetv_scrollwheel_img).setVisibility(8);
	    		return;
	    	}
	    	
	    	this.curChannel = ((Channel)((ChannelType)this.typeList.get(this.curTag)).getChannelList().get(this.curItem));
	    	if(((ChannelType)this.typeList.get(this.curTag)).getChannelList().size() < 20){
	    		this.listChannel.setVerticalScrollBarEnabled(false);
	    		findViewById(R.id.livetv_scrollbar_img).setVisibility(8);
	    		findViewById(R.id.livetv_scrollwheel_img).setVisibility(8);
	    	}else{
	    		LiveTvActivity.this.listChannel.smoothScrollToPosition(0);
	    		findViewById(R.id.livetv_scrollwheel_img).setY(0);
	    		this.listChannel.setVerticalScrollBarEnabled(true);
	    		findViewById(R.id.livetv_scrollbar_img).setVisibility(0);
	    		findViewById(R.id.livetv_scrollwheel_img).setVisibility(0);
	    	}
	    	
	    	findViewById(R.id.four_button_layout).setVisibility(0);
	        try {
				if (!updateEpgInfo()) return;
			} catch (JSONException e) {
				//e.printStackTrace();
			}
	        LiveTvActivity.this.imgIcon.setImageResource(R.drawable.picture);
	        Channel localChannel = (Channel)LiveTvActivity.this.liveAdapter.getItem(0);
	        Bitmap bmplogo = BitmapFactory.decodeFile(localChannel.getLogo());
	        LiveTvActivity.this.imgIcon.setBackground(null);
	        if(bmplogo != null){
	        	LiveTvActivity.this.imgIcon.setImageBitmap(getRoundedCornerBitmap(bmplogo, Color.TRANSPARENT, 16, 0, this));
	        	if(bmplogo.getWidth() != bmplogo.getHeight())
	          		LiveTvActivity.this.imgIcon.setBackgroundResource(R.drawable.sort_bg);
	          	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
	  	  	  	chl_params.setMargins(9 , 9 , 9 , 9);
	  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
	        }else{
	        	LiveTvActivity.this.imgIcon.setImageResource(R.drawable.tv_default);
	        	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
	  	  	  	chl_params.setMargins(0 , 0 , 0 , 0);
	  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
	        }
	        LiveTvActivity.this.txtchannelName.setText(localChannel.getSort() + " " + localChannel.getName());
	        
	        LiveTvActivity.this.changeTypeBackgroundGrey(LiveTvActivity.this.curTag, LiveTvActivity.this.typeList.size());
	    	listChannel.requestFocus();
	    	LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(8, 500L);
	    }
	    else
	    {
	    	 Toast.makeText(this, "No item", Toast.LENGTH_SHORT).show();
	    	 findViewById(R.id.livetv_scrollbar_img).setVisibility(8);
	    	 findViewById(R.id.livetv_scrollwheel_img).setVisibility(8);
	      return;
	    }
	  }
	
	  protected void onCreate(Bundle paramBundle)
	  {
	    super.onCreate(paramBundle);
	    setContentView(R.layout.activity_livetv);
	    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
	    findViewById(R.id.four_button_layout).setVisibility(8);
	    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_FULLSCREEN
	            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
	    this.mApp = ((Motutv1Application)getApplication());
	    m_con = this;
	    ((ImageView)findViewById(R.id.description_logo_border)).setVisibility(8);
	    ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(8);
		((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
		((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);

		  setClick(R.id.btn_exit);
		  setClick(R.id.btn_retry);

		  Start_Proc();
	  }
	 
		public void Start_Proc()
		{

			initView();
			initData();
			refresUserInfo();
			this.dialog.show();

			Thread t= new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (this) {
						runOnUiThread(new Runnable() {
						@Override
						public void run() {
							LiveTvActivity.this.loadData();
						}
					});
					}
				}
			});
			t.start();
			EpgDatas.Initialize(LiveTvActivity.this);
		}
	    public View setClick(int id)
		{
			View v = findViewById(id);
			v.setOnClickListener(this);
			return v;
	    }
	public void goLoadData(){// connection success
		findViewById(R.id.rly_btnback).setVisibility(View.INVISIBLE);
	//	if(this.dialog!=null)
	//		this.dialog.dismiss();
	//	refresUserInfo();
	}
	public void hiddenLoading(){// error networking
		((TextView) findViewById(R.id.btn_sn)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.btn_sn)).setText("SN:" + updateUser.getSn());
		((Button) findViewById(R.id.btn_exit)).setEnabled(true);
		((Button) findViewById(R.id.btn_exit)).setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.btn_retry)).setEnabled(true);
		((Button) findViewById(R.id.btn_retry)).setVisibility(View.VISIBLE);
		((RelativeLayout) findViewById(R.id.rly_btnback)).setVisibility(View.VISIBLE);
		if(this.dialog!=null)
			this.dialog.dismiss();
	}
	  private void refresUserInfo()
	  {
		  this.updateUser.onStartCommand(new UpdateUserService.setIsActiveListener()
		  {
			  public void onFauil()
			  {
		//		 Log.i("ok1","ok1");
				 hiddenLoading();
			  }

			  public void onFinish(){
				  //Log.i("OK","OK");
			  }

			  public void onSuccess(String paramString)
			  {
				  HashMap localHashMap;
				  boolean bool2;
				  boolean bool1 = false;
				  int i = 0;
				  if ((paramString != null) && (paramString.length() > 2))
				  {
					  localHashMap = LoginInforParser.analyLoginInfo(paramString);
					  StringBuilder localStringBuilder = new StringBuilder("map == null?  ");
					  if (localHashMap == null)
					  {
						  bool2 = true;
					  }
					  else 
						  bool2 = false;
		           
					  localHashMap.put("category", Integer.valueOf(1));
					  bool1 = ((Boolean)localHashMap.get("isActive")).booleanValue();
					  String str = String.valueOf(localHashMap.get("str"));
					  try{
						  if (str.substring(0, 1).equals("-"))
							  i = -1;              
						  else 
							  i = Integer.parseInt(str.substring(0, 1));
					  }catch(Exception exception){
						  i = -1;
					  }
				  } else{
					  localHashMap = new HashMap();
					  localHashMap.put("isActive", Boolean.valueOf(false));
					  localHashMap.put("category", Integer.valueOf(2));
					  i = 0;
					  bool1 = false;
				  }

			  }
		  });
	  }
	  
	  protected void onDestroy()
	  {
	    super.onDestroy();
	    if (this.dialog != null)
	      this.dialog.dismiss();
	    if (this.operationLive != null)
	      this.operationLive.close();
	  }
	
	  public boolean updateEpgInfo() throws JSONException
	  {
//		  hiddenLoading();
		  String key = "p_"+curChannel.getId();
		  JSONArray arr = EpgDatas.getEpgProgram(key);

		  int count = 3;
		  ((ImageView)findViewById(R.id.description_logo_border)).setVisibility(0);
		  if(arr == null){
			  ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(0);
			  ((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
			  ((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);
			  ((TextView)findViewById(R.id.livetv_epg_time_1)).setVisibility(0);
			  ((TextView)findViewById(R.id.livetv_epg_info_1)).setVisibility(8);
			  ((TextView)findViewById(R.id.livetv_epg_time_1)).setText("Program Information Not Found.");
			  hiddenLoading();
			  return false;
		  }
		  if(arr.length() == 0){
			  ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(0);
			  ((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
			  ((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);
			  ((TextView)findViewById(R.id.livetv_epg_time_1)).setVisibility(0);
			  ((TextView)findViewById(R.id.livetv_epg_info_1)).setVisibility(8);
			  ((TextView)findViewById(R.id.livetv_epg_time_1)).setText("Program Information Not Found.");
//			  hiddenLoading();
			  return true;
		  }
		  ((TextView)findViewById(R.id.livetv_epg_info_1)).setVisibility(0);
		  ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(0);
		  ((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(0);
		  ((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(0);
		  
		  if(arr.length() < 3)
			  count = arr.length();
		  for(int i = 0 ; i < count ; i++){
			  JSONObject obj = arr.getJSONObject(i);
			  String beginTime = obj.getString("beginTime");
			  beginTime = beginTime.substring(5,beginTime.length()-3);
			  String endTime = obj.getString("endTime");
			  endTime = endTime.substring(5,endTime.length()-3);
			  String info = obj.getString("name");
			  switch(i){
			  case 0:
				  ((TextView)findViewById(R.id.livetv_epg_time_1)).setText(beginTime+" - "+endTime);
				  ((TextView)findViewById(R.id.livetv_epg_info_1)).setText(info);
				  break;
			  case 1:
				  ((TextView)findViewById(R.id.livetv_epg_time_2)).setText(beginTime+" - "+endTime);
				  ((TextView)findViewById(R.id.livetv_epg_info_2)).setText(info);
				  break;
			  case 2:
				  ((TextView)findViewById(R.id.livetv_epg_time_3)).setText(beginTime+" - "+endTime);
				  ((TextView)findViewById(R.id.livetv_epg_info_3)).setText(info);
				  break;
			  }
		  }
		  for(int i = count ; i < 3 ; i++){
			  switch(i){
			  case 1:
				  ((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
				  break;
			  case 2:
				  ((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);
				  break;
			  }
		  }
		  return true;
	  }
	  
	  @SuppressLint("NewApi")
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	  {
	    /*if ((paramInt == 4) && (this.listChannel.hasFocus()))
	    {
	      this.layoutTypeName[this.curTag].requestFocus();
	      return true;
	    }*/
	    if (paramInt == paramKeyEvent.KEYCODE_DPAD_DOWN){
	    	if(this.numberpressed == 0 && listChannel.isFocused()){
	    		int pos = listChannel.getSelectedItemPosition();
	    		int xpos = pos/5;
	    		int count = ((ChannelType)LiveTvActivity.this.typeList.get(LiveTvActivity.this.curTag)).getChannelList().size();
				int xcount = count/5;
				if(count%5 != 0)
					xcount++;
				if(xpos+1 == xcount)
					return true;
	    	}
	    }else if (paramInt == paramKeyEvent.KEYCODE_DPAD_LEFT){
	    	//Log.i("Keydown","Left");
	    	((ImageView)findViewById(R.id.description_logo_border)).setVisibility(8);
		    LiveTvActivity.this.imgIcon.setBackground(null);
		    LiveTvActivity.this.imgIcon.setImageBitmap(null);
	        LiveTvActivity.this.txtchannelName.setText("");
	        ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(8);
			((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
			((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);
	        ((TextView)findViewById(R.id.livetv_epg_time_1)).setText("");
			((TextView)findViewById(R.id.livetv_epg_info_1)).setText("");
			((TextView)findViewById(R.id.livetv_epg_time_2)).setText("");
			((TextView)findViewById(R.id.livetv_epg_info_2)).setText("");
			((TextView)findViewById(R.id.livetv_epg_time_3)).setText("");
			((TextView)findViewById(R.id.livetv_epg_info_3)).setText("");
			
			LiveTvActivity.this.layoutTypeName[LiveTvActivity.this.curTag].requestFocus();
	    	findViewById(R.id.four_button_layout).setVisibility(8);
	    	changeTypeBackground(this.curTag, LiveTvActivity.this.typeList.size());
	    }else if(paramInt == paramKeyEvent.KEYCODE_DEL){//BackKey
	    	if(listChannel.isFocused()){
	    		((ImageView)findViewById(R.id.description_logo_border)).setVisibility(8);
	    	    LiveTvActivity.this.imgIcon.setBackground(null);
	    	    LiveTvActivity.this.imgIcon.setImageBitmap(null);
	            LiveTvActivity.this.txtchannelName.setText("");
	            ((LinearLayout)findViewById(R.id.livetv_epg_1)).setVisibility(8);
	    		((LinearLayout)findViewById(R.id.livetv_epg_2)).setVisibility(8);
	    		((LinearLayout)findViewById(R.id.livetv_epg_3)).setVisibility(8);
	            ((TextView)findViewById(R.id.livetv_epg_time_1)).setText("");
	    		((TextView)findViewById(R.id.livetv_epg_info_1)).setText("");
	    		((TextView)findViewById(R.id.livetv_epg_time_2)).setText("");
	    		((TextView)findViewById(R.id.livetv_epg_info_2)).setText("");
	    		((TextView)findViewById(R.id.livetv_epg_time_3)).setText("");
	    		((TextView)findViewById(R.id.livetv_epg_info_3)).setText("");
	    		
	    		LiveTvActivity.this.layoutTypeName[LiveTvActivity.this.curTag].requestFocus();
	    		findViewById(R.id.four_button_layout).setVisibility(8);
	    		changeTypeBackground(this.curTag, LiveTvActivity.this.typeList.size());
	    		return true;
	    	}else if(LiveTvActivity.this.layoutlivetvsearch.getVisibility() == 0){
	    		LiveTvActivity.this.layoutlivetvsearch.setVisibility(8);
	    		LiveTvActivity.this.cursearchkey = "";
	    		return true;
	    	}else{
	    		super.onBackPressed();
	    		return true;
	    	}
	    }else if (paramInt == paramKeyEvent.KEYCODE_DPAD_RIGHT){
	    	//Log.i("Keydown","Right");
	    	//listChannel
	    	if(listChannel.isFocused()){
	    		//int pos = listChannel.getSelectedItemPosition();
	    		//int xpos = pos%5;
	    		//if(xpos == 4)
					return true;
	    	}
		    //this.liveAdapter.update(((ChannelType)this.typeList.get(this.curTag)).getChannelList());
		    this.curItem = 0;
		    if (((ChannelType)this.typeList.get(this.curTag)).getChannelList() != null)
		    {
		    	if (this.typeList.get(this.curTag).getChannelList().size() == 0){
		    		findViewById(R.id.livetv_scrollbar_img).setVisibility(8);
		    		findViewById(R.id.livetv_scrollwheel_img).setVisibility(8);
		    	}else{
			    	this.curChannel = ((Channel)((ChannelType)this.typeList.get(this.curTag)).getChannelList().get(this.curItem));
			    	if(((ChannelType)this.typeList.get(this.curTag)).getChannelList().size() < 20){
			    		this.listChannel.setVerticalScrollBarEnabled(false);
			    		findViewById(R.id.livetv_scrollbar_img).setVisibility(8);
			    		findViewById(R.id.livetv_scrollwheel_img).setVisibility(8);
			    	}else{
			    		LiveTvActivity.this.listChannel.smoothScrollToPosition(0);
			    		findViewById(R.id.livetv_scrollwheel_img).setY(0);
			    		this.listChannel.setVerticalScrollBarEnabled(true);
			    		findViewById(R.id.livetv_scrollbar_img).setVisibility(0);
			    		findViewById(R.id.livetv_scrollwheel_img).setVisibility(0);
			    	}
			    	
			    	findViewById(R.id.four_button_layout).setVisibility(0);
			        try {
						if (!updateEpgInfo()) return true ;
					} catch (JSONException e) {
						//e.printStackTrace();
					}
			        Channel localChannel = (Channel)LiveTvActivity.this.liveAdapter.getItem(0);
			        Bitmap bmplogo = BitmapFactory.decodeFile(localChannel.getLogo());
			        LiveTvActivity.this.imgIcon.setBackground(null);
			        if(bmplogo != null){
			        	LiveTvActivity.this.imgIcon.setImageBitmap(getRoundedCornerBitmap(bmplogo, Color.TRANSPARENT, 16, 0, LiveTvActivity.this));
			        	if(bmplogo.getWidth() != bmplogo.getHeight())
			          		LiveTvActivity.this.imgIcon.setBackgroundResource(R.drawable.sort_bg);
			          	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
			  	  	  	chl_params.setMargins(9 , 9 , 9 , 9);
			  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
			        }else{
			        	LiveTvActivity.this.imgIcon.setImageResource(R.drawable.tv_default);
			        	FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) LiveTvActivity.this.imgIcon.getLayoutParams();
			  	  	  	chl_params.setMargins(0 , 0 , 0 , 0);
			  	  	  	LiveTvActivity.this.imgIcon.setLayoutParams(chl_params);
			        }
			        LiveTvActivity.this.txtchannelName.setText(localChannel.getSort() + " " + localChannel.getName());
			        findViewById(R.id.four_button_layout).setVisibility(0);
			        
			        LiveTvActivity.this.changeTypeBackgroundGrey(LiveTvActivity.this.curTag, LiveTvActivity.this.typeList.size());
			    	listChannel.requestFocus();
			    	LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(8, 500L);
		    	}
		    }
	    } else if(paramInt >= paramKeyEvent.KEYCODE_0 && paramInt <= paramKeyEvent.KEYCODE_9){
			NumberPressed(paramInt);
		}
	    return super.onKeyDown(paramInt, paramKeyEvent);
	  }
	
	  public static String IntToThreedigits(int t)
	  {
		  String s = String.format("%d", t);
		  if(s.length() == 3)
			  return s;
		  if(s.length() == 2)
			  return "0" + s;
		  if(s.length() == 1)
			  return "00" + s;
		  return "000";
	  }
	  
	  public void NumberPressed(int ascii)
	  {
		  numberpressed++;
		  LiveTvActivity.this.layoutlivetvsearch.setVisibility(0);
		  this.searchmenulist.requestFocus();
		  if(this.cursearchkey.length() == 3){
			  LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(6, 5000L);
			  return;
		  }
		  String s = String.format("%c", ascii+'0'-KeyEvent.KEYCODE_0);
		  this.cursearchkey = this.cursearchkey + s;
		  ArrayList<Channel> menuitems = new ArrayList<Channel>();
		  List<Channel> list = ((ChannelType)this.typeList.get(0)).getChannelList();
		  for(int i = 0 ; i < list.size() ; i++){
			  Channel item = list.get(i);
			  String id = IntToThreedigits(item.getSort());
			  if(id.contains(this.cursearchkey)){
				  menuitems.add(item);
			  }
		  }
		  this.cursearchmenu = menuitems;
		  searchmenuadapter.setNewList(menuitems);
		  if(menuitems.size() > 10){
			  this.searchmenuscrollbar.setVisibility(0);
			  this.searchmenuscrollwheel.setVisibility(0);
		  }else{
			  this.searchmenuscrollbar.setVisibility(8);
			  this.searchmenuscrollwheel.setVisibility(8);
		  }
		  View v = this.searchmenulist.getChildAt(0);
		  if(v != null)
			  v.setSelected(true);
		  LiveTvActivity.this.mHandler.sendEmptyMessageDelayed(6, 5000L);
	  }
	  
	  public void refreshFavorite()
	  {
		  List localList = this.operationLive.queryAllLive();
		  ((ChannelType)this.typeList.get(1)).setChannelList(localList);
		  if(((ChannelType)this.typeList.get(this.curTag)).getId() == -1)
			  refreshContent((ChannelType)this.typeList.get(1));
	  }
	  
	  public void refreshLock()
	  {
	    List localList = this.operationLock.queryAllLive();
	    ((ChannelType)this.typeList.get(2)).setChannelList(localList);
	    if (((ChannelType)this.typeList.get(this.curTag)).getId() == -2)
	      refreshContent((ChannelType)this.typeList.get(2));
	  }
	  
	  public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
		    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
		            Bitmap.Config.ARGB_8888);
		    Canvas canvas = new Canvas(output);

		    final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
		            context.getResources().getDisplayMetrics());
		    final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
		            context.getResources().getDisplayMetrics());
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		    final RectF rectF = new RectF(rect);

		    // prepare canvas for transfer
		    paint.setAntiAlias(true);
		    paint.setColor(0xFFFFFFFF);
		    paint.setStyle(Paint.Style.FILL);
		    canvas.drawARGB(0, 0, 0, 0);
		    canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

		    // draw bitmap
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);

		    // draw border
		    paint.setColor(color);
		    paint.setStyle(Paint.Style.STROKE);
		    paint.setStrokeWidth((float) borderSizePx);
		    canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

		    return output;
		}
	  
	  //EPG Url : http://62.210.182.14:8080/channel_resource/live/program.html
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.LiveTvActivity
 * JD-Core Version:    0.6.0
 */