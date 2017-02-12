package com.redorange.motutv1.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.util.Log;
import com.ipmacro.utils.aes.AESUtil;
import com.redorange.motutv1.model.Channel;
import com.redorange.motutv1.model.ChannelType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChannelParser
{
  private static boolean isInIDS(int paramInt, String paramString)
  {
    String[] arrayOfString;
    if ((paramString != null) && (!paramString.equals("")))
    {
      arrayOfString = paramString.split(",");
      //new int[arrayOfString.length];
      for (int i = 0;i<arrayOfString.length ; i++)
      {        
        if (Integer.parseInt(arrayOfString[i]) == paramInt)
          return true;
      }
      return false;
    }
    return false;
   // return true;
   
  }
  public static String  downloadImage(String str_url)
  {
		  Bitmap bmp = null;
		  FileOutputStream fos = null;
		  String img_path = str_url;
		  String[] paths = img_path.split("/");
		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		  File dir = new File(Environment.getExternalStorageDirectory()+"/logoImages");
		  if (!dir.exists()) {
			  dir.mkdirs();
		  }			  		  
		  File file = new File(Environment.getExternalStorageDirectory()
	    			+"/logoImages" + File.separator +paths[paths.length -1]);
		  if (file.exists()) {
	    		return Environment.getExternalStorageDirectory()
		    			+"/logoImages" + File.separator +paths[paths.length -1];
	    	}
		  /*--- you can select your preferred CompressFormat and quality. 
		   *I'm going to use JPEG and 100% quality ---*/
		  try 
		  {
				URL url = new URL(str_url);   
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        bmp = BitmapFactory.decodeStream(input);
		    } catch (IOException e) {
		        //e.printStackTrace();
		        //Log.e("getBmpFromUrl error: ", e.getMessage().toString());
		        return null;
		   } catch(Exception exception){
			   return null;
		   }
		  bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		  /*--- create a new file on SD card ---*/	    
		  try {
	        file.createNewFile();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
		  fos = null;
		  try {
	        fos = new FileOutputStream(file);	        
		  } catch (FileNotFoundException e) {
			  e.printStackTrace();
		  }
		  try {
			if (fos != null){
				fos.write(bytes.toByteArray());	        
				fos.close();
		        return Environment.getExternalStorageDirectory()+"/logoImages" + File.separator +paths[paths.length -1];
			}
			else 
				return str_url;
		  } catch (IOException e) {			  
			  return null;
		  }
  }
  
  public static Bitmap drawable2Bitmap(Drawable drawable)
  {
      if(drawable instanceof BitmapDrawable)
      {
          return ((BitmapDrawable)drawable).getBitmap();
      }
      if(drawable instanceof NinePatchDrawable)
      {
          int i = drawable.getIntrinsicWidth();
          int j = drawable.getIntrinsicHeight();
          android.graphics.Bitmap.Config config;
          Bitmap bitmap;
          Canvas canvas;
          if(drawable.getOpacity() != -1)
          {
              config = android.graphics.Bitmap.Config.ARGB_8888;
          } else
          {
              config = android.graphics.Bitmap.Config.RGB_565;
          }
          bitmap = Bitmap.createBitmap(i, j, config);
          canvas = new Canvas(bitmap);
          drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
          drawable.draw(canvas);
          return bitmap;
      } else
      {
          return null;
      }
  }
  
  public static List<ChannelType> parser(String paramString1, String paramString2)
    throws JSONException
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    JSONObject localJSONObject1 = new JSONObject(paramString1);
    JSONArray localJSONArray1 = localJSONObject1.getJSONArray("typeList");
    int i = 0;
    JSONArray localJSONArray2;
    int j;
    while (i < localJSONArray1.length())
    {
    	JSONObject localJSONObject2 = localJSONArray1.getJSONObject(i);
        ChannelType localChannelType1 = new ChannelType(localJSONObject2.getInt("id"), localJSONObject2.getString("name"));
        localArrayList1.add(localChannelType1);
        i++;      
    }
    localJSONArray2 = localJSONObject1.getJSONArray("releaseList");
    j = 0;
    int k;
      while (j < localJSONArray2.length())
      {
    	  JSONObject localJSONObject3 = localJSONArray2.getJSONObject(j);
    	  int i2 = localJSONObject3.getInt("isFree");
    	  int i1 = localJSONObject3.getInt("id");

    	  Boolean localBoolean = Boolean.valueOf(false);
          if ((i2 <= 0) && !isInIDS(i1, paramString2)){
        	  j++;
        	  continue;
          }
    	  String str6 = localJSONObject3.getString("playUrl");
    	  if(str6 == null || str6.trim().equals("")){
    		  j++;
    		  continue;
    	  }
          
          String str2 = localJSONObject3.getString("adImage");
          String str3 = localJSONObject3.getString("allowRegion");
          
          boolean bool1 = localJSONObject3.has("isAutoRelay");
          boolean bool2 = false;
          if (bool1)
            bool2 = localJSONObject3.getBoolean("isAutoRelay");
          boolean bool3 = localJSONObject3.getBoolean("isLock");
          boolean bool4 = localJSONObject3.getBoolean("isP2p");
          boolean bool5 = localJSONObject3.getBoolean("isShowGlobalLogo");
          boolean bool6 = localJSONObject3.getBoolean("isShowLogoOnBox");
          String str4 = localJSONObject3.getString("logo");
          if(!str4.toLowerCase().contains("http:"))
        	  str4 = "http://extremeiptv.com:8080" + str4;
              //str4 = "http://62.210.182.14:8080" + str4;
          String logo_img  = downloadImage(str4);
          
          int i3 = localJSONObject3.getInt("mode");
          
          int i4 = localJSONObject3.getInt("sort");
          int i5 = localJSONObject3.getInt("typeId");
          String str7 = localJSONObject3.getString("typeIds");
          //Log.i("yy", "sort+name=" + i4 + "  " + str5);
          Channel localChannel2 = new Channel();
          localChannel2.setAdImage(str2);
          localChannel2.setAllowRegion(str3);
          localChannel2.setId(i1);
          localChannel2.setAutoRelay(bool2);
          localChannel2.setIsFree(i2);
          localChannel2.setLock(bool3);
          localChannel2.setP2p(bool4);
          localChannel2.setShowGlobalLogo(bool5);
          localChannel2.setShowLogoOnBox(bool6);
          if (logo_img == null)  
        	  localChannel2.setLogo(str4);
          else 
        	  localChannel2.setLogo(logo_img);
          localChannel2.setMode(i3);
          String str5 = localJSONObject3.getString("name");
    	  try{
    		  localChannel2.setName(AESUtil.decrypt(str5));
    	  }catch(Exception exception){
    		  
    	  }
            
            localChannel2.setPlayUrl(str6);
            localChannel2.setSort(i4);
            localChannel2.setTypeId(i5);
            localChannel2.setTypeIds(str7);
            localArrayList2.add(localChannel2);
            j++;
      }
      ChannelType localChannelType2 = new ChannelType(0, "All");
      localChannelType2.setChannelList(localArrayList2);
      localArrayList1.add(0, localChannelType2);
      ChannelType localChannelType3 = new ChannelType(-1, "Favourite");
      localChannelType3.setChannelList(null);
      localArrayList1.add(1, localChannelType3);
      ChannelType localChannelType5 = new ChannelType(-2, "Lock Channels");
      localChannelType5.setChannelList(null);
      localArrayList1.add(2, localChannelType5);
      
      k = 0;
      while (k < localArrayList1.size())
      {
    	  ChannelType localChannelType4 = (ChannelType)localArrayList1.get(k);
    	  for (int m = 0;m<localArrayList2.size() ; m++)
    	  {
    		  Channel localChannel1 = (Channel)localArrayList2.get(m);
    	      String str1 = localChannel1.getTypeIds();
    	      if ((str1 != null) && (str1.contains("[" + localChannelType4.getId() + "]"))){
    	    	  localChannelType4.addChild(localChannel1);    	    	  
    	      }
    	  }
    	  k++;    	  
      }
      for(i = localArrayList1.size()-1 ; i >= 3 ; i--){
    	  ChannelType localChannelType4 = (ChannelType)localArrayList1.get(i);
    	  if(localChannelType4.getChannelList() == null){
    		  localArrayList1.remove(i);
    	  }else if(localChannelType4.getChannelList().size() == 0)
    		  localArrayList1.remove(i);
      }
      return localArrayList1;
    }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.parser.ChannelParser
 * JD-Core Version:    0.6.0
 */