package example.com.projectone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import example.com.projectone.models.Result;

/**
 * Created by 16545 on 12/12/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABSE_NAME = "movie.db";
    private static final String TABLE_NAME = "fav_movie_table";
    private final String ID = "id";
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String VOTE_AVERAGE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";
    private final String OVERVIEW = "overview";
    private final String CREATE_DATABASE = "CREATE TABLE if not exists " + TABLE_NAME + " (" +
            ID + " integer PRIMARY KEY," +
            TITLE + " text ," +
            RELEASE_DATE + " text ," +
            MOVIE_POSTER + " text ," +
            VOTE_AVERAGE + " text ," +
            VOTE_COUNT + " text ," +
            OVERVIEW + " text" +
            " );";

    //call this constructor to create the database.
    public DataBaseHelper(Context context) {
        super(context, DATABSE_NAME, null, 1);
    }

    //not using this constructor
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // create a table when onCreate is called.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(Result result){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, result.getResultId());
        contentValues.put(TITLE, result.getTitle());
        contentValues.put(RELEASE_DATE,result.getRelease_date() );
        contentValues.put(MOVIE_POSTER, result.getPoster_path() );
        contentValues.put(VOTE_AVERAGE, result.getVote_average());
        contentValues.put(VOTE_COUNT, result.getVote_count());
        contentValues.put(OVERVIEW, result.getOverview());
        return db.insert(TABLE_NAME, null, contentValues) > 0 ? true : false;
    }

    public List<Result> getAlData(){
        List<Result> movieResults = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        while(result.moveToNext()){
//            movieResults.add(new Result(result.getString(3), false, result.getString(6), result.getString(2),
//                    new ArrayList<Integer>(), Integer.parseInt(result.getString(0)),
//                    "", "", result.getString(1), "", 0.0, Integer.parseInt(result.getString(5)), false, Double.parseDouble(result.getString(4))));
        }
        return movieResults;
    }
}
