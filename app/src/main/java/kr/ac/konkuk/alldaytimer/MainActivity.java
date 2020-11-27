package kr.ac.konkuk.alldaytimer;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    SensorManager manager;//센서 매니저를 생성
    Sensor proximity;//센서의 근접값 생성
int startTime=0;//스타트한 횟수를 측정하기 위한 변수
    float proximityMaximumRange;//근접센서 max 값.
    int pStatus ;//타이머의 퍼센트 값
    private Handler handler = new Handler();//원형 그래프 진행을 작동시키는 쓰레드에서 ui를 다루기 위해 사용되는 handler를 생성합니다.
    TextView tv;//프로그레스바에 퍼센트를 표현 해주는 텍스트
    Button pause;//휴식 버튼
    timer tm;
    private boolean condition = false;//타이머가 실행 상태인지 확인하기위한 boolean값
    ProgressBar mProgress;//프로그레스바 생성
    Button time;//시간 설정 버튼
     int mHour ;// 타이머의 시간
   int mMinute ;// 타이머의 분
    private long TIMER_DURATION ;//타이머 설정 값
    private static final long TIMER_INTERVAL = 1000L;//타이머 간격을 1초로 설정해 줍니다.
    private CountDownTimer mCountDownTimer;//카운트 다운 타이머 생성 합니다.
int pauseStatus = 0;//휴식 횟수를 저장하기 위한 값
    private long mTimeRemaining;//타이머의 남은 시간
 long pTime;//초기화 되었을때 타이머의 경과시간을 표시 합니다.
  Drawable drawable;//원형 프로그레스바의 이미지 리소스 등록을 위해 생성
Button right;//오른쪽 화면으로 전환하는 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (SensorManager) this.getSystemService(SENSOR_SERVICE);//센서 서비스 매니저를 정의 합니다.
        proximity = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);//근접 센서의 값을 할당 합니다.
        if(proximity != null){
            proximityMaximumRange=1; // 물체와 근접 인식 최대거리를 1cm로 설정합니다.

        }
        setContentView(R.layout.activity_main);
        Resources res = getResources();
       drawable = res.getDrawable(R.drawable.circular);
        mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0); //프로그레스 바 초기화
        mProgress.setMax(100);//프로그레스바 최댓값 설정
        mProgress.setProgressDrawable(drawable);//res/drawable/circular로 프로그레스 모양을 설정 합니다.
        time = (Button) findViewById(R.id.time);
        pause = (Button) findViewById(R.id.pause);
        tv = (TextView) findViewById(R.id.tv);
