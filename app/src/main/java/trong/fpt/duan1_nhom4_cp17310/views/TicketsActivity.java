package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterSoGhe;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterSuatXem;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListenerSoGhe;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.ItemClickListenerSuatXem;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Film;
import trong.fpt.duan1_nhom4_cp17310.models.SoGhe;
import trong.fpt.duan1_nhom4_cp17310.models.SuatXem;
import trong.fpt.duan1_nhom4_cp17310.models.Tickets;

public class TicketsActivity extends AppCompatActivity {

    private ImageView imv_ticket;
    private TextView tv_tenphim_ticket, tv_chonngay_xem, tv_chon_suat_xem, tv_soluong;
    private Button btn_chonngay_xem, btn_ticket, btn_cancel_ticket;
    private LinearLayout ln_chon_soluong, ln_soghe;
    private RecyclerView rv_chon_suat_xem, rv_soghe;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<SuatXem> dsSuatXem;
    private AdapterSuatXem adapterSuatXem;
    private AdapterSoGhe adapterSoGhe;

    private String soGheHienThi = "";
    private int soGheChon = -1;
    private String suatXemChon = "";

    private ArrayList<Tickets> dsTicketsSuat8h;
    private ArrayList<Tickets> dsTicketsSuat9h;
    private ArrayList<Tickets> dsTicketsSuat10h;
    private ArrayList<Tickets> dsTicketsSuat11h;
    private ArrayList<Tickets> dsTicketsSuat13h;
    private ArrayList<Tickets> dsTicketsSuat14h;
    private ArrayList<Tickets> dsTicketsSuat15h;
    private ArrayList<Tickets> dsTicketsSuat16h;
    private ArrayList<Tickets> dsTicketsSuat17h;
    private ArrayList<Tickets> dsTicketsSuat18h;
    private ArrayList<Tickets> dsTicketsSuat19h;
    private ArrayList<Tickets> dsTicketsSuat20h;

    private ArrayList<SoGhe> dsSoGhe8h;
    private ArrayList<SoGhe> dsSoGhe9h;
    private ArrayList<SoGhe> dsSoGhe10h;
    private ArrayList<SoGhe> dsSoGhe11h;
    private ArrayList<SoGhe> dsSoGhe13h;
    private ArrayList<SoGhe> dsSoGhe14h;
    private ArrayList<SoGhe> dsSoGhe15h;
    private ArrayList<SoGhe> dsSoGhe16h;
    private ArrayList<SoGhe> dsSoGhe17h;
    private ArrayList<SoGhe> dsSoGhe18h;
    private ArrayList<SoGhe> dsSoGhe19h;
    private ArrayList<SoGhe> dsSoGhe20h;


    int soLuongSuat8h, soLuongSuat9h, soLuongSuat10h, soLuongSuat11h, soLuongSuat13h, soLuongSuat14h,
            soLuongSuat15h, soLuongSuat16h, soLuongSuat17h, soLuongSuat18h, soLuongSuat19h, soLuongSuat20h;

    private ItemClickListenerSuatXem itemClickListenerSuatXem;
    private ItemClickListenerSoGhe itemClickListenerSoGhe;

    private Film film;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        imv_ticket = findViewById(R.id.imv_ticket);
        tv_tenphim_ticket = findViewById(R.id.tv_tenphim_ticket);
        tv_chonngay_xem = findViewById(R.id.tv_chonngay_xem);
        btn_chonngay_xem = findViewById(R.id.btn_chonngay_xem);
        btn_ticket = findViewById(R.id.btn_ticket);
        btn_cancel_ticket = findViewById(R.id.btn_cancel_ticket);
        rv_chon_suat_xem = findViewById(R.id.rv_chon_suat_xem);
        tv_chon_suat_xem = findViewById(R.id.tv_chon_suat_xem);
        ln_chon_soluong = findViewById(R.id.ln_chon_soluong);
        tv_soluong = findViewById(R.id.tv_soluong);
        ln_soghe = findViewById(R.id.ln_soghe);
        rv_soghe = findViewById(R.id.rv_soghe);


