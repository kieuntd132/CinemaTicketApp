package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterSuatXem;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.SoGhe;
import trong.fpt.duan1_nhom4_cp17310.models.Tickets;

public class ThongKe2Activity extends AppCompatActivity {

    private ArrayList<String> dsTenPhim;
    private Spinner spinner;
    private Button btn_chonngay_tk2, xem_doanhthu;
    private TextView tv_ngay_tk2,tv_showdoanhthu;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    int mYear, mMonth, mDay;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Tickets> dsTickets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke2);
        spinner = findViewById(R.id.spn_tk2);
        btn_chonngay_tk2 = findViewById(R.id.btn_chonngay_tk2);
        tv_ngay_tk2 = findViewById(R.id.tv_ngay_tk2);
        xem_doanhthu = findViewById(R.id.xem_doanhthu);
        tv_showdoanhthu = findViewById(R.id.tv_showdoanhthu);
        dsTenPhim = new ArrayList<>();
        dsTickets = new ArrayList<>();

        Intent intent = getIntent();
        dsTenPhim = intent.getStringArrayListExtra("DanhSachTenPhim");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, dsTenPhim);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        btn_chonngay_tk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(ThongKe2Activity.this,
                        0, ngayXem, mYear, mMonth, mDay);
                d.show();
            }
        });

        xem_doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = spinner.getSelectedItemPosition();
                String tenPhim = dsTenPhim.get(index);
                String ngay = tv_ngay_tk2.getText().toString();
                getDataDoanhThu(tenPhim, ngay);
            }
        });

    }

    DatePickerDialog.OnDateSetListener ngayXem = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            tv_ngay_tk2.setText(sdf.format(c.getTime()));
        }
    };

    private void getDataDoanhThu(String tenPhim, String ngayXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String tenPhim = map.get("tenPhim").toString();
                                String giaVe = map.get("giaVe").toString();
                                String ngayXemPhim = map.get("ngayXemPhim").toString();
                                String soLuong = map.get("soLuong").toString();
                                String suatXem = map.get("suatXem").toString();
                                String tenTaiKhoan = map.get("tenTaiKhoan").toString();
                                String maGhe = map.get("maGhe").toString();

                                Map<String, Object> mapSoGhe = (Map<String, Object>) document.get("SoGhe");
                                String soGhe = mapSoGhe.get("soGhe").toString();
                                String trangThai = mapSoGhe.get("trangThai").toString();
                                SoGhe soGhe1 = new SoGhe(Integer.parseInt(soGhe), Integer.parseInt(trangThai));

                                Tickets tickets = new Tickets(tenPhim, suatXem, soLuong, tenTaiKhoan, ngayXemPhim, giaVe, soGhe1, maGhe);
                                tickets.setIdTickets(document.getId());
                                dsTickets.add(tickets);
                            }
                            int doanhThu = 0;
                            if(dsTickets.size() != 0){
                                for (int i = 0; i < dsTickets.size(); i++) {
                                    doanhThu+= Integer.parseInt(dsTickets.get(i).getGiaVe());
                                }
                                tv_showdoanhthu.setText("Doanh thu phim " + tenPhim + ": "+ doanhThu + "VNĐ");

                            }else {
                                tv_showdoanhthu.setText("Doanh thu phim " + tenPhim + ": "+ doanhThu  + "VNĐ");
                            }
                        }
                    }
                });
    }
}