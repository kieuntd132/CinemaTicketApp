package trong.fpt.duan1_nhom4_cp17310.views;

import static trong.fpt.duan1_nhom4_cp17310.Services.LoginService.LOGIN_SERVICE_ACTION_LOGIN;
import static trong.fpt.duan1_nhom4_cp17310.Services.LoginService.LOGIN_SERVICE_ACTION_SAVE_ACCOUNT;
import static trong.fpt.duan1_nhom4_cp17310.Services.LoginService.LOGIN_SERVICE_EVENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.Services.LoginService;
import trong.fpt.duan1_nhom4_cp17310.models.Users;

public class LoginActivity extends AppCompatActivity {

    private CheckBox chkShowPassword;
    private TextInputEditText tietPassword;
    private TextView tv_register;
    private Button btn_login;
    private  String matKhau = "";
    private TextInputEditText editTextInputLayOutName, editTextInputLayOutPass;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Users> dsTaiKhoan = new ArrayList<>();
    private int dem = 0;

    //Đăng nhập google
    GoogleSignInClient gsc;
    GoogleSignInAccount account;

    //Đăng nhập facebook
    Button loginFacebook;
    CallbackManager mCallbackManager;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        chkShowPassword = findViewById(R.id.chkShowPassword);
        tietPassword = findViewById(R.id.EditTextInputLayOutPass);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        editTextInputLayOutName = findViewById(R.id.EditTextInputLayOutName);
        editTextInputLayOutPass = findViewById(R.id.EditTextInputLayOutPass);


        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        chkShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    tietPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    tietPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Đăng nhập google

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(LoginActivity.this, gso);
        //Kiểm tra login Google
        account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (account != null) {
            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();

        }

        Button sib = findViewById(R.id.bt_gg);
        sib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent googleIntent = gsc.getSignInIntent();
                googleLauncher.launch(googleIntent);
            }
        });

        /**
         * Start đăng nhập Facebook
         */
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginFacebook = findViewById(R.id.bt_fb);
        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "email", "user_birthday"));
                LoginManager.getInstance().registerCallback(mCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                handleFacebookAccessToken(loginResult.getAccessToken());
                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                                try {
                                                    String email = jsonObject.getString("email");
                                                    String birthday = jsonObject.getString("birthday");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onError(@NonNull FacebookException e) {
                            }
                        });
            }
        });

        /**
         * End đăng nhập Facebook
         */
    }

    public void onLoginClick(View view) {
        String tenTaiKhoan = editTextInputLayOutName.getText().toString();
        matKhau = editTextInputLayOutPass.getText().toString();
        if (tenTaiKhoan.length() == 0 || matKhau.length() == 0) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn phải nhập đầy đủ thông tin: \n- Tên tài khoản\n- Mật khẩu")
                    .setIcon(R.drawable.attention_warning_14525)
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            Intent intent = new Intent(LoginActivity.this, LoginService.class);
            intent.setAction(LOGIN_SERVICE_ACTION_LOGIN);
            intent.putExtra("tenTaiKhoan", tenTaiKhoan);
            intent.putExtra("matKhau", matKhau);
            startService(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginFBGGReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        readLogin();
        IntentFilter intentFilter = new IntentFilter(LOGIN_SERVICE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(loginReceiver, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(loginFBGGReceiver, intentFilter);

    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Users users = (Users) intent.getSerializableExtra("result");
            if (users == null) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Sai thông tin tài khoản")
                        .setIcon(R.drawable.attention_warning_14525)
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                writeLogin(users);
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);

            }
        }
    };

    private BroadcastReceiver loginFBGGReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    ActivityResultLauncher<Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        String email = account.getEmail();
                        matKhau = "";
                        int loaiTaiKhoan = 0;
                        Log.d(">>>TAG", "onActivityResult: " + email);
                        //Chuyển qua màn hình MainActivity
                        if (account != null) {
                            Intent intentSV = new Intent(LoginActivity.this, LoginService.class);
                            intentSV.setAction(LOGIN_SERVICE_ACTION_SAVE_ACCOUNT);
                            intentSV.putExtra("name", email);
                            intentSV.putExtra("matKhau", matKhau);
                            intentSV.putExtra("loaiTaiKhoan", loaiTaiKhoan);
                            startService(intentSV);
                            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.d(">>>TAG", "onActivityResult error: " + e.getMessage());
                    }
                }
            }
    );

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            String name = user.getEmail();
                            matKhau = "";
                            int loaiTaiKhoan = 0;

                            Intent intentSV = new Intent(LoginActivity.this, LoginService.class);
                            intentSV.setAction(LOGIN_SERVICE_ACTION_SAVE_ACCOUNT);
                            intentSV.putExtra("name", name);
                            intentSV.putExtra("matKhau", matKhau);
                            intentSV.putExtra("loaiTaiKhoan", loaiTaiKhoan);
                            startService(intentSV);

                            writeLogin(new Users(name, matKhau, loaiTaiKhoan));

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        readLogin();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i("TAG", "onStart: Someone logged in <3");
            readLogin();
        } else {
            Log.i("TAG", "onStart: No one logged in :/");
        }
    }

    private void writeLogin(Users users) {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", users.getTenTaiKhoan());
        editor.putString("idUser", users.getIdUser());
        editor.putString("matkhau", users.getMatKhau());
        editor.putInt("loaiTaiKhoan", users.getLoaiTaiKhoan());
        editor.commit();
    }

    private void readLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        Boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String email = sharedPreferences.getString("email", "email");
        if (isLoggedIn) {
            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent1);
        }
    }

}