package trong.fpt.duan1_nhom4_cp17310.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListenerSoGhe;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListenerSuatXem;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.SuatXemViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.SoGhe;
import trong.fpt.duan1_nhom4_cp17310.models.SuatXem;

public class AdapterSoGhe extends RecyclerView.Adapter<SuatXemViewHolder> {
    ArrayList<SoGhe> dsSoGhe;
    ItemClickListenerSoGhe itemClickListenerSoGhe;
    private int lastCheckedPosition = -1;

    public AdapterSoGhe(ArrayList<SoGhe> dsSoGhe, ItemClickListenerSoGhe itemClickListenerSoGhe) {
        this.dsSoGhe = dsSoGhe;
        this.itemClickListenerSoGhe = itemClickListenerSoGhe;
    }

    @NonNull
    @Override
    public SuatXemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suatxem, parent, false);
        return new SuatXemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuatXemViewHolder holder, int position) {
        SoGhe soGhe = dsSoGhe.get(position);


        for(int i = 1; i<=8;i++){
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("A" + soGhe.getSoGhe());
            }
        }

        for(int i = 9; i<=16;i++){
            int j=8;
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("B" + (i-j));
                j++;
            }
        }

        for(int i = 17; i<=24;i++){
            int j=16;
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("C" + (i-j));
                j++;
            }
        }

        for(int i = 25; i<=32;i++){
            int j=24;
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("D" + (i-j));
                j++;
            }
        }

        for(int i = 33; i<=40;i++){
            int j=32;
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("E" + (i-j));
                j++;
            }
        }

        for(int i = 41; i<=48;i++){
            int j=40;
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("F" + (i-j));
                j++;
            }
        }

        for(int i = 49; i<=50;i++){
            int j=48;
            if(soGhe.getSoGhe()==i){
                holder.radioButton.setText("G" + (i-j));
                j++;
            }
        }





//        } else if (soGhe.getSoGhe() < 17) {
//            holder.radioButton.setText("B" + (soGhe.getSoGhe()-16));
//        } else if (soGhe.getSoGhe() < 25) {
//            holder.radioButton.setText("C" + (soGhe.getSoGhe()-24));
//        } else if (soGhe.getSoGhe() < 33) {
//            holder.radioButton.setText("D" + (soGhe.getSoGhe()-32));
//        } else if (soGhe.getSoGhe() < 41) {
//            holder.radioButton.setText("E" + (soGhe.getSoGhe()-40));
//        } else if (soGhe.getSoGhe() < 49) {
//            holder.radioButton.setText("F" + (soGhe.getSoGhe()-48));
//        } else {
//            holder.radioButton.setText("G" + (soGhe.getSoGhe()-50));
//        }

//        holder.radioButton.setText(soGhe.getSoGhe()+"");
        holder.radioButton.setChecked(position == lastCheckedPosition);
        if (soGhe.getTrangThai() == 1) {
            holder.radioButton.setEnabled(false);
            holder.radioButton.setBackgroundResource(R.drawable.bacground_radio_button_soghe);
        }
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = holder.getAdapterPosition();
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);
                itemClickListenerSoGhe.onClick(soGhe.getSoGhe());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dsSoGhe.size();
    }

}
