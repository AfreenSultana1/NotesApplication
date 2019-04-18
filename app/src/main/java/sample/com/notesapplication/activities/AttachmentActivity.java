package sample.com.notesapplication.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import sample.com.notesapplication.R;
import sample.com.notesapplication.adapters.AttachmentsAdapter;
import sample.com.notesapplication.datamodel.Attachment;

public class AttachmentActivity extends AppCompatActivity implements View.OnClickListener {
    public final int REQUEST_CODE_PICK_FILE = 106;
    public final int REQUEST_CODE_CAPTURE_IMAGE = 105;
    ViewPager viewPager;
    private static final String TAG = AttachmentActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        viewPager = findViewById(R.id.attach_viewpager);

        AttachmentsAdapter attachmentsAdapter = new AttachmentsAdapter(this);
        viewPager.setAdapter(attachmentsAdapter);

        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 103:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFileManager();
                } else {
                    showPermissionsAlert();
                }
                break;
            case 104:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkCameraPermissions();
                } else {
                    showPermissionsAlert();
                }

        }

    }

    private void checkCameraPermissions() {
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Please grant necessary permissions");
        builder.setPositiveButton("ok", null);
        builder.setCancelable(false);
        builder.show();
    }

    private void openFileManager() {
        Log.d(TAG, "openFileManager: ");
        if (ActivityCompat.checkSelfPermission(AttachmentActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AttachmentActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 103);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_IMAGE:

                processPickedFile(data);

                break;

        }
    }

    private void processPickedFile(Intent data) {
        Log.d(TAG, "processPickedFile: ");
        if (data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(uri);
                String mimeType = "image/jpeg";
                final Attachment attachment = new Attachment();
                attachment.fileType = mimeType;
                generateImagePrev(inputStream, attachment);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createAdapter(attachment);
                    }
                });
               


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void createAdapter(Attachment attachment) {
        Log.d(TAG, "createAdapter: ");

        AttachmentsAdapter attachmentsAdapter = new AttachmentsAdapter(AttachmentActivity.this);
        attachmentsAdapter.getAttachmentArrayList().add(attachment);
        viewPager.setAdapter(attachmentsAdapter);
    }

    private void initViews() {
        Button pickImage = findViewById(R.id.pickImage);
        Button captureImage = findViewById(R.id.captureImage);
        pickImage.setOnClickListener(this);
        captureImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pickImage:
                openFileManager();
                break;
            case R.id.captureImage:
                break;
        }
    }

    public void generateImagePrev(InputStream inputStream, Attachment attachment) {
        Log.d(TAG, "generateImagePrev: ");
        File file = new File(getFilesDir(), "45");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                fileOutputStream.write(buff, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            attachment.imagePath = file.getAbsolutePath();
            attachment.fileName = file.getName();
            attachment.uri = Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
