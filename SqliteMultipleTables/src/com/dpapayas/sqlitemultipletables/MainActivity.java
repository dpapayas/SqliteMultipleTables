package com.dpapayas.sqlitemultipletables;

import java.util.List;
import com.dpapayas.sqlitemultipletables.helper.DatabaseHelper;
import com.dpapayas.sqlitemultipletables.model.Film;
import com.dpapayas.sqlitemultipletables.model.Kategori;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DatabaseHelper(getApplicationContext());

		Kategori kategori1 = new Kategori("Action");
		Kategori kategori2 = new Kategori("Horror");
		Kategori kategori3 = new Kategori("Adventure");
		Kategori kategori4 = new Kategori("Drama");

		long kategori1_id = db.createKategori(kategori1);
		long kategori2_id = db.createKategori(kategori2);
		long kategori3_id = db.createKategori(kategori3);
		long kategori4_id = db.createKategori(kategori4);

		Log.d("Kategori Count", "Kategori Count: "
				+ db.getAllKategoris().size());

		Film film1 = new Film("Ketika Jomblo Bertasbih", 0);
		Film film2 = new Film("Petualangan Si Gundul", 0);
		Film film3 = new Film("Transformer", 0);

		Film film4 = new Film("Sadako", 0);
		Film film5 = new Film("3 Hati 2 Dunia 1 Cinta", 0);

		db.createListFilm(film1, new long[] { kategori4_id });
		db.createListFilm(film2, new long[] { kategori3_id });
		db.createListFilm(film3, new long[] { kategori1_id });

		db.createListFilm(film4, new long[] { kategori2_id });
		db.createListFilm(film5, new long[] { kategori4_id });

		Log.e("Film Count", "Film count: " + db.getListFilmCount());

		List<Kategori> allKategoris = db.getAllKategoris();
		for (Kategori kategori : allKategoris) {
			Log.d("Kategori Name", kategori.getName());
		}

		Log.d("Get Films", "Getting All ToDos");

		List<Film> allToDos = db.getAllListFilms();
		for (Film film : allToDos) {
			Log.d("ToDo", film.getName());
		}

		db.closeDB();

	}
}
