package com.leopard4.memorecycleapp;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leopard4.memorecycleapp.data.DatabaseHandler;
import com.leopard4.memorecycleapp.model.Memo;

public class AddActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                // 제목과 내용이 모두 있어야 한다!
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddActivity.this, "제목과 내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 묶어서 처리할 Memo 클래스를 하나 만든다.
                Memo memo = new Memo(title, content);

                // DB에 저장한다. // 힙에있는건 날라가므로
                DatabaseHandler db = new DatabaseHandler(AddActivity.this);
                db.addMemo(memo);

                // 유저한테 잘 저장되었다고, 알려주고
                Toast.makeText(AddActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                // 액티비티 종료
                finish();

            }
        });

    }
}