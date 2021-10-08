package com.example.insect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.insect.Retrofit2.APIUtils;
import com.example.insect.Retrofit2.DataClient;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    String DATABASE_NAME = "insect.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    String currentPhotoPath;
    LinearLayout btnCamera, btnThuVien, btnInsect, btnGuide, btnAbout;
    int REQUEST_CODE_LIBRARY = 121;
    int REQUEST_CODE_CAMERA = 122;
    private static final int READ_CAMERA_CODE = 12;
    String realPath="";
    CarouselView carouselView;
    int[] image = {R.drawable.img1, R.drawable.img2, R.drawable.img3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xinQuyen();
        copy();
        anhXa();
        addEvent();
    }

    private void xinQuyen() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        123);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    thongBaoTuChoi();
                }
                return;
            }
        }
    }

    private void thongBaoTuChoi() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Bạn đã từ chối cấp quyền");
        alertDialogBuilder.setMessage("Ứng dụng sẽ không thể hoạt động khi bạn từ chối cấp quyền. Vui lòng cấp quyền lại trong Cài đặt --> Ứng dụng hoặc gỡ và cài đặt lại phần mềm");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNeutralButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void copy() {
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists())
            {
                copyDatabaseFromAsset();
                Log.d("BBB","Copy thành công");
            }
        }
        catch (Exception e)
        {
            Log.d("LOI",e.toString());
        }
    }

    private void copyDatabaseFromAsset() {
        try {
            InputStream myinput = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists()) f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer=new byte[1024];
            int length;
            while ((length=myinput.read(buffer))>0)
            {
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myinput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDatabasePath() {
       return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void addEvent() {
        btnThuVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_LIBRARY);
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    }
                }
            }
        });
        btnInsect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DanhSachConTrungActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        uploadWithLibrary(requestCode, resultCode, data);
        uploadWithCamera(requestCode,resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadWithCamera(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_CAMERA && resultCode==RESULT_OK)
        {
            try {
                upload2();
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this,"Đã xảy ra lỗi. Vui lòng thử lại",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void uploadWithLibrary(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==REQUEST_CODE_LIBRARY && resultCode==RESULT_OK && data!=null)
        {
            Log.d("OK","OK");
            upload(data);
        }
    }
    public void upload(@Nullable Intent data)
    {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            File file = new File(realPath);
            String fileName = file.getAbsolutePath();
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file",fileName,requestBody);

            DataClient dataClient = APIUtils.getData();
            Call<String> call = dataClient.UploadPhoto(part);
            final ProgressDialog progressDialog;
            // Set up progress before call
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Đang nhận diện....");
            progressDialog.setTitle("Nhận diện côn trùng");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response!=null) {
                        openThongTin(response.body());
                        progressDialog.cancel();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this,"Thất bại",Toast.LENGTH_LONG).show();
                }
            });
    }
    public void upload2()
    {
        Log.d("FFF",currentPhotoPath);
        File file = new File(currentPhotoPath);
        String fileName = file.getAbsolutePath();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",fileName,requestBody);

        DataClient dataClient = APIUtils.getData();
        Call<String> call = dataClient.UploadPhoto(part);
        final ProgressDialog progressDialog;
        // Set up progress before call
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Đang nhận diện....");
        progressDialog.setTitle("Nhận diện côn trùng");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response!=null) {
                    openThongTin(response.body());
                    progressDialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(MainActivity.this,"Thất bại",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void openThongTin(String id)
    {
        Intent intent = new Intent(this,ThongTinActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void anhXa() {
        carouselView = findViewById(R.id.carouselViewBackground);
        carouselView.setPageCount(image.length);
        carouselView.setImageListener(imageListener);
        btnCamera = findViewById(R.id.btnCameraHome);
        btnThuVien = findViewById(R.id.btnThuVienHome);
        btnInsect = findViewById(R.id.btnInsectHome);
        btnAbout = findViewById(R.id.btnAboutHome);
        btnGuide = findViewById(R.id.btnGuideHome);
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(image[position]);
        }
    };
}
