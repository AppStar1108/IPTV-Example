package com.redorange.motutv1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.redorange.motutv1.model.Channel;
import java.util.ArrayList;
import java.util.List;

public class OperationLive
{
  SQLiteDatabase db;
  private DBHelper dbHelper = null;

  public OperationLive(Context paramContext)
  {
    this.dbHelper = new DBHelper(paramContext);
    this.db = this.dbHelper.getWritableDatabase();
  }

  public void addToDeleteLive(Channel paramChannel, AddToDeleteListener paramAddToDeleteListener)
  {
    if (isInsert(paramChannel.getId()))
    {
      paramAddToDeleteListener.getNumber(0, Integer.parseInt(String.valueOf(deleteVod(paramChannel.getId()))));
      return;
    }
    if ((this.db == null) || (!this.db.isOpen()))
      this.db = this.dbHelper.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("idTv", Integer.valueOf(paramChannel.getId()));
    localContentValues.put("logo", paramChannel.getLogo());
    localContentValues.put("mode", Integer.valueOf(paramChannel.getMode()));
    localContentValues.put("name", paramChannel.getName());
    localContentValues.put("playUrl", paramChannel.getPlayUrl());
    localContentValues.put("sort", Integer.valueOf(paramChannel.getSort()));
    localContentValues.put("typeId", paramChannel.getTypeIds());
    long l = this.db.insert("Tv", null, localContentValues);
    if ((this.db != null) || (this.db.isOpen()))
      this.db.close();
    paramAddToDeleteListener.getNumber(1, Integer.parseInt(String.valueOf(l)));
  }

  public void close()
  {
    if ((this.db != null) || (this.db.isOpen()))
      this.db.close();
  }

  public int deleteVod(int paramInt)
  {
    if ((this.db == null) || (!this.db.isOpen()))
      this.db = this.dbHelper.getWritableDatabase();
    SQLiteDatabase localSQLiteDatabase = this.db;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    int i = localSQLiteDatabase.delete("Tv", "idTv = ?", arrayOfString);
    if ((this.db != null) || (this.db.isOpen()))
      this.db.close();
    return i;
  }

  public boolean isInsert(int paramInt)
  {
    if ((this.db == null) || (!this.db.isOpen()))
      this.db = this.dbHelper.getWritableDatabase();
    SQLiteDatabase localSQLiteDatabase = this.db;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("Tv", null, "idTv=?", arrayOfString, null, null, null);
    //Cursor localCursor = localSQLiteDatabase.rawQuery("select * from Tv where idTv = ?", arrayOfString);
    if (localCursor.moveToNext())
      return true;
    if ((localCursor != null) && (!localCursor.isClosed()))
      localCursor.close();
    return false;
  }

  public List<Channel> queryAllLive()
  {
    if ((this.db == null) || (!this.db.isOpen()))
      this.db = this.dbHelper.getWritableDatabase();
    ArrayList localArrayList = new ArrayList();
    Cursor localCursor = this.db.query("Tv", null, null, null, null, null, null);
    //Cursor localCursor = this.db.rawQuery("select * from Tv", null);
      while (localCursor.moveToNext())
      {
    	  Channel localChannel = new Channel();
          int i = localCursor.getInt(localCursor.getColumnIndex("idTv"));
          String str1 = localCursor.getString(localCursor.getColumnIndex("logo"));
          int j = localCursor.getInt(localCursor.getColumnIndex("mode"));
          String str2 = localCursor.getString(localCursor.getColumnIndex("name"));
          String str3 = localCursor.getString(localCursor.getColumnIndex("playUrl"));
          int k = localCursor.getInt(localCursor.getColumnIndex("sort"));
          int m = localCursor.getInt(localCursor.getColumnIndex("typeId"));
          localChannel.setId(i);
          localChannel.setLogo(str1);
          localChannel.setMode(j);
          localChannel.setName(str2);
          localChannel.setPlayUrl(str3);
          localChannel.setSort(k);
          localChannel.setTypeId(m);
          localChannel.setPlayUrl(str3);
          localArrayList.add(localChannel);

      }
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
        if ((this.db != null) || (this.db.isOpen()))
          this.db.close();
        return localArrayList;      
  }

  public boolean queryIsHaveData()
  {
    if ((this.db == null) || (!this.db.isOpen()))
      this.db = this.dbHelper.getWritableDatabase();
    return this.db.rawQuery("select * from Tv", null).moveToNext();
  }

  public static abstract interface AddToDeleteListener
  {
    public abstract void getNumber(int paramInt1, int paramInt2);
  }

  public static abstract interface queryVodListener
  {
    public abstract void getVodData(List<Channel> paramList);
  }
}
