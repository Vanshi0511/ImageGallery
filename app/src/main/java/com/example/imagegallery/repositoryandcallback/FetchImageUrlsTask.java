package com.example.imagegallery.repositoryandcallback;

import android.os.AsyncTask;
import android.util.Log;

import com.example.imagegallery.model.Photo;
import com.example.imagegallery.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchImageUrlsTask extends AsyncTask<String, Void, JSONObject> {

    private CallbackInterface listListener;
    private String imagesUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&per_page=20&page=1&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s";

    public FetchImageUrlsTask(CallbackInterface ci) {
        this.listListener = ci;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String json = Utility.getResponse(imagesUrl + strings[0]);
        JSONObject jsonObj = null;
        // try parse the string to a JSON object
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jsonObj;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        List<Photo> photoList = new ArrayList<>();
        if (jsonObject != null) {
            try {
                JSONObject photoJson = jsonObject.getJSONObject("photos");
                if (photoJson != null) {
                    JSONArray photoArray = photoJson.getJSONArray("photo");
                    if (photoArray != null && photoArray.length() > 0) {
                        for (int i = 0; i < photoArray.length(); i++) {
                            JSONObject pJson = photoArray.getJSONObject(i);
                            Photo photo = new Photo();
                            photo.setId(pJson.getString("id"));
                            photo.setOwner(pJson.getString("owner"));
                            photo.setSecret(pJson.getString("secret"));
                            photo.setServer(pJson.getString("server"));
                            photo.setFarm(pJson.getInt("farm"));
                            photo.setTitle(pJson.getString("title"));
                            photo.setIspublic(pJson.getInt("ispublic"));
                            photo.setIsfriend(pJson.getInt("isfriend"));
                            photo.setIsfamily(pJson.getInt("isfamily"));
                            photoList.add(photo);
                        }
                    }
                }
            } catch (JSONException e) {

            }
        }
        if (listListener != null) {
            Log.d("photos",photoList+"");
            listListener.updateImagesList(photoList);
        }
    }
}
