package kr.ac.konkuk.alldaytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

public class recordActivity extends AppCompatActivity {
    ArrayList<String> data;//ArrayList 생성
    private static final String record = "record2";
    ArrayAdapter<String> adapter;//ArrayAdapter 생성
    ListView list;//ListView 생성
    float score;//점수값을 저장할 변수 생성
    long pTime4;//경과 시간값을 저장할 변수 생성
   int day;//일차 값을 저장할 변수 생성


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Button ok = (Button) findViewById(R.id.ok);
        data = new ArrayList<String>();//ArrayList를 생성합니다
        data=getStringArrayPref(recordActivity.this,record);//activity가 종료 되고 다시 recordActivity를 실행하여도 ListView를 유지하기 위해서 아래 메소드를 이용하여 내부 저장소에 저장한 ArrayList를 불러와서 data에 저장합니다.
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent in = getIntent();//RightActivity에서 보낸 intent 정보를 받기 위해 intent를 생성합니다.
        pTime4 = (long) in.getSerializableExtra("time");//RightActivity에서 받은 경과 시간을 pTime4에 저장합니다.
        score = (float) in.getSerializableExtra("Score");//RightActivity에서 받은 점수값을 score에 저장합니다.
        day=(int) in.getSerializableExtra("day");//RightActivity에서 받은 일차값을 day에 저장합니다.
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);//controller 역할을 하는Adapter 객체 생성
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);//리스트뷰에 adapter를 설정합니다.
        data.add(day-1+"일차         집중시간:" +String.format(Locale.getDefault(),"%dh %dm %ds", pTime4 / 3600000L, pTime4%3600000L/60000L, pTime4%3600000L%60000L/1000L)  +"          점수:"+score);
        //RightActivity에서 가져온 일차,집중시간,점수를 ListView에 추가 합니다.
        setStringArrayPref(recordActivity.this,record ,data);//아래 메소드를 사용하여 ArrayList를 내부저장소에 저장합니다.
    }

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) { //ArrayList를 Json으로 변환하여 SharedPreferences에 String을 저장하는 메소드입니다
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);//내부 저장소에 쓰기 위해 프레퍼런스를 생성합니다.
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();//JSONArray를 생성 합니다.
        for (int i = 0; i < values.size(); i++) {//ArrayList의 값들을 JSONArray에 저장합니다.
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {//ArrayList에 값이 존재 한다면 JSONArray를 내부 저장소에 저장합니다.
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);//
        }
        editor.apply();//변경을 저장합니다.
    }

    private ArrayList<String> getStringArrayPref(Context context, String key) {// SharedPreferences에서 Json형식의 String을 가져와서 다시 ArrayList로 변환하는 코드입니다.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);//내부 저장소에서 값을 읽어오기 위해 프레퍼런스를 생성 합니다.
        String json = prefs.getString(key, null);//내부 저장소의 String 값들을 불러옵니다.
        ArrayList<String> urls = new ArrayList<String>();//ArrayList를 생성합니다.
        if (json != null) {//String값을 받았다면
            try {
                JSONArray a = new JSONArray(json);//String 값들을 JSONArray로 변경합니다.
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);//JSONArray의 값들을 ArrayList에 저장합니다.
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;//ArrayList를 결과값으로 반환 합니다.
    }
}