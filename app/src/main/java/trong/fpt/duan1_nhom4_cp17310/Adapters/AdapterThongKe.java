package trong.fpt.duan1_nhom4_cp17310.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.ThongKeViewHolder;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.TinTucViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.News;
import trong.fpt.duan1_nhom4_cp17310.models.ThongKe;
import trong.fpt.duan1_nhom4_cp17310.views.WebviewActivity;

public class AdapterThongKe extends RecyclerView.Adapter<ThongKeViewHolder> {
    Context context;
    ArrayList<ThongKe> dsThongKe;
    public AdapterThongKe(Context context, ArrayList<ThongKe> dsThongKe) {
        this.context = context;
        this.dsThongKe = dsThongKe;
    }

    @NonNull
    @Override
    public ThongKeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thongke, parent, false);
        return new ThongKeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKeViewHolder holder, int position) {
        ThongKe thongKe = dsThongKe.get(position);
        holder.tv_tenphim_thongke.setText(thongKe.getTenPhim());
        holder.tv_doanhthu_thongke.setText(thongKe.getTongDoanhThu()+ " VNĐ");
    }

    @Override
    public int getItemCount() {
        return dsThongKe.size();
    }

}
