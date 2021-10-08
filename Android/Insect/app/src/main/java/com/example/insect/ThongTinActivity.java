package com.example.insect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.insect.Model.Insect;
import com.example.insect.Retrofit2.APIUtils;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;


public class ThongTinActivity extends AppCompatActivity {

    Insect insect = null;
    SQLiteDatabase database = null;
    ArrayList<String> arrGioiThieu, arrTacHai, arrCachPhongNgua;
    String idInsect;
    int txtl = 50;
    TextView txtThongTin,txtTenConTrung,txtDiaBan,txtCayGayHai;
    CarouselView carouselViewGioiThieu, carouselViewTacHai, carouselViewCachPhongNgua, carouselViewThongTin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);
        anhXa();
        loadData();
        config();
    }

    private void config() {
        carouselViewGioiThieu.setPageCount(getPageCount(insect.getDacDiem()));
        carouselViewGioiThieu.setViewListener(viewListenerGioiThieu);
        carouselViewTacHai.setPageCount(getPageCount(insect.getTacHai()));
        carouselViewTacHai.setViewListener(viewListenerTacHai);
        carouselViewCachPhongNgua.setPageCount(getPageCount(insect.getCachPhongNgua()));
        carouselViewCachPhongNgua.setViewListener(viewListenerCachPhongNgua);
        carouselViewThongTin.setPageCount(3);
        carouselViewThongTin.setImageListener(imageListener);
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(APIUtils.baseUrl+"getImage?id="+insect.getId()+"&&file="+(position+1)).into(imageView);
        }
    };
    private void loadData() {
        Intent intent = getIntent();
        idInsect = intent.getStringExtra("ID");
        int id=0;
        if (idInsect != null) {
            id = Integer.parseInt(idInsect)+1;
            Log.d("AAA",id+"");
        }
        database = openOrCreateDatabase("insect.sqlite",MODE_PRIVATE,null);
        Cursor cursor = database.query("insect",null,"ID=?",new String[]{id+""},null,null,null);
        while (cursor.moveToNext())
        {
            insect = new Insect(id,cursor.getString(1),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
        }
        cursor.close();
        if (insect!=null) loadThongTin();
    }

    private void loadThongTin() {
        txtTenConTrung.setText("Tên côn trùng: " + insect.getTenConTrung());
        txtDiaBan.setText("Địa bàn hoạt động: " + insect.getDiaBanHoatDong());
        txtCayGayHai.setText("Cây trồng gây hại: " + insect.getCayTrongBiAnhHuong());
    }

    private void anhXa() {
        txtTenConTrung = findViewById(R.id.txtTenConTrung);
        txtCayGayHai = findViewById(R.id.txtLoayCayGayHai);
        txtDiaBan = findViewById(R.id.txtDiaBanHoatDong);
        carouselViewGioiThieu = findViewById(R.id.carouselViewGioiThieu);
        carouselViewTacHai = findViewById(R.id.carouselViewTacHai);
        carouselViewCachPhongNgua = findViewById(R.id.carouselViewCachPhongNgua);
        carouselViewThongTin = findViewById(R.id.carouselViewThongTin);
    }
    private int getPageCount(String s)
    {
        String[] text = s.split(" ");
        if (text.length>=txtl)
        {
            return (text.length/txtl)+1;
        }
        else
            return 1;
    }
    private ArrayList<String> getText(String s)
    {
        String[] text = s.split(" ");
        ArrayList<String> arrThongTin = new ArrayList<>();
        if (text.length>=txtl)
        {
            String t = "";
            for(int i=0;i<text.length;i++)
            {
                t=t+text[i]+" ";
                if((i%txtl==0&&i!=0)||i==text.length-1)
                {
                    arrThongTin.add(t);
                    t="";
                }
            }
        }
        else arrThongTin.add(s);
        return arrThongTin;
    }
    ViewListener viewListenerGioiThieu = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.custom_carousel_thong_tin, null);
            txtThongTin = customView.findViewById(R.id.txtCarouselThongTin);
            try {
                arrGioiThieu = getText(insect.getDacDiem());
                txtThongTin.setText(arrGioiThieu.get(position));
            }
            catch (Exception ignored)
            {

            }
            return customView;
        }
    };
    ViewListener viewListenerTacHai = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.custom_carousel_thong_tin, null);
            txtThongTin = customView.findViewById(R.id.txtCarouselThongTin);
            try {
                arrTacHai = getText(insect.getTacHai());
                txtThongTin.setText(arrTacHai.get(position));
            }
            catch (Exception ignored)
            {

            }
            return customView;
        }
    };
    ViewListener viewListenerCachPhongNgua = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.custom_carousel_thong_tin, null);
            txtThongTin = customView.findViewById(R.id.txtCarouselThongTin);
            try {
                arrCachPhongNgua = getText(insect.getCachPhongNgua());
                txtThongTin.setText(arrCachPhongNgua.get(position));
            }
           catch (Exception ignored)
           {

           }
            return customView;
        }
    };
}