right=(Button)findViewById((R.id.right));
right.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {//오른쪽 화면으로 전환 합니다.
        Intent intent = new Intent(MainActivity.this, RightActivity.class);
        intent.putExtra("pTime", pTime);//오른쪽 화면으로 이동시 시간 경과 값을 전달 합니다.
        startActivity(intent);
    }
});

    }

    class timer extends Thread {//원형 프로그레스바를 작동 시키는 thread 생성합니다.
        public void run() {
            // TODO Auto-generated method stub
            while (pStatus < 100) {// 타이머의 퍼센트 값이 100이 안되었으면 반복 합니다.
                while (condition) {//카운트 다운 타이머가 종료되면 condition의 값이 false이고 카운트 다운 타이머 다시 실행 되면 true로 변경됩니다. 이런 원리로 프로그레스바와 카운트 다운 타이머의 싱크를 맞췄습니다.
                    handler.post(new Runnable() { //핸들러를 통하여 ui를 조작합니다.

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(pStatus);//프로그레스 바에 증가한 퍼센트 만큼 설정 합니다.
                            tv.setText(pStatus + "%");//텍스트에 퍼센트를 설정 합니다.
                        }

                    });
                    try {
                        Thread.sleep(TIMER_DURATION / 100);//정해진 시간을 100으로 나눈 시간 만큼 정지합니다.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus += 1;//1퍼센트 증가 합니다.
                }
            }
        }
    }
    public void onClick(View v) {//시간 버튼을 누르면 실행되는 메소드 입니다.
        final Dialog timeBT = new Dialog(this);//커스텀 대화 상자 객체를 생성 합니다.
        timeBT.setContentView(R.layout.time);
        timeBT.setTitle("시간 설정");

        Button OK = (Button) timeBT.findViewById(R.id.ok);
        Button CANCEL = (Button)timeBT.findViewById(R.id.cancel);

        final EditText hour = (EditText) timeBT.findViewById(R.id.hour);
        final EditText minute = (EditText) timeBT.findViewById(R.id.minute);
        OK.setOnClickListener(new View.OnClickListener(){//ok버튼이 눌렸을때 실행되는 onClickListener callback 메소드를 익명 클래스로 구현 히였습니다.
            @Override
            public void onClick(View view) {
                if((Integer.parseInt(hour.getText().toString())<15)&&((Integer.parseInt(minute.getText().toString())< 60))){ //hour editText 숫자가 15이하 minute editText에 숫자가 60이하 일때만 입력에 성공 합니다.
                    Toast.makeText(getApplicationContext(),"입력 성공",Toast.LENGTH_SHORT).show();//"입력 성공"이라는 토스트바를 띄우고
                    mHour=Integer.parseInt(hour.getText().toString());//editText에서 입력 받은 숫자를 저장합니다.
                    mMinute=Integer.parseInt(minute.getText().toString());
                    time.setText(mHour+":"+mMinute+":00");//입력 받은 숫자를 버튼 텍스트레 표시해 줍니다
                    timeBT.dismiss();//대화 상자는 종료 됩니다.
                }else{
                    Toast.makeText(getApplicationContext(), "다시 입력하시오", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CANCEL.setOnClickListener(new View.OnClickListener() {//cancel 버튼을 눌렀을때 실행되는 onClickListener callback 메소드를 익명 클래스로 구현 하였습니다.
            @Override
            public void onClick(View view) {
                timeBT.dismiss();
            }
        });
        timeBT.show();//대화상자를 화면에 생성합니다.
    }
    public void start(View view) {//타이머를 처음에 실행할때 실행되는 메소드 입니다.
        TIMER_DURATION = 3600000L*mHour + 60000L*mMinute;// 입력한 시간을 밀리초단위로 변경하여 총 시간에 입력합니다.
        condition = true;// 프로그레스 바를 실행 상태로 설정합니다.
        tm = new timer();// 원형프로그레스바 사용을 위해 진행 쓰레드 객체를 생성 합니다.
        tm.start();//원형 프로그레스바 진행 시작 합니다.
        mCountDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {// 입력한 시간으로 카운트 다운 타이머 객체를생성 합니다.
            @Override
            public void onTick(long millisUntilFinished) {//초마다 실행되어 타이머 기능을 합니다. milisUntilFinished에 남은 시간이 밀리초 단위로 저장되어 있습니다.
                time.setText(String.format(Locale.getDefault(), "%d: %d: %d", millisUntilFinished / 3600000L, millisUntilFinished%3600000L/60000L,millisUntilFinished%3600000L%60000L/1000L));
                //시간 버튼의 텍스트에 남을 시간을 표시합니다.
                mTimeRemaining = millisUntilFinished; //남은 시간을 mTimeRemaining에 저장합니다.
            }
            public void onFinish() {//카운트 다운 타이머가 시간이 다되어 종료될때 실행되는 메소드 입니다.
                condition =false;//타이머가 종료되었기 때문에 condition의 값을 false로 변경하여 프로그레스바의 실행을 정지합니다.
                reset(null);//reset()을 실행 합니다.
            }
        }.start();//카운트 다운 타이머를 실행 합니다.
    }
    public void onPausea(View view) {//타이머의 정지 기능을 하는 메소드입니다.
        if(condition==true||mTimeRemaining == 0) {//타이머가 실행 중이거나 또는 나머지시간이 없을때 즉 타이머가 실행되기전 초기화 상태일때
            if (pauseStatus == 0 && mTimeRemaining == 0) {//정지 횟수가 0이고 나머지 시간이 없을때 휴식 횟수를 입력받습니다.
                final Dialog Pause = new Dialog(this);//커스텀 대화 상자 객체를 생성 합니다.
                Pause.setContentView(R.layout.pause);//pause.xml을 커스텀 대화상자 화면으로 설정합니다.
                Pause.setTitle("정지 화면");//대화상자의 제목을 설정합니다.
                Button OK = (Button) Pause.findViewById(R.id.ok);
                Button CANCEL = (Button) Pause.findViewById(R.id.cancel);
               final EditText number = (EditText) Pause.findViewById(R.id.npause);
                OK.setOnClickListener(new View.OnClickListener() {//ok버튼이 눌렸을때 실행되는 onClickListener callback 메소드를 익명 클래스로 구현 히였습니다.
                    @Override
                    public void onClick(View view) {
                        if (number.getText().toString().trim().length() > 0) { //editText number에 입력된 숫자가 있디면
                            pauseStatus = Integer.parseInt(number.getText().toString());//입력된 값을 pauseStatus 값에 저장합니다.
                            Toast.makeText(getApplicationContext(), "입력 성공", Toast.LENGTH_SHORT).show();//"입력 성공"이라는 토스트바르 띄우고
                            pause.setText("휴식 :" + number.getText().toString());//휴식 버튼 텍스트에 변경된 휴식 횟수를 나타냅니다.
                            Pause.dismiss();//대화 상자는 종료 됩니다.
                           ;
                            startTime=pauseStatus+1;//시작 횟수를 정지횟수에 1을 더하여 저장합니다.
                        } else {//editText number에 입력되 숫자가 없다면
                            Toast.makeText(getApplicationContext(), "다시 입력하시오", Toast.LENGTH_SHORT).show();//"다시 입력하십시오"라는 토스트 바가 나옵니다.
                        }
                    }
                });

                CANCEL.setOnClickListener(new View.OnClickListener() {//cancel 버튼을 눌렀을때 실행되는 onClickListener callback 메소드를 익명 클래스로 구현 하였습니다.
                    @Override
                    public void onClick(View view) {
                        Pause.dismiss(); // canceldl 눌렸을때믄 대화상자가 종료 딥니다.
                    }
                });
                Pause.show();//대화상자를 화면에 생성합니다.

            } else if (pauseStatus == 0 && mTimeRemaining > 0) {//정지 횟수는 0이지만 타이머가 실행 중인 상태이면
                final Dialog Reset = new Dialog(this);//커스텀 대화 상자 객체를 생성 합니다. 대화 상자에서는 타이머를 초기화 할 것인지 묻습니다.
                Reset.setContentView(R.layout.ask);
                Reset.setTitle("정지 화면");
                Button OK = (Button) Reset.findViewById(R.id.ok);
                Button CANCEL = (Button) Reset.findViewById(R.id.cancel);
                OK.setOnClickListener(new View.OnClickListener() {//ok버튼이 눌렸을때 실행되는 onClickListener callback 메소드를 무명 클래스로 구현 히였습니다.
                    @Override
                    public void onClick(View view) {//ok 버튼을 누르면
                        reset(null);//reset()을 실행 합니다.
                            Reset.dismiss();
                    }
                });
                CANCEL.setOnClickListener(new View.OnClickListener() {//cancel 버튼을 눌렀을때 실행되는 onClickListener callback 메소드를 무명 클래스로 구현 하였습니다.
                    @Override
                    public void onClick(View view) {
                        Reset.dismiss(); //
                    }
                });
                Reset.show();//대화상자를 화면에 생성합니다.
            } else {//그외에 휴식횟수가 남아있고 타이머가 실행 중이라면
                pauseStatus--;//휴식 횟수에서 1을 줄이고
                pause.setText("휴식 :" + pauseStatus);//휴식 버튼 텍스트에 줄어든 휴식 횟수로 업데이트 합니다.
                condition = false;//프로그레스 바를 정지 상태로 설정합니다.
                mCountDownTimer.cancel();//카운트 다운 타이머를 종료시킵니다.
                mCountDownTimer = null;//카운트 다운 타이머를 초기화합니다.
            }
        }
        if(condition==false&&mTimeRemaining > 0){//타이머가 정지상태이고 타이머 횟수가0이 아닐때
            Toast.makeText(getApplicationContext(), "정지 상태 입니다.", Toast.LENGTH_SHORT).show();//정지상태라는 텍스트 바를 실행합니다.
        }

    }
    public void onResumea(View view) {//정지된 카운트 다운 타이머를 다시 재생할때 사용하는 메소드 입니다.
        condition = true;//카운트 다운 타이머가 실행중이라는 것을 표시하고

        if (mCountDownTimer == null) { // 실행 중인 카운트 다운 타이머가 없다면( 정지 기능을 구현하기 위해서 카운트 다운 타이머를 삭제 하고 재시작할때 남은 시간으로 카운트 다운 타이머를 다시 생성하여 타이머를 이어나갑니다.)
            mCountDownTimer = new CountDownTimer(mTimeRemaining, 1000L) {// 이전 카운트 다운 타이머에서 남은 시간과 시간 간격을 입력하여 새로운 타이머르 만듭니다.
                @Override
                public void onTick(long millisUntilFinished) {//초마다 실행되어 타이머 기능을 합니다. milisUntilFinished에 남은 시간이 밀리초 단위로 저장되어 있습니다.
                    time.setText(String.format(Locale.getDefault(), "%d: %d: %d", millisUntilFinished / 3600000L, millisUntilFinished%3600000L/60000L, millisUntilFinished%3600000L%60000L/1000L));
                    mTimeRemaining = millisUntilFinished;//남은 시간을 mTimeRemaining에 저장합니다.
                }
                @Override
                public void onFinish() {//카운트 다운 타이머가 시간이 다되어 종료될때 실행되는 메소드 입니다.
                    condition =false;//타이머가 종료되었기 때문에 condition의 값을 false로 변경하여 프로그레스바의 실행을 정지합니다.
                    reset(null);

                }
            }.start();//나머지 시간으로 만들어진 카운트 다운 타이머 실행 합니다.
        }
    }
    public void reset(View view){//타이머를 초기화 하는 메소드입니다.
        pTime = (TIMER_DURATION-mTimeRemaining);//경과 시간을 pTime에 저장합니다.
        condition = false;//원형프로그레스 바를 정지합니다.
        mCountDownTimer.cancel();//카운트 다운 타이머를 종료합니다.
        mCountDownTimer = null;//카운트 다운 타이머를 초기화합니다.
        mTimeRemaining=0;//타이머의 남은 시간을 0으로 초기화 합니다.
        pStatus=0;//프로그레스의 퍼센트를 0으로 초기화 합니다.
        mProgress.setProgress(0); //프로그레스바 진행 상태를 초기화 합니다.
        tv.setText(pStatus + "%");//퍼센트 텍스트를 초기화 합니다.
        pauseStatus=0;//휴식 횟수를 0으로 초기화 합니다.
        pause.setText("휴식 :" + pauseStatus);//휴식 버튼에 초기화 된 횟수로 업데이트 합니다.
        time.setText("0:00:00");//시간 버튼의 시간 텍스트를 초기화 합니다.
tm.yield();//원형프로그레스 실행 쓰레드들 종료합니다.
    }
    public void Left(View view) { //해당 메소드를 onClick으로 호출하는 버튼을 누르면 좌측화면으로 이동합니다.
        Intent intent = new Intent(MainActivity.this, LeftActivity.class);
        startActivity(intent);
    }
    protected void onResume() {//activity가 onResume 상태가 되면
        super.onResume();
        manager.registerListener(listener, proximity, SensorManager.SENSOR_DELAY_UI);//생성된 센서매니저에 센서 리스너와 근사값, 센서를 읽어오는 속도를 정의 하여 리스너를 등록합니다.
    }
    @Override
    protected void onPause() {//activity가 onPause 상태가 되면
        super.onPause();
        //add~~~~~~~~~~~~~~~
        manager.unregisterListener(listener);//센서 리스너 등록을 해제 합니다.
    }
    SensorEventListener listener=new SensorEventListener() {//센서 리스너를 생성합니다.
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {//센서의 근사값이 바뀌면

            if(sensorEvent.sensor.getType()== Sensor.TYPE_PROXIMITY){//센서 이벤트로 근사값을 받았다면
                float distance = sensorEvent.values[0];//serdorEvent로 측정한 값들을 distance에 저장합니다.
                if(distance < proximityMaximumRange){//측정 거리가 1cm보다 작을때
                    if(startTime>pauseStatus&&mTimeRemaining==0) {//시작 횟수가 정지 횟수보다 많고 타이머가 처음 실행되기 전에
                        start(null);//start()메소드 호출 합니다.
                        startTime--;//시작횟수 1감소
                    }else if(startTime>pauseStatus){//시작횟수가 정지 횟수보다 많을때
                        onResumea(null);//onResumea() 메소드 호출하여 정지상태에서 다시 시작 합니다.
                        startTime--;//시작횟수를 1감소 시킵니다.
                    }
                }else {//측정 거리가 1cm보다 클때
                    if(condition==true) {
                        onPausea(null);//onPause()메소드를 실행하여 타이머를 정지 시킵니다.

                    }
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {//센서의 정확성이 변경되었을때 실행됩니다. onSensorChanged 인터페이스를 구현하기위해 생성 되었습니다.

        }
    };
}







