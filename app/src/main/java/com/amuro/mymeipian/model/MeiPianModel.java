package com.amuro.mymeipian.model;

import android.content.Context;

import com.amuro.corelib.orm.OrmManager;
import com.amuro.corelib.utils.SingleTon;
import com.amuro.mymeipian.model.article.ArticleEntity;

/**
 * Created by Amuro on 2017/12/21.
 */

public class MeiPianModel
{
	private MeiPianModel(){}

	private static SingleTon<MeiPianModel> singleTon = new SingleTon<MeiPianModel>()
	{
		@Override
		protected MeiPianModel create()
		{
			return new MeiPianModel();
		}
	};

	public static MeiPianModel getInstance()
	{
		return singleTon.get();
	}

	private boolean initialized = false;
	private Context context;
	private DbManager dbManager;

	public void initialize(Context ctx)
	{
		if(initialized)
		{
			return;
		}

		this.context = ctx.getApplicationContext();
		dbManager = new DbManager(context);

		initialized = true;
	}

	public DbManager getDbManager()
	{
		return dbManager;
	}

	public void mockInitData()
	{
		for(int i = 0; i < 10; i++)
		{
			ArticleEntity entity = new ArticleEntity();
			entity.setAuthor("amuro" + i);
			entity.setComment(20);
			entity.setContent("1234567890");
			entity.setPraise(10);
			entity.setId(100000000 + i);
			entity.setShare(1);
			entity.setTitle("Hello world" + i);
			dbManager.insert(entity);
		}
	}
}
