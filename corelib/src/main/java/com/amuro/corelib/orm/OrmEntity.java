package com.amuro.corelib.orm;

/**
 * Created by Amuro on 2017/12/22.
 */

public abstract class OrmEntity
{
	protected long _id;

	public long get_id()
	{
		return _id;
	}

	public void set_id(long _id)
	{
		this._id = _id;
	}
}
