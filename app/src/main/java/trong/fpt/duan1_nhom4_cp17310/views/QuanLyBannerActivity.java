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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterBannerManager;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.OnItemClickBannerManager;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Banners;

public class QuanLyBannerActivity extends AppCompatActivity implements OnItemClickBannerManager {
    private ImageView iv_trangchu_banner;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageButton flt_insert_banner;
    private RecyclerView rv_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_banner);
        flt_insert_banner = findViewById(R.id.btn_insert_banner);
        rv_banner = findViewById(R.id.rv_banner);
        iv_trangchu_banner = findViewById(R.id.iv_trangchu_banner);

        iv_trangchu_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuanLyBannerActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        flt_insert_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuanLyBannerActivity.this, InsertBannerActivity.class);
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
        db.collection("banner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Banners> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String linkAnh = map.get("linkAnh").toString();
                                String moTa = map.get("tenphim").toString();
                                Banners banners = new Banners(linkAnh, moTa);
                                banners.setIdBanners(document.getId());
                                list.add(banners);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuanLyBannerActivity.this);
                            rv_banner.setLayoutManager(layoutManager);
                            AdapterBannerManager adapterBannerManager = new AdapterBannerManager(QuanLyBannerActivity.this, list);
                            rv_banner.setAdapter(adapterBannerManager);
                        }

                    }
                });
    }

    @Override
    public void onItemClickDelete(Banners banners) {
        new AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage("Deletion will not restore")
                .setIcon(R.drawable.attention_warning_14525)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("banner")
                                .document(banners.getIdBanners())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new AlertDialog.Builder(QuanLyBannerActivity.this)
                                                .setTitle("Notification")
                                                .setMessage("Delete successfully")
                                                .setIcon(R.drawable.attention_warning_14525)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        getData();
                                                    }
                                                })
                                                .show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        new AlertDialog.Builder(QuanLyBannerActivity.this)
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
}