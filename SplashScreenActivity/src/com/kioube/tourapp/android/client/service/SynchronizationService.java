package com.kioube.tourapp.android.client.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.kioube.tourapp.android.client.R;
import com.kioube.tourapp.android.client.domain.Configuration;
import com.kioube.tourapp.android.client.domain.Coordinate;
import com.kioube.tourapp.android.client.domain.GeographicalArea;
import com.kioube.tourapp.android.client.domain.Theme;
import com.kioube.tourapp.android.client.domain.TourItem;
import com.kioube.tourapp.android.client.domain.TourItemImage;
import com.kioube.tourapp.android.client.helper.SessionManager;
import com.kioube.tourapp.android.client.persistence.repository.ConfigurationRepository;
import com.kioube.tourapp.android.client.persistence.repository.CoordinateRepository;
import com.kioube.tourapp.android.client.persistence.repository.GeographicalAreaRepository;
import com.kioube.tourapp.android.client.persistence.repository.ThemeRepository;
import com.kioube.tourapp.android.client.persistence.repository.TourItemImageRepository;
import com.kioube.tourapp.android.client.persistence.repository.TourItemRepository;
import com.kioube.tourapp.android.client.service.listener.ServiceListener;
import com.kioube.tourapp.android.client.service.response.SynchronizationResponse;
//import com.kioube.tourapp.android.client.ui.Date;

public class SynchronizationService {
	
	/* --- Constants --- */
	
	private static final String LOG_TAG = SynchronizationService.class.getSimpleName();
	
	/* --- Static fields --- */
	
	/* --- Fields --- */
	
	private Context context;
	
	private ServiceListener listener;
	
	private ConfigurationRepository configurationRepository;
	private CoordinateRepository coordinateRepository;
	private GeographicalAreaRepository geographicalAreaRepository;
	private TourItemImageRepository imageRepository;
	private ThemeRepository themeRepository;
	private TourItemRepository tourItemRepository;
	private TourItemImageRepository tourItemImageRepository;
	
	private Boolean isSync = false;
	
	private boolean isConnectionAvailable;
	
	/* --- Getters & setters --- */
	
	/**
	 * @author xavier
	 * 
	 * Get the connection available value
	 * 
	 * @return boolean
	 */
	public boolean getIsConnectionAvailable() {
		return isConnectionAvailable;
	}
	
	
	/**
	 * @author xavier
	 * 
	 * Set the connection available value
	 * 
	 * @param isConnectionAvailable
	 */
	public void setIsConnectionAvailable(boolean isConnectionAvailable) {
		this.isConnectionAvailable = isConnectionAvailable;
	}

