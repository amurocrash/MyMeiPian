package com.amuro.mymeipian.view.adv;

import android.graphics.Bitmap;

/**
 * Created by Amuro on 2017/12/29.
 */

public interface IAdvView
{
	void onAdvFetching();
	void onAdvFetched(Bitmap advBm);
	void onAdvFetchFailed();
	void onJumpOver();
}
