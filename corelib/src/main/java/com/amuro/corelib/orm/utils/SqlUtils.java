package com.amuro.corelib.orm.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CalendarContract;

import com.amuro.corelib.orm.OrmEntity;
import com.amuro.corelib.orm.OrmManager;
import com.amuro.corelib.orm.annotation.OrmColumn;
import com.amuro.corelib.orm.annotation.OrmTable;
import com.amuro.corelib.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
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
		else if(clazz.equals(Calendar.class))
		{
			return "date";
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

	public static<T extends OrmEntity> ContentValues objToContentValues(T obj)
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
					if(f.getType() == Calendar.class)
					{
						cv.put(f.getName(), calenderToStr((Calendar)f.get(obj)));
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

	public static String calenderToStr(Calendar calendar)
	{
		return calendar.get(Calendar.YEAR) + "-" +
				calendar.get(Calendar.MONTH) + "-" +
				calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static<T extends OrmEntity> List<T> cursorToList(
		Cursor cursor, Class<? extends OrmEntity> entityClass)
	{
		List<T> entityList = null;
		try
		{
			if (cursor.moveToFirst())
			{
				entityList = new ArrayList<>();

				Field[] fields = ReflectUtils.getAllField(entityClass);
				for (int i = 0; i < cursor.getCount(); i++)
				{
					T entity = (T)entityClass.newInstance();
					setEntity_id(entity, cursor.getInt(0));
//					ReflectUtils.setFieldValue(
//							OrmEntity.class.getName(), "_id", entity, cursor.getInt(0));

//					entity.set_id(cursor.getInt(0));

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
							else if(f.getType() == Calendar.class)
							{
								String dateStr = cursor.getString(columnIndex);
								String[] dateArr = dateStr.split("-");
								Calendar c = Calendar.getInstance();
								c.set(Integer.valueOf(dateArr[0]),
										Integer.valueOf(dateArr[1]),
										Integer.valueOf(dateArr[2])
								);
								value = c;
							}
							else if(f.getType() == String.class)
							{
								value = cursor.getString(columnIndex);
							}

							f.set(entity, value);
						}
					}

					entityList.add(entity);
					cursor.moveToNext();

				}


			}


		}
		catch (Exception e)
		{
			OrmManager.getInstance().logger.v(
					"cursor to List when exception: " + e.getMessage());
		}

		return entityList;
	}

	public static<T extends OrmEntity> void setEntity_id(
			T entity, long _id) throws Exception
	{
		ReflectUtils.setFieldValue(
				OrmEntity.class.getName(), "_id", entity, _id);
	}
}






























