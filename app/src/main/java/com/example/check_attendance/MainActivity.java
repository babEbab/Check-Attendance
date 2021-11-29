package com.example.check_attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    myDBHelper myDBHelper;
    SQLiteDatabase sqlDB, sqlDB2;

    TextView viewSemester;
    ImageButton addSubjectTop, addSubjectUnder, selectSemester, settings;

    LinearLayout underContent;

    View addSubjectDialog, subjectSettingDialog;

    EditText editTextAddSubject, editTextAddProfessor, editTextAddMemo, editTextAddTimes;

    EditText editTextEditSubject, editTextEditProfessor, editTextEditMemo;
    Button buttonEditSubject, buttonEditProfessor, buttonEditMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Check Attendance");
//        sqlDB = myDBHelper.getWritableDatabase(); // SQL 작동 확인
//        Toast.makeText(MainActivity.this, "DB 작동 확인", Toast.LENGTH_SHORT).show();
//        sqlDB.close();
//        Toast.makeText(MainActivity.this, "DB CLOSE 확인", Toast.LENGTH_SHORT).show();

        viewSemester = (TextView) findViewById(R.id.viewSemester);
        addSubjectTop = (ImageButton) findViewById(R.id.addSubjectTop);
        addSubjectUnder = (ImageButton) findViewById(R.id.addSubjectUnder);
        selectSemester = (ImageButton) findViewById(R.id.selectSemester);
        settings = (ImageButton) findViewById(R.id.settings);

//        // 과목 추가 dialog 관련 위젯
//        editTextAddSubject = (EditText) findViewById(R.id.editTextAddSubject); // 과목 이름
//        editTextAddProfessor = (EditText) findViewById(R.id.editTextAddProfessor); // 과목 교수
//        editTextAddMemo = (EditText) findViewById(R.id.editTextAddMemo); // 과목 메모
//        editTextAddTimes = (EditText) findViewById(R.id.editTextAddTimes); // 강의 횟수

        // 과목 수정 dialog 관련 위젯
        editTextEditSubject = (EditText) findViewById(R.id.editTextEditSubject);
        editTextEditProfessor = (EditText) findViewById(R.id.editTextEditProfessor);
        editTextEditMemo = (EditText) findViewById(R.id.editTextEditMemo);

        buttonEditSubject = (Button) findViewById(R.id.buttonEditSubject);
        buttonEditProfessor = (Button) findViewById(R.id.buttonEditProfessor);
        buttonEditMemo = (Button) findViewById(R.id.buttonEditMemo);

        myDBHelper = new myDBHelper(this);

        updateView(Integer.parseInt(viewSemester.getText().toString()));
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
                        editTextAddSubject = (EditText) addSubjectDialog.findViewById(R.id.editTextAddSubject); // 과목 이름
                        editTextAddProfessor = (EditText) addSubjectDialog.findViewById(R.id.editTextAddProfessor); // 과목 교수
                        editTextAddMemo = (EditText) addSubjectDialog.findViewById(R.id.editTextAddMemo); // 과목 메모
                        editTextAddTimes = (EditText) addSubjectDialog.findViewById(R.id.editTextAddTimes); // 강의 횟수
                        // 토스트 메시지
                        Toast.makeText(MainActivity.this, "과목 추가", Toast.LENGTH_SHORT).show();

                        // DB에 과목 추가하기
                        sqlDB = myDBHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO subjectTBL(semester, subjectName, subjectProfessor, subjectMemo, times) " +
                                "VALUES('" + viewSemester.getText().toString() + "', '" + editTextAddSubject.getText().toString() +
                                "','" + editTextAddProfessor.getText().toString() + "','" + editTextAddMemo.getText().toString() +
                                "'," + editTextAddTimes.getText().toString() + ")");
                        Toast.makeText(MainActivity.this, "DB에 과목 추가", Toast.LENGTH_SHORT).show();

                        Cursor cursor = sqlDB.rawQuery("SELECT subjectId FROM subjectTBL ORDER BY ROWID DESC LIMIT 1", null);
                        int recentSubjectId = 0;
                        while (cursor.moveToNext()) { // while 무조건 써줘야 함
                            recentSubjectId = cursor.getInt(0); // subjectTBL에 가장 최근에 저장된 데이터의 subjectId 가져오기
                        }
