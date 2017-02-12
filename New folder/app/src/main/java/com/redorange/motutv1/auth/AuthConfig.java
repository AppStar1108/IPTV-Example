package com.redorange.motutv1.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import android.util.Log;

public class AuthConfig
{
  public static final String KEY_CUSTOMERNAME = "key_customername";
  public static final String KEY_INFO = "key_info";
  public static final String SHARED_NAME = "com.redorange.motutv1.auths";
  public static final String TAG = "auth_config";
  String customeName;
  Context mContext;
  String netMac;
  String sn;

  public AuthConfig(Context paramContext)
  {
    this.mContext = paramContext;
    String android_id = Secure.getString(mContext.getContentResolver(),
            Secure.ANDROID_ID); 
    int hash = android_id.hashCode();
    
    //doActivate(MacTools.getDefaultNetMacAddress(paramContext));    
    doActivate(String.valueOf(hash));
  }

  private void doActivate(String paramString)
  {
    //String str = productSN(paramString);
	  String str = paramString;
    //Log.i("yy", "sn=" + str);
    if (str.contains("-"))
      str = str.substring(1, str.length());
    //Log.i("yy", "new sn=" + str);
    this.sn = str;
    this.netMac = paramString;
  }

  public static String productSN(String paramString)
  {
    //Log.i("yy", "MS=" + paramString + "=");
    String[] arrayOfString;
    int[] arrayOfInt;
    if (paramString.trim().length() == 17)
    {
      arrayOfString = paramString.split(":");      
    }
    else {
    	int i = paramString.trim().length();
    	arrayOfString = null;
        if (i == 12){
        	arrayOfString = new String[6];
             for (int j = 0; j < 6; j++)
               arrayOfString[j] = paramString.substring(j * 2, 2 + j * 2);        	
        }
    }
    arrayOfInt = new int[6];
    if (arrayOfString!=null){
    	for (int k = 0;k<arrayOfString.length ; k++)
    	{ 
    		arrayOfInt[k] = Integer.parseInt(arrayOfString[k].trim(), 16);
    	}
    	  return String.valueOf(352321536 + (arrayOfInt[2] << 24 | arrayOfInt[3] << 16 | arrayOfInt[4] << 8 | arrayOfInt[5]));
    }
    return String.valueOf(0x15000000);
    //return paramString;
  }

  public String getNetMac()
  {
    return this.netMac;
  }

  public String getSn()
  {
    //Log.i("tcp", "sn=" + this.sn);
    return this.sn;
  }

  public void saveCustomerName(String paramString)
  {
    SharedPreferences.Editor localEditor = this.mContext.getSharedPreferences("com.redorange.motutv1.auths", 0).edit();
    localEditor.putString("key_customername", paramString);
    localEditor.commit();
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.auth.AuthConfig
 * JD-Core Version:    0.6.0
 */