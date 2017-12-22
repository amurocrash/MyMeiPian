package com.amuro.corelib.orm.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.amuro.corelib.orm.OrmEntity;
import com.amuro.corelib.orm.annotation.OrmColumn;
import com.amuro.corelib.orm.annotation.OrmTable;
import com.amuro.corelib.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amuro on 2017/12/21.
 */

public class SqlUtils
{
	public static String javaBaseTypeToSql(Class<?> clazz)
	{
		if(clazz.equals(int.class) || clazz.equals(Integer.class))
		{
			return "integer";
		}
		else if(clazz.equals(String.class))
		{
			return "text";
		}

		return "text";
	}

	public static String getTableName(Class<?> tableClass)
	{
		if(tableClass.getAnnotation(OrmTable.class) == null)
		{
			return null;
		}

		return tableClass.getAnnotation(OrmTable.class).tableName();
	}

	public static ContentValues objToContentValues(Object obj)
	{
		try
		{

			Field[] fields = ReflectUtils.getAllField(obj.getClass());
			ContentValues cv = new ContentValues();

			for (Field f : fields)
			{
				f.setAccessible(true);
				if (f.getAnnotation(OrmColumn.class) != null)
				{
					if(f.getType() == int.class)
					{
						cv.put(f.getName(), (int)f.get(obj));
					}
					else if(f.getType() == String.class)
					{
						cv.put(f.getName(), (String)f.get(obj));
					}
				}

			}

			return cv;
		}
		catch (Exception e)
		{
			return null;
		}

	}

	public static List<OrmEntity> cursorToList(
			Cursor cursor, Class<? extends OrmEntity> entityClass)
	{
		List<OrmEntity> entityList = null;
		try
		{
			if (cursor.moveToFirst())
			{
				entityList = new ArrayList<>();

				for (int i = 0; i < cursor.getCount(); i++)
				{
					OrmEntity entity = entityClass.newInstance();
					cursor.move(i);
					entity.set_id(cursor.getInt(0));

					Field[] fields = ReflectUtils.getAllField(entityClass);
					for (Field f : fields)
					{
						f.setAccessible(true);
						if (f.getAnnotation(OrmColumn.class) != null)
						{
							int columnIndex = cursor.getColumnIndex(f.getName());
							Object value = null;
							if(f.getType() == int.class)
							{
								value = cursor.getInt(columnIndex);
							}
							else if(f.getType() == String.class)
							{
								value = cursor.getString(columnIndex);
							}

							f.set(entity, value);
						}
					}

					entityList.add(entity);

				}


			}


		}
		catch (Exception e)
		{

		}

		return entityList;
	}
}






























