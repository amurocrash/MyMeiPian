package com.amuro.mymeipian.model;

import android.content.Context;

import com.amuro.corelib.orm.OrmEntity;
import com.amuro.corelib.orm.OrmManager;

import java.util.List;

/**
 * Created by Amuro on 2017/12/21.
 */

public class DbManager
{
	private static final String DB_NAME = "meipian.db";
	private static final int DB_VERSION = 1;

	private Context context;
	private OrmManager ormManager;

	public DbManager(Context context)
	{
		this.context = context;
		ormManager = OrmManager.getInstance();
		ormManager.initialize(context, DB_NAME, DB_VERSION);
	}

	public long insert(OrmEntity data)
	{
		return ormManager.insert(data);
	}

	public List<OrmEntity> queryAll(Class<? extends OrmEntity> entityClass)
	{
		return ormManager.queryAll(entityClass);
	}
}
