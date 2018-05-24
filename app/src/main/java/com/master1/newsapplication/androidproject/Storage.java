package com.master1.newsapplication.androidproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    private Context context;
    private StorageReference mStorageRef;

    public Storage(Context context) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    public void getImage(final ImageView view) throws IOException {

        final File localFile = File.createTempFile("news0", "png");
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("news/news0.png");
        //Toast.makeText(context, riversRef.getName(), Toast.LENGTH_SHORT).show();
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        //Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0,400, 400, null, true);
                        view.setImageBitmap(bitmap);
                        System.out.println(taskSnapshot.getTotalByteCount());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
        /*Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(riversRef)
                .into(view);*/

        riversRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                System.out.println(bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
}
