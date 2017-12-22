package com.amuro.corelib.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.amuro.corelib.logger.Logger;
import com.amuro.corelib.orm.annotation.OrmColumn;
import com.amuro.corelib.orm.annotation.OrmTable;
import com.amuro.corelib.orm.utils.SqlUtils;
import com.amuro.corelib.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amuro on 2017/12/21.
 */

public class OrmCore extends SQLiteOpenHelper
{
	private Context context;
	private SQLiteDatabase db;
	private Logger logger;
	private List<String> createTableSqlList;

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

		for(String sql : createTableSqlList)
		{
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

	public void createTables()
	{
		try
		{
			List<Class<?>> tableEntityList =
					ReflectUtils.scanClassesOfPkgWithAnnotation(
							context,
							context.getPackageName(),
							OrmTable.class);

			if (tableEntityList == null || tableEntityList.size() == 0)
			{
				return;
			}

			createTableSqlList = new ArrayList<>();

			for (Class<?> tableClass : tableEntityList)
			{
				String createTableSql = generateCreateTableSqls(tableClass);
				if (createTableSql != null)
				{
					logger.v("createSql: " + createTableSql);
					createTableSqlList.add(createTableSql);
				}
			}
		}
		catch (Exception e)
		{
			logger.v("create table exception: " + e.getMessage());
		}
	}

	private String generateCreateTableSqls(Class<?> tableClass)
	{
		String createSql = null;

		try
		{
			StringBuilder sb = new StringBuilder();
			String tableName =
					SqlUtils.getTableName(tableClass);
			sb.append(
					"CREATE TABLE " + tableName + "(_id integer primary key autoincrement, ");

			Field[] fields = ReflectUtils.getAllField(tableClass);
			for(Field f : fields)
			{
				f.setAccessible(true);
				if(f.getAnnotation(OrmColumn.class) != null)
				{
					sb.append(f.getName() + " " + SqlUtils.javaBaseTypeToSql(f.getType()) + ",");
				}

			}

			//delete the last comma
			sb.deleteCharAt(sb.length() - 1);
			createSql = sb.append(")").toString();
		}
		catch (Exception e)
		{
			logger.v("generate table sqls exception: " + e.getMessage());
		}


		return createSql;

	}

	public long insert(OrmEntity entity)
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
			entity.set_id(result);
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}

		return result;
	}

	public int delete(OrmEntity entity)
	{
		int result = 0;
		db = this.getWritableDatabase();

		db.beginTransaction();
		try
		{
			String tableName =
					SqlUtils.getTableName(entity.getClass());

			result = db.delete(tableName, "_id=?", new String[]{String.valueOf(entity.get_id())});

			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}

		return result;
	}

	public List<OrmEntity> queryAll(Class<? extends OrmEntity> tableClass)
	{
		db = this.getWritableDatabase();
		db.beginTransaction();

		try
		{
			String tableName = SqlUtils.getTableName(tableClass);
			Cursor cursor = db.rawQuery("select * from " + tableName, null);
			if(cursor.moveToFirst())
			{
				return SqlUtils.cursorToList(cursor, tableClass);
			}

			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}

		return null;
	}

	public void recycle()
	{
		db.close();
	}
}
