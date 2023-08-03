package trong.fpt.duan1_nhom4_cp17310.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.R;

public class ThongKeViewHolder extends RecyclerView.ViewHolder{

    public TextView tv_tenphim_thongke, tv_doanhthu_thongke;

    public ThongKeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_tenphim_thongke = itemView.findViewById(R.id.tv_tenphim_thongke);
        this.tv_doanhthu_thongke = itemView.findViewById(R.id.tv_doanhthu_thongke);
    }

}
