package com.amuro.mymeipian;

import android.app.Application;

import com.amuro.mymeipian.model.MeiPianModel;

/**
 * Created by Amuro on 2017/12/21.
 */

public class MeiPianApp extends Application
{
	static
	{
		System.loadLibrary("meipian_native");
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		MeiPianModel.getInstance().initialize(this);
	}
}
