package com.mavedev.android.screenonproximitycheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity
{
	private static final String NAME = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent i = new Intent(this, ScreenService.class);
		this.startService(i);

		ComponentName mDeviceAdmin = new ComponentName(this,
				DeviceAdminReceiver.class);
		DevicePolicyManager mgr = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		if (!mgr.isAdminActive(mDeviceAdmin))
		{
			Log.i(NAME, "Requesting to activate Device Administrator");

			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					new ComponentName(this, DeviceAdminReceiver.class));
			startActivityForResult(intent, 6969);
		}
		else
		{
			Log.i(NAME, "Device Administrator already active");
		}

		try
		{
			PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			boolean isSystemApp = (info.applicationInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0;
			Log.i(NAME, "isSystemApp: " + isSystemApp);

			if (!isSystemApp)
			{
				new AlertDialog.Builder(this)
						.setTitle("Note")
						.setMessage(
								"App is not installed as a system app - service may be stopped by the system after some time.")
						.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int which)
									{
									}
								}).show();
			}
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
