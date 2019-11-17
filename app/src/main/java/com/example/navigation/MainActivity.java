package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //GET_STRING, GET_NUMBER를 통해서 수정 액티비티인지 추가 액티비티인지 구별한다. = requestcode 가 된다.
    static final int GET_STRING = 1;
    static final int GET_NUMBER = 2;
    //activity_main에 있는 ListView를 받을 변수를 선언한다.
    //List를 조작할 adapter를 선언한다.
    //파일을 조작할 파일매니저를 선언한다.

    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;
    TextFileManager mFileMgr = new TextFileManager(this);
    //ListView에서 플로팅메뉴를 부를때 어떤 리스트인지 주소를 기록하기 위한 변수 fPos
    int fPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> values = new ArrayList<>();

        // Android에서 제공하는 String 문자열 하나를 출력하는 layout으로 어댑터 생성
        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        // layout xml 파일에 정의된 ListView의 객체
        m_ListView = findViewById(R.id.list);
        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        //파일매니저의 내용을 문자열로 받아옴
       String data = mFileMgr.load();

       //줄바꿈을 기준으로 잘라서 m_adapter에 넣음
       String [] s2 = data.split("\n");
        if (!data.isEmpty()) {
            for (int i = 0; i < s2.length; i++) {
                m_Adapter.add(s2[i]);
            }
        }

        // ListView 아이템 터치 시 이벤트를 처리할 리스너 설정
        m_ListView.setOnItemClickListener(onClickListItem);
        //
        m_ListView.setOnItemLongClickListener(onLongClickListItem);

    }
    //리스트를 짧게 클릭할 때 지도로 넘겨주는 이벤트
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //현재 클릭한 리스트 아이템을 어뎁터에서 불러와서 문자열로받고
            String s1 = m_Adapter.getItem(position);

            //받은 문자열을 ->를 기준으로 출발지와 도착지를 구분한다. s2[0]출발지, s2[1]도착지
            String [] s2 = s1.split("->");

            //이벤트 처리시 내용을 전달할때 사용할 매개체 intent를 선언
            Intent intent = null;
            //intent 이벤트 처리를 암묵적으로 처리한다.
            intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://www.google.com/maps/dir/"+s2[0]+"/"+s2[1]));

            //예외처리
            if (intent != null) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }

    };
    //길게 클릭했을때 그 뷰에서 플로팅메뉴가 열린다.
    //이때 길게 누른 리스트의 위치를 받아야 하는데 이를 전역변수로 선언해준다.
    private AdapterView.OnItemLongClickListener onLongClickListItem = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //길게 클릭한 위치를 선언
            fPos=position;
            registerForContextMenu(view);
            return false;
        }
    };

    //플로팅메뉴 창
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("메모");
        menu.add(0, 1, 0, "수정");
        menu.add(0, 2, 0, "삭제");
    }
    //플로팅 메뉴 아이템을 클릭했을 때
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: //수정을 눌렀을 때
                //액티비티를 할때 가져올 액티비티는 reviseActivity.class이다.
                Intent in = new Intent(this, reviseActivity.class);
                //그 액티비티를 분별할 requestcode는 GET_NUMBER로 reviseActivity임을 의미한다.
                startActivityForResult(in, GET_NUMBER);
                return true;

            case 2: //삭제를 눌렀을 때
                //삭제할 리스트의 문자열을 받아와 remove 시킨다.
                String items = m_Adapter.getItem(fPos);
                m_Adapter.remove(items);

                //제거된 리스트의 어뎁터 내용을 다시 파일에 초기화해준다.
                //파일의 모든 내용을 삭제하고 다시 파일에 내용을 save 시킴. 이때 줄바꿈도 저장해줘야함
                mFileMgr.delete();
                for(int i=0;i<m_Adapter.getCount();i++)
                    mFileMgr.save(m_Adapter.getItem(i)+"\n");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    //optionmenu바에 ADD메뉴를 추가한다
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //ADD메뉴를 누르면 액티비티가 실행됨
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:   //ADD를 누르면
                Log.v("ActionBar", "refresh button");
                Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_SHORT).show();
                //Addactivity로 부터 인텐트를 받아온다.
                Intent in = new Intent(MainActivity.this, AddActivity.class);
                //액티비티를 분별할 requestcode는GET_STRING로 AddActivity 를 의미한다.
                startActivityForResult(in, GET_STRING);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }
    //액티비티를 반환하는 처리
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if를 통해 requestCode를 분별한다.
        if(requestCode == GET_STRING) {         //AddActivity에서 가져온 액티비티일경우 어뎁터에 내용을 추가하고 파일을 세이브한다.
            if(resultCode == RESULT_OK) {
                m_Adapter.add(data.getStringExtra("INPUT_TEXT1")+"->"+data.getStringExtra("INPUT_TEXT2"));
                mFileMgr.save(data.getStringExtra("INPUT_TEXT1")+"->"+data.getStringExtra("INPUT_TEXT2")+"\n");
            }
        }

        else if(requestCode == GET_NUMBER) {    //reviseActivity에서 가져온 액티비티일 경우 선택된 아이템을 전역으로 선언된 주소로 인해 지우고 그 위치로 insert한다.
            if(resultCode == RESULT_OK) {
                String item = m_Adapter.getItem(fPos);
                m_Adapter.remove(item);
                m_Adapter.insert(data.getStringExtra("INPUT_TEXT1")+"->"+data.getStringExtra("INPUT_TEXT2"),fPos);

                //파일의 내용을 지우고 새로 추가하는데 어뎁터가 리스트별로 주소값을 가지는 것을 참고하여
                //주소마다 끊어서 텍스트를 받아오고 저장한다.
                mFileMgr.delete();
                for(int i=0;i<m_Adapter.getCount();i++)
                mFileMgr.save(m_Adapter.getItem(i)+"\n");
            }

        }
    }
}
