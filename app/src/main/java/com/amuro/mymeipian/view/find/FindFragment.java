package com.amuro.mymeipian.view.find;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amuro.corelib.utils.ToastUtils;
import com.amuro.mymeipian.R;
import com.amuro.mymeipian.model.entity.ArticleEntity;
import com.amuro.mymeipian.presenter.find.FindPresenter;

import java.util.List;

/**
 * Created by Amuro on 2017/12/29.
 */

public class FindFragment extends Fragment implements IFindView
{
	private View rootView;
	private ProgressDialog pd;
	private FindPresenter mainPresenter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_find, container, false);

		pd = new ProgressDialog(getContext());
		mainPresenter = new FindPresenter(this);

		rootView.findViewById(R.id.bt_refresh).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mainPresenter.requestDataRx();
			}
		});

		return rootView;
	}

	@Override
	public void onDataFetching()
	{
		pd.show();
	}

	@Override
	public void onDataError(int errorCode, String msg)
	{
		pd.dismiss();
		ToastUtils.show(getContext(), "Error: " + msg);
	}

	@Override
	public void onDataFinish(List<ArticleEntity> entityList)
	{
		pd.dismiss();
		ArticleEntity entity = entityList.get(0);
		ToastUtils.show(getContext(), entity.toString());
	}
}
