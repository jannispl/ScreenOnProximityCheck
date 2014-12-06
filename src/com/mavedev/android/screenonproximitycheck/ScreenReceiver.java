package com.mavedev.android.screenonproximitycheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent i = new Intent(context, ScreenService.class);
		i.setAction(ScreenService.ACTION_SCREEN_ON);
		context.startService(i);
	}
}
