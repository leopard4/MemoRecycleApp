package com.leopard4.memorecycleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.leopard4.memorecycleapp.adapter.MemoAdapter;
import com.leopard4.memorecycleapp.data.DatabaseHandler;
import com.leopard4.memorecycleapp.model.Memo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText editSearch;
    ImageView imgSearch, imgDelete;


    RecyclerView recyclerView;
    MemoAdapter adapter;
    List<Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        imgDelete = findViewById(R.id.imgDelete);

        // 리사이클러뷰를 화면에 연결하고,
        // 쌍으로 같이 다니는 코드도 작성
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 버튼을 누르면 메모 생성화면으로 이동
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);

            }

    });
        // 검색기능
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 검색어를 가져와서, DB에서 검색
                String search = editSearch.getText().toString().trim();

                // search가 비어있거나, 공백이면 이런코드도 작성할 수 있으나 여기선 필요없다.
//                if(search.isEmpty() || search.equals("")){
//                    return;
//                }
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.searchMemo(search);

                // 리사이클러뷰에 어댑터를 연결(화면에 표시)
                adapter = new MemoAdapter(MainActivity.this, memoList);
                recyclerView.setAdapter(adapter);

                // 검색이 끝나면 검색어를 지운다.
                editSearch.setText("");

            }
        });
        // 유저가 검색어를 입력하는 동시에 검색내용 표시
        editSearch.addTextChangedListener(new TextWatcher() {
            //
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                // 유저가 입력한 키워드 뽑아서
                String search = editSearch.getText().toString().trim();

                // 2글자 이상 입력했을때만, 검색이 되도록 한다.
                if(search.length() < 2 && search.length() > 0){
                    return;
                }
                // 디비에서 가져온후
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                memoList = db.searchMemo(search);
                // 화면에 표시
                adapter = new MemoAdapter(MainActivity.this, memoList);
                recyclerView.setAdapter(adapter);
            }
        });

        // X이미지를 눌르면 메인화면으로 돌아간다.
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    // DB에서 데이터를 가져와서, 리사이클러뷰에 뿌려준다.
                    DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                    memoList = db.getAllMemo();

                    // 리사이클러뷰에 어댑터를 연결
                    adapter = new MemoAdapter(MainActivity.this, memoList);
                    recyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    protected void onResume() {

        // DB에서 데이터를 가져와서, 리사이클러뷰에 뿌려준다.
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        memoList = db.getAllMemo();

        // 리사이클러뷰에 어댑터를 연결
        adapter = new MemoAdapter(MainActivity.this, memoList);
        recyclerView.setAdapter(adapter);

        super.onResume();

    }

}