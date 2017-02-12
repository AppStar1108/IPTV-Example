package com.redorange.motutv1.window;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public abstract class BaseWindowSin
{
  protected static final int DELAY_MILLIS = 5000;
  protected static final int DELAY_PLAY = 10000;
  protected static final int MESSAGE_HIDE = 1;
  int height;
  protected Context mContext;
  protected Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
        BaseWindowSin.this.hide();
    }
  };
  protected View parent;
  protected PopupWindow popupWindow;
  int width;

  public BaseWindowSin(Context paramContext)
  {
    this(paramContext, 0, 0, 0);
  }

  public BaseWindowSin(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, 0, 0);
  }

  public BaseWindowSin(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mContext = paramContext;
    if ((paramInt2 == 0) || (paramInt3 == 0))
    {    
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);
      if (paramInt2 == 0)
        paramInt2 = localDisplayMetrics.widthPixels;
      if (paramInt3 == 0)
        paramInt3 = localDisplayMetrics.heightPixels;
    }
    this.width = paramInt2;
    this.height = paramInt3;
    if (paramInt1 != 0)
    {
      this.parent = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(paramInt1, null);
      this.popupWindow = new PopupWindow(this.parent, paramInt2, paramInt3);
      this.popupWindow.setBackgroundDrawable(new BitmapDrawable());
      this.popupWindow.setFocusable(true);
      this.popupWindow.setOutsideTouchable(true);
    }
  }

  public void dismiss()
  {
    if (this.popupWindow != null)
      this.popupWindow.dismiss();
  }

  public void hide()
  {
    this.popupWindow.dismiss();
  }

  public boolean isShowing()
  {
    return this.popupWindow.isShowing();
  }

  public void onExecute(String paramString)
  {
  }

  public abstract void setData(Object paramObject);

  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    if (this.popupWindow != null)
      this.popupWindow.setOnDismissListener(paramOnDismissListener);
  }

  public void showAtLocation(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    this.popupWindow.showAtLocation(paramView, paramInt1, paramInt2, paramInt3);
  }
  
  public void showAtLocation(View paramView, int paramInt1, int paramInt2, int paramInt3, int width, int height)
  {
    this.popupWindow.showAtLocation(paramView, paramInt1, paramInt2, paramInt3);
    this.popupWindow.update(width, height);
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.window.BaseWindowSin
 * JD-Core Version:    0.6.0
 */