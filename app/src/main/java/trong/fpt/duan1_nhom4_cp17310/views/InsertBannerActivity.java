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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.R;

public class InsertBannerActivity extends AppCompatActivity {

    private ImageView iv_storage, iv_img_banner;
    private Button btn_insert_banner;
    private TextInputEditText tv_description_banner;
    final int REQUESTCODE_READ_EXTERNAL_STORAGE = 120;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String linkDL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_banner);

        iv_storage = findViewById(R.id.iv_storage);
        iv_img_banner = findViewById(R.id.iv_img_banner);
        btn_insert_banner = findViewById(R.id.btn_insert_banner);
        tv_description_banner = findViewById(R.id.tv_description_banner);

        iv_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectPicture(null);
            }
        });

        btn_insert_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moTa = tv_description_banner.getText().toString();
                String link = linkDL;

                if (moTa.isEmpty() || link.length() == 0) {
                    new AlertDialog.Builder(InsertBannerActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Phải nhập đầy đủ thông tin")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }else if(link.length()==0){
                    new AlertDialog.Builder(InsertBannerActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Chọn banner")
                            .setIcon(R.drawable.attention_warning_14525)
                            .setPositiveButton("OK", null)
                            .show();
                }
                else {


                    Map<String, Object> banner = new HashMap<>();
                    banner.put("linkAnh", link);
                    banner.put("tenphim", moTa);

                    // Add a new document with a generated ID
                    db.collection("banner")
                            .add(banner)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    new AlertDialog.Builder(InsertBannerActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Banner added successfully")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(InsertBannerActivity.this, QuanLyBannerActivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new AlertDialog.Builder(InsertBannerActivity.this)
                                            .setTitle("Notification")
                                            .setMessage("Add banner failed")
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
                        iv_img_banner.setImageBitmap(bitmap);
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
}