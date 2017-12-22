package com.amuro.mymeipian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amuro.corelib.orm.OrmEntity;
import com.amuro.corelib.utils.ToastUtils;
import com.amuro.mymeipian.model.MeiPianModel;
import com.amuro.mymeipian.model.article.ArticleEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

	static
	{
		System.loadLibrary("meipian_native");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MeiPianModel.getInstance().initialize(this);
		MeiPianModel.getInstance().mockInitData();

		findViewById(R.id.bt_insert).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ArticleEntity entity = new ArticleEntity();
				entity.setAuthor("char");
				entity.setComment(20);
				entity.setContent("1234567890");
				entity.setPraise(10);
				entity.setId(100000000);
				entity.setShare(1);
				entity.setTitle("Hello orm");
				MeiPianModel.getInstance().getDbManager().insert(entity);
			}
		});

		findViewById(R.id.bt_query).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				List<OrmEntity> list =
						MeiPianModel.getInstance().getDbManager().queryAll(ArticleEntity.class);
				ToastUtils.show(MainActivity.this, ((ArticleEntity)list.get(0)).getAuthor());
			}
		});
	}

	public native String stringFromJNI();


}
