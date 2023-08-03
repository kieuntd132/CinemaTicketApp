package trong.fpt.duan1_nhom4_cp17310.Adapters;

import android.content.Context;
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
import trong.fpt.duan1_nhom4_cp17310.Interfaces.OnItemClickBannerManager;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.ViewHolder.BannerViewHolder;
import trong.fpt.duan1_nhom4_cp17310.models.Banners;

public class AdapterBannerManager extends RecyclerView.Adapter<BannerViewHolder> {
    Context context;
    ArrayList<Banners> dsBanners;
    public AdapterBannerManager(Context context, ArrayList<Banners> dsBanners) {
        this.context = context;
        this.dsBanners = dsBanners;

    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manager_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banners banners = dsBanners.get(position);
        holder.tv_banner.setText(banners.getMoTa());
        String imageLink = banners.getLinkAnh();
        new DownloadImageFromInternet(holder.imv_banner).execute(imageLink);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                OnItemClickBannerManager onItemClickBannerManager = (OnItemClickBannerManager) view.getContext();
                onItemClickBannerManager.onItemClickDelete(banners);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsBanners.size();
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
