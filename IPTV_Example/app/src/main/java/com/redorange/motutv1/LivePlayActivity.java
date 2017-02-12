package com.redorange.motutv1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.extremeiptv.buzziptv.R;
import com.ipmacro.player.other;
import com.ipmacro.ppcore.PPCore;
import com.ipmacro.utils.aes.AESUtil;
import com.redorange.motutv1.adapter.ChannelAdapter;
import com.redorange.motutv1.adapter.SearchMenuAdapter;
import com.redorange.motutv1.app.Motutv1Application;
import com.redorange.motutv1.db.EpgDatas;
import com.redorange.motutv1.model.Channel;
import com.redorange.motutv1.model.ChannelType;
import com.redorange.motutv1.utils.Clock;
import com.redorange.motutv1.utils.OnClockTickListner;
import com.redorange.motutv1.utils.SyncImageLoader;
import com.redorange.motutv1.window.ChannelChooseWindow;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) 
public class LivePlayActivity extends Activity
{
  private static final int EPG_WAITTIME = 5000;
  public static final String ITEM_ID = "item_id";
  private static final int MSG_ERROR = 4;
  private static final int MSG_HIDE_EPG = 1;
  private static final int MSG_START_PLAY = 3;
  public static final String TAG = "PPCore";
  public static final String TYPE_ID = "type_id";
  private static final int start_waitTime = 1000;
  Channel curChannel;
  int curItemId = 0;
  int curTypeId = 0;
  ImageView imgIcon;
  boolean bool;
  SyncImageLoader imgLoader;
  public ChannelAdapter liveAdapter;
  boolean isDisplayEPG = false;
  LinearLayout layoutInfo;
  RelativeLayout layoutPlay;
  int curItem;
  int curTag;
  
  Motutv1Application mApp;
  
  //volume
  LinearLayout layoutVolume;
  AudioManager audioManager;
  ImageView playVolumeBackground;
  ImageView playVolumeProgress;
  TextView playVolumeAmount;
  int maxVolume;
  int currentVolume;
  boolean isMuted;
  ImageView playVolumeImage;
  int volumechanged;
  
