package org.zhurba.lunch;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class LunchContentProvider extends ContentProvider {

	private LunchDbHelper mDBHelper;
	private SQLiteDatabase mDB;

	private static final int UM_LUNCHES = 1;
	private static final int UM_LUNCHES_ID = 2;
	private static final int UM_PLACES = 3;
	private static final int UM_PLACES_ID = 4;

	// описание и создание UriMatcher
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(LunchContract.AUTHORITY, LunchContract.Lunches.PATH, UM_LUNCHES);
		uriMatcher.addURI(LunchContract.AUTHORITY, LunchContract.Lunches.PATH + "/#", UM_LUNCHES_ID);
		uriMatcher.addURI(LunchContract.AUTHORITY, LunchContract.Places.PATH, UM_PLACES);
		uriMatcher.addURI(LunchContract.AUTHORITY, LunchContract.Places.PATH + "/#", UM_PLACES_ID);
	}
	
	
	@Override
	public boolean onCreate() {
		mDBHelper = new LunchDbHelper(getContext());
	    return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		int match = uriMatcher.match(uri);
		
		// установим SortOrder & Selection
		switch (match) {
		case UM_LUNCHES: // общий Uri
		case UM_PLACES:
			// если сортировка не указана, ставим свою - по ID
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = BaseColumns._ID + " ASC";
			}
			break;
		case UM_LUNCHES_ID: // Uri с ID
		case UM_PLACES_ID:
			String id = uri.getLastPathSegment();
			// добавляем ID к условию выборки
			if (TextUtils.isEmpty(selection)) {
				selection = BaseColumns._ID + " = " + id;
			} else {
				selection = selection + " AND " + BaseColumns._ID + " = " + id;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		
		// установим TableName & ContentUri
		String table_name;
		//Uri content_uri;
		switch (match) {
		case UM_LUNCHES: 
		case UM_LUNCHES_ID:
			table_name = LunchContract.Lunches.TABLE_NAME;
			//content_uri = LunchContract.Lunches.CONTENT_URI;
			break;
		case UM_PLACES: 
		case UM_PLACES_ID:
			table_name = LunchContract.Places.TABLE_NAME;
			//content_uri = LunchContract.Places.CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		
		mDB = mDBHelper.getWritableDatabase();
		Cursor cursor = mDB.query(table_name, projection, selection,selectionArgs, null, null, sortOrder);
		// просим ContentResolver уведомлять этот курсор 
	    // об изменениях данных в CONTENT_URI
	    cursor.setNotificationUri(getContext().getContentResolver(),uri);
	    return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		String table_name;

		int match = uriMatcher.match(uri);
		switch(match) {
		case UM_LUNCHES:
			table_name = LunchContract.Lunches.TABLE_NAME;
			break;
		case UM_PLACES:
			table_name = LunchContract.Places.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		mDB = mDBHelper.getWritableDatabase();
		long rowID = mDB.insert(table_name, null, values);
		Uri resultUri = ContentUris.withAppendedId(uri, rowID);
		// уведомляем ContentResolver, что данные по адресу resultUri изменились
		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table_name;
		String id;
		
		switch (uriMatcher.match(uri)) {
		case UM_LUNCHES:
			table_name = LunchContract.Lunches.TABLE_NAME;
			break;
		case UM_LUNCHES_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = LunchContract.Lunches._ID + " = " + id;
			} else {
				selection = selection + " AND " + LunchContract.Lunches._ID + " = " + id;
			}
			table_name = LunchContract.Lunches.TABLE_NAME;
			break;
		case UM_PLACES:
			table_name = LunchContract.Places.TABLE_NAME;
			break;
		case UM_PLACES_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = LunchContract.Lunches._ID + " = " + id;
			} else {
				selection = selection + " AND " + LunchContract.Places._ID + " = " + id;
			}
			table_name = LunchContract.Places.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
	    mDB = mDBHelper.getWritableDatabase();
	    int count = mDB.delete(table_name, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String table_name;
		String id;
		
		switch (uriMatcher.match(uri)) {
		case UM_LUNCHES:
			table_name = LunchContract.Lunches.TABLE_NAME;
			break;
		case UM_LUNCHES_ID:
	    	id = uri.getLastPathSegment();
	    	if (TextUtils.isEmpty(selection)) {
	    		selection = LunchContract.Lunches._ID + " = " + id;
	    	} else {
	    		selection = selection + " AND " + LunchContract.Lunches._ID + " = " + id;
	    	}
	    	table_name = LunchContract.Lunches.TABLE_NAME;
	    	break;
		case UM_PLACES:
			table_name = LunchContract.Places.TABLE_NAME;
			break;
		case UM_PLACES_ID:
	    	id = uri.getLastPathSegment();
	    	if (TextUtils.isEmpty(selection)) {
	    		selection = LunchContract.Places._ID + " = " + id;
	    	} else {
	    		selection = selection + " AND " + LunchContract.Places._ID + " = " + id;
	    	}
	    	table_name = LunchContract.Places.TABLE_NAME;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		mDB = mDBHelper.getWritableDatabase();
		int count = mDB.update(table_name, values, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case UM_LUNCHES:
			return LunchContract.Lunches.CONTENT_TYPE;
		case UM_LUNCHES_ID:
			return LunchContract.Lunches.CONTENT_ITEM_TYPE;
		case UM_PLACES:
			return LunchContract.Places.CONTENT_TYPE;
		case UM_PLACES_ID:
			return LunchContract.Places.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	private class LunchDbHelper extends SQLiteOpenHelper {

		public LunchDbHelper(Context context) {
			super(context, LunchContract.DATABASE_NAME, null, LunchContract.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(LunchContract.Lunches.SQL_CREATE_ENTRIES);
			db.execSQL(LunchContract.Places.SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// тут должен быть код который конвертирует базу если она изменилась
		}

	}

}
