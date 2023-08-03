package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Interfaces.DateValidator;
import trong.fpt.duan1_nhom4_cp17310.R;

public class InsertFilmActivity extends AppCompatActivity {

    private ImageView imv_choose_image_film, iv_anh_quanli_phim;
    private TextInputEditText ip_themTenPhim, ip_themNgay, ip_themGiaVe, ip_noiDungPhim;
    private Button btn_insert_film, btn_cancel_insert_film;
    final int REQUESTCODE_READ_EXTERNAL_STORAGE = 120;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String linkDL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_film);

        imv_choose_image_film = findViewById(R.id.imv_choose_image_film);
        iv_anh_quanli_phim = findViewById(R.id.iv_anh_quanli_phim);
        ip_themTenPhim = findViewById(R.id.ip_themTenPhim);
        ip_themNgay = findViewById(R.id.ip_themNgay);
        ip_themGiaVe = findViewById(R.id.ip_themGiaVe);
        btn_insert_film = findViewById(R.id.btn_insert_film);
        btn_cancel_insert_film = findViewById(R.id.btn_cancel_insert_film);
        ip_noiDungPhim = findViewById(R.id.ip_noiDungPhim);

        imv_choose_image_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectPicture(null);
            }
        });

        btn_cancel_insert_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertFilmActivity.this, QuanLyFilmsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_insert_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String tenPhim = ip_themTenPhim.getText().toString();
                String ngayKhoiChieu = ip_themNgay.getText().toString();
                String giaVe = ip_themGiaVe.getText().toString();
                String link = linkDL;
                String details = ip_noiDungPhim.getText().toString();


                if (tenPhim.isEmpty()) {
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập tên phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (ngayKhoiChieu.isEmpty()) {
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập ngày khởi chiếu")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (giaVe.isEmpty()) {
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập giá vé")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (checkGiaVe(giaVe)== false) {
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập sai giá vé")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();

                }else if(checkDate(ngayKhoiChieu) == false){
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập sai định dạng ngày khởi chiếu")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }else if(link.length() ==0){
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Chọn poster phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }
                else if(details.length() ==0){
                    new AlertDialog.Builder(InsertFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập nội dung phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }
                else {

                    Map<String, Object> film = new HashMap<>();
                    film.put("linkAnh", link);
                    film.put("giaVe", giaVe);
                    film.put("ngayKhoiChieu", ngayKhoiChieu);
                    film.put("tenPhim", tenPhim);
                    film.put("details", details);

                    // Add a new document with a generated ID
                    db.collection("films")
                            .add(film)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    new AlertDialog.Builder(InsertFilmActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Film added successfully")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(InsertFilmActivity.this, QuanLyFilmsActivity.class);
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
                                    new AlertDialog.Builder(InsertFilmActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Add film failed")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            });
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onSelectPicture(View view) {
        Boolean isPermissionAllowed = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (isPermissionAllowed) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            selectCapture.launch(intent);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    selectCapture.launch(intent);
                }
                break;
            }
            default:
                break;
        }
    }

    ActivityResultLauncher<Intent> selectCapture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    Uri uri = intent.getData();
                    Bitmap bitmap;
                    try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        iv_anh_quanli_phim.setImageBitmap(bitmap);
                        uploadToFirebase(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    private void uploadToFirebase(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference imgaeReference = storageReference.child(Calendar.getInstance().getTimeInMillis() + ".jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        UploadTask uploadTask = imgaeReference.putBytes(bytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return imgaeReference.getDownloadUrl();
                }
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri dowloadUri = task.getResult();
                    linkDL = dowloadUri + "";
                }
            }
        });
    }

   private Boolean checkDate(String ngayKhoiChieu){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
       Date ngay_khoan = null;
       try {
           ngay_khoan = simpleDateFormat.parse(ngayKhoiChieu);
           return  true;
       } catch (Exception e) {
           ngay_khoan = null;
           return  false;
       }
   }


    private Boolean checkGiaVe(String giaVe){
        int giave = 0;
        try {
            giave = Integer.parseInt(giaVe);
            if(giave < 0){
                new AlertDialog.Builder(InsertFilmActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Nhập giá vé sai")
                        .setIcon(R.drawable.attention_warning_14525)
                        .setPositiveButton("OK", null)
                        .show();
                return  false;
            }
            return  true;
        } catch (Exception e) {
            return  false;
        }
    }

}