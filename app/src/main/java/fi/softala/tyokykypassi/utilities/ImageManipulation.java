package fi.softala.tyokykypassi.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by vllle on 14.10.2016.
 */

public class ImageManipulation {

    // pienentää kuvaa
    public static void pienennaKuvaa(final File file, int reqWidth, int reqHeight) {
        try {
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(file.getAbsolutePath(), o2);

            o2.inSampleSize = calculateInSampleSize(o2, reqWidth, reqHeight);
            o2.inJustDecodeBounds = false;
            final FileInputStream inputStream = new FileInputStream(file);
            final Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            final FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Laske millä jakaa
    https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d("Kuvahifi", "heigh: " + height + " width= " + width);

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d("Kuvahifi", "samplataan " + inSampleSize + " arvolla");

        return inSampleSize;
    }
}
