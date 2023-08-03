package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterBookedTickets;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterFilmManager;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Film;
import trong.fpt.duan1_nhom4_cp17310.models.Tickets;

public class BookedTicketActivity extends AppCompatActivity {

    private String t_tenTaiKhoan;
    private TextView tv_chuadatve;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView rv_bookedTicket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_ticket);
        rv_bookedTicket = findViewById(R.id.rv_bookedticket);
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
        t_tenTaiKhoan = sharedPreferences.getString("email", "");
        tv_chuadatve = findViewById(R.id.tv_chuadatve);

        getDataBookedTickets();
    }

    private void getDataBookedTickets(){
        db.collection("tickets")
                .whereEqualTo("tenTaiKhoan",t_tenTaiKhoan)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Tickets> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String tenPhim = map.get("tenPhim").toString();
                                String tenTaiKhoan = map.get("tenTaiKhoan").toString();
                                String suatXem = map.get("suatXem").toString();
                                String soLuong = map.get("soLuong").toString();
                                String ngayXemPhim = map.get("ngayXemPhim").toString();
                                String maGhe = map.get("maGhe").toString();
                                String giaVe = map.get("giaVe").toString();

                                Tickets tickets = new Tickets(tenPhim, suatXem, soLuong, tenTaiKhoan,ngayXemPhim,giaVe,maGhe);
                                tickets.setIdTickets(document.getId());
                                list.add(tickets);
                            }
                            if(list.size() == 0)
                            {
                                tv_chuadatve.setVisibility(View.VISIBLE);
                            }

                            else
                            {
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookedTicketActivity.this);
                                rv_bookedTicket.setLayoutManager(layoutManager);
                                AdapterBookedTickets adapterBookedTickets = new AdapterBookedTickets(BookedTicketActivity.this, list);
                                rv_bookedTicket.setAdapter(adapterBookedTickets);
                                rv_bookedTicket.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
    }
}