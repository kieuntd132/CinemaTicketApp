package trong.fpt.duan1_nhom4_cp17310.Adapters;

import android.app.AlertDialog;
import android.content.Context;
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
import trong.fpt.duan1_nhom4_cp17310.Interfaces.OnItemClickBannerManager;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.OnItemClickFilmManager;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.BannerViewHolder;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.FilmManagerViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.Banners;
import trong.fpt.duan1_nhom4_cp17310.models.Film;

public class AdapterFilmManager extends RecyclerView.Adapter<FilmManagerViewHolder> {
    Context context;
    ArrayList<Film> dsFilm;
    public AdapterFilmManager(Context context, ArrayList<Film> dsFilm) {
        this.context = context;
        this.dsFilm = dsFilm;
    }

    @NonNull
    @Override
    public FilmManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film_manager, parent, false);
        return new FilmManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmManagerViewHolder holder, int position) {
        Film film = dsFilm.get(position);
        holder.tv_name_film.setText(film.getTenFilm());
        holder.tv_date_film.setText(film.getNgayChieu());
        holder.tv_price_film.setText(film.getGiaVe()+"đ");
        String imageLink = film.getLinkAnh();
        new DownloadImageFromInternet(holder.imv_film_manager).execute(imageLink);

        holder.btn_delete_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClickFilmManager onItemClickFilmManager = (OnItemClickFilmManager) view.getContext();
                onItemClickFilmManager.onItemClickDelete(film);
            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                OnItemClickFilmManager onItemClickFilmManager = (OnItemClickFilmManager) view.getContext();
                onItemClickFilmManager.onItemClickUpdate(film);
            }
        });

        holder.imv_film_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Nội dung phim")
                        .setMessage(film.getDetails())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsFilm.size();
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
