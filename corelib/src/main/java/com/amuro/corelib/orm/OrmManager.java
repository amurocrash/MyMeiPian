package com.amuro.corelib.orm;

import android.content.Context;

import com.amuro.corelib.logger.Logger;
import com.amuro.corelib.orm.annotation.OrmColumn;
import com.amuro.corelib.orm.annotation.OrmTable;
import com.amuro.corelib.orm.utils.SqlUtils;
import com.amuro.corelib.utils.ReflectUtils;
import com.amuro.corelib.utils.SingleTon;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	private Map<String, String> createSqlMap;
	private Map<String, OrmCore> coreMap;

	public void initialize(Context context, String dbName, int version)
	{
		this.context = context;
		coreMap = new ConcurrentHashMap<>();
		createSqlMap = new ConcurrentHashMap<>();
		createOrmCores(dbName, version);
	}

	public void createOrmCores(String dbName, int version)
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

			for (Class<?> tableClass : tableEntityList)
			{
				String createTableSql = generateCreateTableSqls(tableClass);
				if (createTableSql != null)
				{
					logger.v("createSql: " + createTableSql);
					OrmCore oc = new OrmCore(context, dbName, version);
					coreMap.put(tableClass.getName(), oc);
					createSqlMap.put(tableClass.getName(), createTableSql);
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

	protected Collection<String> getCreateSqls()
	{
		return createSqlMap.values();
	}

	public<T extends OrmEntity> long insert(T data)
	{
		OrmCore ormCore =
				coreMap.get(data.getClass().getName());
		return ormCore.insert(data);
	}

	public<T extends OrmEntity> int delete(T data)
	{
		OrmCore ormCore =
				coreMap.get(data.getClass().getName());
		return ormCore.delete(data);
	}

	public<T extends OrmEntity> List<T> queryAll(Class<? extends OrmEntity> entityClass)
	{
		OrmCore ormCore =
				coreMap.get(entityClass.getName());
		return ormCore.queryAll(entityClass);
	}

	public <T extends OrmEntity> int update(T entity)
	{
		OrmCore ormCore =
				coreMap.get(entity.getClass().getName());
		return ormCore.update(entity);
	}
}
