//                        int[] attInfo = new int[Integer.parseInt(editTextAddTimes.getText().toString())];
                        Toast.makeText(MainActivity.this, "editTextAddTimes.getText().toString()): " + editTextAddTimes.getText().toString(), Toast.LENGTH_SHORT).show();
                        for (int k = 0; k < Integer.parseInt(editTextAddTimes.getText().toString()); k++) { // 강의 횟수만큼 attInfoTBL에 데이터 삽입
                            //            db.execSQL("CREATE TABLE attInfoTBL(infoId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            //                    "subjectId INTEGER, date INTEGER, attendance INTEGER," +
                            //                    "FOREIGN KEY(subjectId) REFERENCES subjectTBL(subjectId))");
                            sqlDB.execSQL("INSERT INTO attInfoTBL(subjectId, date, attendance) VALUES(" + recentSubjectId + ", 99999999, 0)");
                        }
                        Toast.makeText(MainActivity.this, "강의 횟수만큼 attInfoTBL에 데이터 삽입 성공", Toast.LENGTH_SHORT).show();
                        sqlDB.close();
                        // 추가된 과목의 출석표를 화면에 보이게 하기
                        updateView(Integer.parseInt(viewSemester.getText().toString()));
                    }
                });
                dlg.show();
            }
        });

        addSubjectUnder.setOnClickListener(new View.OnClickListener() { // addSubjectTop하고 똑같이 해주면 됨
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

                // 해당 학기에 해당되는 출석표를 화면에 보이게 하기
                updateView(Integer.parseInt(viewSemester.getText().toString()));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정 listView 불러오기
            }
        });
    }

    public void updateView(int semester) {
        Toast.makeText(MainActivity.this, "updateView 시작", Toast.LENGTH_SHORT).show();
        // DB 업데이트 시 해당 학기에 맞는 과목 출석표가 화면에 즉시 반영되도록 함
        sqlDB = myDBHelper.getReadableDatabase();

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM subjectTBL WHERE semester = " + semester, null);
        Toast.makeText(MainActivity.this, "cursor.getCount():" + cursor.getCount(), Toast.LENGTH_SHORT).show();
        int numOfSubject = cursor.getCount();

        // 화면 초기화
        underContent = (LinearLayout) findViewById(R.id.underContent); // underContent Layout
        underContent.removeAllViewsInLayout();
//        for (int i = 0; i < numOfSubject; i++) {
//            underContent.removeViewAt(i);
//        }

        //        while (cursor.moveToNext()) {
//            Toast.makeText(MainActivity.this, "cursor.getInt():" + cursor.getInt(0) + " " + cursor.getInt(1), Toast.LENGTH_SHORT).show();
//        }
//        cursor = sqlDB.rawQuery("SELECT * FROM subjectTBL", null);
//        while (cursor.moveToNext()) {
//            Toast.makeText(MainActivity.this, "cursor.getInt():" + cursor.getInt(0) + " " + cursor.getInt(1), Toast.LENGTH_SHORT).show();
//        }
        if (cursor.getCount() == 0) {
            return;
        }

        Subject[] subjects = new Subject[numOfSubject];
        LinearLayout[] subjectLinearLayouts = new LinearLayout[numOfSubject];

        underContent = (LinearLayout) findViewById(R.id.underContent); // underContent Layout
        for (int i = 0; (i < numOfSubject) && cursor.moveToNext(); i++) { // Subject Class의 인스턴스 만들기
            subjects[i] = new Subject(cursor.getInt(0), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getInt(5));
            subjectLinearLayouts[i] = new LinearLayout(this);
            makeSubjectLayout(subjectLinearLayouts[i], subjects[i]);
        }
        sqlDB.close();
    }

    public void makeSubjectLayout(LinearLayout subjectLinearLayout, Subject subject) { // 과목 정보를 화면에 띄운다(1과목)

        // underContent Layout(id) 아래에 과목 띄우기
        // 1차
//        subjectLinearLayout = new LinearLayout(this);
        subjectLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        subjectLinearLayout.setOrientation(LinearLayout.VERTICAL);
        subjectLinearLayout.setPadding(20, 20, 20, 40);

        // 2차(과목 설명(과목 이름, 교수 이름 등) LinearLayout)
        LinearLayout subjectDescLinearLayout = new LinearLayout(this);
        subjectDescLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        subjectDescLinearLayout.setBackgroundColor(Color.WHITE);
        subjectDescLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // 3차(과목 이름 LinearLayout)
        LinearLayout subjectNameLinearLayout = new LinearLayout(this);
        subjectNameLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        subjectNameLinearLayout.setGravity(View.FOCUS_LEFT);
        subjectNameLinearLayout.setPadding(10, 10, 10, 10);

        // 4차(과목 이름 TextView)
        TextView subjectNameTextView = new TextView(this);
        LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        subjectNameTextView.setLayoutParams(lparam);
        subjectNameTextView.setTextSize(convertDPtoPX(this, 10));
        subjectNameTextView.setText(subject.subjectName);

        // 3차(교수 이름 LinearLayout)
        LinearLayout professorNameLinearLayout = new LinearLayout(this);
        professorNameLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        professorNameLinearLayout.setGravity(Gravity.RIGHT);
        professorNameLinearLayout.setPadding(10, 10, 10, 10);

        // 4차(교수 이름 TextView)
        TextView professorNameTextView = new TextView(this);
        lparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        professorNameTextView.setLayoutParams(lparam);
        professorNameTextView.setTextSize(convertDPtoPX(this, 10));
        professorNameTextView.setPadding(0, 0, 10, 0);
        professorNameTextView.setText(subject.subjectProfessor);

        // 4차(과목 설정 imageButton)
        ImageButton settingSubject = new ImageButton(this);
        lparam = new LinearLayout.LayoutParams(convertDPtoPX(this, 20), convertDPtoPX(this, 20));
        lparam.gravity = Gravity.CENTER;
        settingSubject.setLayoutParams(lparam);
        settingSubject.setPadding(0, 0, 0, 0);
        settingSubject.setBackground(getResources().getDrawable(R.drawable.settings));

        //                dlg.setView(addSubjectDialog);
        //                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
        //                    @Override
        //                    public void onClick(DialogInterface dialogInterface, int i) {
        //                        // 토스트 메시지
        //                        Toast.makeText(MainActivity.this, "과목 추가", Toast.LENGTH_SHORT).show();
        //
        //                        // DB에 과목 추가하기
        //                        sqlDB = myDBHelper.getWritableDatabase();
        //                        sqlDB.execSQL("INSERT INTO subjectTBL VALUES ('" + Integer.parseInt(viewSemester.getText().toString()) + ", '" +
        //                                editTextAddSubject.getText().toString() + "','" + editTextAddProfessor.getText().toString() +
        //                                "','" + editTextAddMemo.getText().toString() + "'," +
        //                                Integer.parseInt(editTextAddTimes.getText().toString()) + ")");
        //
        //                        Cursor cursor = sqlDB.rawQuery("SELECT subjectId FROM subjectTBL ORDER BY ROWID DESC LIMIT 1", null);
        //                        int recentSubjectId = cursor.getInt(0); // subjectTBL에 가장 최근에 저장된 데이터의 subjectId 가져오기
        //                        int[] attInfo = new int[Integer.parseInt(editTextAddTimes.getText().toString())];
        //                        for (int count = 0; i < attInfo.length; count++) { // 강의 횟수만큼 attInfoTBL에 데이터 삽입
        //                            sqlDB.execSQL("INSERT INTO attInfoTBL VALUES (recentSubjectId + " + "," + null + "," + 0);
        //                        }
        //                        sqlDB.close();
        //                        // 추가된 과목의 출석표를 화면에 보이게 하기
        //                        updateView(Integer.parseInt(viewSemester.getText().toString()));
        //                    }
        //                });
        //                dlg.show();
        //            }
        settingSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectSettingDialog = (View) View.inflate(MainActivity.this, R.layout.subject_setting_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setIcon(R.drawable.settings);
                dlg.setView(subjectSettingDialog);
                dlg.show();
            }
        });

        // 2차(메모 LinearLayout)
        LinearLayout memoLinearLayout = new LinearLayout(this);
        memoLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        memoLinearLayout.setPadding(10, 10, 10, 10);
        memoLinearLayout.setBackgroundColor(Color.WHITE);

        // 3차(메모 TextView)
        TextView memoTextView = new TextView(this);
        lparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        memoTextView.setLayoutParams(lparam);
        memoTextView.setTextSize(convertDPtoPX(this, 10));
        memoTextView.setText(subject.subjectMemo);

        // addView 모음(Layout 안에 Layout, View 등 추가)
        // 3차 안에 4차 추가
        subjectNameLinearLayout.addView(subjectNameTextView);
        professorNameLinearLayout.addView(professorNameTextView);
        professorNameLinearLayout.addView(settingSubject);

        // 2차 안에 3차 추가
        subjectDescLinearLayout.addView(subjectNameLinearLayout);
        subjectDescLinearLayout.addView(professorNameLinearLayout);
        memoLinearLayout.addView(memoTextView);

        // 1차 안에 2차 추가
        subjectLinearLayout.addView(subjectDescLinearLayout);
        subjectLinearLayout.addView(memoLinearLayout);

        // 출석 정보 가져오기
        sqlDB = myDBHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM attInfoTBL WHERE subjectId = " + subject.subjectId, null);
        Toast.makeText(MainActivity.this, "출석 정보 가져오기 성공: " + cursor.getCount(), Toast.LENGTH_SHORT).show();

        // 출석 정보 띄우기(TableLayout)
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayout.setBackgroundColor(Color.WHITE);
        tableLayout.setOrientation(LinearLayout.VERTICAL);
        tableLayout.setStretchAllColumns(true);
        Toast.makeText(MainActivity.this, "출석 정보 띄우기 성공", Toast.LENGTH_SHORT).show();
        // 여기까지 성공

        //         for (int i = 0; (i < numOfSubject) && cursor.moveToNext(); i++) { // Subject Class의 인스턴스 만들기
        //            subjects[i] = new Subject(cursor.getInt(0), cursor.getString(2), cursor.getString(3),
        //                    cursor.getString(4), cursor.getInt(5));
        //            subjectLinearLayouts[i] = new LinearLayout(this);
        //            makeSubjectLayout(subjectLinearLayouts[i], subjects[i]);
        //        }

        int attTimes = subject.times;
        int[] attArr = new int[attTimes]; // cursor를 1번 움직일 때마다 출석 여부를 배열에 기록해둔다.
        TableRow tableRowDate, tableRowAtt;
