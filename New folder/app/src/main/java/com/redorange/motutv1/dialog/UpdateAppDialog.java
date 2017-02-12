package com.redorange.motutv1.dialog;

import com.extremeiptv.buzziptv.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class UpdateAppDialog
{
  private Dialog mDialog;

  @SuppressLint({"NewApi"})
  public UpdateAppDialog(Activity paramActivity, int paramInt, checkAppUpdateHandler paramcheckAppUpdateHandler)
  {
	final checkAppUpdateHandler p1 =  paramcheckAppUpdateHandler;
    this.mDialog = new AlertDialog.Builder(paramActivity, paramInt).setTitle(paramActivity.getString(R.string.update_info)).setMessage(paramActivity.getString(R.string.new_version)).setPositiveButton(paramActivity.getString(R.string.ok), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        p1.setOnPositiveClick();
      }
    }).create();
    this.mDialog.setCancelable(false);
    this.mDialog.show();
  }

  public boolean isDialogShowing()
  {
    return this.mDialog.isShowing();
  }

  public static abstract interface checkAppUpdateHandler
  {
    public abstract void setOnPositiveClick();
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.dialog.UpdateAppDialog
 * JD-Core Version:    0.6.0
 */