	/**
	 * Gets the SynchronizationService object's context value
	 * 
	 * @return The SynchronizationService object's context value
	 */
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * Gets the SynchronizationService object's configurationRepository value
	 * 
	 * @return The SynchronizationService object's configurationRepository value
	 */
	public ConfigurationRepository getConfigurationRepository() {
		if (this.configurationRepository == null) {
			this.configurationRepository = new ConfigurationRepository(this.context);
		}
		
		return this.configurationRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's coordinateRepository value
	 * 
	 * @return The SynchronizationService object's coordinateRepository value
	 */
	public CoordinateRepository getCoordinateRepository() {
		if (this.coordinateRepository == null) {
			this.coordinateRepository = new CoordinateRepository(this.context);
		}
		
		return this.coordinateRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's geographicalAreaRepository value
	 * 
	 * @return The SynchronizationService object's geographicalAreaRepository value
	 */
	public GeographicalAreaRepository getGeographicalAreaRepository() {
		if (this.geographicalAreaRepository == null) {
			this.geographicalAreaRepository = new GeographicalAreaRepository(this.context);
		}
		
		return this.geographicalAreaRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's imageRepository value
	 * 
	 * @return The SynchronizationService object's imageRepository value
	 */
	public TourItemImageRepository getImageRepository() {
		if (this.imageRepository == null) {
			this.imageRepository = new TourItemImageRepository(this.context);
		}
		
		return this.imageRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's themeRepository value
	 * 
	 * @return The SynchronizationService object's themeRepository value
	 */
	public ThemeRepository getThemeRepository() {
		if (this.themeRepository == null) {
			this.themeRepository = new ThemeRepository(this.context);
		}
		
		return this.themeRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's tourItemRepository value
	 * 
	 * @return The SynchronizationService object's tourItemRepository value
	 */
	public TourItemRepository getTourItemRepository() {
		if (this.tourItemRepository == null) {
			this.tourItemRepository = new TourItemRepository(this.context);
		}
		
		return this.tourItemRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's tourItemImageRepository value
	 * 
	 * @return The SynchronizationService object's tourItemRepository value
	 */
	public TourItemImageRepository getTourItemImageRepository() {
		if (this.tourItemImageRepository == null) {
			this.tourItemImageRepository = new TourItemImageRepository(this.context);
		}
		
		return this.tourItemImageRepository;
	}
	
	/**
	 * Gets the SynchronizationService object's listener value
	 * 
	 * @return The SynchronizationService object's listener value
	 */
	public ServiceListener getListener() {
		return this.listener;
	}
	
	/* --- .ctors --- */
	
	/**
	 * Constructs a new SynchronizationService object.
	 * 
	 * @param context
	 * @param loadingMessage
	 */
	public SynchronizationService(Context context, ServiceListener listener) {
		super();
		
		this.context = context;
		this.listener = listener;
	}
	
	/**
	 * @author xavier
	 * 
	 * Constructs a new SynchronizationService object
	 * 
	 * @param context
	 * @param listener
	 * @param isConnectionAvailable
	 */
	public SynchronizationService(Context context, ServiceListener listener, boolean isConnectionAvailable) {
		super();
		
		this.context = context;
		this.listener = listener;
		this.isConnectionAvailable = isConnectionAvailable;
	}
	
	/* --- Class operations --- */
	
	/* --- Object operations --- */
	
	/**
	 * Gets the remote service URL to read data from
	 * 
	 * @return The remote service to read data from
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressLint("SimpleDateFormat")
	private String getServiceUrl() throws UnsupportedEncodingException {
		String result = null;
		
		// TODO [2014-03-18, JMEL] For developement purposes, remove this when services are developed
		boolean fakeServices = this.getContext().getResources().getBoolean(R.bool.useFakeServices);
		
		if (fakeServices) {
			Log.d(LOG_TAG, "Using fake synchronization services because of lazy server side developer.");
			
			result = this.getContext().getString(R.string.fakeFirstSyncService);
			
			/**
			 * The following part will do a full sync on first start 
			 * then an update on second launch
			 * then a delete on third launch
			 */
			/*
			// Simulates a first load
			if (this.getConfigurationRepository().getCountAll() == 0) {
				result = this.getContext().getString(R.string.fakeFirstSyncService);
			}
			
			// Simulates an update on second launch
			else if (this.getThemeRepository().getCountAll() == 3) {
				result = this.getContext().getString(R.string.fakeUpdateSyncService);
			}
			
			// Simulates a delete on third launch
			else {
				result = this.getContext().getString(R.string.fakeDeleteSyncService);
			}
			*/
		}
		else {
			String serviceRoot = this.getContext().getString(R.string.serviceRoot) + "synchronize.xml";
			
			SessionManager sessionManager = new SessionManager(this.getContext());
			
			// First pull
			if (sessionManager.getLastSynchronizationDate().getTime() == 0) {
				result = serviceRoot;
			}
			
			// Update
			else {
				String date = URLEncoder.encode(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sessionManager.getLastSynchronizationDate()) + " GMT",
					"UTF-8");
				
				result = String.format("%s?date=%s", serviceRoot, date);
			}
			
			sessionManager.setLastSynchronizationDate();			
		}
		
		return result;
	}
	
	/**
	 * @author xavier
	 * 
	 * Open a file in the asset
	 * 
	 * @return InputStream
	 * @throws IOException
	 * 
	 */
	private InputStream getServiceStream() throws IOException {
		
		return this.getContext().getAssets().open("synchronize.xml");
	}
	
	/**
	 * Runs the synchronization service
	 */
	public void run() {
		
		// Creates the import thread
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String urlString = null;
				InputStream stream = null;
				
				// Deserializes from URL
				Serializer serializer = new Persister(new AnnotationStrategy());
				
				SynchronizationResponse response = null;
				
				try {
					if(SynchronizationService.this.getIsConnectionAvailable() && !SynchronizationService.this.isSync) {
						urlString = SynchronizationService.this.getServiceUrl();

						if (urlString != null) {
							URL url = new URL(urlString);
							stream = url.openStream();
							Log.d(LOG_TAG, url.toString());
						}
					}
					else {
						stream = SynchronizationService.this.getServiceStream();
					}
					
					Log.d(LOG_TAG, "Synchronizing from 'synchronize.xml'.");

					response = serializer.read(SynchronizationResponse.class, stream);
					
					// Save data
					if (response != null) {
						
						SynchronizationService.this.persistGeographicalAreaList(response.getGeographicalAreaList());
						SynchronizationService.this.persistThemeList(response.getThemeList());
						SynchronizationService.this.persistTourItemList(response.getTourItemList());
						SynchronizationService.this.persistTourItemImageList(response.getTourItemList());
						SynchronizationService.this.persistCoordinateList(response.getCoordinateList());
						SynchronizationService.this.persistConfigurationList(response.getConfigurationList());
					}
					else {
						throw new Exception("Service response is null using 'synchronize.xml'.");
					}
					
					// Runs the onCompleted event of the listener
					if (SynchronizationService.this.getListener() != null) {
						SynchronizationService.this.getListener().onCompleted();
					}
				}
				catch (Exception e) {
					
					// Sends back the exception
					if (SynchronizationService.this.getListener() != null) {
						SynchronizationService.this.getListener().onError(new Exception(
							"Failed to synchronize data from 'synchronize.xml'.",
							e
							));
					}
				}
			}
		});
		
		// Starts the import process
		thread.start();
	}
	
