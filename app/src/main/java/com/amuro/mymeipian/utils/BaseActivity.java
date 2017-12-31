package com.amuro.mymeipian.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Amuro on 2017/12/29.
 */

public abstract class BaseActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(needHideActionBar())
		{
			if (getSupportActionBar() != null)
			{
				getSupportActionBar().hide();
			}
		}

		if(needFullScreen())
		{
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	protected boolean needHideActionBar()
	{
		return true;
	}

	protected boolean needFullScreen()
	{
		return false;
	}
}
