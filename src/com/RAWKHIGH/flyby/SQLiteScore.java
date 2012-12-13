package com.RAWKHIGH.flyby;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SQLiteScore extends Activity {

private SQLiteAdapter mySQLiteAdapter;
private Context context = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView listContent = (ListView)findViewById(R.id.ScoreList);

        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        mySQLiteAdapter.deleteAll();

        mySQLiteAdapter.insert("Stephen", "0");
        mySQLiteAdapter.insert("Ryan", "82");
        mySQLiteAdapter.insert("Andy", "169");
       
        mySQLiteAdapter.close();

        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToRead();

        Cursor cursor = mySQLiteAdapter.queueAll();
        startManagingCursor(cursor);

        String[] columns = new String[]{MyConstants.PLAYER_NAME, MyConstants.SCORE};
        int[] to = new int[]{R.id.listItem , R.id.quantity};

        SimpleCursorAdapter cursorAdapter =
        	new SimpleCursorAdapter(this, R.layout.row, cursor, columns, to);
        listContent.setAdapter(cursorAdapter);     
        mySQLiteAdapter.close();
    
    } // end onCreate
} // SQLiteScore
