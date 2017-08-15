package nagihan.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cont";
    private static final int DATABASE_VERSION = 1008;
    private static final String TABLE_NAME = "myapp";

    private static final String ID = "_id";
    private static final String TEXT = "contact";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table_create = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TEXT + " TEXT );";

        db.execSQL(table_create);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }




    public void Update(String notes, int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TEXT, notes);
        int i = db.update(TABLE_NAME, cv, ID + "=?", new String[]{String.valueOf(id)});
        Log.d("Database Update", String.valueOf(i));
        db.close();

    }

    public void Delete(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME, ID + "=" + String.valueOf(id), null);

        db.close();
    }

   /* public List<ContactsModel> GetContactsAdd() {

        List<ContactsModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] line = {ID, TEXT};
        Cursor cursor = db.query(TABLE_NAME, line, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ContactsModel friends = new ContactsModel();
            friends.setId(Integer.parseInt(cursor.getString(0)));
            friends.setDisplayName(cursor.getString(1));
            data.add(friends);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }*/
}