        btn_cancel_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TicketsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dsSoGhe8h = dsSoGhe();
        dsSoGhe9h = dsSoGhe();
        dsSoGhe10h = dsSoGhe();
        dsSoGhe11h = dsSoGhe();
        dsSoGhe13h = dsSoGhe();
        dsSoGhe14h = dsSoGhe();
        dsSoGhe15h = dsSoGhe();
        dsSoGhe16h = dsSoGhe();
        dsSoGhe17h = dsSoGhe();
        dsSoGhe18h = dsSoGhe();
        dsSoGhe19h = dsSoGhe();
        dsSoGhe20h = dsSoGhe();


        dsTicketsSuat8h = new ArrayList<>();
        dsTicketsSuat9h = new ArrayList<>();
        dsTicketsSuat10h = new ArrayList<>();
        dsTicketsSuat11h = new ArrayList<>();
        dsTicketsSuat13h = new ArrayList<>();
        dsTicketsSuat14h = new ArrayList<>();
        dsTicketsSuat15h = new ArrayList<>();
        dsTicketsSuat16h = new ArrayList<>();
        dsTicketsSuat17h = new ArrayList<>();
        dsTicketsSuat18h = new ArrayList<>();
        dsTicketsSuat19h = new ArrayList<>();
        dsTicketsSuat20h = new ArrayList<>();

        Intent intent = getIntent();
        film = (Film) intent.getSerializableExtra("thongTinPhim");
        String imageLink = film.getLinkAnh();
        new DownloadImageFromInternet(imv_ticket).execute(imageLink);
        tv_tenphim_ticket.setText(film.getTenFilm());
        dsSuatXem = dsSuatXem();

