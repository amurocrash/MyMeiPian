package com.amuro.mymeipian;

import android.os.Bundle;

import com.amuro.mymeipian.utils.BaseActivity;
import com.amuro.mymeipian.view.find.FindFragment;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportFragmentManager().beginTransaction().
				replace(R.id.fl_content, new FindFragment()).commit();

	}

	public native String stringFromJNI();

}
