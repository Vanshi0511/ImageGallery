package com.example.imagegallery.repositoryandcallback;

import android.graphics.Bitmap;

import com.example.imagegallery.model.Photo;

import java.util.List;


public interface CallbackInterface {

    void updateImagesList(List<Photo> photoList);


    void updateImageBitmap(Bitmap bitmap);
}
