package org.zhurba.lunch;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

public class PlacesFragment extends MainListFragment {
	
	public PlacesFragment() {
		super();
		
	    Bundle args = new Bundle();
		args.putString(MainListFragment.ARG_ID,LunchContract.Places._ID);
		args.putInt(MainListFragment.ARG_ITEM_RESID, R.layout.lvitem_place);
		args.putString(MainListFragment.ARG_CONTENT_URI,LunchContract.Places.CONTENT_URI_STRING);
		args.putStringArray(MainListFragment.ARG_PROJECTION, LunchContract.Places.mProjection);
		args.putStringArray(MainListFragment.ARG_FROM_COLUMNS, new String[] {
				LunchContract.Places.COLUMN_NAME_PLACENAME, 
				LunchContract.Places.COLUMN_NAME_RATING
		});
		args.putIntArray(MainListFragment.ARG_TO_FIELDS, new int[] { 
				R.id.tvPlacesItemPlacename, 
				R.id.tvPlacesItemRating 
		});
		setArguments(args);
	}
	
	@Override
	public void addItem(String aItemName) {
		ContentValues cv = new ContentValues();
		
		cv.put(LunchContract.Places.COLUMN_NAME_PLACENAME, aItemName);
		cv.put(LunchContract.Places.COLUMN_NAME_RATING, aItemName);
		
		getActivity().getContentResolver().insert(Uri.parse(LunchContract.Places.CONTENT_URI_STRING), cv);
	}


}
