package com.redorange.motutv1.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BackgroundExecutor
{
  private static Executor executor = Executors.newCachedThreadPool();

  public static void execute(Runnable paramRunnable)
  {
    executor.execute(paramRunnable);
  }

  public static void setExecutor(Executor paramExecutor)
  {
    executor = paramExecutor;
  }
}

/* Location:           D:\Workspace\ReverseTool\classes_dex2jar.jar
 * Qualified Name:     com.redorange.motutv1.utils.BackgroundExecutor
 * JD-Core Version:    0.6.0
 */