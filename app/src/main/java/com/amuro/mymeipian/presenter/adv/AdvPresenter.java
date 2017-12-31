package com.amuro.mymeipian.presenter.adv;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.amuro.mymeipian.model.MeiPianModel;
import com.amuro.mymeipian.view.adv.IAdvView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Amuro on 2017/12/30.
 */

public class AdvPresenter
{
	private MeiPianModel model;
	private IAdvView advView;

	public AdvPresenter(IAdvView advView)
	{
		model = MeiPianModel.getInstance();
		this.advView = advView;
	}

	public void requestAdv()
	{
		advView.onAdvFetching();
		final String url =
				"http://g.hiphotos.baidu.com/zhidao/pic/item/1e30e924b899a901da2aece318950a7b0308f5cc.jpg";
		Observable.
				create(new Observable.OnSubscribe<Bitmap>()
				{
					@Override
					public void call(Subscriber<? super Bitmap> subscriber)
					{
						try
						{
							Bitmap bitmap = model.getHttpManager().syncGetBitmap(url);
							if(bitmap != null)
							{
								subscriber.onNext(bitmap);
								subscriber.onCompleted();
							}
						}
						catch (Exception e)
						{
							subscriber.onError(e);
						}
					}
				}).
				subscribeOn(Schedulers.io()).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(new Subscriber<Bitmap>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError(Throwable e)
					{
						advView.onAdvFetchFailed();
					}

					@Override
					public void onNext(Bitmap bm)
					{
						advView.onAdvFetched(bm);
					}
				});

		new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				jumpOver();
			}
		}, 2000);

	}

	public void jumpOver()
	{
		advView.onJumpOver();
	}
}
