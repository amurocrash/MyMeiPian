package com.amuro.mymeipian.presenter.find;

import com.amuro.mymeipian.model.MeiPianModel;
import com.amuro.mymeipian.model.entity.ArticleEntity;
import com.amuro.mymeipian.view.find.IFindView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Amuro on 2017/12/25.
 */

public class FindPresenter
{
	private MeiPianModel model;
	private IFindView mainView;

	public FindPresenter(IFindView mainView)
	{
		model = MeiPianModel.getInstance();
		this.mainView = mainView;
	}

	public void requestDataRx()
	{
		mainView.onDataFetching();
		final String url = "http://223.111.8.99:8001/mpp-portal/test_article";
		Observable.
				create(new Observable.OnSubscribe<List<ArticleEntity>>()
				{
					@Override
					public void call(Subscriber<? super List<ArticleEntity>> subscriber)
					{
						List<ArticleEntity> articleEntityList = null;

						try
						{
							articleEntityList =
									model.getHttpManager().syncGetList(
											url, null, null, ArticleEntity.class);
							subscriber.onNext(articleEntityList);
							subscriber.onCompleted();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							subscriber.onError(e);
							subscriber.onCompleted();
						}


					}
				}).
				subscribeOn(Schedulers.io()).
				observeOn(AndroidSchedulers.mainThread()).
				subscribe(new Subscriber<List<ArticleEntity>>()
				{
					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError(Throwable e)
					{
						mainView.onDataError(-2, e.getMessage());
					}

					@Override
					public void onNext(List<ArticleEntity> articleEntities)
					{
						mainView.onDataFinish(articleEntities);
					}
				});

	}

}
