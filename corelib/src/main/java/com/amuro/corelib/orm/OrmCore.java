package com.amuro.corelib.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.amuro.corelib.logger.Logger;
import com.amuro.corelib.orm.utils.SqlUtils;

import java.util.List;

/**
 * Created by Amuro on 2017/12/21.
 */

public class OrmCore<T extends OrmEntity> extends SQLiteOpenHelper
{
	private Context context;
	private SQLiteDatabase db;
	private Logger logger;

	public OrmCore(Context context, String name, int version)
	{
		super(context, name, null, version);
		this.context = context;
		logger = OrmManager.getInstance().logger;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		logger.v("sql oh onCreate");
		this.db = db;

		for(String sql : OrmManager.getInstance().getCreateSqls())
		{
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}


	public long insert(T entity)
	{
		String tableName = SqlUtils.getTableName(entity.getClass());
		if(TextUtils.isEmpty(tableName))
		{
			return -1;
		}

		ContentValues cv = SqlUtils.objToContentValues(entity);

		if(cv == null)
		{
			return -1;
		}

		db = this.getWritableDatabase();
		db.beginTransaction();
		long result = -1;
		try
		{
			result = db.insert(tableName, null, cv);
			SqlUtils.setEntity_id(entity, result);
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			logger.v("insert exception: " + e.getMessage());
		}
		finally
		{
			db.endTransaction();
		}

		return result;
	}

	public int delete(T entity)
	{
		int result = 0;
		db = this.getWritableDatabase();

		db.beginTransaction();
		try
		{
			String tableName =
					SqlUtils.getTableName(entity.getClass());

			result = db.delete(
					tableName, "_id=?", new String[]{String.valueOf(entity.get_id())});

			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}

		return result;
	}

	public List<T> queryAll(Class<? extends OrmEntity> tableClass)
	{
		db = this.getWritableDatabase();
		db.beginTransaction();
		List<T> resultList = null;

		try
		{
			String tableName = SqlUtils.getTableName(tableClass);
			Cursor cursor = db.rawQuery("select * from " + tableName, null);
			if(cursor.moveToFirst())
			{
				resultList = SqlUtils.cursorToList(cursor, tableClass);
			}

			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
			return resultList;
		}

	}

	public int update(T entity)
	{
		db = this.getWritableDatabase();
		db.beginTransaction();
		int result = 0;

		try
		{
			String tableName =
					SqlUtils.getTableName(entity.getClass());
			ContentValues cv = SqlUtils.objToContentValues(entity);

			result = db.update(
					tableName, cv, "_id=?", new String[]{String.valueOf(entity.get_id())});
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{

		}
		finally
		{
			db.endTransaction();
			return result;
		}

	}

	public void recycle()
	{
		db.close();
	}
}