//        lparam = new LinearLayout.LayoutParams(50,50);
//                = new TableRow(this);
//        tableRowDate.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        Toast.makeText(MainActivity.this, "tableRowDate 생성 start", Toast.LENGTH_SHORT).show();
        int i = 0;
        while (cursor.moveToNext() && (i < attTimes)) {
//            Toast.makeText(MainActivity.this, "cursor.moveToNext: " +cursor.getInt(2)+" "
//                    + cursor.getInt(3), Toast.LENGTH_SHORT).show();
//        for (int i = 0; (i < subject.times) && cursor.moveToNext(); i++) { // Subject Class의 인스턴스 만들기
            if (i % 5 == 0) {
                // 날짜표
                tableRowDate = new TableRow(this);
                tableRowDate.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
//                tableRowDate.setGravity(Gravity.CENTER);
//                Toast.makeText(MainActivity.this, "tableRowDate 생성" + i, Toast.LENGTH_SHORT).show();
                if ((attTimes - i) < 4) {
                    for (int j = 0; j < (attTimes - i); j++) {
                        Button dateButton = new Button(this);
//                    dateButton.setLayoutParams(lparam);
//                    Toast.makeText(MainActivity.this, "Button 생성"+i+j, Toast.LENGTH_SHORT).show();
                        dateButton.setText(String.valueOf(cursor.getInt(2) % 10000)); // 연도 빼고 월/일만 가져오기
                        dateButton.setBackgroundColor(Color.rgb(234, 186, 186));
//                        dateButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                                TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        attArr[i + j] = cursor.getInt(3);
                        tableRowDate.addView(dateButton);
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        Button dateButton = new Button(this);
                        dateButton.setText(String.valueOf(cursor.getInt(2) % 10000)); // 연도 빼고 월/일만 가져오기
                        dateButton.setBackgroundColor(Color.rgb(234, 186, 186));
                        dateButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        attArr[i + j] = cursor.getInt(3);
                        tableRowDate.addView(dateButton);
                    }
                }

                // 출석여부표
                tableRowAtt = new TableRow(this);
                tableRowAtt.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                if ((attTimes - i) < 4) {
                    for (int j = 0; j < (attTimes - i); j++) {
                        Button attButton = new Button(this);
                        attButton.setWidth(50);
                        attButton.setText(String.valueOf(attArr[i + j]));
                        attButton.setBackgroundColor(Color.rgb(185,235,199));
                        attButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(attButton.getText().toString().equals("0")){
//                                    sqlDB.execSQL();
                                }
                            }
                        });
                        tableRowAtt.addView(attButton);
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        Button attButton = new Button(this);
                        attButton.setWidth(50);
                        attButton.setText(String.valueOf(attArr[i + j]));
                        attButton.setBackgroundColor(Color.rgb(185,235,199));
                        attButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        tableRowAtt.addView(attButton);
                    }
                }

                tableLayout.addView(tableRowDate);
                tableLayout.addView(tableRowAtt);
            }
