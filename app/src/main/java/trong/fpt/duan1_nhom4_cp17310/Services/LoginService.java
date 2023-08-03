package trong.fpt.duan1_nhom4_cp17310.Services;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Users;
import trong.fpt.duan1_nhom4_cp17310.views.LoginActivity;
import trong.fpt.duan1_nhom4_cp17310.views.MainActivity;

public class LoginService extends IntentService {

    public static final String LOGIN_SERVICE_EVENT = "LOGIN_SERVICE_EVENT";
    public static final String LOGIN_SERVICE_ACTION_LOGIN = "LOGIN_SERVICE_ACTION_LOGIN";
    public static final String REGISTER_SERVICE_ACTION_LOGIN = "REGISTER_SERVICE_ACTION_LOGIN";
    public static final String LOGIN_SERVICE_ACTION_SAVE_ACCOUNT = "LOGIN_SERVICE_ACTION_SAVE_ACCOUNT";

    private FirebaseFirestore db;
    private ArrayList<Users> dsTaiKhoan;
    private int dem = 0;

    public LoginService() {
        super("LoginService");
        db = FirebaseFirestore.getInstance();
        dsTaiKhoan = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case LOGIN_SERVICE_ACTION_LOGIN: {
                    String tenTaiKhoan = intent.getStringExtra("tenTaiKhoan");
                    String matKhau = intent.getStringExtra("matKhau");
                    ArrayList<Users> list = new ArrayList<>();
                    db.collection("users")
                            .whereEqualTo("tenTaiKhoan", tenTaiKhoan)
                            .whereEqualTo("matKhau", matKhau)
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
                                            dsTaiKhoan.add(users);
                                        }

                                    }
                                    if(dsTaiKhoan.size()==0){
                                        Users users = null;
                                        Intent outIntent = new Intent(LOGIN_SERVICE_EVENT);
                                        outIntent.putExtra("result", users);
                                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(outIntent);
                                    }else {
                                        Users users = dsTaiKhoan.get(0);
                                        Intent outIntent = new Intent(LOGIN_SERVICE_EVENT);
                                        outIntent.putExtra("result", users);
                                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(outIntent);
                                    }
                                }
                            });
                    break;
                }
                case LOGIN_SERVICE_ACTION_SAVE_ACCOUNT:{
                    String name = intent.getStringExtra("name");
                    String matKhau = intent.getStringExtra("matKhau");
                    int loaiTaiKhoan = intent.getIntExtra("loaiTaiKhoan", 0);

                    ArrayList<Users> list = new ArrayList<>();

                    db.collection("users")
                            .whereEqualTo("tenTaiKhoan", name)
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
                                            list.add(users);
                                        }
                                        dsTaiKhoan = list;
                                        if (list.size() == 0) {
                                            Map<String, Object> users = new HashMap<>();
                                            users.put("tenTaiKhoan", name);
                                            users.put("matKhau", matKhau);
                                            users.put("loaiTaiKhoan", 0);
                                            db.collection("users")
                                                    .add(users)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            for (int i = 0; i < list.size(); i++) {
                                                String tenTaiKhoans = list.get(i).getTenTaiKhoan();
                                                if (tenTaiKhoans.equalsIgnoreCase(name)) {
                                                    dem = 1;
                                                    break;
                                                }
                                            }
                                            if (dem == 0) {

                                                Map<String, Object> users = new HashMap<>();
                                                users.put("tenTaiKhoan", name);
                                                users.put("matKhau", matKhau);
                                                users.put("loaiTaiKhoan", loaiTaiKhoan);

                                                db.collection("users")
                                                        .add(users)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }else {
                                                Toast.makeText(getApplicationContext(), "This account has already existed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                    Intent outIntent = new Intent(LOGIN_SERVICE_EVENT);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(outIntent);
                    break;
                }
                case REGISTER_SERVICE_ACTION_LOGIN:{
                    String user = intent.getStringExtra("name");
                    String matKhau = intent.getStringExtra("matKhau");
                    int loaiTaiKhoan = intent.getIntExtra("loaiTaiKhoan", 0);

                    ArrayList<Users> list = new ArrayList<>();

                    db.collection("users")
                            .whereEqualTo("tenTaiKhoan", user)
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
                                            list.add(users);
                                        }
                                        dsTaiKhoan = list;
                                        if (list.size() == 0) {
                                            Map<String, Object> users = new HashMap<>();
                                            users.put("tenTaiKhoan", user);
                                            users.put("matKhau", matKhau);
                                            users.put("loaiTaiKhoan", 0);
                                            db.collection("users")
                                                    .add(users)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                                                            Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                                                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent1);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            for (int i = 0; i < list.size(); i++) {
                                                String tenTaiKhoans = list.get(i).getTenTaiKhoan();
                                                if (tenTaiKhoans.equalsIgnoreCase(user)) {
                                                    dem = 1;
                                                    break;
                                                }
                                            }
                                            if (dem == 0) {

                                                Map<String, Object> users = new HashMap<>();
                                                users.put("tenTaiKhoan", user);
                                                users.put("matKhau", matKhau);
                                                users.put("loaiTaiKhoan", loaiTaiKhoan);

                                                db.collection("users")
                                                        .add(users)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                                                                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                                                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent1);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }else {
                                                Toast.makeText(getApplicationContext(), "This account has already existed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                    Intent outIntent = new Intent(LOGIN_SERVICE_EVENT);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(outIntent);
                    break;
                }
            }


        }

    }
}