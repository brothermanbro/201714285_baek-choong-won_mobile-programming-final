package kr.ac.konkuk.alldaytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EndOfDay extends AppCompatActivity {
    ArrayList<String> data;//ArrayList 생성
    ArrayAdapter<String> adapter;//ArrayAdapter 생성
    ListView list;//ListView 생성
    RatingBar ratingBar;//레이팅바 생성
    float score;//점수값 생성
    long pTime3;//경과 시간값 생성
    TextView pTime;//
   List<String> selectedItems;//ListView에서 체크된 List
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_day);
        Intent in = getIntent();//RightActivity에서 보낸 intent를 받기 위한 intent를 생성합니다.
        pTime=(TextView)findViewById(R.id.textView2);
       data = (ArrayList<String>)in.getStringArrayListExtra("Data");//RightActivity에서 보낸 ArrayList를 받아 data에 저장합니다.
       pTime3 = (long) in.getSerializableExtra("pTime");//RightActivity에서 보낸 경과 시간값을 받아서 pTime3에 저장합니다.
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,data);//controller 역할을 하는Adapter 객체 생성
        list =(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);//리스트뷰에 adapter를 설정합니다.
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//리스트뷰를 선택할 수 있는 모드로 설정합니다.
ratingBar=(RatingBar)findViewById(R.id.score);
pTime.setText("오늘의 총 집중 시간은"+String.format(Locale.getDefault(),"%dh %dm %ds", pTime3 / 3600000L, pTime3%3600000L/60000L, pTime3%3600000L%60000L/1000L)+"입니다");//경과 시간을 알려주는 문구를 텍스트에 설정합니다.
ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {//ratingBar로 입력된 점수를 score에 저장합니다.
    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        score=v;
    }
});
        Button ok = (Button)findViewById(R.id.ok);
        Button cancel = (Button)findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {//ok버튼을 누르면 실행 됩니다.
            @Override
            public void onClick(View view) {
               selectedItems = new ArrayList<>();//선텍된 리스트들을 저장할 ArrayList를 생성합니다.
                SparseBooleanArray checkedItemPositions = list.getCheckedItemPositions();//ListView의 항목이 체크 되었는지 확인하고 체크된 리스트의 위치를 배열로 저장합니다.
                for( int i=0; i<checkedItemPositions.size(); i++){//체크된 리스트의 수만큼 반복합니다.
                    int pos = checkedItemPositions.keyAt(i);//체크된 리스트의 위치를 pos에 저장합니다.
                    if (checkedItemPositions.valueAt(i))
                    {
                        selectedItems.add(list.getItemAtPosition(pos).toString());//체크된 리스트의 값을 selectedItems에 추가합니다.
                    }
                }

                final String[] items = selectedItems.toArray(new String[selectedItems.size()]);//selectedItems를 String 배열로 변환 합니다.
                Intent intent = new Intent();// RightActivity에 보낼 intent를 생성합니다.
                intent.putExtra("score", score);//점수 값을 넣습니다.
                intent.putExtra("newData", items);//체크된 값들이 있는 String 배열도 넣습니다.
                setResult(RESULT_OK, intent);//결과 값으로 RightActivity에 보냅니다.
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//cancel을 누르면
                setResult(RESULT_CANCELED);//작업을 취소합니다.
                finish();//현재 activity를 종료합니다.
            }
        });
    }
}
