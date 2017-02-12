package com.redorange.motutv1.parser;

import android.util.Log;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginInforParser
{
  public static HashMap<String, Object> analyLoginInfo(String paramString)
  {
    HashMap localHashMap = new HashMap();
    try
    {
      //Log.i("live", paramString);
      JSONObject localJSONObject1 = new JSONObject(paramString);
      boolean bool = localJSONObject1.getBoolean("isActive");
      String str1 = localJSONObject1.getString("str");
      if ((str1 != null) && (!str1.trim().equals("")))
        str1 = decryption(str1);
      JSONObject localJSONObject2 = localJSONObject1.getJSONObject("stbInfo");
      String str2 = localJSONObject2.getString("expiredHint");
      String str3 = localJSONObject2.getString("inactiveHint");
      localHashMap.put("isActive", Boolean.valueOf(bool));
      localHashMap.put("str", str1);
      if (str2 == null)
        str2 = "";
      localHashMap.put("expiredHint", str2);
      if (str3 == null)
        str3 = "";
      localHashMap.put("inactiveHint", str3);
      return localHashMap;
    }
    catch (JSONException localJSONException)
    {
      //localJSONException.printStackTrace();
    }
    return localHashMap;
  }

  private static String decryption(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < arrayOfChar.length; i++)
    {
      localStringBuffer.append((char)(-20 + arrayOfChar[i]));
    }
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.parser.LoginInforParser
 * JD-Core Version:    0.6.0
 */