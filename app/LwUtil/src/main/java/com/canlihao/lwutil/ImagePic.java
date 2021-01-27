package com.canlihao.lwutil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * @Author : Panda李
 * @Time : On 2021/1/27 11:28
 * @Description : ImagePic
 */
public class ImagePic {
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        // 计算缩放比
        float h = options.outHeight;//140 2340
        float w = options.outWidth;//140 1080
        float beWidth = w / width;//  1.54
        float beHeight = h / height;// 3.34
        float be = 1.00f;
        int realWidth = width;
        int realHeight = height;
        if (beWidth < beHeight) {
            be = beWidth;
            realWidth = width;
            realHeight = Integer.valueOf((int) (h/beWidth));
        } else {
            be = beHeight;
            realWidth = Integer.valueOf((int) (w/beHeight));
            realHeight = height;
        }
        if (be <= 0) {
            be = 1;
        }
        MyLog.i("李魏","h="+h+" w="+w+" be="+be+" realWidth="+realWidth+" realHeight="+realHeight+" beWidth="+beWidth+" beHeight="+beHeight);
        options.inSampleSize = (int) be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false Integer.valueOf(h/beWidth)
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, realWidth, realHeight,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        int gaibpm = getBitmapDegree(imagePath);
        bitmap = rotateBitmapByDegree(bitmap,gaibpm);
        //String string121=null;
//        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
//        byte[]bytes=bStream.toByteArray();
//        //string121= Base64.encodeToString(bytes,Base64.DEFAULT);
        return bitmap;
    }
    public static String getSmallBitmap(String filePath,float Size,String SavePath,String FileName) {
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.decodeFile(filePath, options);
            int height = options.outHeight;
            int width= options.outWidth;
            int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
            int minLen = Math.min(height, width); // 原图的最小边长
            //Log.i("lw","minLen="+minLen);
            if(minLen > (int)Size) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                float ratio = (float)minLen / Size; // 计算像素压缩比例
                inSampleSize = 2;
                options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                Bitmap bm = BitmapFactory.decodeFile(filePath, options); // 解码文件
                //Log.i("lw", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
                int gaibpm = getBitmapDegree(filePath);
                bm = rotateBitmapByDegree(bm,gaibpm);
                boolean r =  ImagePic.SavaImage(bm, SavePath, FileName);
                if(r) return SavePath+"/"+FileName;
                else return filePath;
            } else{
                return filePath;
            }
        }catch(Exception e){
            return filePath;
        }
    }
    private static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
    public static boolean SavaImage(Bitmap bitmap, String path,String Name){
        File file=new File(path);
        FileOutputStream fileOutputStream=null;
        //文件夹不存在，则创建它
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            fileOutputStream=new FileOutputStream(path+"/"+Name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
