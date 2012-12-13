package com.RAWKHIGH.flyby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAdapter {
	
	private static final String SCRIPT_CREATE_DATABASE =
		"CREATE TABLE " + MyConstants.MYDATABASE_TABLE + " ("
		+ MyConstants.KEY_ID + " INTEGER NOT NULL primary key autoincrement, "
		+ MyConstants.PLAYER_NAME + " VARCHAR NOT NULL, " + MyConstants.SCORE + " VARCHAR NOT NULL);";
	
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;
	
	public SQLiteAdapter(Context c){
		context = c;
	} // End SQLiteAdapter
	
	public SQLiteAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MyConstants.MYDATABASE_NAME, null, MyConstants.MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;	
	} // End SQLiteAdapter openToRead
	
	public SQLiteAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, MyConstants.MYDATABASE_NAME, null, MyConstants.MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;	
	} // End SQLiteAdapter openToWrite
	
	public void close(){
		sqLiteHelper.close();
	} // End close
	
	public long insert(String content, String quantity){	
		ContentValues contentValues = new ContentValues();
		contentValues.put(MyConstants.PLAYER_NAME, content);
		contentValues.put(MyConstants.SCORE, quantity);
		return sqLiteDatabase.insert(MyConstants.MYDATABASE_TABLE, null, contentValues);
	} // End insert
	
	public int deleteAll(){
		return sqLiteDatabase.delete(MyConstants.MYDATABASE_TABLE, null, null);
	} // End deleteAll
	
	public Cursor queueAll(){
		String[] columns = new String[]{MyConstants.KEY_ID, MyConstants.PLAYER_NAME,MyConstants.SCORE};
		Cursor cursor = sqLiteDatabase.query(MyConstants.MYDATABASE_TABLE, columns, 
				null, null, null, null, null);
		
		return cursor;
	} // End Cursor queueAll
	
	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		} // End SQLiteHelper

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SCRIPT_CREATE_DATABASE);
		} // End onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		} // End onUpgrade

	} // End SQLiteHelper

} // End SQLiteAdapter
