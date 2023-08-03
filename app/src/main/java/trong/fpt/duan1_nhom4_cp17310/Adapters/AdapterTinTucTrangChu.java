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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListener;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.TinTucTrangChuViewHolder;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.TinTucViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.News;
import trong.fpt.duan1_nhom4_cp17310.views.WebviewActivity;

public class AdapterTinTucTrangChu extends RecyclerView.Adapter<TinTucTrangChuViewHolder> {
    Context context;
    ArrayList<News> dsNews;
    public AdapterTinTucTrangChu(Context context, ArrayList<News> dsNews) {
        this.context = context;
        this.dsNews = dsNews;
    }

    @NonNull
    @Override
    public TinTucTrangChuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tintuc_trangchu, parent, false);
        return new TinTucTrangChuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TinTucTrangChuViewHolder holder, int position) {
        News news = dsNews.get(position);
        holder.tv_title_tintuc.setText(news.getTitle());
        String imageLink = news.getLinkAnh();
        new DownloadImageFromInternet(holder.imv_tintuc).execute(imageLink);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(view.getContext(), WebviewActivity.class);
                intent.putExtra("linkWeb", news.getLinkWeb());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        holder.tv_xemthem_tintuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebviewActivity.class);
                intent.putExtra("linkWeb", news.getLinkWeb());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsNews.size();
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
