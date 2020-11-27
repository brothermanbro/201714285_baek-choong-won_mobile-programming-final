package kr.ac.konkuk.alldaytimer;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;


public class RightActivity extends Activity {
    private static final String LIST1 = "LIST1";
    private static final String DAYM="MYDAY2";
int CODE =1;//EndOfDay로 intent를 보낼때 사용하는 식별 코드입니다.
int day;//일차를 나타내는 변수
float score;//EndOfDay에서 raingBar로 입력된 점수를 받는 변수
    ArrayList<String> data;//ArrayList 생성
    ArrayAdapter<String> adapter;//ArrayAdapter 생성
    ListView list;//ListView 생성
    String[] newList;// EndOfDay에서 하지 못하고 내일로 미루기 위해 선택된 ListView의 텍스트 값을 받는 Stirng배열값
    TextView TextDay;//일차를 화면에 나타내는 택스트
long pTime2;//MainActivity에서 받은 시간 경과 값입니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
SharedPreferences dayPass = getSharedPreferences(DAYM, 0);//Activity가 종료되어도 경과된 날짜를 표시 하기 위해SharedPreference를 통하여 저장된 day값을 읽어옵니다.
day= dayPass.getInt("day", 0);//휴대폰내에 저장된 일차 변수를 불러옵니다.
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_right);
        Button prev = (Button)findViewById(R.id.prev);
        Button Eod = (Button)findViewById(R.id.finish);
        Button recordbtn = (Button)findViewById(R.id.record);
        TextDay = (TextView)findViewById(R.id.textDay);
        TextDay.setText(day+"일차");//경과된 일차 수를 텍스트에 표시합니다.
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           finish();
            }
        });
        data = new ArrayList<String>();//ArrayList를 생성합니다.
data=getStringArrayPref(RightActivity.this,LIST1);//activity가 종료 되고 다시 RightActivity를 실행하여도 ListView를 유지하기 위해서 아래 메소드를 이용하여 내부 저장소에 저장한 ArrayList를 불러와서 data에 저장합니다.
        Intent intent = getIntent();//MainActivity애서 intent를 받아오기 위해서 intent를 생성합니다.
        pTime2 = (long) intent.getSerializableExtra("pTime");//경과된 시간을 받아서 pTime2에 저장합니다.
        Eod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//하루 마무리 버튼을 누르면 실행 됩니다.
                Intent in = new Intent(RightActivity.this, EndOfDay.class);//EndOfDay로 화면을 전환합니다.
                in.putExtra("Data", data);//체크되지 않은 ArrayList를 intent에 넣어 보냅니다.
                in.putExtra("pTime", pTime2);//경과 시간을 intent에 넣어 보냅니다.
                startActivityForResult(in,CODE );//결과를 받기 위한 intent를 보내고 화면이 전환 됩니다.
            }

        });
     recordbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {//이전 기록 버튼을 누르면 실행 됩니다.
             Intent in2 = new Intent(RightActivity.this, recordActivity.class);//recordActivity로 화면을 전환 합니다.
             in2.putExtra("Score", score);//EndOfDay 에서 넘어온 점수를 intent에 넣어보냅니다.
             in2.putExtra("time", pTime2);//경과 시간을 intent에 넣어보냅니다.
             in2.putExtra("day",day);//일차를 intent에 넣어보냅니다.
             startActivity(in2);//recordActivity로 화면이 전환 됩니다.
         }
     });
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,data);//controller 역할을 하는Adapter 객체 생성
        list =(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);//리스트뷰에 adapter를 설정합니다.
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//리스트뷰를 선택할 수 있는 모드로 설정합니다.

        Button add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//리스트뷰에 항목을 추가하기 위해 add버튼을 누르면 실행 됩니다.

                EditText newItem = (EditText)findViewById(R.id.newitem);
                String item = newItem.getText().toString(); // EditText의 입력값을 toString()으로 변경하여 저장합니다.
                if(item!=null || item.trim().length()>0){//EditText애 입력된 문자가 있다면
                    data.add(item.trim());//ArrayList에 입력된 문자열을 추가해줍니다.
                    adapter.notifyDataSetChanged();//adapter를 이용해 ListView를 갱신합니다.
                    newItem.setText("");//EditText를 초기화 합니다.
setStringArrayPref(RightActivity.this,LIST1 ,data);//아래 메소드를 사용하여 ArrayList를 내부저장소에 저장합니다.
                }

            }
        });
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//리스뷰의 항목을 삭제하기위해 delete버튼을 누르면 실행됩니다.
                SparseBooleanArray array = list.getCheckedItemPositions();//ListView의 항목이 선택 되었는지 확인하고 위치를 배열에 저장합니다.
                for(int i=0;i<array.size();i++){//체크된 항목의 위치가 있는 배열을 이용하여 체크된 항목들을 이중 for문을 이용하여 ListView에서 삭제 합니다.
                    for(int j=0;j<data.size();j++){
                        if(array.get(j)){
                            data.remove(j);
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();//adapter를 이용해 ListView를 갱신합니다.
                list.clearChoices();//ListView의 선택지를 초기화 시킵니다.
                setStringArrayPref(RightActivity.this,LIST1,data);//아래 메소드를 사용하여 ArrayList를 내부저장소에 저장합니다.
            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, Intent in) {//startActivityForResult로 생성한 EndOfDay의 응답을 받습니다.
        int count = 0 ;
        count = adapter.getCount() ;
        for (int i=0; i<count; i++) {// listView 전체를 체크합니다.
            list.setItemChecked(i, true) ;
        }
        SparseBooleanArray array = list.getCheckedItemPositions();
        for(int i=0;i<array.size();i++) {//체크한 리스트는 삭제하여 리스트를 모두 삭제 합니다.
            for (int j = 0; j < data.size(); j++) {
                if (array.get(j)) {
                    data.remove(j);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, in);
        if (requestCode == CODE) {//requestCode로 EndOfDay를 구별합니다.
            if (resultCode == RESULT_OK) {//EndOfDay에서 보낸 2개의 데이터를 받습니다.
score= (float) in.getSerializableExtra("score");//EndOfDay에서 책정된 점수를 받습니다.
newList=in.getStringArrayExtra("newData");//EndOfDay에서 보낸 String배열을 받습니다.
for(int i=0; i<newList.length; i++) {
    data.add(newList[i]);//String 배열의 값들을 모두 ArrayList에 추가합니다.
}
                list.clearChoices();//ListView의 선택지를 초기화 시킵니다.
day++;//일차의 값의 1을 증가 시킵니다.
                TextDay.setText(day+"일차");//일차를 업데이크 하여 표시합니다.
SharedPreferences dayPass = getSharedPreferences(DAYM, 0);//day 값을 내부 저장소에 저장하기 위해서 SharedPreferences 객체를 생성합니다.
SharedPreferences.Editor editor = dayPass.edit();
editor.putInt("day", day);//일차를 내부저장소에 저장합니다.
editor.commit();//변경을 최종 반영합니다.
            }
        }



    }



}

