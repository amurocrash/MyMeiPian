package com.amuro.mymeipian.view.adv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.amuro.mymeipian.MainActivity;
import com.amuro.mymeipian.R;
import com.amuro.mymeipian.presenter.adv.AdvPresenter;
import com.amuro.mymeipian.utils.BaseActivity;

/**
 * Created by Amuro on 2017/12/29.
 */

public class AdvActivity extends BaseActivity implements IAdvView
{
	private AdvPresenter advPresenter;
	private ImageView imageViewAdv;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adv);

		progressDialog = new ProgressDialog(this);
		advPresenter = new AdvPresenter(this);
		advPresenter.requestAdv();

		imageViewAdv = (ImageView)findViewById(R.id.iv_adv);

		findViewById(R.id.bt_jump_over).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				advPresenter.jumpOver();
			}
		});
	}

	@Override
	protected boolean needFullScreen()
	{
		return true;
	}

	@Override
	public void onAdvFetching()
	{
		progressDialog.show();
	}

	@Override
	public void onAdvFetched(Bitmap advBm)
	{
		progressDialog.dismiss();
		imageViewAdv.setImageBitmap(advBm);
	}

	@Override
	public void onAdvFetchFailed()
	{
		progressDialog.dismiss();
	}

	@Override
	public void onJumpOver()
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();

	}
}
