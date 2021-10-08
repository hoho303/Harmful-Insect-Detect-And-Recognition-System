package com.example.insect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insect.Model.Insect;
import com.example.insect.R;
import com.example.insect.Retrofit2.APIUtils;
import com.example.insect.ThongTinActivity;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class ConTrungAdapter extends RecyclerView.Adapter<ConTrungAdapter.ViewHolder> {

    ArrayList<Insect> listIndect;
    Context context;

    public ConTrungAdapter(Context context, ArrayList<Insect> listIndect) {
        this.listIndect = listIndect;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_danh_sach_con_trung,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtTenContrung.setText(listIndect.get(position).getTenConTrung());
        holder.txtDacDiem.setText(listIndect.get(position).getDacDiem());
        Picasso.get().load(APIUtils.baseUrl+"getImage?id="+listIndect.get(position).getId()+"&&file=1").into(holder.imgInsect);
        holder.frConTrung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThongTinActivity.class);
                Log.d("AAA",listIndect.get(position).getId()+"");
                intent.putExtra("ID",(listIndect.get(position).getId()-1)+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listIndect.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgInsect;
        FrameLayout frConTrung;
        TextView txtTenContrung;
        TextView txtDacDiem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgInsect = itemView.findViewById(R.id.imgInsectThongTin);
            frConTrung = itemView.findViewById(R.id.frConTrung);
            txtTenContrung = itemView.findViewById(R.id.txtNameThongTin);
            txtDacDiem = itemView.findViewById(R.id.txtDacDiemThongTin);
        }
    }
}
