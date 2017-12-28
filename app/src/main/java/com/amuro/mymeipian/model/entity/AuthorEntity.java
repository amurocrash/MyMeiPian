package com.amuro.mymeipian.model.entity;

import com.amuro.corelib.orm.OrmEntity;
import com.amuro.corelib.orm.annotation.OrmColumn;
import com.amuro.corelib.orm.annotation.OrmTable;

import java.util.Calendar;

/**
 * Created by Amuro on 2017/12/23.
 */
@OrmTable(tableName = "author")
public class AuthorEntity extends OrmEntity
{
	@OrmColumn
	private String nickName;
	@OrmColumn
	private int id;
	@OrmColumn
	private int mpAge;
	@OrmColumn
	private int sex;
	@OrmColumn
	private Calendar birthDay;
	@OrmColumn
	private String signature;

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getMpAge()
	{
		return mpAge;
	}

	public void setMpAge(int mpAge)
	{
		this.mpAge = mpAge;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public Calendar getBirthDay()
	{
		return birthDay;
	}

	public void setBirthDay(Calendar birthDay)
	{
		this.birthDay = birthDay;
	}

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}
}
