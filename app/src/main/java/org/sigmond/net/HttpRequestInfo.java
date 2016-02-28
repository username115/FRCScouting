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



import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestInfo
{

	private HttpUriRequest request_;
	private HttpCallback callback_;
	private Exception exception_;
	private HttpResponse response_;
	private CookieStore cookieStore;
	private String response;
	private Map<String, String> params;

	public HttpRequestInfo(HttpUriRequest request, HttpCallback callback)
	{
		super();
		request_ = request;
		callback_ = callback;
	}

	public HttpUriRequest getRequest()
	{
		return request_;
	}

	public void setRequest(HttpUriRequest request)
	{
		request_ = request;
	}

	public HttpCallback getCallback()
	{
		return callback_;
	}

	public void setCallback(HttpCallback callback)
	{
		callback_ = callback;
	}

	public Exception getException()
	{
		return exception_;
	}

	public void setException(Exception exception)
	{
		exception_ = exception;
	}

	public HttpResponse getResponse()
	{
		return response_;
	}

	public void setResponse(HttpResponse response)
	{
		response_ = response;
	}

	public void requestFinished()
	{
		if (exception_ != null)
		{
			callback_.onError(exception_);
		}
		else
		{
			callback_.onResponse(this);
		}
	}
	
	public void setCookieStore(CookieStore store)
	{
		cookieStore = store;
	}
	
	public CookieStore getCookieStore()
	{
		return cookieStore;
	}
	
	public void setResponseString(String response) {
		this.response = response;
	}
	
	public String getResponseString() {
		return response;
	}
	
	public void setParams(Map<String, String> param) {
		params = param;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
}
