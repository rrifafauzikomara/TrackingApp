package com.example.rrifafauzikomara.proyek2.Server;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rrifafauzikomara.proyek2.R;
import com.example.rrifafauzikomara.proyek2.Tracking.UpdateLokasi;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private List<ModelData> mItems ;
    private Context context;
    public AdapterData (Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, final int position) {
        ModelData md  = mItems.get(position);
        holder.tvid.setText(md.getId());
        holder.tvnama.setText(md.getNama());
        holder.tvlng.setText(md.getLng());
        holder.tvlat.setText(md.getLat());
        holder.tvidk.setText(md.getIdk());
        holder.tvdate.setText(md.getDate());
        holder.tvtime.setText(md.getTime());

        holder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UpdateLokasi.class);
                i.putExtra("id_lokasi", mItems.get(position).getId());
                i.putExtra("nama", mItems.get(position).getNama());
                i.putExtra("lng", mItems.get(position).getLng());
                i.putExtra("lat", mItems.get(position).getLat());
                i.putExtra("id_kendaraan", mItems.get(position).getIdk());
                i.putExtra("tgl", mItems.get(position).getDate());
                i.putExtra("time", mItems.get(position).getTime());
                v.getContext().startActivity(i);
            }
        });

        holder.md = md;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HolderData extends RecyclerView.ViewHolder {
        TextView tvid, tvnama,tvlng, tvlat, tvidk, tvdate, tvtime;
        ModelData md;
        CardView cd;
        public  HolderData (View view)
        {
            super(view);
            tvid = view.findViewById(R.id.idValue);
            tvnama = view.findViewById(R.id.namaValue);
            tvlng = view.findViewById(R.id.lngValue);
            tvlat = view.findViewById(R.id.latValue);
            tvidk = view.findViewById(R.id.idkValue);
            tvdate = view.findViewById(R.id.dateValue);
            tvtime = view.findViewById(R.id.timeValue);
            cd = view.findViewById(R.id.cardview);
        }
    }

}