        itemClickListenerSuatXem = new ItemClickListenerSuatXem() {
            @Override
            public void onClick(String s) {

                suatXemChon = s;
                if (s.equals("08:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat8h));
                    suatXemChon = s;
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe8h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("09:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat9h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe9h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("10:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat10h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe10h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("11:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat11h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe11h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("13:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat13h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe13h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("14:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat14h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe14h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("15:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat15h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe15h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("16:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat16h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe16h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("17:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat17h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe17h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("18:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat18h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe18h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("19:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat19h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe19h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                if (s.equals("20:00")) {
                    tv_soluong.setText("Số lượng ghế còn lại: " + (50 - soLuongSuat20h));
                    adapterSoGhe = new AdapterSoGhe(dsSoGhe20h, itemClickListenerSoGhe);
                    rv_soghe.setAdapter(adapterSoGhe);
                }

                ln_chon_soluong.setVisibility(View.VISIBLE);
                ln_soghe.setVisibility(View.VISIBLE);
            }
        };

        itemClickListenerSoGhe = new ItemClickListenerSoGhe() {
            @Override
            public void onClick(int s) {

                for (int i = 1; i <= 50; i++) {
                    if (s == i) {
                        soGheChon = i;
                    }
                }

                for (int i1 = 1; i1 <= 8; i1++) {
                    if (s == i1) {
                        soGheHienThi = "A" + s;
                    }
                }

                for (int i2 = 9; i2 <= 16; i2++) {
                    int j = 8;
                    if (s == i2) {
                        soGheHienThi = "B" + (i2 - j);
                        j++;
                    }
                }

                for (int i3 = 17; i3 <= 24; i3++) {
                    int j = 16;
                    if (s == i3) {
                        soGheHienThi = "C" + (i3 - j);
                        j++;
                    }
                }

                for (int i4 = 25; i4 <= 32; i4++) {
                    int j = 24;
                    if (s == i4) {
                        soGheHienThi = "D" + (i4 - j);
                        j++;
                    }
                }

                for (int i5 = 33; i5 <= 40; i5++) {
                    int j = 32;
                    if (s == i5) {
                        soGheHienThi = "E" + (i5 - j);
                        j++;
                    }
                }

                for (int i6 = 41; i6 <= 48; i6++) {
                    int j = 40;
                    if (s == i6) {
                        soGheHienThi = "F" + (i6 - j);
                        j++;
                    }
                }

                for (int i7 = 49; i7 <= 50; i7++) {
                    int j = 48;
                    if (s == i7) {
                        soGheHienThi = "G" + (i7 - j);
                        j++;
                    }
                }
            }
        };


        int numberOfColumns = 4;
        rv_chon_suat_xem.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        int numberOfColumnsSoGhe = 8;
        rv_soghe.setLayoutManager(new GridLayoutManager(this, numberOfColumnsSoGhe));


        btn_chonngay_xem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(TicketsActivity.this,
                        0, ngayXem, mYear, mMonth, mDay);
                d.show();
            }
        });

        btn_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int t_soghe = soGheChon;
                int t_trangThai = 1;
                SoGhe soGhe = new SoGhe(t_soghe, t_trangThai);
                String t_giaVe = film.getGiaVe();
                String t_ngayXemPhim = tv_chonngay_xem.getText().toString();
                String t_soLuong = "1";
                String t_suatXem = suatXemChon;
                String t_tenPhim = film.getTenFilm();
                SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
                String t_tenTaiKhoan = sharedPreferences.getString("email", "");
                String t_maGhe = soGheHienThi;

               if (t_ngayXemPhim.length() == 0) {
                    new AlertDialog.Builder(TicketsActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Chọn ngày xem phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (t_suatXem.length() == 0) {
                    new AlertDialog.Builder(TicketsActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Chọn suất xem phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }
                else if (soGheChon < 0) {
                    new AlertDialog.Builder(TicketsActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Chọn số ghế")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
//
//                Intent intent1 = new Intent(TicketsActivity.this, PayPalActivity.class);
//                intent1.putExtra("t_soghe", t_soghe);
//                intent1.putExtra("soGhe", soGhe);
//                intent1.putExtra("t_trangThai", t_trangThai);
//                intent1.putExtra("t_giaVe", t_giaVe);
//                intent1.putExtra("t_ngayXemPhim", t_ngayXemPhim);
//                intent1.putExtra("t_soLuong", t_soLuong);
//                intent1.putExtra("t_suatXem", t_suatXem);
//                intent1.putExtra("t_tenPhim", t_tenPhim);
//                intent1.putExtra("t_tenTaiKhoan", t_tenTaiKhoan);
//                intent1.putExtra("t_maGhe", t_maGhe);
//                startActivity(intent1);
//                finish();


                    Map<String, Object> tickets = new HashMap<>();
                    tickets.put("giaVe", t_giaVe);
                    tickets.put("ngayXemPhim", t_ngayXemPhim);
                    tickets.put("soLuong", t_soLuong);
                    tickets.put("suatXem", t_suatXem);
                    tickets.put("tenPhim", t_tenPhim);
                    tickets.put("tenTaiKhoan", t_tenTaiKhoan);
                    tickets.put("SoGhe", soGhe);
                    tickets.put("maGhe", t_maGhe);

                    // Add a new document with a generated ID
                    db.collection("tickets")
                            .add(tickets)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
//                                    new AlertDialog.Builder(TicketsActivity.this)
//                                            .setTitle("Notification")
//                                            .setMessage("Successful ticket booking")
//                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    Intent intent = new Intent(TicketsActivity.this, BookedTicketActivity.class);
//                                                    startActivity(intent);
//                                                    finish();
//                                                }
//                                            })
//                                            .show();

                                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View viewdialog = inflater.inflate(R.layout.item_bookedticket2, null);
                                    TextView tv_tenphim = viewdialog.findViewById(R.id.tv_tenphim);
                                    TextView tv_tentaikhoan = viewdialog.findViewById(R.id.tv_tentaikhoan);
                                    TextView tv_suatxem = viewdialog.findViewById(R.id.tv_suatxem);
                                    TextView tv_ngayxemphim = viewdialog.findViewById(R.id.tv_ngayxemphim);
                                    TextView tv_maghe = viewdialog.findViewById(R.id.tv_maghe);
                                    TextView tv_giave = viewdialog.findViewById(R.id.tv_giave);

                                    tv_tenphim.setText(t_tenPhim);
                                    tv_tentaikhoan.setText(t_tenTaiKhoan);
                                    tv_suatxem.setText(t_suatXem);
                                    tv_ngayxemphim.setText(t_ngayXemPhim);
                                    tv_maghe.setText(t_maGhe);
                                    tv_giave.setText(t_giaVe);

                                    builder.setView(viewdialog);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(TicketsActivity.this, "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new AlertDialog.Builder(TicketsActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Ticket booking failed")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            });
                }
            }
        });
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private ArrayList<SuatXem> dsSuatXem() {
        ArrayList<SuatXem> dsSuatXem = new ArrayList<>();
        dsSuatXem.add(new SuatXem("08:00"));
        dsSuatXem.add(new SuatXem("09:00"));
        dsSuatXem.add(new SuatXem("10:00"));
        dsSuatXem.add(new SuatXem("11:00"));
        dsSuatXem.add(new SuatXem("13:00"));
        dsSuatXem.add(new SuatXem("14:00"));
        dsSuatXem.add(new SuatXem("15:00"));
        dsSuatXem.add(new SuatXem("16:00"));
        dsSuatXem.add(new SuatXem("17:00"));
        dsSuatXem.add(new SuatXem("18:00"));
        dsSuatXem.add(new SuatXem("19:00"));
        dsSuatXem.add(new SuatXem("20:00"));
        return dsSuatXem;
    }

    private ArrayList<SoGhe> dsSoGhe() {
        ArrayList<SoGhe> dsSoGhe = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            int trangThai = 0;
            SoGhe soGhe = new SoGhe(i, trangThai);
            dsSoGhe.add(soGhe);
        }
        return dsSoGhe;
    }


    DatePickerDialog.OnDateSetListener ngayXem = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            tv_chonngay_xem.setText(sdf.format(c.getTime()));
            tv_chon_suat_xem.setVisibility(View.VISIBLE);
            rv_chon_suat_xem.setVisibility(View.VISIBLE);

            getDataPhimSuat8h(film.getTenFilm(), sdf.format(c.getTime()), "08:00");
            getDataPhimSuat9h(film.getTenFilm(), sdf.format(c.getTime()), "09:00");
            getDataPhimSuat10h(film.getTenFilm(), sdf.format(c.getTime()), "10:00");
            getDataPhimSuat11h(film.getTenFilm(), sdf.format(c.getTime()), "11:00");
            getDataPhimSuat13h(film.getTenFilm(), sdf.format(c.getTime()), "13:00");
            getDataPhimSuat14h(film.getTenFilm(), sdf.format(c.getTime()), "14:00");
            getDataPhimSuat15h(film.getTenFilm(), sdf.format(c.getTime()), "15:00");
            getDataPhimSuat16h(film.getTenFilm(), sdf.format(c.getTime()), "16:00");
            getDataPhimSuat17h(film.getTenFilm(), sdf.format(c.getTime()), "17:00");
            getDataPhimSuat18h(film.getTenFilm(), sdf.format(c.getTime()), "18:00");
            getDataPhimSuat19h(film.getTenFilm(), sdf.format(c.getTime()), "19:00");
            getDataPhimSuat20h(film.getTenFilm(), sdf.format(c.getTime()), "20:00");

            btn_chonngay_xem.setEnabled(false);

        }
    };


