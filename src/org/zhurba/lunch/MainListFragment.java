package org.zhurba.lunch;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.Adapter;
import android.widget.ListView;

public abstract class MainListFragment extends ListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor>{
	
	// args
	public static final String ARG_ITEM_RESID = "ItemResID";
	public static final String ARG_FROM_COLUMNS = "FromColumns";
    public static final String ARG_TO_FIELDS = "ToFields";
    public static final String ARG_CONTENT_URI = "ContentUri";
    public static final String ARG_PROJECTION = "Projection";
    public static final String ARG_ID = "_ID";

	SimpleCursorAdapter mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    Bundle args = getArguments();
	    
	    // создаем адаптер и настраиваем список
	    mAdapter = new SimpleCursorAdapter(
	    		getActivity(), 
	    		args.getInt(ARG_ITEM_RESID), 
	    		null, 
	    		args.getStringArray(ARG_FROM_COLUMNS), 
	    		args.getIntArray(ARG_TO_FIELDS), 
	    		Adapter.NO_SELECTION
	    );
	    setListAdapter(mAdapter);	    
	    
	    // инициализируем CursorLoader
		getLoaderManager().initLoader(0, null, this);
		
		ListView listView = getListView();

		listView.setMultiChoiceModeListener((MultiChoiceModeListener) getActivity());//new LunchesMultiChoiceModeListener());
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
	}
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
	    Bundle args = getArguments();
	    
		switch (loaderID) {
        case 0:
            // Returns a new CursorLoader
            return new CursorLoader(
                        getActivity(),   // Parent activity context
                        Uri.parse(args.getString(ARG_CONTENT_URI)),        // Table to query
                        args.getStringArray(ARG_PROJECTION),     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
            );
        default:
            // An invalid id was passed in
            return null;
		}
	}

	public void deleteSelectedItems() {
		Bundle args = getArguments();
		//формируем строку со списком индексов, которые мы удаляем
		String selection = "";
		long[] ids= getListView().getCheckedItemIds();
		for (long id : ids) 
			 selection+= " OR "+args.getString(ARG_ID)+"="+ id;
		//уберем лишнее ИЛИ в начале
		getActivity().getContentResolver().delete(Uri.parse(args.getString(ARG_CONTENT_URI)),selection.substring(4),null);
	}

	public abstract void addItem(String aItemName);
}