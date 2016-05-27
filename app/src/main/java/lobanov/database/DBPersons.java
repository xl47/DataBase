package lobanov.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBPersons {

    private static final String DATABASE_NAME = "simple.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tablePersons";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRSTNAME = "Firstname";
    private static final String COLUMN_LASTNAME = "Lastname";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_FIRSTNAME = 1;
    private static final int NUM_COLUMN_LASTNAME = 2;

    private SQLiteDatabase mDataBase;

    public DBPersons(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String firstname,String lastname) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FIRSTNAME, firstname);
        cv.put(COLUMN_LASTNAME, lastname);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Person md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_FIRSTNAME, md.get_firstname());
        cv.put(COLUMN_LASTNAME, md.get_lastname());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(md.get_id())});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Person select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String Firstname = mCursor.getString(NUM_COLUMN_FIRSTNAME);
        String Lastname = mCursor.getString(NUM_COLUMN_LASTNAME);
        return new Person(id, Firstname, Lastname);
    }

    public ArrayList<Person> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Person> arr = new ArrayList<Person>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String Firstname = mCursor.getString(NUM_COLUMN_FIRSTNAME);
                String Lastname = mCursor.getString(NUM_COLUMN_LASTNAME);
                arr.add(new Person(id, Firstname, Lastname));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRSTNAME+ " TEXT, " +
                    COLUMN_LASTNAME + " TEXT); ";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}


