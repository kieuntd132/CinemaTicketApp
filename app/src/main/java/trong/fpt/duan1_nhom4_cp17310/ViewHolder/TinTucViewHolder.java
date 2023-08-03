package trong.fpt.duan1_nhom4_cp17310.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.R;

public class TinTucViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

    public TextView tv_title_tintuc, tv_xemthem_tintuc;
    public ImageView imv_tintuc;
    private ItemClickListener itemClickListener;

    public TinTucViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        this.tv_title_tintuc = itemView.findViewById(R.id.tv_title_tintuc);
        this.tv_xemthem_tintuc = itemView.findViewById(R.id.tv_xemthem_tintuc);
        this.imv_tintuc = itemView.findViewById(R.id.imv_tintuc);

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

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
