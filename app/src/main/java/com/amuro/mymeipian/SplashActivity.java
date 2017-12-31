package com.amuro.mymeipian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.amuro.mymeipian.utils.BaseActivity;
import com.amuro.mymeipian.view.adv.AdvActivity;

/**
 * Created by Amuro on 2017/12/29.
 */

public class SplashActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Intent intent =
						new Intent(SplashActivity.this, AdvActivity.class);
				startActivity(intent);
				finish();
			}

		}, 2000);
	}

	@Override
	protected boolean needFullScreen()
	{
		return true;
	}
}
