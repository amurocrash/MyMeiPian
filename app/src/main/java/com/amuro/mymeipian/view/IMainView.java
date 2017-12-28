package com.amuro.mymeipian.view;

import com.amuro.mymeipian.model.entity.ArticleEntity;

import java.util.List;

/**
 * Created by Amuro on 2017/12/25.
 */

public interface IMainView
{
	void onDataFetching();
	void onDataError(int errorCode, String msg);
	void onDataFinish(List<ArticleEntity> entityList);
}
