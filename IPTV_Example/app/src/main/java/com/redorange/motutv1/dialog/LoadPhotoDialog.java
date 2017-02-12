package com.redorange.motutv1.dialog;

import com.extremeiptv.buzziptv.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class LoadPhotoDialog extends Dialog
{
  public LoadPhotoDialog(Context paramContext)
  {
    super(paramContext);
  }

  public LoadPhotoDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.loading_dialog);
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.dialog.LoadPhotoDialog
 * JD-Core Version:    0.6.0
 */