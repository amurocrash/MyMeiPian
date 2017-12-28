package com.amuro.corelib.utils;

import android.content.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class ReflectUtils
{
	public static Field[] getAllField(Class<?> clazz) throws Exception
	{
		return clazz.getDeclaredFields();
	}

	public static Object getStaticFieldValue(
			String className, String fieldName) throws Exception
	{
		Field field = Class.forName(className).getDeclaredField(fieldName);
		field.setAccessible(true);
		
		return field.get(null);
	}

	public static Object getFieldValue(
			String className, String fieldName, Object target) throws Exception
	{
		Field field = Class.forName(className).getDeclaredField(fieldName);
		field.setAccessible(true);
		
		return field.get(target);
	}

	public static Object invokeStaticMethod(
			String className, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception
	{
		Method method = Class.forName(className).getDeclaredMethod(methodName, paramTypes);
		method.setAccessible(true);
		return method.invoke(null, args);
	}

	public static Object invokeMethod(
			String className, String methodName, Class<?>[] paramTypes, Object target, Object[] args) throws Exception
	{
		Method method = Class.forName(className).getDeclaredMethod(methodName, paramTypes);
		method.setAccessible(true);
		return method.invoke(target, args);
	}

	public static Object newInstance(String className, Class<?>[] typeParams, Object[] args) throws Exception
	{
		Object result = null;

		Class<?> clazz = Class.forName(className);
		Constructor<?> constructor = clazz.getDeclaredConstructor(typeParams);
		result = constructor.newInstance(args);

		return result;
	}

	public static List<Class<?>> scanClassesOfPkg(Context ctx, String entityPackage)
			throws Exception
	{
		return scanClassesOfPkgWithAnnotation(ctx, entityPackage, null);
	}

	public static List<Class<?>> scanClassesOfPkgWithAnnotation(
			Context ctx, String entityPackage, Class<? extends Annotation> annotationClass)
			throws Exception
	{
		PathClassLoader pathClassLoader = (PathClassLoader) ctx.getClassLoader();
		Object dexPathList = getFieldValue(
				"dalvik.system.BaseDexClassLoader", "pathList", pathClassLoader);
		Object dexElements = getFieldValue(
				"dalvik.system.DexPathList", "dexElements", dexPathList);
		List<Class<?>> classes = new ArrayList<>();

		int length = Array.getLength(dexElements);
		for (int i = 0; i < length; i++)
		{
			Object element = Array.get(dexElements, i);

			DexFile dex = (DexFile) ReflectUtils.getFieldValue(
					"dalvik.system.DexPathList$Element", "dexFile", element
			);

			Enumeration<String> entries = dex.entries();
			while (entries.hasMoreElements())
			{
				String entryName = entries.nextElement();
				if (entryName.contains(entityPackage))
				{
					Class<?> entryClass =
							Class.forName(entryName, true, pathClassLoader);
					if (annotationClass == null)
					{
						classes.add(entryClass);
					}
					else
					{
						Annotation annotation =
								entryClass.getAnnotation(annotationClass);
						if (annotation != null)
						{
							classes.add(entryClass);
						}
					}
				}
			}
		}

		return classes;
	}

	public static void setFieldValue(
			String className, String fieldName, Object obj, Object value) throws Exception
	{
		Class clazz = Class.forName(className);
		Field f = clazz.getDeclaredField(fieldName);
		f.setAccessible(true);
		f.set(obj, value);
	}

}










































