package com.superdild.app.newweatherapp;

import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivityForecast extends AppCompatActivity implements CustomListAdapter.ListItemClickListener{

    private RecyclerView recycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SQLiteDatabase weatherdatebase;
    Cursor c;
    TextView nome_citta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_forecast);

        nome_citta = findViewById(R.id.nome_citta);
        recycler = findViewById(R.id.recyclerView);
        WeatherDataBaseHelper weatherDataBaseHelper = WeatherDataBaseHelper.getInstance(this);
        weatherdatebase = weatherDataBaseHelper.getReadableDatabase();

        recycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        try {
            c = weatherdatebase.query(WeatherDataBaseContract.WeatherDataBaseEntry.TABLE_NAME, null, null, null, null, null, null);
        }catch (IllegalStateException e){
            e.printStackTrace();
            Log.e("ListActivityForecast", "cursor Illegal STATE EXCEP" + e.toString());
        }
        mAdapter = new CustomListAdapter(c,this,this);
        recycler.setAdapter(mAdapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nomeCitta = sharedPreferences.getString("location",null);
        nome_citta.setText(nomeCitta);

    }

    @Override
    public void onListItemCLick(int postition) {
        Toast.makeText(this, "Clicckato elemento: "+postition,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ListActivityForecast", "ON_DESTROY");
        if (c!=null)c.close();

    }
}
