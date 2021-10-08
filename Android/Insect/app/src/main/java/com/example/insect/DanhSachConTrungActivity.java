package com.example.insect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.insect.Adapter.ConTrungAdapter;
import com.example.insect.Model.Insect;

import java.util.ArrayList;

public class DanhSachConTrungActivity extends AppCompatActivity {

    SQLiteDatabase database = null;
    Insect insect = null;
    RecyclerView rclConTrung;
    ConTrungAdapter conTrungAdapter;
    ArrayList<Insect> listInsect = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_con_trung);
        anhxa();
        loadData();
    }

    private void anhxa() {
        rclConTrung = findViewById(R.id.rclDanhSachConTrung);
    }

    private void loadData() {
        database = openOrCreateDatabase("insect.sqlite",MODE_PRIVATE,null);
        Cursor cursor = database.query("insect",null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            insect = new Insect(cursor.getInt(0),cursor.getString(1),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            listInsect.add(insect);
        }
        cursor.close();
        database.close();
        conTrungAdapter = new ConTrungAdapter(this,listInsect);
        rclConTrung.setAdapter(conTrungAdapter);
        rclConTrung.setLayoutManager(new LinearLayoutManager(this));
    }
}
