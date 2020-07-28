package com.gulasehat.android.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Common {

    private static Context context;

    public static void init(Context mContext) {
        context = mContext;
    }

    public static RequestBody getRequestBodyWithUri(Uri uri) {

        RequestBody requestFile = null;

        try {
            String photoRealPath = getRealPathFromUri(context, uri);
            if (photoRealPath != null) {
                File file = new File(photoRealPath);
                requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(uri)), file);
            }
        } catch (Exception e) {
            try {
                ParcelFileDescriptor parcelFileDescriptor =
                        context.getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), stream.toByteArray());
            } catch (IOException e1) {
                return null;
            }
        }

        return requestFile;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {

            String[] proj = {MediaStore.Images.ImageColumns.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                if (path != null) {
                    return path;
                }
            }

        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    public static int findFilterIndex(String needle, int haystack){
        String[] keys = context.getResources().getStringArray(haystack);
        for (int i = 0; i<keys.length; i++){
            if(keys[i].equals(needle)){
                return i;
            }
        }
        return -1;
    }

}
