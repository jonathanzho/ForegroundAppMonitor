package com.example.jonathan.foregroundappmonitor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends Activity {
  private static final String TAG = " FAM " + MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String fgPackage = getForegroundPackage();

    Log.v(TAG, "onCreate: end");
  }

  private String getForegroundPackage() {
    Log.d(TAG, "getForegroundPackage");

    String fgPackage = null;
    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      UsageStatsManager usm = (UsageStatsManager)this.getSystemService(Context.USAGE_STATS_SERVICE);
      long time = System.currentTimeMillis();
      List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
      if (appList != null && appList.size() > 0) {
        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
        for (UsageStats usageStats : appList) {
          mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
        }
        if (mySortedMap != null && !mySortedMap.isEmpty()) {
          fgPackage = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
        }
      }
    } else {
      ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
      List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
      fgPackage = tasks.get(0).processName;
    }

    Log.v(TAG, "getForegroundPackage: fgPackage=[" + fgPackage + "]");

    return fgPackage;
  }
}
