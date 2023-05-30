package com.example.imagegallery.viewmodel;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imagegallery.model.Photo;
import com.example.imagegallery.repositoryandcallback.CallbackInterface;
import com.example.imagegallery.repositoryandcallback.DownloadImageTask;
import com.example.imagegallery.repositoryandcallback.FetchImageUrlsTask;
import com.example.imagegallery.utils.ImagesCache;
import com.example.imagegallery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class ImagesViewModel extends ViewModel {


    ImagesCache cache;

    MutableLiveData<List<Photo>> photoList;
    MutableLiveData<Bitmap> bitmap;


    public MutableLiveData<List<Photo>> getPhotoList() {
        if(photoList==null)
        {
            photoList=new MutableLiveData<>();
            photoList.setValue(new ArrayList<Photo>());
        }
        return photoList;
    }

    public void setPhotoList(List<Photo> newPhotoList) {

        Log.d("ViewModel","set photolist");
        if(photoList==null){
            photoList=new MutableLiveData<>();
        }
        List<Photo> existing= photoList.getValue();
        existing.addAll(newPhotoList);

        photoList.setValue(existing);
    }

    public MutableLiveData<Bitmap> getBitmap() {
        if(bitmap==null)
        {
            bitmap=new MutableLiveData<>();
        }
        return bitmap;
    }

    public void setBitmap(Bitmap newBitmap) {

        Log.d("ViewModel","set bitmap");
        if(bitmap==null){
            bitmap=new MutableLiveData<>();
        }

        bitmap.setValue(newBitmap);
    }


    public void initCache() {
        cache = ImagesCache.getInstance();
        cache.initializeCache();
    }

    public void fetchImagesUrl(int pageCount, CallbackInterface callbackInterface) {
        // fetch images list from api
        FetchImageUrlsTask fetchImagesTask = new FetchImageUrlsTask(callbackInterface);
        Utility.execute(fetchImagesTask, String.valueOf(pageCount));
    }

    public void fetchBitmap(String imgUrl,CallbackInterface callbackInterface) {
        Bitmap bm = cache.getImageFromWarehouse(imgUrl);
        if (bm == null) {
            DownloadImageTask imgTask = new DownloadImageTask(cache,callbackInterface);
            Utility.execute(imgTask, imgUrl);
        }
        else {
            callbackInterface.updateImageBitmap(bm);
        }
    }

    public void clearCache() {
        cache.clearCache();
    }

}