  //Search
  ListView searchmenulist;
  ImageView searchmenuscrollbar;
  ImageView searchmenuscrollwheel;
  SearchMenuAdapter searchmenuadapter;
  String cursearchkey;
  ArrayList<Channel> cursearchmenu;
  LinearLayout searchmenulayout;
  int numberpressed = 0;
  
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 2:
      default:
        return;
      case 1:
        LivePlayActivity.this.layoutInfo.setVisibility(8);
        LivePlayActivity.this.isDisplayEPG = false;
        return;
      case 3:
        LivePlayActivity.this.startPlay(LivePlayActivity.this.getChannel(LivePlayActivity.this.curTypeId, LivePlayActivity.this.curItemId));
        return;
      case 4:
    	  LivePlayActivity.this.startPlay(LivePlayActivity.this.getChannel(LivePlayActivity.this.curTypeId, LivePlayActivity.this.curItemId));
    	  return;
      case 5:
    	  if(LivePlayActivity.this.volumechanged > 0)
    		  LivePlayActivity.this.volumechanged--;
    	  if(LivePlayActivity.this.volumechanged == 0)
    		  LivePlayActivity.this.layoutVolume.setVisibility(8);
    	  return;
      case 6:
    	  if(LivePlayActivity.this.numberpressed > 0)
    		  LivePlayActivity.this.numberpressed--;
    	  if(LivePlayActivity.this.numberpressed == 0){
    		  LivePlayActivity.this.searchmenulayout.setVisibility(8);
    		  LivePlayActivity.this.cursearchkey = "";
    	  }
    	  return;
      }
    }
  };
  SurfaceHolder mHolder;
  SurfaceView mSurface;
  other othr;
  ChannelChooseWindow popupWindow;
  TextView txtLoadTip;
  TextView txtLoadingRate;
  TextView txtName;
  TextView txtNoSignal;
  TextView txtEpg1;
  TextView txtEpg2;
  TextView txtEpg3;
  List<ChannelType> typeList;
  UserManager userManager;
  TextView txtCurTime;
  TextView txtCurDate;
  
  private void changeChannelByKeyup(boolean paramBoolean)
  {
      int i = ((ChannelType)this.typeList.get(this.curTypeId)).getChannelList().size();
      if (i < 2)
    	  return;
      if (paramBoolean)
      {
    	  this.curItemId = (-1 + this.curItemId);
    	  if (this.curItemId < 0)
    		 this.curItemId = (i - 1);
      } else {
    	  this.curItemId = (1 + this.curItemId);
    	  if (this.curItemId >= i)          
        	this.curItemId = 0;    	
      }
    
      Channel localChannel = getChannel(this.curTypeId, this.curItemId);
      this.curChannel = localChannel;
      updateEpgInfo();
      play(localChannel, 0);
  }
  
  private void changeChannelByKeyOK()
  {
	    int i = ((ChannelType)this.typeList.get(this.curTypeId)).getChannelList().size();
	    if (i < 2)
	      return;   
	    play(this.curChannel, 0);
  }

  private void changeEPGInfo(Channel paramChannel)
  {
    this.txtLoadTip.setText("");
    this.txtName.setText(paramChannel.getSort() + "  " + paramChannel.getName());
    LivePlayActivity.this.imgIcon.setImageBitmap(BitmapFactory.decodeFile(paramChannel.getLogo()));
    //this.imgLoader.displayImage(this.imgIcon, "http://69.64.62.156:8080" + paramChannel.getLogo());
    this.layoutInfo.setVisibility(0);
  }

  private Channel getChannel(int paramInt1, int paramInt2)
  {
    return (Channel)((ChannelType)this.typeList.get(paramInt1)).getChannelList().get(paramInt2);
  }
  
  private void updateEpgInfo()
  {
	  try{
		  String key = "p_"+curChannel.getId();
		  JSONArray arr = EpgDatas.getEpgProgram(key);
		  int count = 3;
		
		  if(arr.length() < 3)
			  count = arr.length();
		  for(int i = 0 ; i < count ; i++){
			  JSONObject obj = arr.getJSONObject(i);
			  String beginTime = obj.getString("beginTime");
			  beginTime = beginTime.substring(11,beginTime.length()-3);
			  String endTime = obj.getString("endTime");
			  endTime = endTime.substring(11,endTime.length()-3);
			  String info = obj.getString("name");
			  switch(i){
			  case 0:
				  this.txtEpg1.setText("•" + beginTime + "-" + endTime + "    " + info);
				  break;
			  case 1:
				  this.txtEpg2.setText("•" + beginTime + "-" + endTime + "    " + info);
				  break;
			  case 2:
				  this.txtEpg3.setText("•" + beginTime + "-" + endTime + "    " + info);
				  break;
			  }
		  }
		  for(int i = count ; i < 3 ; i++){
			  switch(i){
			  case 0:
				  this.txtEpg1.setText("");
				  break;
			  case 1:
				  this.txtEpg2.setText("");
				  break;
			  case 2:
				  this.txtEpg3.setText("");
				  break;
			  }
		  }
	  }catch(Exception exception){
		  
	  }
  }
  
  @SuppressLint("NewApi")
