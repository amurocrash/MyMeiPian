package com.amuro.mymeipian.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amuro.corelib.utils.ToastUtils;
import com.amuro.mymeipian.R;
import com.amuro.mymeipian.model.entity.ArticleEntity;
import com.amuro.mymeipian.presenter.MainPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView
{
	private ProgressDialog pd;
	private MainPresenter mainPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pd = new ProgressDialog(this);
		mainPresenter = new MainPresenter(this);

		findViewById(R.id.bt_refresh).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mainPresenter.requestData();
			}
		});

	}

	public native String stringFromJNI();


	@Override
	public void onDataFetching()
	{
		pd.show();
	}

	@Override
	public void onDataError(int errorCode, String msg)
	{
		pd.dismiss();
		ToastUtils.show(this, "Error: " + msg);
	}

	@Override
	public void onDataFinish(List<ArticleEntity> entityList)
	{
		pd.dismiss();
		ArticleEntity entity = entityList.get(0);
		ToastUtils.show(this, entity.toString());
	}
}
