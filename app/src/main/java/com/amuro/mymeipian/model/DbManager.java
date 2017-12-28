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

	public<T extends OrmEntity> long insert(T entity)
	{
		return ormManager.insert(entity);
	}

	public<T extends OrmEntity> List<T> queryAll(Class<? extends OrmEntity> entityClass)
	{
		return ormManager.queryAll(entityClass);
	}

	public<T extends OrmEntity> int delete(T entity)
	{
		return ormManager.delete(entity);
	}

	public<T extends OrmEntity> int update(T entity)
	{
		return ormManager.update(entity);
	}
}
