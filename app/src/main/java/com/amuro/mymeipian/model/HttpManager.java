package com.amuro.mymeipian.model;

import android.content.Context;

import com.amuro.corelib.http.okhttp.OkHttpCore;

import java.util.Map;

/**
 * Created by Amuro on 2017/12/25.
 */

public class HttpManager
{
	private OkHttpCore httpCore;

	public HttpManager(Context context)
	{
		httpCore = new OkHttpCore(context);
	}

	public<T> void getEntity(
			String url, Map<String, String> headers, Map<String, String> params,
			Class<T> clazzOfT, OkHttpCore.IHttpCallback<T> httpCallback)
	{
		httpCore.getEntity(url, headers, params, clazzOfT, httpCallback);
	}

	public<T> void postEntity(
			String url, Map<String, String> headers, Map<String, String> params,
			Class<T> clazzOfT, OkHttpCore.IHttpCallback<T> httpCallback)
	{
		httpCore.postEntity(url, headers, params, clazzOfT, httpCallback);
	}

	public<T> void getList(
			String url, Map<String, String> headers, Map<String, String> params, Class<T> clazzOfT, OkHttpCore.IHttpCallback<T> httpCallback)
	{
		httpCore.getList(url, headers, params, clazzOfT, httpCallback);
	}

	public<T> void postList(
			String url, Map<String, String> headers, Map<String, String> params, Class<T> clazzOfT, OkHttpCore.IHttpCallback<T> httpCallback)
	{
		httpCore.postList(url, headers, params, clazzOfT, httpCallback);
	}
}
