/*
 * Copyright 2013 Daniel Logan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sigmond.net;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

/**
 * 
 * Performs an asynchronous picture request.
 *
 */
public class AsyncPictureRequest extends AsyncTask<PicRequestInfo, Integer, PicRequestInfo> {

	@Override
	protected PicRequestInfo doInBackground(PicRequestInfo... params) {
		
		PicRequestInfo info = params[0];
		Drawable drawable = LoadImageFromWebOperations(info.url);
		info.drawable = drawable;
		
		return info;
	}
	
	protected void onPostExecute(PicRequestInfo info)
	{
		super.onPostExecute(info);
		info.finished();
	}
	
	protected static Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			return null;
		}
	}

}
