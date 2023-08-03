package trong.fpt.duan1_nhom4_cp17310.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.views.BookedTicketActivity;
import trong.fpt.duan1_nhom4_cp17310.views.ChangePassActivity;
import trong.fpt.duan1_nhom4_cp17310.views.LoginActivity;
import trong.fpt.duan1_nhom4_cp17310.views.ProfileActivity;
import trong.fpt.duan1_nhom4_cp17310.views.QuanLyBannerActivity;
import trong.fpt.duan1_nhom4_cp17310.views.QuanLyFilmsActivity;
import trong.fpt.duan1_nhom4_cp17310.views.ThongKeActivity;

public class FragmentKhac extends Fragment {
    LinearLayout ln_quanlyphim, ln_quanlybanner, ln_thongtinuser, ln_logout, ln_bookedTickets, ln_changepass, ln_thongke;
    TextView tv_quanly_khac;
    //Google
    GoogleSignInClient gsc;
    GoogleSignInAccount account;

    //Facebook
    FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_khac, container, false);

        ln_quanlyphim = view.findViewById(R.id.ln_quanlyphim);
        ln_quanlybanner = view.findViewById(R.id.ln_quanlybanner);
        ln_thongtinuser = view.findViewById(R.id.ln_thongtinuser);
        ln_logout = view.findViewById(R.id.ln_logout);
        ln_bookedTickets = view.findViewById(R.id.ln_bookedTickets);
        ln_changepass = view.findViewById(R.id.ln_changepass);
        ln_thongke = view.findViewById(R.id.ln_thongke);
        tv_quanly_khac = view.findViewById(R.id.tv_quanly_khac);

        account = GoogleSignIn.getLastSignedInAccount(view.getContext());

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(view.getContext(), gso);

        mFirebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
        int loaiTaiKhoan = sharedPreferences.getInt("loaiTaiKhoan", 10);
        if (loaiTaiKhoan != 1) {
            ln_quanlyphim.setVisibility(View.GONE);
            ln_quanlybanner.setVisibility(View.GONE);
            ln_thongke.setVisibility(View.GONE);
            tv_quanly_khac.setVisibility(View.GONE);
        }

        ln_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChangePassActivity.class);
                startActivity(i);
            }
        });

        ln_quanlyphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), QuanLyFilmsActivity.class);
                startActivity(i);
            }
        });

        ln_bookedTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BookedTicketActivity.class);
                startActivity(i);
            }
        });

        ln_thongtinuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                startActivity(i);
            }
        });

        ln_quanlybanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), QuanLyBannerActivity.class);
                startActivity(i);
            }
        });

        ln_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (account != null) {
                    gsc.signOut().addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent homeIntent = new Intent(view.getContext(), LoginActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeIntent);
                        }
                    });
                } else if (mFirebaseAuth != null) {
                    mFirebaseAuth.signOut();
                    LoginManager.getInstance().logOut();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        ln_thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ThongKeActivity.class);
                startActivity(i);
            }
        });


        return view;

    }
}
