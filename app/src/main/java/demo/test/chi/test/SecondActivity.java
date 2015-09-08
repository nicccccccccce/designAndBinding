package demo.test.chi.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import demo.test.chi.http.BaseHttp;
import demo.test.chi.mode.BitmapScaleUtil;

/**
 * Created by eve on 2015/8/26.
 */
public class SecondActivity extends BaseHttp {
    public final static int TAKE_PICTURE = 1001;
    public final static int CHOOSE_PICTURE = 1002;
    public final static int CROP_PICTURE = 1003;
    public static Uri photoUri;
    //    "file:///sdcard/temp.jpg"
//    private final static File Temp_File = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
    public Uri imageUri = Uri.parse("file:///sdcard/temp.jpg");
    @InjectView(R.id.imageview)
    ImageView imageView;
    @InjectView(R.id.takePhoto)
    Button TakePhotoBtn;
    @InjectView(R.id.choosePhoto)
    Button choosePhotoBtn;

    /**
     * 压缩图片
     */

    public static Bitmap createImageThumbnail(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        opts.inSampleSize = computeSampleSize(opts, -1,
                bmp.getWidth() * bmp.getHeight());
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 将bitmap转化为base64字节流
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null)
            return "";

        String base64 = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();

            byte[] bitmapBytes = byteArrayOutputStream.toByteArray();
            base64 = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);

        } catch (IOException e) {
            Log.d("TAG", "imageError==" + e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", "imageError==" + e);
            }
        }
        return base64;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        ButterKnife.inject(this);
//        if(!Temp_File.exists())
//            try {
//                Temp_File.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        imageUri=Uri.fromFile(Temp_File);
    }

    @OnClick({R.id.takePhoto, R.id.choosePhoto})
    public void SecondClick(View view) {
        switch (view.getId()) {
            case R.id.takePhoto:
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra("android.intent.extra.videoQuality", 0);
                startActivityForResult(intent, TAKE_PICTURE);
                break;
            case R.id.choosePhoto:
                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, CHOOSE_PICTURE);
                break;
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void cropImageUri(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setClassName("com.android.camera", "com.android.camera.CropImage");
//        intent.setData(uri);
        intent.setDataAndType(uri, "image/*");
//        intent.setType("image/*");
//        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪框的比例，1：1
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", false); // no face detection
        startActivityForResult(intent, CROP_PICTURE);
    }


    private String photoResult(ImageView imageView, Intent data,Uri uri) {
        Bitmap bitmap = null;
        try {
            if (data != null) {
                if (data.getData() != null) {
                    Log.e("c","=====1");
                    bitmap = decodeUriAsBitmap(data.getData());

                } else{
                    Log.e("c","=====2");
                    Bundle bundle = data.getExtras();
                    if(bundle!=null)
                    bitmap = (Bitmap) bundle.get("data");
                }

            }else{
                Log.e("c","=====3");
                bitmap = decodeUriAsBitmap(uri);
            }
            if (imageView != null && bitmap != null) {
                bitmap = BitmapScaleUtil.comp(bitmap);
                imageView.setImageBitmap(bitmap);
                return bitmapToBase64(bitmap);
            }
        } catch (Exception e) {
//        bitmap = decodeUriAsBitmap(imageUri);
            Log.e("TAG", "图片获取失败");
        }


        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == TAKE_PICTURE) {
//                cropImageUri(imageUri);
            photoResult(imageView,data,imageUri);
            } else if (requestCode == CHOOSE_PICTURE) {
//                cropImageUri(data.getData());
              photoResult(imageView, data, imageUri);
            } else if (requestCode == CROP_PICTURE) {
                photoResult(imageView, data,imageUri);
            }


    }
}