private void initData()  
  {  
	  Display display = getWindowManager().getDefaultDisplay();
	  DisplayMetrics outMetrics = new DisplayMetrics();
	  display.getRealMetrics(outMetrics);
	  //display.getMetrics(outMetrics);
	  
	  this.imgLoader = new SyncImageLoader(this);
      Intent localIntent = getIntent();
      this.curTypeId = localIntent.getIntExtra("type_id", 0);
      this.curItemId = localIntent.getIntExtra("item_id", 0);
      
      //Volume
      this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      this.currentVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
      this.maxVolume = this.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      this.isMuted = (this.audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE);
      this.volumechanged = 0;
      
      this.searchmenuadapter = new SearchMenuAdapter(outMetrics.widthPixels, (int)((float)outMetrics.heightPixels*0.7));
      LivePlayActivity.this.searchmenulayout.setVisibility(8);
      this.searchmenulist.setAdapter(this.searchmenuadapter);
      this.cursearchmenu = new ArrayList<Channel>();
      this.searchmenulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Channel item = LivePlayActivity.this.cursearchmenu.get(arg2);
			LivePlayActivity.this.curTypeId = 0;
			
			List<Channel> list = ((ChannelType)LivePlayActivity.this.typeList.get(0)).getChannelList();
			  for(int i = 0 ; i < list.size() ; i++){
				  Channel ch = list.get(i);
				  if(ch.getSort() == item.getSort()){
					  LivePlayActivity.this.curItemId = i;
					  break;
				  }
			  }
			  
			LivePlayActivity.this.curChannel = item;
		    updateEpgInfo();
		    play(item, 0);
		    LivePlayActivity.this.searchmenulayout.setVisibility(8);
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
					int xcount = LivePlayActivity.this.cursearchmenu.size();
					if(xcount > 10)
						xcount -= 10;
					else 
						return;
					float pos = (float)index / xcount;
					
					float y = pos * (LivePlayActivity.this.searchmenuscrollbar.getHeight()-LivePlayActivity.this.searchmenuscrollwheel.getHeight());
					LivePlayActivity.this.searchmenuscrollwheel.setY(y);
				}catch(Exception exception){
					//exception.printStackTrace();
				}
		  }
      });
      
      this.searchmenulist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			numberpressed++;
			LivePlayActivity.this.mHandler.sendEmptyMessageDelayed(6, 5000L);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
    	  
      });
      this.cursearchkey = "";
      
      int width = this.playVolumeBackground.getWidth();
      if(width <= 0){
    	  RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)this.playVolumeBackground.getLayoutParams();
    	  width = lp1.width;
    	  if(width <= 0)
    		  width = 300;
      }
      
      float percent = (float)this.currentVolume/this.maxVolume;
      int amount = (int)((float)width*percent);
      
      RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams)this.playVolumeProgress.getLayoutParams();
      lp2.width = amount;
      this.playVolumeProgress.setLayoutParams(lp2);
      this.playVolumeAmount.setText(Integer.toString(this.currentVolume));
      
      if(this.isMuted)
    	  this.playVolumeImage.setImageResource(R.drawable.speaker02);
      else
    	  this.playVolumeImage.setImageResource(R.drawable.speaker01);
      
      this.othr = new other(this);
      this.othr.setOnDownloadChangeListener(new other.OnDownloadChangeListener()
      {
    	  public void onAfterDownload()
    	  {
    		  LivePlayActivity.this.mHandler.sendEmptyMessageDelayed(1, 5000L);
    	  }

	      public void onChange(int paramInt1, int paramInt2)
	      {
	        if (paramInt1 * 2 > 100)
	        {
	          paramInt1 = 50;
	          LivePlayActivity.this.txtLoadTip.setVisibility(8);
	        }
	        LivePlayActivity.this.txtLoadTip.setText(paramInt1 * 2 + "%   " + paramInt2 / 1024 + "KB/s");
	        LivePlayActivity.this.txtLoadingRate.setText(paramInt2 / 1024 + "KB/s");
	      }
	
	      public void onInfo(int paramInt)
	      {
	        if (LivePlayActivity.this.isDisplayEPG){
	          if (LivePlayActivity.this.txtLoadingRate.getVisibility() == 0){
	            LivePlayActivity.this.txtLoadingRate.setVisibility(8);
	            return;
	          }
	          return;
	        }
	        if (paramInt == 701){
	          LivePlayActivity.this.txtLoadingRate.setVisibility(0);
	          return;
	        }
	        
	        if (paramInt != 702){
	        	LivePlayActivity.this.txtLoadingRate.setVisibility(8);
	        	return;
	        }
	      }
	
	      public void onStartDownload()
	      {
	        LivePlayActivity.this.txtLoadTip.setText("");
	      }
      });
      this.othr.setOnMeidaListener(new other.OnLiveMediaListener()
      {
	      public void onError()
	      {
	        LivePlayActivity.this.mHandler.sendEmptyMessage(4);
	      }
	
	      public void onPrepared(MediaPlayer paramMediaPlayer)
	      {
	      }
      });
      this.othr.addHolder(this.mHolder);
      
      Time now = new Time();
  	  now.setToNow();
  	  this.txtCurTime.setText(DateFormat.format("kk:mm", now.toMillis(true)).toString());
      SimpleDateFormat sdf = new SimpleDateFormat("EEE");
      Date d = new Date();
  	  SimpleDateFormat month_date = new SimpleDateFormat("MMM");
  	  Calendar cal = Calendar.getInstance();
      String month_name = month_date.format(cal.getTime());
  	  String dayOfTheWeek = sdf.format(d);
  	  String cur_date = dayOfTheWeek +" " + month_name + " " +now.monthDay +", " + now.year;
  	  this.txtCurDate.setText(cur_date);
  	  
  	  Clock c = new Clock(this);
  	  c.AddClockTickListner(new OnClockTickListner() {
  		  @Override
  		  public void OnSecondTick(Time currentTime) {
  			  Log.d("Tick Test per Second",DateFormat.format("kk:mm", currentTime.toMillis(true)).toString());
  		  }
  		  @Override
  		  public void OnMinuteTick(Time currentTime) {
  			  Log.d("Tick Test per Minute",DateFormat.format("kk:mm", currentTime.toMillis(true)).toString());
  			  LivePlayActivity.this.txtCurTime.setText(DateFormat.format("kk:mm", currentTime.toMillis(true)).toString());
  		  }
  	  });
  }

  @SuppressLint("NewApi")