//            Toast.makeText(MainActivity.this, "tableDate 성공" + i, Toast.LENGTH_SHORT).show();

//            Toast.makeText(MainActivity.this, "날짜: " +cursor.getInt(2) % 10000+" 출석여부: "
//                    + attArr[i], Toast.LENGTH_SHORT).show();
//            if (i % 5 == 4 || i == (attTimes - 1)) {
//                tableRowAtt = new TableRow(this);
//                tableRowAtt.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//                for (int j = 0; j < (5-i%5); j++) {
//                    Button attButton = new Button(this);
//                    attButton.setText(attArr[i]);
//                    tableRowAtt.addView(attButton);
//                    Toast.makeText(MainActivity.this, "tableRowAtt.addView(attButton): " + i, Toast.LENGTH_SHORT).show();
//                }
////                tableLayout.addView(tableRowDate);
//                tableLayout.addView(tableRowAtt);
//                Toast.makeText(MainActivity.this, "tableLayout.addView: " + i, Toast.LENGTH_SHORT).show();
//            }
            i++;
        }

        // 커서 관련 문제 해결 요망
//        for (int i = 1; i < subject.times; i++) {
//            if (i % 5 == 1) {
//                TableRow tableRowDate = new TableRow(this);
//                tableRowDate.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//                int[] attArr = new int[5]; // cursor를 1번 움직일 때마다 출석 여부를 배열에 기록해둔다.
//                for (int j = 0; (j <= subject.times - i) && cursor.moveToNext(); j++) {
//                    Button dateButton = new Button(this);
//                    dateButton.setText(cursor.getInt(2) / 10000); // 연도 빼고 월/일만 가져오기
//                    attArr[j] = cursor.getInt(3);
//                    tableRowDate.addView(dateButton);
//                }
//                Toast.makeText(MainActivity.this, "커서 문제일까?", Toast.LENGTH_SHORT).show();
//                TableRow tableRowAtt = new TableRow(this);
//                tableRowAtt.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//                for (int j = 0; j <= subject.times - i; j++) {
//                    Button attButton = new Button(this);
//                    attButton.setText(attArr[j]);
//                    tableRowAtt.addView(attButton);
//                }
//                tableLayout.addView(tableRowDate);
//                tableLayout.addView(tableRowAtt);
//            }
//        }
        sqlDB.close();

        subjectLinearLayout.addView(tableLayout);
        // underContent Layout 안에 subjectLinearLayout 추가(제일 마지막 부분)
