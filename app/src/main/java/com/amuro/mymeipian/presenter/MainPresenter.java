package com.amuro.mymeipian.presenter;

import com.amuro.corelib.http.okhttp.OkHttpCore;
import com.amuro.mymeipian.model.MeiPianModel;
import com.amuro.mymeipian.model.entity.ArticleEntity;
import com.amuro.mymeipian.view.IMainView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Amuro on 2017/12/25.
 */

public class MainPresenter
{
	private MeiPianModel model;
	private IMainView mainView;

	public MainPresenter(IMainView mainView)
	{
		model = MeiPianModel.getInstance();
		this.mainView = mainView;
	}

	public void requestDataRx()
	{
		mainView.onDataFetching();
		Observable.just("").
				observeOn(Schedulers.io()).
				map(new Func1<String, Object>()
				{
					@Override
					public Object call(String s)
					{


						return null;
					}
				}).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(new Action1<Object>()
				{
					@Override
					public void call(Object o)
					{

					}
				});
	}

	public void requestData()
	{
		mainView.onDataFetching();
		String url = "http://223.111.8.99:8001/mpp-portal/test_article";
		model.getHttpManager().getList(
				url, null, null, ArticleEntity.class,
				new OkHttpCore.IHttpCallback<ArticleEntity>()
		{

			@Override
			public void onSuccess(ArticleEntity entity)
			{

			}

			@Override
			public void onSuccess(List<ArticleEntity> entityList)
			{
				mainView.onDataFinish(entityList);
			}

			@Override
			public void onFailed(int errorCode, String msg)
			{
				mainView.onDataError(errorCode, msg);
			}
		});
	}
}
