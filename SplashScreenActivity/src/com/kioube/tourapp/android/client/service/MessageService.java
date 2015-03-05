package com.kioube.tourapp.android.client.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.util.Log;

import com.kioube.tourapp.android.client.R;
import com.kioube.tourapp.android.client.service.listener.MessageServiceListener;
import com.kioube.tourapp.android.client.service.response.MessageResponse;

public class MessageService {
	
	/* --- Constants --- */
	
	private static final String LOG_TAG = MessageService.class.getSimpleName();
	
	/* --- Static fields --- */
	
	/* --- Fields --- */
	
	private Context context;
	private MessageServiceListener listener;
	
	/* --- Getters & setters --- */
	
	/**
	 * Gets the MessageService object's context value
	 * 
	 * @return The MessageService object's context value
	 */
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * Gets the MessageService object's listener value
	 * 
	 * @return The MessageService object's listener value
	 */
	public MessageServiceListener getListener() {
		return this.listener;
	}
	
	/* --- .ctors --- */
	
	/**
	 * Constructs a new MessageService object.
	 * 
	 * @param context Application context
	 * @param listener Service listener used to handle complete and error events
	 */
	public MessageService(Context context, MessageServiceListener listener) {
		super();
		
		this.context = context;
		this.listener = listener;
	}
	
	/* --- Class operations --- */
	
	/* --- Object operations --- */
	
	/**
	 * Gets the remote service URL to read data from
	 * 
	 * @return The remote service to read data from
	 */
	private String getServiceUrl() {
		String result = null;
		
		// TODO [2014-03-18, JMEL] For developement purposes, remove this when services are developed
		boolean fakeServices = this.getContext().getResources().getBoolean(R.bool.useFakeServices);
		
		if (fakeServices) {
			Log.d(LOG_TAG, "Using fake messages service because of lazy server side developer.");
			
			result = this.getContext().getString(R.string.fakeMessageService);
		}
		else {
			result = this.getContext().getString(R.string.serviceRoot) + "messages.xml";
		}
		
		return result;
	}
	
	/**
	 * @author xavier
	 * 
	 * Open message.xml in the assets and return a input stream
	 * 
	 * @return
	 * @throws IOException
	 */
	private InputStream getServiceStream() throws IOException {
		return this.getContext().getAssets().open("message.xml");
	}
	
	/**
	 * Runs the messages service
	 */
	public void run() {
		
		// Creates the service thread
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				//String urlString = null;
				
				// Deserializes from URL
				Serializer serializer = new Persister(new AnnotationStrategy());
				InputStream stream = null;
				
				MessageResponse response = null;
				
				try {
					//urlString = MessageService.this.getServiceUrl();
					stream = MessageService.this.getServiceStream();
					
					Log.d(LOG_TAG, "Loading messages from 'message.xml'.");
					
					if (stream != null) {
						//URL url = new URL(urlString);
						//InputStream stream = url.openStream();
						
						response = serializer.read(MessageResponse.class, stream);
					}
					
					// Runs the onCompleted event of the listener
					if (MessageService.this.getListener() != null) {
						MessageService.this.getListener().onCompleted(response);
					}
				}
				catch (Exception e) {
					
					// Sends back the exception
					if (MessageService.this.getListener() != null) {
						MessageService.this.getListener().onError(new Exception(
							"Failed to get messages from 'message.xml'.",
							e
						));
					}
				}
			}
		});
		
		// Starts the import process
		thread.start();
	}
	
}
