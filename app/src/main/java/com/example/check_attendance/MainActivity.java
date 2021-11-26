package com.example.check_attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    myDBHelper myDBHelper;
    SQLiteDatabase sqlDB;

    TextView viewSemester;
    ImageButton addSubjectTop, addSubjectUnder, selectSemester, settings;

    View addSubjectDialog, subjectSettingDialog;

    EditText editTextAddSubject, editTextAddProfessor, editTextAddMemo, editTextAddTimes;

    EditText editTextEditSubject, editTextEditProfessor, editTextEditMemo;
    Button buttonEditSubject, buttonEditProfessor, buttonEditMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Check Attendance");

        viewSemester = (TextView) findViewById(R.id.viewSemester);
        addSubjectTop = (ImageButton) findViewById(R.id.addSubjectTop);
        addSubjectUnder = (ImageButton) findViewById(R.id.addSubjectUnder);
        selectSemester = (ImageButton) findViewById(R.id.selectSemester);
        settings = (ImageButton) findViewById(R.id.settings);

        // 과목 추가 dialog 관련 위젯
        editTextAddSubject = (EditText) findViewById(R.id.editTextAddSubject); // 과목 이름
        editTextAddProfessor = (EditText) findViewById(R.id.editTextAddProfessor); // 과목 교수
        editTextAddMemo = (EditText) findViewById(R.id.editTextAddMemo); // 과목 메모
        editTextAddTimes = (EditText) findViewById(R.id.editTextAddTimes); // 강의 횟수

        // 과목 수정 dialog 관련 위젯
        editTextEditSubject = (EditText) findViewById(R.id.editTextEditSubject);
        editTextEditProfessor = (EditText) findViewById(R.id.editTextEditProfessor);
        editTextEditMemo = (EditText) findViewById(R.id.editTextEditMemo);

        buttonEditSubject = (Button) findViewById(R.id.buttonEditSubject);
        buttonEditProfessor = (Button) findViewById(R.id.buttonEditProfessor);
        buttonEditMemo = (Button) findViewById(R.id.buttonEditMemo);

        myDBHelper = new myDBHelper(this);
        addSubjectTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubjectDialog = (View) View.inflate(MainActivity.this, R.layout.add_subject_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setIcon(R.drawable.add);
                dlg.setView(addSubjectDialog);
                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 토스트 메시지
                        Toast.makeText(MainActivity.this, "과목 추가", Toast.LENGTH_SHORT).show();
                        // DB에 과목 추가하기
                        sqlDB = myDBHelper.getWritableDatabase();
                        // CREATE TABLE subjectTBL(subjectId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        //                    "semester INTEGER, subjectName CHAR(30), subjectProfessor CHAR(20), " +
                        //                    "subjectMemo CHAR(50), times INTEGER)
                        sqlDB.execSQL("INSERT INTO subjectTBL VALUES (0," + viewSemester.getText() + ", '" + editTextAddSubject + "','"
                                + editTextAddProfessor + "','" + editTextAddMemo + "'," + editTextAddTimes + ")");
                    }
                });
                dlg.show();
            }
        });

        addSubjectUnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubjectDialog = (View) View.inflate(MainActivity.this, R.layout.add_subject_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setIcon(R.drawable.add);
                dlg.setView(addSubjectDialog);
                dlg.setPositiveButton("추가", null);
                dlg.show();
            }
        });


        selectSemester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 학기 선택 가능한 dialog 불러오기
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정 listView 불러오기

            }
        });

    }

    public void UpdateView() {
        // DB 업데이트 시 화면에 즉시 반영 가능하도록 함
        sqlDB = myDBHelper.getWritableDatabase();

    }

    private class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE subjectTBL(subjectId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "semester INTEGER, subjectName CHAR(30), subjectProfessor CHAR(20), " +
                    "subjectMemo CHAR(50), times INTEGER)");
            db.execSQL("CREATE TABLE attInfoTBL(infoId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "subjectId INTEGER, date INTEGER, attendance INTEGER," +
                    "FOREIGN KEY(subjectId) REFERENCES subjectTBL(subjectId))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS subjectTBL");
            db.execSQL("DROP TABLE IF EXISTS attInfoTBL"); // Attendance Info Table
            onCreate(db);
        }
    }
}