	/**
	 * Persists the imported GeographicalArea list by managing creation, update and deletion
	 * 
	 * @param geographicalAreaList A list of persistent items to save
	 */
	private void persistGeographicalAreaList(List<GeographicalArea> geographicalAreaList) {
		if (geographicalAreaList != null) {
			for (GeographicalArea geographicalArea : geographicalAreaList) {
				
				// Deletion
				if (geographicalArea.getDeletedOn().after(new GregorianCalendar(1970, 1, 1).getTime()))
				{
					this.getGeographicalAreaRepository().delete(geographicalArea);
				}
				
				// Save
				else {
					this.getGeographicalAreaRepository().createOrUpdate(geographicalArea);
				}
			}
		}
	}
	
	/**
	 * Persists the imported Theme list by managing creation, update and deletion
	 * 
	 * @param themeList A list of persistent items to save
	 */
	private void persistThemeList(List<Theme> themeList) {
		if (themeList != null) {
			for (Theme theme : themeList) {
				
				// Deletion
				if (theme.getDeletedOn().after(new GregorianCalendar(1970, 1, 1).getTime()))
				{
					this.getThemeRepository().delete(theme);
				}
				
				// Save
				else {
					this.getThemeRepository().createOrUpdate(theme);
				}
				
			}
		}
	}
	
	/**
	 * Persists the imported TourItem list by managing creation, update and deletion
	 * 
	 * @param TourItemList A list of persistent items to save
	 */
	private void persistTourItemList(List<TourItem> tourItemList) {
		
		if (tourItemList != null) {
			for (TourItem tourItem : tourItemList) {
				
				// Deletion
				if (tourItem.getDeletedOn().after(new GregorianCalendar(1970, 1, 1).getTime()))
				{
					this.getTourItemRepository().delete(tourItem);
				}
				
				// Save
				else {
					
					// If the tourItem already exists and is bookmarked, mark the update as bookmarked
					TourItem previousTourItem = this.getTourItemRepository().getById(tourItem.getId());
					
					if (previousTourItem != null) {
						tourItem.setBookmarked(previousTourItem.isBookmarked());
					}
					
					this.getTourItemRepository().createOrUpdate(tourItem);
				}
			}
		}
	}
	
	/**
	 * Persists the imported TourItemImage list by managing creation, update and deletion
	 * 
	 * @param tourItemList A list of persistent items to save
	 */
	private void persistTourItemImageList(List<TourItem> tourItemList) {
		if (tourItemList != null) {
			for (TourItem tourItem : tourItemList) {
				
				// Deletion
				if (tourItem.getDeletedOn().after(new GregorianCalendar(1970, 1, 1).getTime()))
				{
					List<TourItemImage> imageList = this.getTourItemImageRepository().getByTourItem(tourItem);
					
					// Deletes all images
					for (TourItemImage image : imageList) {
						this.getTourItemImageRepository().delete(image);
					}
				}
				
				// Save
				else {
					
					if (tourItem.getImageList() != null) {
						for (TourItemImage image : tourItem.getImageList()) {
							image.setTourItem(tourItem);
							this.getTourItemImageRepository().createOrUpdate(image);
							Log.d(LOG_TAG, "Saved image ID = " + image.getId() + " for tourItem ID = " + image.getTourItem().getId() + " (" + image.getTourItem().getName() + ")");
						}
					}
				}
				
			}
		}
	}
	
	/**
	 * Persists the imported Coordinate list by managing creation, update and deletion
	 * 
	 * @param coordinateAreaList A list of persistent items to save
	 */
	private void persistCoordinateList(List<Coordinate> coordinateList) {
		if (coordinateList != null) {
			for (Coordinate coordinate : coordinateList) {
				
				// Deletion
				if (coordinate.getDeletedOn().after(new GregorianCalendar(1970, 1, 1).getTime()))
				{
					this.getCoordinateRepository().delete(coordinate);
				}
				
				// Save
				else {
					this.getCoordinateRepository().createOrUpdate(coordinate);
				}
			}
		}
			
	}
	
	/**
	 * Persists the imported Configuration list by managing creation, update and deletion
	 * 
	 * @param configurationList A list of persistent items to save
	 */
	private void persistConfigurationList(List<Configuration> configurationList) {
		if (configurationList != null) {
			for (Configuration configuration : configurationList) {
				
				// Tries to get the configuration if exists
				Configuration previousConfiguration = this.getConfigurationRepository().getByKey(configuration.getKey());
				
				// If exists, updates it
				if (previousConfiguration != null) {
					previousConfiguration.setValue(configuration.getValue());
					this.getConfigurationRepository().update(previousConfiguration);
				}
				
				// Else create it
				else {
					this.getConfigurationRepository().create(configuration);
					
				}
			}
		}
	}

	/**
	 * @author xavier
	 * 
	 * Retourne la dernière date de modification du fichier xml
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Date getLastUpdateDate() throws UnsupportedEncodingException {
		// TODO Vérifier la fonction
		File file = new File(this.getServiceUrl());
		
		Date d = new Date(file.lastModified());;
		Log.d(LOG_TAG, d.toGMTString());
		return d;
	}
	
	
	public void setIsSync(Boolean isSync) {
		this.isSync= isSync;
	}
}
