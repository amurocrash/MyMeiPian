package com.amuro.mymeipian.model;

import android.content.Context;

import com.amuro.corelib.logger.Logger;
import com.amuro.corelib.utils.SingleTon;
import com.amuro.mymeipian.model.entity.ArticleEntity;
import com.amuro.mymeipian.model.entity.AuthorEntity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Amuro on 2017/12/21.
 */

public class MeiPianModel
{
	private MeiPianModel()
	{
		logger = Logger.newInstance(DEBUG_TAG);
	}

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

	private static final String DEBUG_TAG = "model";

	private boolean initialized = false;
	private Logger logger;
	private Context context;
	private DbManager dbManager;
	private HttpManager httpManager;

	public void initialize(Context ctx)
	{
		if(initialized)
		{
			return;
		}

		this.context = ctx.getApplicationContext();
		dbManager = new DbManager(context);
		httpManager = new HttpManager(context);

		initialized = true;
	}

	public DbManager getDbManager()
	{
		return dbManager;
	}

	public HttpManager getHttpManager()
	{
		return httpManager;
	}

	public void mockInitData()
	{
//		for(int i = 0; i < 10; i++)
//		{
//			ArticleEntity entity = new ArticleEntity();
//			entity.setAuthor("amuro" + i);
//			entity.setComment(20);
//			entity.setContent("1234567890");
//			entity.setPraise(10);
//			entity.setId(100000000 + i);
//			entity.setShare(1);
//			entity.setTitle("Hello world" + i);
//			dbManager.insert(entity);
//		}
//
//		for(int i = 0; i < 10; i++)
//		{
//			AuthorEntity entity = new AuthorEntity();
//			entity.setId(1000 + i);
//			entity.setMpAge(10 + i);
//			entity.setNickName("amuro" + i);
//			entity.setSex(0);
//			entity.setSignature("fxxk char");
//			Calendar c = Calendar.getInstance();
//			c.set(1986, 3, 12 + i);
//			entity.setBirthDay(c);
//			dbManager.insert(entity);
//		}
	}
}
