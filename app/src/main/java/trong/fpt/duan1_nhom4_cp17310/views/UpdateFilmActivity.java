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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterFilmManager;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Film;

public class UpdateFilmActivity extends AppCompatActivity {

    private ImageView iv_image_update_film, imv_choose_update;
    private TextInputEditText ip_suaTenPhim, ip_suaNgay, ip_suaGiaVe, ip_noiDungPhim;
    private Button btn_update_film, btn_cancel_update_film;
    final int REQUESTCODE_READ_EXTERNAL_STORAGE = 120;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String linkDL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_film);

        iv_image_update_film = findViewById(R.id.iv_image_update_film);
        imv_choose_update = findViewById(R.id.imv_choose_update);
        ip_suaTenPhim = findViewById(R.id.ip_suaTenPhim);
        ip_suaNgay = findViewById(R.id.ip_suaNgay);
        ip_suaGiaVe = findViewById(R.id.ip_suaGiaVe);
        btn_update_film = findViewById(R.id.btn_update_film);
        btn_cancel_update_film = findViewById(R.id.btn_cancel_update_film);
        ip_noiDungPhim = findViewById(R.id.ip_noiDungPhim);

        Intent intent = getIntent();
        Film film = (Film) intent.getSerializableExtra("film");

        ip_suaTenPhim.setText(film.getTenFilm());
        ip_suaNgay.setText(film.getNgayChieu());
        ip_suaGiaVe.setText(film.getGiaVe());
        linkDL = film.getLinkAnh();
        ip_noiDungPhim.setText(film.getDetails());

        new DownloadImageFromInternet(iv_image_update_film).execute(linkDL);

        imv_choose_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectPicture(null);
            }
        });

        btn_cancel_update_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateFilmActivity.this, QuanLyFilmsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_update_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenPhim = ip_suaTenPhim.getText().toString();
                String ngayKhoiChieu = ip_suaNgay.getText().toString();
                String giaVe = ip_suaGiaVe.getText().toString();
                String details = ip_noiDungPhim.getText().toString();

                if (tenPhim.isEmpty()) {
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập tên phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (ngayKhoiChieu.isEmpty()) {
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập ngày khởi chiếu")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (giaVe.isEmpty()) {
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập giá vé")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                } else if (checkGiaVe(giaVe)== false) {
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập sai giá vé")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();

                }else if(checkDate(ngayKhoiChieu) == false){
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập sai định dạng ngày khởi chiếu")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }else if(linkDL.length() ==0){
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Chọn poster phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }
                else if(details.length() ==0){
                    new AlertDialog.Builder(UpdateFilmActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Nhập nội dung phim")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }
                else {

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("tenPhim", tenPhim);
                user.put("ngayKhoiChieu", ngayKhoiChieu);
                user.put("giaVe", giaVe);
                user.put("linkAnh", linkDL);
                user.put("details", details);

                db.collection("films")
                        .document(film.getIdFilm())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                new AlertDialog.Builder(UpdateFilmActivity.this)
                                        .setTitle("Notification")
                                        .setMessage("Film updated successfully")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(UpdateFilmActivity.this, QuanLyFilmsActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new AlertDialog.Builder(UpdateFilmActivity.this)
                                        .setTitle("Notification")
                                        .setMessage("Updated film failed")
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });
            }}
        });
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
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
                        iv_image_update_film.setImageBitmap(bitmap);
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
                new AlertDialog.Builder(UpdateFilmActivity.this)
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