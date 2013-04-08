package org.zhurba.lunch;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

public class LunchesFragment extends MainListFragment {
	
	public LunchesFragment(){
		super();
		
		Bundle args = new Bundle();
		args.putString(MainListFragment.ARG_ID,LunchContract.Lunches._ID);
		args.putInt(MainListFragment.ARG_ITEM_RESID, R.layout.lvitem_lunch);
		args.putString(MainListFragment.ARG_CONTENT_URI,LunchContract.Lunches.CONTENT_URI_STRING);
		args.putStringArray(MainListFragment.ARG_PROJECTION, LunchContract.Lunches.mProjection);
		args.putStringArray(MainListFragment.ARG_FROM_COLUMNS, new String[] {
				LunchContract.Lunches.COLUMN_NAME_PLACE, 
				LunchContract.Lunches.COLUMN_NAME_STATUS
		});
		args.putIntArray(MainListFragment.ARG_TO_FIELDS, new int[] { 
				R.id.tvLunchesItemPlace, 
				R.id.tvLunchesItemStatus 
		});
		setArguments(args);
	}

	@Override
	public void addItem(String aItemName) {
		ContentValues cv = new ContentValues();
		
		cv.put(LunchContract.Lunches.COLUMN_NAME_PLACE, aItemName);
		cv.put(LunchContract.Lunches.COLUMN_NAME_STATUS, aItemName);
		
		getActivity().getContentResolver().insert(Uri.parse(LunchContract.Lunches.CONTENT_URI_STRING), cv);
	}

}
