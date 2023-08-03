package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Pay;
import trong.fpt.duan1_nhom4_cp17310.models.SoGhe;

public class PayPalActivity extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Pay.PAYPAL_CLIENT_ID);
    private TextView tv_tenphim, tv_suatxem, tv_maghe, tv_ngayxemphim, tv_giave, tv_tentaikhoan;
    private Button btn_thanhtoan;
    String t_giaVe = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
        tv_tenphim = findViewById(R.id.tv_tenphim);
        tv_suatxem = findViewById(R.id.tv_suatxem);
        tv_maghe = findViewById(R.id.tv_maghe);
        tv_ngayxemphim = findViewById(R.id.tv_ngayxemphim);
        tv_giave = findViewById(R.id.tv_giave);
        tv_tentaikhoan = findViewById(R.id.tv_tentaikhoan);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);

        Intent intent = getIntent();
        int t_soghe = intent.getIntExtra("t_soghe", 0);
        int t_trangThai = intent.getIntExtra("t_trangThai", 1);
        SoGhe soGhe = (SoGhe) intent.getSerializableExtra("soGhe");
        t_giaVe = intent.getStringExtra("t_giaVe");
        String t_ngayXemPhim = intent.getStringExtra("t_ngayXemPhim");
        String t_soLuong = intent.getStringExtra("t_soLuong");
        String t_suatXem = intent.getStringExtra("t_suatXem");
        String t_tenPhim = intent.getStringExtra("t_tenPhim");
        String t_tenTaiKhoan = intent.getStringExtra("t_tenTaiKhoan");
        String t_maGhe = intent.getStringExtra("t_maGhe");

        tv_tenphim.setText(t_tenPhim);
        tv_suatxem.setText(t_suatXem);
        tv_maghe.setText(t_maGhe);
        tv_ngayxemphim.setText(t_ngayXemPhim);
        tv_giave.setText(t_giaVe);
        tv_tentaikhoan. setText(t_tenTaiKhoan  + "  "+ t_ngayXemPhim);

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processsPayment();
            }
        });

        Intent intent1 = new Intent(this, PayPalService.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent1);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void processsPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(t_giaVe), "USD", "Donate for EDMTDev", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                        Toast.makeText(this, "Thanh toans thanhf coong", Toast.LENGTH_SHORT).show();
                    try {
                        String  paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", t_giaVe));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}