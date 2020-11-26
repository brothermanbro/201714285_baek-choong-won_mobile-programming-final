package kr.ac.konkuk.alldaytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Image extends AppCompatActivity {
    int[] images = new int[] {R.drawable.wp1, R.drawable.wp2, R.drawable.wp3, R.drawable.wp4, R.drawable.wp5};//이미지 배열에 drawable의 이미지를 추가 합니다.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView mImageView = (ImageView)findViewById(R.id.landom);

        int imageId = (int)(Math.random() * images.length);//이미지 배열의 범위 안에서 랜덤 난수를 생성합니다.

        mImageView.setBackgroundResource(images[imageId]);//이미지 배열 범위 안에서 생성된 난수를 인덱스로 이용하여 이미지를  랜덤으로 imageView에 나타나게 합니다.


    }

}
