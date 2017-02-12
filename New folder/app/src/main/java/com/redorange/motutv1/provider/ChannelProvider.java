package com.redorange.motutv1.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.redorange.motutv1.model.ChannelType;
import com.redorange.motutv1.parser.ChannelParser;
import com.redorange.motutv1.utils.HttpUtil;

import org.json.JSONException;

import java.util.List;

public class ChannelProvider
{
  private static final String CHANNEL_JSON = "ChannelJson";
  public static final long HOUR = 3600000L;
  private static final String SHARED_NAME = "com.redorange.channel";
  Context mContext;
  long updateProTime;
  long updateTime = 0L;

  public ChannelProvider(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private String getChannelJson()
  {
    return this.mContext.getSharedPreferences("com.redorange.channel", 0).getString("ChannelJson", null);
  }

  private void loadingChannel(String paramString,loadChannelTypeHandler paramloadChannelTypeHandler)
  {
	  final String p1 = paramString;
	  final loadChannelTypeHandler p2 = paramloadChannelTypeHandler;
	  /*InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://62.210.182.14:8080/channel_resource/channel/liveListNew.html");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception e1){
			//e1.printStackTrace();
			p2.onFailure(null);
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			p2.onFailure(null);
			return;
		}
		
			List localList = null;
		  try
		  {
			  localList = ChannelParser.parser(json, p1);
			  ChannelProvider.this.setChannelJson(paramString);
		  }
		  catch (JSONException localJSONException)
		  {
          //localJSONException.printStackTrace();
		  }
		  catch(Exception exception){
      	//exception.printStackTrace();
		  }
		  p2.onSuccess(localList);
		  p2.onFinish();*/
    //  HttpUtil.get("http://62.210.182.14:8080/channel_resource/channel/liveListNew.html", new AsyncHttpResponseHandler()
	  HttpUtil.get("http://extremeiptv.com:8080/channel_resource/channel/liveListNew.html", new AsyncHttpResponseHandler()
	  {
		  public void onFailure(Throwable paramThrowable)
		  {

              p2.onFailure(paramThrowable);
		  }

		  public void onFinish()
		  {

              p2.onFinish();
		  }

		  public void onSuccess(String paramString)
		  {
			  List localList = null;
			  try
			  {
				  localList = ChannelParser.parser(paramString, p1);
				  ChannelProvider.this.setChannelJson(paramString);
			  }
			  catch (JSONException localJSONException)
			  {
	            //localJSONException.printStackTrace();
			  }
			  catch(Exception exception){
	        	//exception.printStackTrace();
			  }
	          p2.onSuccess(localList);
	      }
	  });
  }

  private void setChannelJson(String paramString)
  {
    if ((paramString != null) && (paramString.indexOf("typeList") > 0))
    {
      SharedPreferences.Editor localEditor = this.mContext.getSharedPreferences("com.redorange.channel", 0).edit();
      localEditor.putString("ChannelJson", paramString);
      localEditor.commit();
      this.updateTime = System.currentTimeMillis();
    }
  }

  public void loadChannel(String paramString, loadChannelTypeHandler paramloadChannelTypeHandler)
  {
    String str = getChannelJson();
    long l = System.currentTimeMillis();
    //paramString = "350";
    if ((str != null) && (l - this.updateTime < 3600000L)){
      try
      {
        paramloadChannelTypeHandler.onSuccess(ChannelParser.parser(str, paramString));
        paramloadChannelTypeHandler.onFinish();
        return;
      }
      catch (JSONException localJSONException)
      {
       // localJSONException.printStackTrace();
      }
    }
    else 
    	loadingChannel(paramString,paramloadChannelTypeHandler);
  }

  public static abstract interface loadChannelTypeHandler
  {
    public abstract void onFailure(Throwable paramThrowable);

    public abstract void onFinish();

    public abstract void onSuccess(List<ChannelType> paramList);
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.provider.ChannelProvider
 * JD-Core Version:    0.6.0
 */