//        underContent.addView(subjectLinearLayout, 0);
        underContent.addView(subjectLinearLayout);
    }

//    public void makeSubjectAttLayout(int count) { // 과목별 출석정보를 화면에 띄운다(강의 횟수만큼 반복)
//
//    }

    private class Subject { // 과목 클래스
        String subjectName, subjectProfessor, subjectMemo;
        int subjectId, times;
        int[] dates, attendances;

        public Subject(int subjectId, String subjectName, String subjectProfessor, String subjectMemo, int times) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.subjectProfessor = subjectProfessor;
            this.subjectMemo = subjectMemo;
            this.times = times;
            this.dates = new int[times];
            this.attendances = new int[times];
        }
    }

    public int convertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
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
            Toast.makeText(MainActivity.this, "subjectTBL 생성 확인", Toast.LENGTH_SHORT).show();

            db.execSQL("CREATE TABLE attInfoTBL(infoId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "subjectId INTEGER, date INTEGER, attendance INTEGER," +
                    "FOREIGN KEY(subjectId) REFERENCES subjectTBL(subjectId))");
            Toast.makeText(MainActivity.this, "attInfoTBL 생성 확인", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 최초 실행 시에만 작동하도록 변경
            Toast.makeText(MainActivity.this, "App 시작", Toast.LENGTH_SHORT).show();
            db.execSQL("DROP TABLE IF EXISTS subjectTBL");
            db.execSQL("DROP TABLE IF EXISTS attInfoTBL"); // Attendance Info Table
            onCreate(db);
        }
    }
}