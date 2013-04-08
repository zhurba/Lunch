package org.zhurba.lunch;

import android.provider.BaseColumns;

public final class LunchContract {

	// Prevents the LunchContract class from being instantiated.
	private LunchContract() {}
    
    public static final String AUTHORITY = "org.zhurba.provider.Lunch";
 
	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Lunch.db";

	public static class Lunches implements BaseColumns {

		public static final String PATH = "lunches";
		public static final String CONTENT_URI_STRING = "content://"+AUTHORITY+"/"+PATH;

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."+AUTHORITY+"."+PATH;
 		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."+AUTHORITY+"."+PATH;
	 	
		public static final String TABLE_NAME = "lunches";
		
	    public static final String COLUMN_NAME_DATE = "date";
	    public static final String COLUMN_NAME_PLACE = "place";
	    public static final String COLUMN_NAME_AMOUNT = "amount";
	    public static final String COLUMN_NAME_STATUS = "status";
	    public static final String COLUMN_NAME_VERDICT = "verdict";

	    public static final String SQL_CREATE_ENTRIES =
	    		"CREATE TABLE " + TABLE_NAME + " (" +
	        		_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
	        		COLUMN_NAME_PLACE + TEXT_TYPE + COMMA_SEP +
	        		COLUMN_NAME_STATUS + TEXT_TYPE +
	        		" )";
		//pUBLIC static final String SQL_DELETE_ENTRIES =
		//    "DROP TABLE IF EXISTS " + TABLE_NAME_ENTRIES;
    
	    public static final String[] mProjection = { 
	    		_ID, 
	    		COLUMN_NAME_PLACE, 
	    		COLUMN_NAME_STATUS
	    };

	}

	public static class Places implements BaseColumns {

		public static final String PATH = "places";
		public static final String CONTENT_URI_STRING = "content://"+AUTHORITY+"/"+PATH;

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."+AUTHORITY+"."+PATH;
 		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."+AUTHORITY+"."+PATH;
	 	
		public static final String TABLE_NAME = "places";
		
	    public static final String COLUMN_NAME_PLACENAME = "placename";
	    public static final String COLUMN_NAME_RATING = "rating";

	    public static final String SQL_CREATE_ENTRIES =
	    		"CREATE TABLE " + TABLE_NAME + " (" +
	        		_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
	        		COLUMN_NAME_PLACENAME + TEXT_TYPE + COMMA_SEP +
	        		COLUMN_NAME_RATING + TEXT_TYPE +
	        		" )";
		//pUBLIC static final String SQL_DELETE_ENTRIES =
		//    "DROP TABLE IF EXISTS " + TABLE_NAME_ENTRIES;
    
	    public static final String[] mProjection = { 
	    		_ID, 
	    		COLUMN_NAME_PLACENAME, 
	    		COLUMN_NAME_RATING
	    };

	}

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
    


}
