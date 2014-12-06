package com.mavedev.android.screenonproximitycheck;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class ScreenService extends Service
{
	public static final String ACTION_SCREEN_ON = "com.mavedev.android.screenonproximity.SCREEN_ON";

	BroadcastReceiver mReceiver = null;
	SensorManager mSensorManager;
	DevicePolicyManager mDeviceManager;
	Sensor mSensor;

	SensorEventListener mEventListener = new SensorEventListener()
	{
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			float proxValue = event.values[0];
			if (proxValue <= 0.2f)
			{
				mDeviceManager.lockNow();
			}
			mSensorManager.unregisterListener(this);
		}
	};
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		// Register receiver that handles screen on and screen off logic
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mDeviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		if (intent != null
				&& intent.getAction() != null
				&& intent.getAction().equals(ACTION_SCREEN_ON))
		{
			mSensorManager.registerListener(mEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		if (mReceiver != null)
		{
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
}
