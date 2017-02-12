package com.redorange.motutv1.parser;

import com.redorange.motutv1.model.App;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateApkParser
{
  private static App jsonToApp(JSONObject paramJSONObject)
    throws JSONException
  {
    int i = paramJSONObject.getInt("id");
    String str1 = paramJSONObject.getString("describe");
    int j = paramJSONObject.getInt("fileSize");
    String str2 = paramJSONObject.getString("fileUrl");
    String str3 = paramJSONObject.getString("iconPath");
    String str4 = paramJSONObject.getString("name");
    String str5 = paramJSONObject.getString("packageName");
    int k = paramJSONObject.getInt("verCode");
    String str6 = paramJSONObject.getString("verName");
    int m = paramJSONObject.getInt("star");
    String str7 = "";
    if (paramJSONObject.has("lastUpdate"))
      str7 = paramJSONObject.getString("lastUpdate");
    String str8 = paramJSONObject.getString("screenshot");
    App localApp = new App();
    localApp.setIcon(str3);
    localApp.setId(i);
    localApp.setDescribe(str1);
    localApp.setFile(str2);
    localApp.setName(str4);
    localApp.setSize(j);
    localApp.setPackageName(str5);
    localApp.setVerCode(k);
    localApp.setVerName(str6);
    localApp.setStar(m);
    localApp.setUpdateTime(str7);
    localApp.setScreenshot(str8);
    return localApp;
  }

  public static List<App> parser(String paramString)
    throws JSONException
  {
    JSONArray localJSONArray = new JSONObject(paramString).getJSONArray("apkLists");
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i<localJSONArray.length(); i++)
    {
      localArrayList.add(jsonToApp(localJSONArray.getJSONObject(i)));
    }
    return localArrayList;
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.parser.UpdateApkParser
 * JD-Core Version:    0.6.0
 */