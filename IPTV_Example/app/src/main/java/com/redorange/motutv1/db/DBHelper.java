package com.redorange.motutv1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
  public DBHelper(Context paramContext)
  {
    super(paramContext, "vod.db", null, 3);
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE video(_id integer primary key autoincrement, name varchar(20), image varchar(40) NULL, vodid  varchar(10) NULL, queue varchar(20) NULL, category integer DEFAULT 1)");
    paramSQLiteDatabase.execSQL("CREATE TABLE Tv(_id integer primary key autoincrement, idTv integer DEFAULT 1 , logo varchar(50) null, mode integer DEFAULT 1, name varchar(20), playUrl varchar(50) null, sort integr DEFAULT 1, typeId integer DEFAULT 1)");
    paramSQLiteDatabase.execSQL("CREATE TABLE lock(_id integer primary key autoincrement, idTv integer DEFAULT 1 , logo varchar(50) null, mode integer DEFAULT 1, name varchar(20), playUrl varchar(50) null, sort integr DEFAULT 1, typeId integer DEFAULT 1)");
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.db.DBHelper
 * JD-Core Version:    0.6.0
 */