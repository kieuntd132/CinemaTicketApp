package trong.fpt.duan1_nhom4_cp17310.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.BookedTicketsViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.Tickets;

public class AdapterBookedTickets extends RecyclerView.Adapter<BookedTicketsViewHolder> {
    private Context c;
    private ArrayList<Tickets> listBookedTicket;

    public AdapterBookedTickets(Context c, ArrayList<Tickets> listBookedTicket)
    {
        this.c = c;
        this.listBookedTicket = listBookedTicket;
    }


    @NonNull
    @Override
    public BookedTicketsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookedticket,parent,false);
        return new BookedTicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedTicketsViewHolder holder, int position) {
        Tickets bookedTickets = listBookedTicket.get(position);
        holder.tv_tenphim.setText(bookedTickets.getTenPhim());
        holder.tv_tentaikhoan.setText(bookedTickets.getTenNguoiDat() +"  "+bookedTickets.getNgayXem());
        holder.tv_suatxem.setText(bookedTickets.getSuatXem());
        holder.tv_ngayxemphim.setText(bookedTickets.getNgayXem());
        holder.tv_maghe.setText(bookedTickets.getMaGhe());
        holder.tv_giave.setText(bookedTickets.getGiaVe());

    }

    @Override
    public int getItemCount() {
        return listBookedTicket.size();
    }
}
