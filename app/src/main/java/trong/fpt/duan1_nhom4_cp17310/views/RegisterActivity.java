package trong.fpt.duan1_nhom4_cp17310.views;

import static trong.fpt.duan1_nhom4_cp17310.Services.LoginService.LOGIN_SERVICE_ACTION_SAVE_ACCOUNT;
import static trong.fpt.duan1_nhom4_cp17310.Services.LoginService.LOGIN_SERVICE_EVENT;
import static trong.fpt.duan1_nhom4_cp17310.Services.LoginService.REGISTER_SERVICE_ACTION_LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.Services.LoginService;
import trong.fpt.duan1_nhom4_cp17310.models.Users;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText ip_user, ip_passWord, ip_enterPass;
    private int dem = 0;
    private Button btn_register;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Users> dsTaiKhoan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ip_user = findViewById(R.id.ip_user);
        ip_passWord = findViewById(R.id.ip_pass);
        ip_enterPass = findViewById(R.id.ip_enterpass);
        btn_register = findViewById(R.id.btn_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ip_user.getText().toString();
                String passWord = ip_passWord.getText().toString();
                String enterPass = ip_enterPass.getText().toString();
                int loaiTaiKhoan = 0;

                if(user.length()==0 || passWord.length()==0 || enterPass.length() == 0){
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Bạn phải nhập đầy đủ thông tin: \n- Tên tài khoản\n- Mật khẩu")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }else if(!passWord.equalsIgnoreCase(enterPass)){
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Mật khẩu không trùng khớp")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }else{
                    Intent intentSV = new Intent(RegisterActivity.this, LoginService.class);
                    intentSV.setAction(REGISTER_SERVICE_ACTION_LOGIN);
                    intentSV.putExtra("name", user);
                    intentSV.putExtra("matKhau", passWord);
                    intentSV.putExtra("loaiTaiKhoan", loaiTaiKhoan);
                    startService(intentSV);
//                    insertUser(user, passWord, enterPass);
                }
            }
        });

    }




    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registerReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(LOGIN_SERVICE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(registerReceiver, intentFilter);

    }



    private BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean check = intent.getBooleanExtra("check", false);
            if(check){
                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        }
    };
}