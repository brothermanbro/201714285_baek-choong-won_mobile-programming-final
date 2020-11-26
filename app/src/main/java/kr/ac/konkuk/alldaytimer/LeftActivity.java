package kr.ac.konkuk.alldaytimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class LeftActivity extends AppCompatActivity implements OnClickListener{
    Button word1;
    Button word2;
    Button play;
    Button stop;
    Button play2;
    Button stop2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
word1=(Button)findViewById(R.id.button7);
word2=(Button)findViewById(R.id.button6);
        Button prev = (Button)findViewById(R.id.prev);
        play = (Button)findViewById(R.id.button2);
        stop = (Button)findViewById(R.id.button3);
        play2 = (Button)findViewById(R.id.button4);
        stop2 = (Button)findViewById(R.id.button5);

        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        play2.setOnClickListener(this);
        stop2.setOnClickListener(this);

        prev.setOnClickListener(new OnClickListener() {//prev 버튼을 누르면 현재 activity를 종료하고 이전 activity로 돌아갑니다.
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void wpImage(View view) {//이미지를 랜덤으로 보여주는 activity화면으로 넘어갑니다.
        Intent intent = new Intent(LeftActivity.this, Image.class);
        startActivity(intent);
    }
    public void prImage(View view) {//이미지를 랜덤으로 보여주는 activity화면으로 넘어갑니다.
        Intent intent = new Intent(LeftActivity.this, Image2.class);
        startActivity(intent);
    }
    public void onClick(View src){//onClick을 호출한 버튼 중에
        switch(src.getId()){
            case R.id.button2:// 버튼2가 선택 되었다면

                startService(new Intent(this, MusicService.class));//MusicService activity service 실행
                break;
            case R.id.button3:

                stopService(new Intent(this, MusicService.class));//MusicService activity service 종료
                break;
            case R.id.button4:

                startService(new Intent(this, MusicService2.class));//MusicService2 activity service 실행
                break;
            case R.id.button5:

                stopService(new Intent(this, MusicService2.class));//MusicService2 activity service 종료
                break;
        }
    }

}
