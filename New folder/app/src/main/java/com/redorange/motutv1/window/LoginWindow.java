package com.redorange.motutv1.window;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import java.util.HashMap;
import com.extremeiptv.buzziptv.R;
import com.redorange.motutv1.app.Motutv1Application;

public class LoginWindow extends BaseWindowSin
{
  Button btnReconnect;
  setOnClickReConnectBtnListener handler;
  TextView txtContent = (TextView)this.parent.findViewById(R.id.txt_content);
  String SN;
  
  public LoginWindow(Context paramContext,int a, int b)
  {
    super(paramContext, R.layout.window_login, a, b);    
    
    this.btnReconnect = ((Button)this.parent.findViewById(R.id.btn_reconnect));
    this.btnReconnect.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        LoginWindow.this.handler.onClick();
      }
    });
  }

  public LoginWindow(Context paramContext,int a, int b, String serialno)
  {
    super(paramContext, R.layout.window_login, a, b);    
    this.btnReconnect = ((Button)this.parent.findViewById(R.id.btn_reconnect));
    SN = serialno;
    this.txtContent.setText(serialno);
    this.btnReconnect.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        LoginWindow.this.handler.onClick();
      }
    });
  }
  
  public void setData(Object paramObject)
  {
    HashMap localHashMap = (HashMap)paramObject;
    String str;
    if (((Integer)localHashMap.get("category")).intValue() == 1){
      if (!((Boolean)localHashMap.get("isActive")).booleanValue())
      {
        str = String.valueOf(localHashMap.get("inactiveHint"));
        if (str.trim().equals(""))
          str = "Your account is not activated yet，\tplease contact administrator for help.";
      }
      else {
    	  str = String.valueOf(localHashMap.get("expiredHint"));
    	  if (str.trim().equals("")){
    		  str = "Your account expires ，please recharge.";    		  
    	  }
      }
    }
    else
    	str = "Connecting server fails,please try later.";
    
    
      this.txtContent.setText(str);
      return;
    
    
  }

  public void setOnClickReConnectHandler(setOnClickReConnectBtnListener paramsetOnClickReConnectBtnListener)
  {
    this.handler = paramsetOnClickReConnectBtnListener;
  }

  public void showAtLocation(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
	  super.showAtLocation(paramView, paramInt1, paramInt2, paramInt3);
  }

  public void showAtLocation(View paramView, int paramInt1, int paramInt2, int paramInt3, int popwidth, int popheight)
  {
	  super.showAtLocation(paramView, paramInt1, paramInt2, paramInt3, popwidth, popheight);
  }
  
  public static abstract interface setOnClickReConnectBtnListener
  {
    public abstract void onClick();
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.window.LoginWindow
 * JD-Core Version:    0.6.0
 */