private void initPopupWindow()
  {
	  Display display = getWindowManager().getDefaultDisplay();
	  DisplayMetrics outMetrics = new DisplayMetrics();
	  display.getRealMetrics(outMetrics);
	  //display.getMetrics(outMetrics);
	  int width = outMetrics.widthPixels;
	  int height = outMetrics.heightPixels;
	  //((TextView)findViewById(R.id.screenheighttest)).setText("width:"+width+", height:"+height);
      this.popupWindow = new ChannelChooseWindow(this,(int)((float)width*0.3), height);
      this.popupWindow.setOnClickListener(new ChannelChooseWindow.OnClickListener()
      {
          public void onClickListener(Object paramObject)
          {
              Bundle localBundle = (Bundle)paramObject;
              int typeid = localBundle.getInt("typeId");
              int itemid = localBundle.getInt("itemId");
              if(LivePlayActivity.this.curTypeId == typeid && LivePlayActivity.this.curItemId == itemid){
            	  LivePlayActivity.this.popupWindow.hide();
            	  return;
              }
              LivePlayActivity.this.curTypeId = typeid;
	          LivePlayActivity.this.curItemId = itemid;
	          Channel localChannel = LivePlayActivity.this.getChannel(LivePlayActivity.this.curTypeId, LivePlayActivity.this.curItemId);
	          LivePlayActivity.this.popupWindow.hide();
	          LivePlayActivity.this.curChannel = localChannel;
	          updateEpgInfo();
	          LivePlayActivity.this.play(localChannel, 0);
          }
      });
	    HashMap localHashMap = new HashMap();
	    localHashMap.put("typeList", this.typeList);
	    localHashMap.put("curTypeId", Integer.valueOf(this.curTypeId));
	    this.popupWindow.setData(localHashMap);
  }

  @SuppressLint("NewApi")
  private void initView()
  {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getRealMetrics(outMetrics);
		//display.getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;
	
		LinearLayout m_info = (LinearLayout) findViewById(R.id.live_layout_info);
		RelativeLayout.LayoutParams lp =  (RelativeLayout.LayoutParams) m_info.getLayoutParams();
		lp.width = width;
		lp.height  = height/4;
		m_info.setLayoutParams(lp);
		
		this.liveAdapter = new ChannelAdapter(true,width,height);
	    this.layoutPlay = ((RelativeLayout)findViewById(R.id.layout_play));
	    this.mSurface = ((SurfaceView)findViewById(R.id.live_surface));
	    this.mHolder = this.mSurface.getHolder();
	    this.layoutInfo = ((LinearLayout)findViewById(R.id.live_layout_info));
	    this.layoutVolume = (LinearLayout)findViewById(R.id.live_layout_volume);
	    this.txtName = ((TextView)findViewById(R.id.txt_title));
	    this.imgIcon = ((ImageView)findViewById(R.id.img_icon));
	    this.txtLoadingRate = ((TextView)findViewById(R.id.txt_liveloading_rate));
	    this.txtLoadTip = ((TextView)findViewById(R.id.txt_load_tip));
	    this.txtNoSignal = ((TextView)findViewById(R.id.txt_liveplay_nosignal));
	    this.playVolumeBackground = (ImageView)findViewById(R.id.live_play_volume_back);
	    this.playVolumeProgress = (ImageView)findViewById(R.id.live_play_volume_progress);
	    this.playVolumeAmount = (TextView)findViewById(R.id.live_play_volume_amount); 
	    this.playVolumeImage = (ImageView)findViewById(R.id.live_play_volume_img);
	    this.txtEpg1 = (TextView)findViewById(R.id.txt_live_program1);
	    this.txtEpg2 = (TextView)findViewById(R.id.txt_live_program2);
	    this.txtEpg3 = (TextView)findViewById(R.id.txt_live_program3);
	    this.txtCurTime = (TextView)findViewById(R.id.txt_live_play_time);
	    this.txtCurDate = (TextView)findViewById(R.id.txt_live_play_day);
	    this.searchmenulist = (ListView)findViewById(R.id.search_menu_list);
	    this.searchmenuscrollbar = (ImageView)findViewById(R.id.searchmenu_scrollbar);
	    this.searchmenuscrollwheel = (ImageView)findViewById(R.id.searchmenu_scrollwheel);
	    this.searchmenulayout = (LinearLayout)findViewById(R.id.search_menu_layout);
  }

  private void play(Channel paramChannel, int paramInt)
  {
	  this.isDisplayEPG = true;
	  if (this.txtLoadTip.getVisibility() == 8)
		  this.txtLoadTip.setVisibility(0);
	  changeEPGInfo(paramChannel);
	  if (this.mHandler.hasMessages(1))
		  this.mHandler.removeMessages(1);
	  if ((paramChannel.getPlayUrl() == null) || (paramChannel.getPlayUrl().trim().equals(""))){
		  if (this.txtNoSignal.getVisibility() == 8)
			  this.txtNoSignal.setVisibility(0);
	  } else {
    	if (this.txtNoSignal.getVisibility() == 0)            
          this.txtNoSignal.setVisibility(8);
      }
      this.mHandler.sendEmptyMessageDelayed(3, paramInt);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_live_play);
    
    this.mApp = ((Motutv1Application)getApplicationContext());
    this.typeList = this.mApp.getTypeList();
    PPCore.login4(this, Integer.parseInt(this.mApp.getSN()));
    bool = false;
    initView();
    initData();
    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    Channel localChannel = getChannel(this.curTypeId, this.curItemId);
    this.curChannel = localChannel;
    updateEpgInfo();
    play(localChannel, 1000);
  }

  protected void onDestroy()
  {
    PPCore.setLog(true);
    //Log.i("yy", "onDestroy");
    this.othr.close();
    this.othr.close2();
    this.othr.stop();
    this.othr.removeHandler();
    PPCore.logout();
    super.onDestroy();
  }
  
  public boolean onTouchEvent(MotionEvent event)
  {
	  //Log.i("Touch", "Touched");
	  if (this.popupWindow == null)
	        initPopupWindow();
	  this.layoutInfo.setVisibility(8);
	  HashMap localHashMap = new HashMap();
	  localHashMap.put("curTypeId", Integer.valueOf(this.curTypeId));
	  localHashMap.put("curItemId", Integer.valueOf(this.curItemId));
	  this.popupWindow.setData(localHashMap);
	  this.popupWindow.showAtLocation(this.layoutPlay, 51, 0, 0);
	  return true;
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
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
	    //Log.i("KeyDown",String.valueOf(paramInt));
	  	if(paramInt == paramKeyEvent.KEYCODE_DPAD_UP){
	  		if(this.numberpressed == 0)
	  			changeChannelByKeyup(true);
		}else if(paramInt == paramKeyEvent.KEYCODE_DPAD_DOWN){
			if(this.numberpressed == 0)
				changeChannelByKeyup(false);
		}else if(paramInt == 20){
			volumeChangeByKeyUp(true);
		}else if(paramInt == 21){
			volumeChangeByKeyUp(false);
		}else if(paramInt == paramKeyEvent.KEYCODE_DPAD_RIGHT){
			volumeChangeByKeyUp(true);
		}else if(paramInt == paramKeyEvent.KEYCODE_DPAD_LEFT){
			volumeChangeByKeyUp(false);
		}else if(paramInt == paramKeyEvent.KEYCODE_A){//Remote Key
			volumeMute();
		}else if(paramInt >= paramKeyEvent.KEYCODE_0 && paramInt <= paramKeyEvent.KEYCODE_9){
			NumberPressed(paramInt);
		}else if(paramInt == paramKeyEvent.KEYCODE_DEL){//BackKey
			if(LivePlayActivity.this.searchmenulayout.getVisibility() == 0){
				LivePlayActivity.this.searchmenulayout.setVisibility(8);
				LivePlayActivity.this.cursearchkey = "";
				return true;
			}else{
				super.onBackPressed();
	    		return true;
			}
		}else{
			switch(paramInt){
			case 10:
				volumeMute();
				break;
			case 23:case 66:
				if (this.popupWindow == null)
			       initPopupWindow();
			    this.layoutInfo.setVisibility(8);
			    HashMap localHashMap = new HashMap();
			    localHashMap.put("curTypeId", Integer.valueOf(this.curTypeId));
			    localHashMap.put("curItemId", Integer.valueOf(this.curItemId));
			    this.popupWindow.setData(localHashMap);
			    this.popupWindow.showAtLocation(this.layoutPlay, 51, 0, 0);
			    return true;
			default:
				break;
			}
		}
	  	return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void NumberPressed(int ascii)
  {
	  numberpressed++;
	  LivePlayActivity.this.searchmenulayout.setVisibility(0);
	  if(this.cursearchkey.length() == 3){
		  LivePlayActivity.this.mHandler.sendEmptyMessageDelayed(6, 5000L);
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
	  this.searchmenulist.requestFocus();
	  View v = this.searchmenulist.getChildAt(0);
	  if(v != null)
		  v.setSelected(true);
	  LivePlayActivity.this.mHandler.sendEmptyMessageDelayed(6, 5000L);
  }
  
  private void volumeChangeByKeyUp(boolean paramBoolean)
  {
	  if(paramBoolean){
		  this.currentVolume++;
	  }else{
		  this.currentVolume--;
	  }
	  if(this.currentVolume > this.maxVolume)
		  this.currentVolume = this.maxVolume;
	  if(this.currentVolume < 0)
		  this.currentVolume = 0;
	  if(this.currentVolume == 0)
		  this.playVolumeImage.setImageResource(R.drawable.speaker02);
	  else
		  this.playVolumeImage.setImageResource(R.drawable.speaker01);
	  
      int width = this.playVolumeBackground.getWidth();
      
      if(width <= 0){
    	  RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)this.playVolumeBackground.getLayoutParams();
    	  width = lp1.width;
    	  if(width <= 0)
    		  width = 300;
      }
      
      float percent = (float)this.currentVolume/this.maxVolume;
      int amount = (int)((float)width*percent);
      
      RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams)this.playVolumeProgress.getLayoutParams();
      lp2.width = amount;
      this.playVolumeProgress.setLayoutParams(lp2);
      this.playVolumeAmount.setText(Integer.toString(this.currentVolume));
      this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, this.currentVolume, 0);
      if (this.layoutVolume.getVisibility() == 8)
		  this.layoutVolume.setVisibility(0);
      this.volumechanged++;
      LivePlayActivity.this.mHandler.sendEmptyMessageDelayed(5, 5000L);
  }
  
  private void volumeMute()
  {
	  this.isMuted = !this.isMuted;
	  if(isMuted){
		  this.playVolumeImage.setImageResource(R.drawable.speaker02);
		  this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
	  }else{
		  this.playVolumeImage.setImageResource(R.drawable.speaker01);
		  this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, this.currentVolume, 0);
	  }
      
      if (this.layoutVolume.getVisibility() == 8)
		  this.layoutVolume.setVisibility(0);
      this.volumechanged++;
      LivePlayActivity.this.mHandler.sendEmptyMessageDelayed(5, 5000L);
  }
  
  public void startPlay(Channel paramChannel)
  {
    if (paramChannel == null)
      return;
    try
    {
      LivePlayActivity.this.imgIcon.setImageBitmap(BitmapFactory.decodeFile(paramChannel.getLogo()));
      this.othr.open(paramChannel.isP2p(), paramChannel.getMode(), AESUtil.decrypt(paramChannel.getPlayUrl()));
      return;
    }
    catch (Exception localException)
    {
     // localException.printStackTrace();
    }
  }
}
