package com.example.treeco;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Hdr;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TagActivity1 extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 1;
    ImageView imgshow, submitimg, back,backgroundImg;
    TextView donetxt;
    Intent intent;
    int photoclicked = 0;
    int imgid = 0;
    ArrayList<Bitmap> AddImages;
    RecyclerView recyclerView;
    CameraView camera;
    FloatingActionButton captureBtn;
    ImageView closeBtn;
    ExtendedFloatingActionButton  doneButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag1);
        getSupportActionBar().hide();
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#32CB00"));
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);
//        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
//                + "tre" + "</small>" + "" + "<big>" + "e"
//                + "</big>" + "" + "<small>" + "co" + "</small>"));
        camera = findViewById(R.id.camera);
        captureBtn=findViewById(R.id.captureBtn);
        closeBtn=findViewById(R.id.closebtn);
        doneButton2=findViewById(R.id.doneButton2);


        captureBtn.setOnClickListener(this);
        doneButton2.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        backgroundImg=findViewById(R.id.background_image);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(8 * 2));
        AddImages = new ArrayList<Bitmap>();
        camera.setLifecycleOwner(this);
        camera.setHdr(Hdr.OFF);
        camera.setSnapshotMaxWidth(700);
        camera.setSnapshotMaxHeight(700);
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                result.toFile(new File(getExternalFilesDir(null), "myImage.jpg"), new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {
                        if (file != null) {
                            // Convert file to bitmap
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                            // Save bitmap to directory
                            createDirectoryAndSaveImage(bitmap, "/img" + imgid + ".jpg");
                            RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(getApplicationContext(),AddImages);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            imgid = imgid + 1;
                            photoclicked = 1;
                            file.delete();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.submitimg) {
//            Toast.makeText(this, "click photo", Toast.LENGTH_LONG).show();
//
//        }
        if (v.getId() == R.id.captureBtn) {
            //Toast.makeText(this, "clicked ", Toast.LENGTH_LONG).show();
//            intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            startActivityForResult(intent, 7);
            if(imgid<4)
                camera.takePictureSnapshot();
            else
                Toast.makeText(TagActivity1.this, "Maximum no of photos are clicked", Toast.LENGTH_SHORT).show();

        }
        if (v.getId() == R.id.closebtn) {
            //Toast.makeText(this, " back clicked ", Toast.LENGTH_LONG).show();
//            Intent iback = new Intent(this, UserDashboard.class);
//            startActivity(iback);
            backgroundImg.setVisibility(GONE);
            LinearLayout linearLayout=findViewById(R.id.linearLayout);
            linearLayout.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
            doneButton2.setVisibility(View.VISIBLE);
            captureBtn.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.doneButton2) {

            if (photoclicked != 0) {
                Intent i = new Intent(this, TagActivity2.class);
                startActivity(i);
            } else {
                Toast.makeText(this, " Click photo first ", Toast.LENGTH_LONG).show();
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 7 && resultCode == RESULT_OK) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imgshow.setImageBitmap(bitmap);
//            createDirectoryAndSaveImage(bitmap, "/img" + imgid + ".jpg");
//
//
//            galleryImageAdapter = new GalleryImageAdapter(TagActivity1.this, AddImages);
//            gallery.setAdapter(galleryImageAdapter);
//            imgid = imgid + 1;
//            photoclicked = 1;
//        }
    }

    private void createDirectoryAndSaveImage(Bitmap imageToSave, String fileName) {
        //File direct = new File(Environment.getExternalStorageDirectory() + "/TempDirfortreeco");
        File direct = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco");
        //directory.mkdirs();

        if (!direct.exists()) {
            File wallpaperDirectory = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco");
            boolean storedir = wallpaperDirectory.mkdirs();
            //Toast.makeText(this, "i am inside creation" + storedir, Toast.LENGTH_LONG).show();
        }

        File file = new File(new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            boolean result = imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // Toast.makeText(this, "i am inside" + result, Toast.LENGTH_LONG).show();

            out.flush();
            out.close();

            File imgFile = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco/" + fileName);

            if (imgFile.exists()) {
                // Toast.makeText(this,"i am inside image file exist",Toast.LENGTH_LONG).show();
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                AddImages.add(myBitmap);
                //Toast.makeText(this, "number of images" + AddImages.size(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}