package com.amuro.corelib.http.okhttp;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.amuro.corelib.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCore
{
	private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;

	private Context context;
	private OkHttpClient httpClient;
	private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
	private Handler handler;

	public OkHttpCore(Context context)
	{
		this(context, null);
	}

	public OkHttpCore(Context context, OkHttpClient client)
	{
		this.context = context.getApplicationContext();
		this.handler = new Handler(Looper.getMainLooper());
		init(client);
	}

	private void init(OkHttpClient client)
	{
		if(client != null)
		{
			httpClient = client;
			return;
		}

		if (httpClient == null)
		{
			String externalCacheDir =
					Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
					context.getPackageName() + File.separator +
					"okHttpCache";
			File cacheDir = FileUtil.getDir(externalCacheDir);

			Https.SSLParams sslParams = Https.getSslSocketFactory(null, null, null);


			httpClient = new OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
				.writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
				.readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
				.pingInterval(20, TimeUnit.SECONDS) //webSocket轮训间隔(单位:秒)
				.cache(new Cache(cacheDir.getAbsoluteFile(), DEFAULT_CACHE_SIZE))//设置缓存
				.cookieJar(new CookieJar()
				{
					@Override
					public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
					{
						cookieStore.put(url.host(), cookies);
					}

					@Override
					public List<Cookie> loadForRequest(HttpUrl url)
					{
						List<Cookie> cookies = cookieStore.get(url.host());
						return cookies == null ? new ArrayList<Cookie>() : cookies;
					}
				})
				.hostnameVerifier(new HostnameVerifier()
				{
					@Override
					public boolean verify(String hostname, SSLSession session)
					{
						return true;
					}
				})
				.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
				.build();
		}
	}

	public Response syncGet(
			String url, Map<String, String> headers, Map<String, String> params)
	{
		return realSyncRequest(url, headers, params, false);
	}

	public Response syncPost(
			String url, Map<String, String> headers, Map<String, String> params)
	{
		return realSyncRequest(url, headers, params, true);
	}

	private Response realSyncRequest(
			String url, Map<String, String> headers, Map<String, String> params, boolean isPost)
	{
		if(TextUtils.isEmpty(url))
		{
			return null;
		}

		Response response = null;

		try
		{
			response = httpClient.newCall(
					generateRequest(url, headers, params, isPost)).execute();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return response;
	}

	private Request generateRequest(
			String url, Map<String, String> headers, Map<String, String> params, boolean isPost)
	{

		Request.Builder builder = new Request.Builder();
		if(headers != null && headers.size() > 0)
		{
			for(Map.Entry<String, String> header : headers.entrySet())
			{
				builder.header(header.getKey(), header.getValue());
			}
		}

		if(!isPost)
		{
			String urlWithParams = HttpUtils.urlWithParamsForGet(url, params);
			builder.url(urlWithParams);
			builder.get();
		}
		else
		{
			builder.url(url);
			FormBody.Builder fbBuilder = new FormBody.Builder();
			for(Map.Entry<String, String> param : params.entrySet())
			{
				fbBuilder.add(param.getKey(), param.getValue());
			}

			builder.post(fbBuilder.build());
		}


		return builder.build();
	}

	public interface IHttpCallback<T>
	{
		void onSuccess(T entity);
		void onSuccess(List<T> entityList);
		void onFailed(int errorCode, String msg);
	}

	public<T> void getEntity(
			String url, Map<String, String> headers, Map<String, String> params, Class<T> clazzOfT, IHttpCallback<T> httpCallback)
	{
		realAsyncRequest(url, headers, params, false, clazzOfT, httpCallback, false);
	}

	public<T> void postEntity(
			String url, Map<String, String> headers, Map<String, String> params, Class<T> clazzOfT, IHttpCallback<T> httpCallback)
	{
		realAsyncRequest(url, headers, params, true, clazzOfT, httpCallback, false);
	}

	public<T> void getList(
			String url, Map<String, String> headers, Map<String, String> params, Class<T> clazzOfT, IHttpCallback<T> httpCallback)
	{
		realAsyncRequest(url, headers, params, false, clazzOfT, httpCallback, true);
	}

	public<T> void postList(
			String url, Map<String, String> headers, Map<String, String> params, Class<T> clazzOfT, IHttpCallback<T> httpCallback)
	{
		realAsyncRequest(url, headers, params, true, clazzOfT, httpCallback, true);
	}

	private<T> void realAsyncRequest(
			String url, final Map<String, String> headers, Map<String, String> params,
			boolean isPost, final Class<T> clazzOfT,
			final IHttpCallback<T> httpCallback, final boolean isList)
	{
		if(TextUtils.isEmpty(url))
		{
			if(httpCallback != null)
			{
				httpCallback.onFailed(-2, "url is empty");
			}
			return;
		}

		Call call = httpClient.newCall(generateRequest(url, headers, params, isPost));
		call.enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, final IOException e)
			{
				if(httpCallback != null)
				{
					handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							httpCallback.onFailed(-2, "http failed: " + e.getMessage());
						}
					});
				}
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException
			{
				if (httpCallback != null)
				{

					try
					{
						if (isList)
						{

							final List<T> entityList = JSON.parseArray(
								response.body().string(),
								clazzOfT
							);

							handler.post(new Runnable()
							{
								@Override
								public void run()
								{
									httpCallback.onSuccess(entityList);
								}
							});

						}
						else
						{

							final T entity =
									JSON.parseObject(response.body().string(), clazzOfT);

							handler.post(new Runnable()
							{
								@Override
								public void run()
								{
									httpCallback.onSuccess(entity);
								}
							});

						}
					}
					catch (final IOException e)
					{
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								httpCallback.onFailed(
										-2, "http success but failed to result: " + e.getMessage());
							}
						});

					}
				}

			}

		});
	}
}