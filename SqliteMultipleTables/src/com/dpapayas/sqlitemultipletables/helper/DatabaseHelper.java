package com.dpapayas.sqlitemultipletables.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




import com.dpapayas.sqlitemultipletables.model.Film;
import com.dpapayas.sqlitemultipletables.model.Kategori;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "ListFilm";

	private static final String TABLE_FILM = "film";
	private static final String TABLE_KATEGORI = "kategori";
	private static final String TABLE_FILM_KATEGORI = "film_kategori";

	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	private static final String KEY_FILM = "film";
	private static final String KEY_STATUS = "status";

	private static final String KEY_KATEGORI_NAME = "kategori_name";

	private static final String KEY_FILM_ID = "film_id";
	private static final String KEY_KATEGORI_ID = "kategori_id";
	
	SQLiteDatabase db;

	private static final String CREATE_TABLE_FILM = "CREATE TABLE "
			+ TABLE_FILM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FILM
			+ " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
			+ " DATETIME" + ")";

	private static final String CREATE_TABLE_KATEGORI = "CREATE TABLE " + TABLE_KATEGORI
			+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_KATEGORI_NAME + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	private static final String CREATE_TABLE_FILM_KATEGORI = "CREATE TABLE "
			+ TABLE_FILM_KATEGORI + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_FILM_ID + " INTEGER," + KEY_KATEGORI_ID + " INTEGER,"
			+ KEY_CREATED_AT + " DATETIME" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_FILM);
		db.execSQL(CREATE_TABLE_KATEGORI);
		db.execSQL(CREATE_TABLE_FILM_KATEGORI);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORI);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILM_KATEGORI);

		// create new tables
		onCreate(db);
	}
	
	public long createListFilm(Film film, long[] kategori_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FILM, film.getName());
		values.put(KEY_STATUS, film.getStatus());
		values.put(KEY_CREATED_AT, getDateTime());

		long film_id = db.insert(TABLE_FILM, null, values);
		
	    for (long kategori_id : kategori_ids) {
	        createFilmKategori(film_id, kategori_id);
	    }


		return film_id;
	}
	
	public boolean isFilmHasData()
	{
		Cursor cursor = db.query(TABLE_FILM, new String[] { "id" }, null,
				null, null, null, null);
		return (cursor.getCount() > 0) ? true : false;
	}
	
	public Film getFilm(long film_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_FILM + " WHERE "
				+ KEY_ID + " = " + film_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Film td = new Film();
		td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		td.setName((c.getString(c.getColumnIndex(KEY_FILM))));
		td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

		return td;
	}

	/**
	 * getting all film
	 * */
	public List<Film> getAllListFilms() {
		List<Film> film = new ArrayList<Film>();
		String selectQuery = "SELECT  * FROM " + TABLE_FILM;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				Film td = new Film();
				td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				td.setName((c.getString(c.getColumnIndex(KEY_FILM))));
				td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				film.add(td);
			} while (c.moveToNext());
		}

		return film;
	}

	public List<Film> getAllListFilmsByKategori(String kategori_name) {
		List<Film> film = new ArrayList<Film>();

		String selectQuery = "SELECT  * FROM " + TABLE_FILM + " td, "
				+ TABLE_KATEGORI + " tg, " + TABLE_FILM_KATEGORI + " tt WHERE tg."
				+ KEY_KATEGORI_NAME + " = '" + kategori_name + "'" + " AND tg." + KEY_ID
				+ " = " + "tt." + KEY_KATEGORI_ID + " AND td." + KEY_ID + " = "
				+ "tt." + KEY_FILM_ID;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Film td = new Film();
				td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				td.setName((c.getString(c.getColumnIndex(KEY_FILM))));
				td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				// adding to film list
				film.add(td);
			} while (c.moveToNext());
		}

		return film;
	}

	/*
	 * getting film count
	 */
	public int getListFilmCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FILM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a film
	 */
	public int updateListFilm(Film film) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FILM, film.getName());
		values.put(KEY_STATUS, film.getStatus());

		// updating row
		return db.update(TABLE_FILM, values, KEY_ID + " = ?",
				new String[] { String.valueOf(film.getId()) });
	}

	/*
	 * Deleting a film
	 */
	public void deleteListFilm(long tado_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FILM, KEY_ID + " = ?",
				new String[] { String.valueOf(tado_id) });
	}

	public long createKategori(Kategori kategori) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_KATEGORI_NAME, kategori.getName());
		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long kategori_id = db.insert(TABLE_KATEGORI, null, values);

		return kategori_id;
	}

	public List<Kategori> getAllKategoris() {
		List<Kategori> kategori = new ArrayList<Kategori>();
		String selectQuery = "SELECT  * FROM " + TABLE_KATEGORI;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Kategori t = new Kategori();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				t.setName(c.getString(c.getColumnIndex(KEY_KATEGORI_NAME)));

				// adding to kategori list
				kategori.add(t);
			} while (c.moveToNext());
		}
		return kategori;
	}

	/*
	 * Updating a kategori
	 */
	public int updateKategori(Kategori kategori) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_KATEGORI_NAME, kategori.getName());

		// updating row
		return db.update(TABLE_KATEGORI, values, KEY_ID + " = ?",
				new String[] { String.valueOf(kategori.getId()) });
	}

	/*
	 * Deleting a kategori
	 */
	public void deleteKategori(Kategori kategori, boolean should_delete_all_kategori_film) {
		SQLiteDatabase db = this.getWritableDatabase();

		if (should_delete_all_kategori_film) {
			// get all film under this kategori
			List<Film> allKategoriListFilms = getAllListFilmsByKategori(kategori.getName());

			// delete all film
			for (Film film : allKategoriListFilms) {
				// delete film
				deleteListFilm(film.getId());
			}
		}

		// now delete the kategori
		db.delete(TABLE_KATEGORI, KEY_ID + " = ?",
				new String[] { String.valueOf(kategori.getId()) });
	}

	public long createFilmKategori(long film_id, long kategori_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FILM_ID, film_id);
		values.put(KEY_KATEGORI_ID, kategori_id);
		values.put(KEY_CREATED_AT, getDateTime());

		long id = db.insert(TABLE_FILM_KATEGORI, null, values);

		return id;
	}

	public int updateNamaKategori(long id, long kategori_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_KATEGORI_ID, kategori_id);

		// updating row
		return db.update(TABLE_FILM, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	public void deleteListFilmKategori(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FILM, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
}