    private void getDataPhimSuat8h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat8h.add(tickets);
                            }
                            if (dsTicketsSuat8h.size() == 0) {
                                soLuongSuat8h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat8h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat8h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat8h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe8h.size(); j++) {
                                        int soGhe1 = dsSoGhe8h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe8h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat8h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("08:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);


                        }
                    }
                });
    }

    private void getDataPhimSuat9h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat9h.add(tickets);
                            }
                            if (dsTicketsSuat9h.size() == 0) {
                                soLuongSuat9h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat9h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat9h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat9h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe9h.size(); j++) {
                                        int soGhe1 = dsSoGhe9h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe9h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat9h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("09:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);


                        }
                    }
                });
    }

    private void getDataPhimSuat10h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat10h.add(tickets);
                            }
                            if (dsTicketsSuat10h.size() == 0) {
                                soLuongSuat10h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat10h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat10h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat10h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe10h.size(); j++) {
                                        int soGhe1 = dsSoGhe10h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe10h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat10h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("10:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat11h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat11h.add(tickets);
                            }
                            if (dsTicketsSuat11h.size() == 0) {
                                soLuongSuat11h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat11h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat11h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat11h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe11h.size(); j++) {
                                        int soGhe1 = dsSoGhe11h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe11h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat11h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("10:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat13h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat13h.add(tickets);
                            }

                            if (dsTicketsSuat13h.size() == 0) {
                                soLuongSuat13h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat13h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat13h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat13h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe13h.size(); j++) {
                                        int soGhe1 = dsSoGhe13h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe13h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            Log.d("SL", soLuongSuat13h + "");

                            if (soLuongSuat13h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("13:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);

                        }
                    }
                });
    }

    private void getDataPhimSuat14h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat14h.add(tickets);
                            }
                            if (dsTicketsSuat14h.size() == 0) {
                                soLuongSuat14h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat14h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat14h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat14h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe14h.size(); j++) {
                                        int soGhe1 = dsSoGhe14h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe14h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat14h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("14:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat15h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat15h.add(tickets);
                            }
                            if (dsTicketsSuat15h.size() == 0) {
                                soLuongSuat15h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat15h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat15h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat15h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe15h.size(); j++) {
                                        int soGhe1 = dsSoGhe15h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe15h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat15h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("15:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat16h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat16h.add(tickets);
                            }
                            if (dsTicketsSuat16h.size() == 0) {
                                soLuongSuat16h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat16h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat16h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat16h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe16h.size(); j++) {
                                        int soGhe1 = dsSoGhe16h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe16h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat16h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("16:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat17h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat17h.add(tickets);
                            }
                            if (dsTicketsSuat17h.size() == 0) {
                                soLuongSuat17h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat17h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat17h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat17h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe17h.size(); j++) {
                                        int soGhe1 = dsSoGhe17h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe17h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat17h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("17:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat18h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat18h.add(tickets);
                            }
                            if (dsTicketsSuat18h.size() == 0) {
                                soLuongSuat18h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat18h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat18h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat18h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe18h.size(); j++) {
                                        int soGhe1 = dsSoGhe18h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe18h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat18h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("18:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat19h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat19h.add(tickets);
                            }
                            if (dsTicketsSuat19h.size() == 0) {
                                soLuongSuat19h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat19h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat19h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat19h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe19h.size(); j++) {
                                        int soGhe1 = dsSoGhe19h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe19h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat19h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("19:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }

    private void getDataPhimSuat20h(String tenPhim, String ngayXem, String suatXem) {
        db.collection("tickets")
                .whereEqualTo("tenPhim", tenPhim)
                .whereEqualTo("ngayXemPhim", ngayXem)
                .whereEqualTo("suatXem", suatXem)
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
                                dsTicketsSuat20h.add(tickets);
                            }
                            if (dsTicketsSuat20h.size() == 0) {
                                soLuongSuat20h = 0;
                            } else {
                                for (int i = 0; i < dsTicketsSuat20h.size(); i++) {
                                    Tickets tickets = dsTicketsSuat20h.get(i);
                                    int soLuong = Integer.parseInt(tickets.getSoLuong());
                                    soLuongSuat20h += soLuong;

                                    SoGhe soGhe = tickets.getSoGhe();
                                    Log.d("AAA", soGhe.getSoGhe() + "" + soGhe.getTrangThai());
                                    for (int j = 0; j < dsSoGhe20h.size(); j++) {
                                        int soGhe1 = dsSoGhe20h.get(j).getSoGhe();
                                        if (soGhe1 == soGhe.getSoGhe()) {
                                            dsSoGhe20h.set(j, soGhe);
                                        }
                                    }
                                }
                            }

                            if (soLuongSuat20h == 50) {
                                for (int i = 0; i < dsSuatXem.size(); i++) {
                                    if (dsSuatXem.get(i).getSuatXem().equals("20:00")) {
                                        dsSuatXem.remove(i);
                                    }
                                }
                            }

                            adapterSuatXem = new AdapterSuatXem(dsSuatXem, itemClickListenerSuatXem);
                            rv_chon_suat_xem.setAdapter(adapterSuatXem);
                        }
                    }
                });
    }
}