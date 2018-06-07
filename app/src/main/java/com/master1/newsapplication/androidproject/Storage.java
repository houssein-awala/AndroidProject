package com.master1.newsapplication.androidproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {
    private Context context;
    private StorageReference mStorageRef;
    static File dir;
    FullNews news;
    String path;

    public Storage(Context context,FullNews news) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        this.context=context;
        this.news=news;
        if(dir==null)
            dir=new File(context.getFilesDir(),"news");
        path=dir.getAbsolutePath()+File.pathSeparator+news.getCategorie()+File.pathSeparator+news.getId();
    }

    public void DownloadAll()
    {

    }

    public void DownloadMain() throws IOException {

        final File localFile = new File(path+File.pathSeparatorChar+"main.png");
        System.out.println(localFile.getAbsolutePath());
        //System.out.println("sssssssssssssssss"+news.getPathMainPhot());
        StorageReference riversRef = mStorageRef.child(news.getPathMainPhot());
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        news.setMainPhoto(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                        System.out.println("cccc");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    public void DownloadSub() throws IOException {

        StorageReference dir = mStorageRef.child(news.getPathPhotos());
        ArrayList<String> paths=new ArrayList<>();//getmn7sen();
        for(String mp:paths)
        {
            StorageReference riversRef = dir.child(mp);
            final File localFile = new File(path+File.pathSeparator+"Photos"+File.pathSeparator+mp);
            riversRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            news.addPhoto(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
    }
}
