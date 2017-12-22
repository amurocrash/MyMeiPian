package com.amuro.corelib.utils;

/**
 * Created by Amuro on 2017/12/20.
 */

public abstract class SingleTon<T>
{
	private T instance;

	protected abstract T create();

	public final T get()
	{
		synchronized (this)
		{
			if (instance == null)
			{
				instance = create();
			}

			return instance;
		}

	}
}
