package com.leopard4.memorecycleapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.leopard4.memorecycleapp.model.Memo;
import com.leopard4.memorecycleapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_MEMO_TABLE = "create table memo ( id integer primary key, title text, content text)";

        sqLiteDatabase.execSQL(CREATE_MEMO_TABLE); // SQL문 실행

    }

    // 기존에 테이블을 삭제하고, 새 테이블을 다시 만든다.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE = "drop table memo";

        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }
    // 이제부터는 우리가 앱 동작시키는데 필요한
    // CRUD 관련된 SQL문이 들어간,
    // 메소드를 만들면 된다.

    // 1. 메모 추가하는 메소드(함수) c (create)
    public void addMemo(Memo memo) {
        // 데이터베이스에 쓰기 위한 객체를 생성
        SQLiteDatabase db = this.getWritableDatabase();

        // 쿼리문작성
        String query = "insert into memo (title, content) values ( ? , ?)";
        String[] args = new String[] {memo.getTitle(), memo.getContent()};
        // 데이터베이스에 저장
        db.execSQL(query, args);
        db.close();
    }
    // 2. 메모 읽어오는 메소드(함수) r (read)
    public List<Memo> getAllMemo() {

        // 데이터베이스 읽기 위한 객체 생성
        SQLiteDatabase db = this.getReadableDatabase();

        // 데이터베이스에서 데이터를 읽어오는 SQL문
        String query = "select * from memo order by id desc";

        // SQL문 실행
        Cursor cursor = db.rawQuery(query, null);


        // 데이터베이스에서 읽어온 데이터를
        // Memo 객체에 저장
        List<Memo> memoList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);  // 컬럼의 인덱스
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                Log.i("MEMO_TABLE", "id: " + id + ", title: " + title + ", content: " + content);

                // 이 데이터를, 화면에 표시하기 위해서는
                // 메모리에 전부 다 남아있어야 한다!!!
                // 그래서, 이 데이터를, Memo 객체로 만들어서
                Memo memo = new Memo(id, title, content);
                // 이 Contact 객체를, List에 담아둔다.
                memoList.add(memo); // 이미 order by로 역순으로 받아서 0 생략

            } while (cursor.moveToNext());
        }
        db.close();

        return memoList;
    }
    // 3. 메모 수정하는 메소드(함수) u (update)
    public void updateMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "update memo " +
                "set title = ?, content = ? " +
                "where id = ? ";
        db.execSQL(query, new String[]{memo.title, memo.content, memo.id + ""});     // flask의 record와 비슷하다.

        db.close();
    }

    // 4. 메모 삭제하는 메소드(함수) d (delete)
    public void deleteMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "delete from memo where id = ?";

        String[] args = new String[]{memo.id + ""};  // 파라미터로 넘겨줄 값들을 배열로 만든다.

        db.execSQL(query, args);

        db.close();
    }

    // 검색기능
    List<Memo> memoList = new ArrayList<>();
    public List<Memo> searchMemo(String search) {
        // 데이터 베이스에서 saerch를 찾아서 리턴해주는 메소드
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from memo where title like ? or content like ? order by id desc";
        String[] args = new String[]{"%" + search + "%", "%" + search + "%"};
        Log.i("searchLog", search + " " + args[0] + " " + args[1]);
        Cursor cursor = db.rawQuery(query, args);

        // 데이터베이스에서 읽어온 데이터를
        // Memo 객체에 저장
        if (cursor.moveToFirst()) {            // 커서가 첫번째 행으로 이동
            do {
                int id = cursor.getInt(0);  // 컬럼의 인덱스
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                Log.i("MEMO_TABLE", "id: " + id + ", title: " + title + ", content: " + content);

                // 이 데이터를, 화면에 표시하기 위해서는
                // 메모리에 전부 다 남아있어야 한다!!!
                // 그래서, 이 데이터를, Memo 객체로 만들어서
                Memo memo = new Memo(id, title, content);
                // 이 Memo 객체를, List에 담아둔다.
                memoList.add(memo); // 이미 order by로 역순으로 받아서 0 생략

            } while (cursor.moveToNext());
        }
        db.close();

        return memoList;

    }
}
