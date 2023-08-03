package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterBannerManager;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterFilmManager;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.OnItemClickFilmManager;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Banners;
import trong.fpt.duan1_nhom4_cp17310.models.Film;

public class QuanLyFilmsActivity extends AppCompatActivity implements OnItemClickFilmManager {

    private ImageView iv_trangchu_films;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageButton btn_insert_film;
    private RecyclerView rv_manager_film;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_films);
        iv_trangchu_films = findViewById(R.id.iv_trangchu_films);

        btn_insert_film = findViewById(R.id.btn_insert_film);
        rv_manager_film = findViewById(R.id.rv_film);


        iv_trangchu_films.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuanLyFilmsActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        btn_insert_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLyFilmsActivity.this, InsertFilmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        db.collection("films")
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
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuanLyFilmsActivity.this);
                            rv_manager_film.setLayoutManager(layoutManager);
                            AdapterFilmManager adapterFilmManager = new AdapterFilmManager(QuanLyFilmsActivity.this, list);
                            rv_manager_film.setAdapter(adapterFilmManager);
                        }
                    }
                });
    }

    @Override
    public void onItemClickDelete(Film film) {
        new AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage("Deletion will not restore")
                .setIcon(R.drawable.attention_warning_14525)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("films")
                                .document(film.getIdFilm())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new AlertDialog.Builder(QuanLyFilmsActivity.this)
                                                .setTitle("Notification")
                                                .setMessage("Delete successfully")
                                                .setIcon(R.drawable.attention_warning_14525)
                                                .setPositiveButton("OK", null)
                                                .show();
                                        getData();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        new AlertDialog.Builder(QuanLyFilmsActivity.this)
                                                .setTitle("Notification")
                                                .setMessage("Delete failed")
                                                .setIcon(R.drawable.attention_warning_14525)
                                                .setPositiveButton("OK", null)
                                                .show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onItemClickUpdate(Film film) {
        Intent intent = new Intent(QuanLyFilmsActivity.this, UpdateFilmActivity.class);
        intent.putExtra("film", film);
        startActivity(intent);
    }
}