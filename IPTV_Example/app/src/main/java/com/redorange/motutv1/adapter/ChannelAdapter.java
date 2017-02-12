package com.redorange.motutv1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.extremeiptv.buzziptv.R;
import com.redorange.motutv1.LiveTvActivity;
import com.redorange.motutv1.model.Channel;
import com.redorange.motutv1.model.RoundedImageView;

import java.net.URL;
import java.util.List;

public class ChannelAdapter extends BaseAdapter
{
  int curSelId = -1;
  boolean bool;
  List<Channel> list;
  int width;
  int height;
  public LiveTvActivity context;
  
  public ChannelAdapter(boolean a,int w, int h)
  {
	  super();
	  bool = a;
	  width = w;
	  height = h;
  }
  
  public ChannelAdapter(boolean a,int w, int h, LiveTvActivity c)
  {
	  super();
	  bool = a;
	  width = w;
	  height = h;
	  context = c;
  }
  
  public int getCount()
  {
    if (this.list == null)
      return 0;
    return this.list.size();
  }

  public Object getItem(int paramInt)
  {
    return this.list.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return 0L;
  }

  @SuppressLint("NewApi")
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
	  View localView = paramView;
	  final ViewHolder localViewHolder;
	  if (localView == null)
	  {
	      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_channel, null);
	      localViewHolder = new ViewHolder();
	      localViewHolder.txtSort = ((RoundedImageView)localView.findViewById(R.id.txt_channel_sort));
	      localViewHolder.txtSortBorder = ((ImageView)localView.findViewById(R.id.txt_channel_sort_border));
	      localViewHolder.lay_img = ((LinearLayout)localView.findViewById(R.id.txt_channel_img));
	      
	      if (bool){
	    	  LinearLayout.LayoutParams m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    	  m.width = (int)(((float)height*0.72-8*4-5)/4);
	    	  m.height = m.width;
	    	  localViewHolder.lay_img.setLayoutParams(m);
	      }else{
	    	  LinearLayout.LayoutParams m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    	  m.width = (int)(((float)height*0.72-8*4-5)/4);
	    	  m.height = m.width;
	    	  localViewHolder.lay_img.setLayoutParams(m);
	      }
	      localViewHolder.index = paramInt;
	      localViewHolder.status = -1;
	      localView.setTag(localViewHolder);
	  }else {
    	  localViewHolder = (ViewHolder)localView.getTag();    	
    	  if(localViewHolder.index != paramInt){
          	  
    	  }
	  }
	  URL newurl = null;
	  Bitmap mIcon_val = list.get(paramInt).getBitmap();
	  localViewHolder.txtSort.setBackground(null);
	  if(mIcon_val == null)
    	mIcon_val = BitmapFactory.decodeFile(((Channel)ChannelAdapter.this.list.get(paramInt)).getLogo());
	  
    	if (mIcon_val != null){
    		list.get(paramInt).setBitmap(mIcon_val);
    		localViewHolder.txtSort.setImageBitmap(getRoundedCornerBitmap(mIcon_val, Color.TRANSPARENT, 18, 0, context));
    		FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) localViewHolder.txtSort.getLayoutParams();
	  	  	chl_params.setMargins(7 , 7 , 7 , 7);
	  	  	localViewHolder.txtSort.setLayoutParams(chl_params);
    	}else{
    		localViewHolder.txtSort.setImageResource(R.drawable.tv_default);
    		FrameLayout.LayoutParams chl_params = (FrameLayout.LayoutParams) localViewHolder.txtSort.getLayoutParams();
    		chl_params.setMargins(0, 0 , 0, 0);
	  	  	localViewHolder.txtSort.setLayoutParams(chl_params);
    	}
   	
     return localView;      
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
  
  public void update(List<Channel> paramList)
  {
	  this.list = paramList;
	  notifyDataSetChanged();
  }

  static class ViewHolder
  {
	  LinearLayout lay_img;
	  RoundedImageView txtSort;
	  ImageView txtSortBorder;
	  int index;
	  Bitmap bmp;
	  int status;
  }
}