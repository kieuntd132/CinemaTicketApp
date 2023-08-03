package trong.fpt.duan1_nhom4_cp17310.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import trong.fpt.duan1_nhom4_cp17310.R;

public class BookedTicketsViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_tenphim,tv_tentaikhoan,tv_suatxem,tv_ngayxemphim,tv_maghe,tv_giave;

    public BookedTicketsViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_tenphim = itemView.findViewById(R.id.tv_tenphim);
        tv_tentaikhoan = itemView.findViewById(R.id.tv_tentaikhoan);
        tv_suatxem = itemView.findViewById(R.id.tv_suatxem);
        tv_ngayxemphim = itemView.findViewById(R.id.tv_ngayxemphim);
        tv_maghe = itemView.findViewById(R.id.tv_maghe);
        tv_giave = itemView.findViewById(R.id.tv_giave);

    }
}
