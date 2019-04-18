package sample.com.notesapplication.offline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sample.com.notesapplication.datamodel.Notes;

public class NDataHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_NOTES = "notes";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NOTES = "notes";
    public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
    private static final String CREATE_TABLE_COMMAND = "CREATE TABLE IF NOT EXISTS ";

    public static final String DATA_TYPE_INTEGER = " INTEGER ";
    public static final String DATA_TYPE_TEXT = " TEXT ";
    public static final String DATA_TYPE_INTEGER_PRIMARY = " INTEGER PRIMARY KEY AUTOINCREMENT ";

    public static final String CREATE_TABLE_NOTES = CREATE_TABLE_COMMAND + " " + TABLE_NAME_NOTES + "(" + COLUMN_NAME_ID + DATA_TYPE_INTEGER_PRIMARY + "," +
            COLUMN_NAME_NOTES + DATA_TYPE_TEXT + "," +
            COLUMN_NAME_TIMESTAMP +  " DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";


    public static final String DATABASE_NAME = "notes app";
    public static final int DATABASE_VERSION = 1;


    public NDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTES);
    }

    public long insertNote(String note) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NOTES, note);
        long id = database.insert(TABLE_NAME_NOTES, null, contentValues);

        database.close();
        return id;
    }

    public ArrayList<Notes> getAllNotes() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        ArrayList<Notes> notesArrayList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {


            do {
                if (cursor.moveToFirst()) {
                    Notes notes = new Notes();
                    notes.id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                    notes.notes = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NOTES));
                    notes.timeStamp = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIMESTAMP));
                    notesArrayList.add(notes);
                }

            } while (cursor.moveToNext());
        }
        return notesArrayList;
    }

    public Notes getNotes(long id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_NOTES,
                new String[]{COLUMN_NAME_ID, COLUMN_NAME_NOTES, COLUMN_NAME_TIMESTAMP}, COLUMN_NAME_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        Notes notes = new Notes();
        notes.id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
        notes.notes = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NOTES));
        notes.timeStamp = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIMESTAMP));

        return notes;
    }

    public int updateNotes(Notes notes) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NOTES, notes.notes);
        return sqLiteDatabase.update(TABLE_NAME_NOTES, contentValues,
                COLUMN_NAME_ID + "=?",
                new String[]{String.valueOf(notes.notes)});
    }

    public void deleteNotes(Notes notes) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME_NOTES,
                COLUMN_NAME_ID + "=?",
                new String[]{String.valueOf(notes.notes)});
        sqLiteDatabase.close();
    }


}
