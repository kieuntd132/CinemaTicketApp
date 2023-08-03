package trong.fpt.duan1_nhom4_cp17310.Adapters;

import static trong.fpt.duan1_nhom4_cp17310.R.drawable.custom_suatxem2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListenerSuatXem;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.SuatXemViewHolder;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.TinTucViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.News;
import trong.fpt.duan1_nhom4_cp17310.models.SuatXem;
import trong.fpt.duan1_nhom4_cp17310.views.TicketsActivity;
import trong.fpt.duan1_nhom4_cp17310.views.WebviewActivity;

public class AdapterSuatXem extends RecyclerView.Adapter<SuatXemViewHolder> {
    ArrayList<SuatXem> dsSuatXem;
    ItemClickListenerSuatXem itemClickListenerSuatXem;
    private int lastCheckedPosition = -1;
    public AdapterSuatXem(ArrayList<SuatXem> dsSuatXem, ItemClickListenerSuatXem itemClickListenerSuatXem) {
        this.dsSuatXem = dsSuatXem;
        this.itemClickListenerSuatXem = itemClickListenerSuatXem;
    }

    @NonNull
    @Override
    public SuatXemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suatxem, parent, false);
        return new SuatXemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuatXemViewHolder holder, int position) {
        SuatXem suatXem = dsSuatXem.get(position);
        holder.radioButton.setText(suatXem.getSuatXem());
        holder.radioButton.setChecked(position == lastCheckedPosition);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = holder.getAdapterPosition();
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);
                itemClickListenerSuatXem.onClick(suatXem.getSuatXem());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dsSuatXem.size();
    }

}
