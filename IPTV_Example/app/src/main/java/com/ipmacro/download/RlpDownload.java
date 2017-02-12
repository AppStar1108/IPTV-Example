package com.ipmacro.download;

import com.ipmacro.ppcore.Rlp;
import com.ipmacro.ppcore.RlpApple;
import com.ipmacro.ppcore.Url;
import com.ipmacro.ppcore.Url.Query;

public class RlpDownload extends AppleDownload
{
  String mFileID;

  public int getProgress2()
  {
    if (this.mFileID == null)
      return 0;
    return Rlp.getProgress(this.mFileID);
  }

  public int getRate2()
  {
    if (this.mFileID == null)
      return 0;
    return Rlp.getRate(this.mFileID);
  }

  public boolean prepare()
  {
    if (this.mApple == null)
    {
      if (this.mFileID == null) return false;      
      if (Rlp.getProgress(this.mFileID) == 0) return false;
      this.mApple = new RlpApple(this.mFileID);
    }
    return super.prepare();
  }

  public void release()
  {
    super.release();
    if (this.mFileID != null)
    {
      Rlp.stop(this.mFileID);
      this.mFileID = null;
    }
  }

  public void start(String paramString)
  {
    Url localUrl = new Url();
    localUrl.parseUrl(paramString);
    this.mFileID = localUrl.Host;
    Rlp.start(this.mFileID);
    Url.Query[] arrayOfQuery = localUrl.QueryList;
    int i = arrayOfQuery.length;
    int j = 0;
    while (j < i)
    {
    	Url.Query localQuery = arrayOfQuery[j];
        if (localQuery.Name.compareTo("preroll") != 0)
        {
          if (localQuery.Name.compareTo("prefetch") != 0)
          {
        	  if ((localQuery.Name.compareTo("tracker") == 0) || (localQuery.Name.compareTo("sourcer") == 0) || (localQuery.Name.compareTo("relayer") == 0))
              {
                String[] arrayOfString1 = localQuery.Value.split(":");
                if (arrayOfString1.length == 3)                
                	Rlp.addPeer(this.mFileID, Integer.parseInt(arrayOfString1[0]), localQuery.Name, arrayOfString1[1], Short.parseShort(arrayOfString1[2]), false);                
              }
        	  else {
        		  if ((localQuery.Name.compareTo("peers") == 0))
        		  {
        			  String[] arrayOfString2 = localQuery.Value.split(",");
        		      int k = arrayOfString2.length;
        		      for (int m = 0; m < k; m++)
        		      {
        		        String[] arrayOfString3 = arrayOfString2[m].split(":");
        		        if (arrayOfString3.length == 4)        		          
        		        	Rlp.addPeer(this.mFileID, Integer.parseInt(arrayOfString3[0]), arrayOfString3[3], arrayOfString3[1], Short.parseShort(arrayOfString3[2]), false);
        		      }        			          			  
        		  }         		  
        	  }
         }
         else {
        	 String[] arrayOfString4 = localQuery.Value.split(":");
             if (arrayOfString4.length == 3)
               Rlp.preFetch(this.mFileID, arrayOfString4[0], Short.parseShort(arrayOfString4[1]), arrayOfString4[2]);        	  
         }
          
        }    	
        j++;
    }
    return;
    
  }

  public void stop()
  {
    super.stop();
    if (this.mFileID != null)
    {
      Rlp.stop(this.mFileID);
      this.mFileID = null;
    }
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.ipmacro.download.RlpDownload
 * JD-Core Version:    0.6.0
 */