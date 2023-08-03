package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterBannerManager;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterFilmManager;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterThongKe;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Film;
import trong.fpt.duan1_nhom4_cp17310.models.ThongKe;

public class ThongKeActivity extends AppCompatActivity {

    private PieChart barchart;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<ThongKe> dsThongKe;
    private ArrayList<String> dsTenPhim = new ArrayList<>();
    private Button btn_xemDoanhThu, btn_doanhThuTheoNgay;
    private RecyclerView rv_thongke;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        barchart = findViewById(R.id.barchart);
        btn_xemDoanhThu = findViewById(R.id.btn_xemDoanhThu);
        btn_doanhThuTheoNgay = findViewById(R.id.btn_doanhThuTheoNgay);
        rv_thongke = findViewById(R.id.rv_thongke);
        tv_title = findViewById(R.id.tv_title);

        getDataFilm();

        btn_doanhThuTheoNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongKeActivity.this, ThongKe2Activity.class);
                intent.putExtra("DanhSachTenPhim", dsTenPhim);
                startActivity(intent);
                finish();
            }
        });

        btn_xemDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barchart.setVisibility(View.VISIBLE);
                rv_thongke.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);

                ArrayList<PieEntry> visittors2 = new ArrayList<>();
                for (int i = 0; i < dsThongKe.size(); i++) {
                    int doanhThu = dsThongKe.get(i).getTongDoanhThu();
                    if(doanhThu > 0){
                        visittors2.add(new PieEntry(doanhThu,dsThongKe.get(i).getTenPhim()));
                    }
               }

                PieDataSet barDataSet2 = new PieDataSet(visittors2, "Tên Phim");
                barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet2.setValueTextColor(Color.BLACK);
                barDataSet2.setValueTextSize(16f);

                PieData pieData = new PieData(barDataSet2);

                barchart.setData(pieData);
                barchart.getDescription().setEnabled(false);
                barchart.setCenterText("Doanh Thu Phim");
                barchart.animateXY(2000, 2000);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ThongKeActivity.this);
                rv_thongke.setLayoutManager(layoutManager);
                AdapterThongKe adapterThongKe = new AdapterThongKe(ThongKeActivity.this, dsThongKe);
                rv_thongke.setAdapter(adapterThongKe);
            }
        });
    }

    public void getDataFilm() {
        dsThongKe = new ArrayList<>();
        ArrayList<Film> list = new ArrayList<>();
        db.collection("films")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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
                            for (int i = 0; i < list.size(); i++) {
                                dsTenPhim.add(list.get(i).getTenFilm());
                            }

                            for (int i = 0; i < dsTenPhim.size(); i++) {
                                ArrayList<ThongKe> list1 = new ArrayList<>();
                                String tenPhim = dsTenPhim.get(i);
                                db.collection("tickets")
                                        .whereEqualTo("tenPhim", tenPhim)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Map<String, Object> map = document.getData();
                                                        String tenPhim = map.get("tenPhim").toString();
                                                        int giaVe = Integer.parseInt(map.get("giaVe").toString());
                                                        ThongKe thongKe = new ThongKe(tenPhim, giaVe);
                                                        list1.add(thongKe);
                                                    }
                                                    int tongDoanhThu = 0;
                                                    String tenPhima = "";
                                                    if(list1.size() == 0){
                                                        tenPhima = tenPhim;
                                                        tongDoanhThu = 0;
                                                    }else{
                                                        for (int j = 0; j < list1.size(); j++) {
                                                            int giaVe = list1.get(j).getTongDoanhThu();
                                                            tongDoanhThu+=giaVe;
                                                            tenPhima = list1.get(j).getTenPhim();
                                                        }
                                                    }
                                                    ThongKe thongKe = new ThongKe(tenPhima, tongDoanhThu);
                                                    dsThongKe.add(thongKe);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}