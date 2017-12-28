package com.amuro.corelib.http.okhttp;

import android.net.Uri;

import java.util.Map;

/**
 * Created by Amuro on 2017/12/25.
 */

public class HttpUtils
{
	public static String urlWithParamsForGet(String url, Map<String, String> params)
	{
		String urlWithParams = url;
		if(params != null && params.size() > 0)
		{
			Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
			for(Map.Entry<String, String> entry : params.entrySet())
			{
				uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
			}

			urlWithParams = uriBuilder.build().toString();
		}

		return urlWithParams;
	}
}
