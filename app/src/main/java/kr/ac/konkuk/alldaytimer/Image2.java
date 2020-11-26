package kr.ac.konkuk.alldaytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class Image2 extends AppCompatActivity {
    int[] images = new int[] {R.drawable.pr1, R.drawable.pr2, R.drawable.pr3, R.drawable.pr4, R.drawable.pr5};//이미지 배열에 drawable의 이미지를 추가 합니다.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);
        ImageView mImageView = (ImageView)findViewById(R.id.landom);

        int imageId = (int)(Math.random() * images.length);//이미지 배열의 범위 안에서 랜덤 난수를 생성합니다.

        mImageView.setBackgroundResource(images[imageId]);//이미지 배열 범위 안에서 생성된 난수를 인덱스로 이용하여 이미지를 랜덤으로 imageView에 나타나게 합니다.


    }
}
