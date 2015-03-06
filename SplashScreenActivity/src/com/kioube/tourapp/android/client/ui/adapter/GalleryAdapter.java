package com.kioube.tourapp.android.client.ui.adapter;

import java.util.List;

import com.kioube.tourapp.android.client.R;
import com.kioube.tourapp.android.client.domain.TourItemImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter { 
	
    private Context mContext;
    private List<TourItemImage> objets;

    public GalleryAdapter(Context context, List<TourItemImage> objects) 
    {
        this.mContext = context;
        this.objets = objects;
    }

    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    @SuppressWarnings("deprecation")
	public View getView(int index, View view, ViewGroup viewGroup) 
    {
    	TourItemImage item = this.objets.get(index);
		
		// Get required views
		ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);

		// Sets item thumbnail
		if (item.getByteCode() != null && item.getByteCode().length() > 0) {
			
			String imageBase64 = item.getByteCode();
			
			byte[] imageData = Base64.decode(imageBase64, Base64.NO_WRAP | Base64.NO_PADDING | Base64.URL_SAFE);
			Bitmap bitmap = BitmapFactory.decodeByteArray(imageData , 0, imageData.length);
			
			thumbnailView.setImageBitmap(bitmap);
			thumbnailView.setLayoutParams(new Gallery.LayoutParams(200, 200));
		    
			thumbnailView.setScaleType(ImageView.ScaleType.FIT_XY);
		}
        return thumbnailView;
    }
}
