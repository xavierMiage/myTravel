package com.kioube.tourapp.android.client.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Callable;

import android.util.Log;

import com.kioube.tourapp.android.client.service.SynchronizationService;

/**
 * CallableTask type definition
 * 
 * @author xavier
 *
 */
public class CallableTask implements Callable<Date>{
	private SynchronizationService service;

	  public CallableTask(SynchronizationService service) {
	    this.service = service;
	  }

	  public Date call() throws InterruptedException {
		  Date d = null;
		  URL url;
			try {
				url = new URL(service.getServiceUrl());

				long date = url.openConnection().getHeaderFieldDate("Last-Modified", 0);

				d = new Date(date);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return d;
	  }
}
