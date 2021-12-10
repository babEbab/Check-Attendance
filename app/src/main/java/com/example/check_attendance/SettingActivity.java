package com.example.check_attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SettingActivity extends AppCompatActivity {

    static final String[] listMenu = {"모든 데이터 초기화", "개발자 정보"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // DBHelper 변수 생성
        MainActivity.myDBHelper myDBHelper;
        myDBHelper = new MainActivity.myDBHelper(this);
        SQLiteDatabase sqlDB;
        sqlDB = myDBHelper.getWritableDatabase();

        ListView listView = findViewById(R.id.settingListView);

        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listMenu);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        AlertDialog.Builder dlgReset = new AlertDialog.Builder(SettingActivity.this);
                        dlgReset.setIcon(R.drawable.settings);
                        dlgReset.setTitle("모든 데이터 초기화");
                        dlgReset.setMessage("앱의 모든 과목정보, 출석정보를 초기화합니다. 확인 버튼을 누른 후 앱을 종료하고 재시작하세요.");
                        dlgReset.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sqlDB.execSQL("DELETE FROM attInfoTBL;");
                                sqlDB.execSQL("DELETE FROM subjectTBL;");
                            }
                        });
                        dlgReset.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dlgReset.show();
                        break;
                    case 1:
                        AlertDialog.Builder dlgDeveloper = new AlertDialog.Builder(SettingActivity.this);
                        dlgDeveloper.setIcon(R.drawable.settings);
                        dlgDeveloper.setTitle("개발자 정보");
                        dlgDeveloper.setMessage("공주대학교 컴퓨터 공학 전공 최다솜\n" +
                                "Github: https://github.com/babEbab");
                        dlgDeveloper.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dlgDeveloper.show();
                        break;
                }
            }
        });
    }
}