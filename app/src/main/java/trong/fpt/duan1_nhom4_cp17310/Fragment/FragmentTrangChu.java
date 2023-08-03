package trong.fpt.duan1_nhom4_cp17310.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterBannerTrangChu;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterFilmTrangChu;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterGoiYTrangChu;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterTinTucTrangChu;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.SetSelectedItemMenuBottom;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Banners;
import trong.fpt.duan1_nhom4_cp17310.models.Film;
import trong.fpt.duan1_nhom4_cp17310.models.News;
import trong.fpt.duan1_nhom4_cp17310.models.Pager2_GateTransformer;
import trong.fpt.duan1_nhom4_cp17310.views.MainActivity;
import trong.fpt.duan1_nhom4_cp17310.views.TicketsActivity;

public class FragmentTrangChu extends Fragment  {

    private ViewPager2 viewPager2, vp_goiy;
    private CircleIndicator3 circleIndicator3;
    private RecyclerView rv_all_film, rv_tintuc_trangchu;
    private Button btn_xem_all_film, btn_datve_goiy;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tv_tenphim_goiy, tv_ngaychieu_goiy;
    private List<Banners> mList;
    private List<Film> filmList;
    private int index_goiy;
    private Handler mHanler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int current = viewPager2.getCurrentItem();
            if(current == mList.size() - 1 ){
                viewPager2.setCurrentItem(0);
            }else {
                viewPager2.setCurrentItem(current + 1);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_trangchu, container, false);
        viewPager2 = view.findViewById(R.id.vp_sl_Top);
        vp_goiy = view.findViewById(R.id.vp_goiy);
        tv_tenphim_goiy = view.findViewById(R.id.tv_tenphim_goiy);
        tv_ngaychieu_goiy = view.findViewById(R.id.tv_ngaychieu_goiy);
        circleIndicator3 = view.findViewById(R.id.circleIndicator);
        rv_all_film = view.findViewById(R.id.rv_all_film);
        rv_tintuc_trangchu = view.findViewById(R.id.rv_tintuc_trangchu);
        btn_xem_all_film = view.findViewById(R.id.btn_xem_all_film);
        btn_datve_goiy = view.findViewById(R.id.btn_datve_goiy);
        btn_xem_all_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fr_main, new FragmentDatVe()).commit();
                SetSelectedItemMenuBottom setSelectedItemMenuBottom = (SetSelectedItemMenuBottom) view.getContext();
                setSelectedItemMenuBottom.onSelected();
            }
        });

        btn_datve_goiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TicketsActivity.class);
                intent.putExtra("thongTinPhim", filmList.get(index_goiy));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        //setting viewpager2
//        viewPager2.setOffscreenPageLimit(3);
//        viewPager2.setClipToPadding(false);
//        viewPager2.setClipChildren(false);

//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHanler.removeCallbacks(mRunnable);
                mHanler.postDelayed(mRunnable, 3000);
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int current = viewPager2.getCurrentItem();
                viewPager2.setCurrentItem(current);
            }
        });

        //setting viewpager goi y

        vp_goiy.setOffscreenPageLimit(3);
        vp_goiy.setPageTransformer(new Pager2_GateTransformer());
        vp_goiy.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Film film = filmList.get(position);
                tv_tenphim_goiy.setText(film.getTenFilm());
                tv_ngaychieu_goiy.setText(film.getNgayChieu());
                index_goiy = position;
            }
        });

        vp_goiy.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int current = vp_goiy.getCurrentItem();
                vp_goiy.setCurrentItem(current);
            }
        });

        return view;
    }

    private void getDataBanner(){
        db.collection("banner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String linkAnh = map.get("linkAnh").toString();
                                String moTa = map.get("tenphim").toString();
                                Banners banners = new Banners(linkAnh, moTa);
//                                course.setCourseId(document.getId());
                                mList.add(banners);
                            }
                            AdapterBannerTrangChu adapter = new AdapterBannerTrangChu(mList);
                            viewPager2.setAdapter(adapter);
                            circleIndicator3.setViewPager(viewPager2);
                        }
                    }
                });
    }

    private void getDataFilm() {
        db.collection("films")
                .limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Film> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String tenPhim = map.get("tenPhim").toString();
                                String linkAnh = map.get("linkAnh").toString();
                                String giaVe = map.get("giaVe").toString();
                                String ngayKhoiChieu = map.get("ngayKhoiChieu").toString();
                                String details = map.get("details").toString();
                                Film film = new Film(tenPhim, ngayKhoiChieu, giaVe, linkAnh, details);
                                film.setIdFilm(document.getId());
                                list.add(film);
                            }
                            int numberOfColumns = 2;
                            rv_all_film.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
                            AdapterFilmTrangChu adapterFilmTrangChu = new AdapterFilmTrangChu(getContext(), list);
                            rv_all_film.setAdapter(adapterFilmTrangChu);
                        }
                    }
                });
    }

    private void getDataFilm2() {
        db.collection("films")
                .limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Film> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String tenPhim = map.get("tenPhim").toString();
                                String linkAnh = map.get("linkAnh").toString();
                                String giaVe = map.get("giaVe").toString();
                                String ngayKhoiChieu = map.get("ngayKhoiChieu").toString();
                                String details = map.get("details").toString();
                                Film film = new Film(tenPhim, ngayKhoiChieu, giaVe, linkAnh, details);
                                film.setIdFilm(document.getId());
                                list.add(film);
                                filmList.add(film);
                            }
                            AdapterGoiYTrangChu adapter = new AdapterGoiYTrangChu(list);
                            vp_goiy.setAdapter(adapter);
                        }
                    }
                });
    }

    private void getDataTinTuc() {
        db.collection("news")
                .limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<News> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String linkAnh = map.get("linkAnh").toString();
                                String title = map.get("title").toString();
                                String linkWeb = map.get("linkWeb").toString();
                                News news = new News(linkAnh, title, linkWeb);
                                list.add(news);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rv_tintuc_trangchu.setLayoutManager(layoutManager);
                            AdapterTinTucTrangChu adapter = new AdapterTinTucTrangChu(getContext(), list);
                            rv_tintuc_trangchu.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataBanner();
        getDataFilm();
        getDataFilm2();
        getDataTinTuc();
    }


}
