package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Users;

public class ChangePassActivity extends AppCompatActivity {
    private Button btn_changepass;
    private String idUser = "";
    private String matKhau = "";
    private TextInputEditText ip_oldPassword, ip_newPassword, ip_enterNewPassword;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        btn_changepass = findViewById(R.id.btn_changepass);
        ip_oldPassword = findViewById(R.id.ip_oldpass);
        ip_newPassword = findViewById(R.id.ip_newpass);
        ip_enterNewPassword = findViewById(R.id.ip_enternewpass);
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("idUser", "");
        matKhau = sharedPreferences.getString("matkhau", "");

        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = ip_newPassword.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "");
                int loaiTaiKhoan = sharedPreferences.getInt("loaiTaiKhoan", 0);

                if(validate()>0){
                    // Create a new user with a first and last name
                    Map<String, Object> users = new HashMap<>();
                    users.put("matKhau", newPass);
                    users.put("loaiTaiKhoan", loaiTaiKhoan);
                    users.put("tenTaiKhoan", email);

                    db.collection("users")
                            .document(idUser)
                            .set(users)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new AlertDialog.Builder(ChangePassActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Changepass successfully")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.clear();
                                                    editor.apply();
                                                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new AlertDialog.Builder(ChangePassActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Changepass failed")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            });
                }
                else
                {
                    new AlertDialog.Builder(ChangePassActivity.this)
                            .setTitle("Notification")
                            .setMessage("Changepass failed")
                            .setPositiveButton("OK", null)
                            .show();
                }

            }
        });
    }


    public int validate() {
        int check = 1;
        if (ip_oldPassword.getText().length() == 0 || ip_newPassword.getText().length() == 0 || ip_enterNewPassword.getText().length() == 0) {
            Toast.makeText(this, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
            idUser = sharedPreferences.getString("idUser", "");
            matKhau = sharedPreferences.getString("matkhau", "");
            String pass = ip_newPassword.getText().toString();
            String rePass = ip_enterNewPassword.getText().toString();
            if (!matKhau.equals(ip_oldPassword.getText().toString())) {
                Toast.makeText(this, "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                check = -1;
            }
            if (!pass.equals(rePass)) {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }


}