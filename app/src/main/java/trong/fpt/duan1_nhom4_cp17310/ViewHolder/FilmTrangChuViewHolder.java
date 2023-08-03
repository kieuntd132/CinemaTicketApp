package trong.fpt.duan1_nhom4_cp17310.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.R;

public class FilmTrangChuViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    public TextView  tv_date_film;
    public ImageView imv_film_manager;
    public Button btn_datve;
    private ItemClickListener itemClickListener;

    public FilmTrangChuViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnLongClickListener(this);
        this.imv_film_manager = itemView.findViewById(R.id.imv_film_manager);
        this.btn_datve = itemView.findViewById(R.id.btn_datve);

    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),true);
        return true;
    }
}
