package trong.fpt.duan1_nhom4_cp17310.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.R;

public class FilmManagerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    public TextView tv_name_film, tv_date_film, tv_price_film;
    public ImageView imv_film_manager;
    public Button btn_delete_film;
    private ItemClickListener itemClickListener;

    public FilmManagerViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnLongClickListener(this);
        this.tv_name_film = itemView.findViewById(R.id.tv_name_film);
        this.tv_date_film = itemView.findViewById(R.id.tv_date_film);
        this.tv_price_film = itemView.findViewById(R.id.tv_price_film);
        this.imv_film_manager = itemView.findViewById(R.id.imv_film_manager);
        this.btn_delete_film = itemView.findViewById(R.id.btn_delete_film_manager);

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
