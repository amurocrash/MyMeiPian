package com.amuro.corelib.logger;

import android.util.Log;

/**
 * Created by Amuro on 2017/12/21.
 */

public class Logger
{
	public static Logger newInstance(String defaultTag)
	{
		return new Logger(defaultTag);
	}

	private String defaultTag;

	private Logger(String defaultTag)
	{
		this.defaultTag = defaultTag;
	}

	public void v(String msg)
	{
		v(defaultTag, msg);
	}

	public void v(String tag, String msg)
	{
		Log.v(tag, msg);
	}
}
