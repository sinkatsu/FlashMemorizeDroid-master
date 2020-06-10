package me.timgu.flashmemorize;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {
//Code inspired from https://medium.com/quick-code/pinch-to-zoom-with-multi-touch-gestures-in-android-d6392e4bf52d

    private ScaleGestureDetector mScaleDetector;//ピンチイン、ピンチアウトを認識
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    private float mPosX = 0f;
    private float mPosY = 0f;

    private float mLastTouchX;
    private float mLastTouchY;
    private static final int INVALID_POINTER_ID = -1;
    private static final String LOG_TAG = "TouchImageView";

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();//インテントを使って別のアクティビティから渡されたデータを受け取る
        File imageFile = new File(getCacheDir(), intent.getStringExtra("image"));//imageという名前でキャッシュファイル作成
        SerialBitmap pic = new SerialBitmap(imageFile);//イメージファイルのデータを読み込み画像作成
        PhotoView photoView = (PhotoView) findViewById(R.id.imageView_photoView);//指定されたidのimageviewを使用
        photoView.setImageBitmap(pic.bitmap);//表示する画像を指定（変換したbitmapをレイアウトエディタにセットしたphotoviewにセット）

        //clearing cache
        LocalDecksManager ldm = new LocalDecksManager(this);
        ldm.clearCache();//キャッシュファイルを削除
    }
}
