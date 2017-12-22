package com.amuro.corelib.orm;

import android.content.Context;

import com.amuro.corelib.logger.Logger;
import com.amuro.corelib.utils.SingleTon;

import java.util.List;

/**
 * Created by Amuro on 2017/12/20.
 */

public class OrmManager
{
	private OrmManager()
	{
		logger = Logger.newInstance(DEBUG_TAG);
	}

	private static SingleTon<OrmManager> singleTon = new SingleTon<OrmManager>()
	{
		@Override
		protected OrmManager create()
		{
			return new OrmManager();
		}
	};

	public static OrmManager getInstance()
	{
		return singleTon.get();
	}

	public static final String DEBUG_TAG = "orm_mgr";
	public Logger logger;

	private Context context;
	private OrmCore ormCore;

	public void initialize(Context context, String dbName, int version)
	{
		ormCore = new OrmCore(context, dbName, version);
		ormCore.createTables();
	}

	public long insert(OrmEntity data)
	{
		return ormCore.insert(data);
	}

	public int delete(OrmEntity data)
	{
		return ormCore.delete(data);
	}

	public List<OrmEntity> queryAll(Class<? extends OrmEntity> entityClass)
	{
		return ormCore.queryAll(entityClass);
	}

}
































