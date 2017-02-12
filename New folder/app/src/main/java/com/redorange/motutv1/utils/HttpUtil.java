package com.redorange.motutv1.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil
{
  private static final int TIME_OUT = 30000;
  private static AsyncHttpClient client = new AsyncHttpClient();

  static
  {
    client.setTimeout(30000);
  }

  public static void get(String paramString, AsyncHttpResponseHandler paramAsyncHttpResponseHandler)
  {
    client.get(paramString, paramAsyncHttpResponseHandler);
  }

  public static void get(String paramString, BinaryHttpResponseHandler paramBinaryHttpResponseHandler)
  {
    client.get(paramString, paramBinaryHttpResponseHandler);
  }

  public static void get(String paramString, JsonHttpResponseHandler paramJsonHttpResponseHandler)
  {
    client.get(paramString, paramJsonHttpResponseHandler);
  }

  public static void get(String paramString, RequestParams paramRequestParams, AsyncHttpResponseHandler paramAsyncHttpResponseHandler)
  {
    client.get(paramString, paramRequestParams, paramAsyncHttpResponseHandler);
  }

  public static void get(String paramString, RequestParams paramRequestParams, JsonHttpResponseHandler paramJsonHttpResponseHandler)
  {
    client.get(paramString, paramRequestParams, paramJsonHttpResponseHandler);
  }

  public static AsyncHttpClient getClient()
  {
    return client;
  }

  public static void post(String paramString, AsyncHttpResponseHandler paramAsyncHttpResponseHandler)
  {
    client.post(paramString, paramAsyncHttpResponseHandler);
  }

  public static void post(String paramString, BinaryHttpResponseHandler paramBinaryHttpResponseHandler)
  {
    client.post(paramString, paramBinaryHttpResponseHandler);
  }

  public static void post(String paramString, JsonHttpResponseHandler paramJsonHttpResponseHandler)
  {
    client.post(paramString, paramJsonHttpResponseHandler);
  }

  public static void post(String paramString, RequestParams paramRequestParams, AsyncHttpResponseHandler paramAsyncHttpResponseHandler)
  {
    client.post(paramString, paramRequestParams, paramAsyncHttpResponseHandler);
  }

  public static void post(String paramString, RequestParams paramRequestParams, JsonHttpResponseHandler paramJsonHttpResponseHandler)
  {
    client.post(paramString, paramRequestParams, paramJsonHttpResponseHandler);
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.utils.HttpUtil
 * JD-Core Version:    0.6.0
 */