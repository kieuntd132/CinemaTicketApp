package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Users;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iv_home;
    private TextView tv_welcome, tv_tentaikhoan, tv_loaiTK;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Users> dstTaiKhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iv_home = findViewById(R.id.iv_home);
        tv_welcome = findViewById(R.id.tv_welcome);
        tv_tentaikhoan = findViewById(R.id.tv_tentaikhoan);
        tv_loaiTK = findViewById(R.id.tv_loaiTK);
        dstTaiKhoan = new ArrayList<>();

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("email", "");
        getDataUsers(username);
    }

    private void getDataUsers(String username){
        ArrayList<Users> list = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("tenTaiKhoan", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String tenTaiKhoan = map.get("tenTaiKhoan").toString();
                                String matKhau = map.get("matKhau").toString();
                                Integer loaiTaiKhoan = Integer.valueOf(map.get("loaiTaiKhoan").toString());
                                Users users = new Users(tenTaiKhoan, matKhau, loaiTaiKhoan);
                                users.setIdUser(document.getId());
                                list.add(users);
                                dstTaiKhoan.add(users);
                            }
                        if(dstTaiKhoan.size()!= 0){
                            Users users = dstTaiKhoan.get(0);
                            tv_welcome.setText(users.getTenTaiKhoan());
                            tv_tentaikhoan.setText(users.getTenTaiKhoan());
                            if(users.getLoaiTaiKhoan()==1){
                                tv_loaiTK.setText("Admin");
                            }else {
                                tv_loaiTK.setText("User");
                            }
                        }
                        }

                    }
                });
    }
}