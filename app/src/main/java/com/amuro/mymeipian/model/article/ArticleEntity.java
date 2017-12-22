package com.amuro.mymeipian.model.article;

import com.amuro.corelib.orm.OrmEntity;
import com.amuro.corelib.orm.annotation.OrmColumn;
import com.amuro.corelib.orm.annotation.OrmTable;

/**
 * Created by Amuro on 2017/12/20.
 */

@OrmTable(tableName = "article")
public class ArticleEntity extends OrmEntity
{
	@OrmColumn
	private int id;
	@OrmColumn
	private String title;
	@OrmColumn
	private String content;
	@OrmColumn
	private String author;
	@OrmColumn
	private int praise;
	@OrmColumn
	private int share;
	@OrmColumn
	private int comment;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public int getPraise()
	{
		return praise;
	}

	public void setPraise(int praise)
	{
		this.praise = praise;
	}

	public int getShare()
	{
		return share;
	}

	public void setShare(int share)
	{
		this.share = share;
	}

	public int getComment()
	{
		return comment;
	}

	public void setComment(int comment)
	{
		this.comment = comment;
	}

	@Override
	public String toString()
	{
		return "ArticleEntity{" +
				"id=" + id +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", author='" + author + '\'' +
				", praise=" + praise +
				", share=" + share +
				", comment=" + comment +
				'}';
	}
}
