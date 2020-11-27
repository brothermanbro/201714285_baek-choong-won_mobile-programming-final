package kr.ac.konkuk.alldaytimer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService2 extends Service {

    MediaPlayer player;//MediaPlayer 객체를 생성합니다.

    public IBinder onBind(Intent intent){
        return null;
    }//service와 activity를 연결하는 역할로 실행됩니다.
    public void onCreate(){
        player = MediaPlayer.create(this, R.raw.classicb);// MediaPlayer객체에 raw파일의 음악파일을 연결합니다.
        player.setLooping(true);//음악이 반복 하도록 설정합니다.
    }
    public void onDestroy(){//service가 종료 되면
        Toast.makeText(this, "Music Service가 중지 되었습니다.", Toast.LENGTH_SHORT).show();
        player.stop();//player를 종료 합니다.
    }

    public int onStartCommand(Intent intent, int flags, int startId){//LeftActivity에서 startService()를 호출해서 서비스가 시작되면 이 메소드가 호출됩니다
        Toast.makeText(this,"Music Service가 시작되었습니다.", Toast.LENGTH_LONG).show();
        player.start();//player를 실행하여 음악을 재생합니다.
        return super.onStartCommand(intent, flags, startId);
    }
}

