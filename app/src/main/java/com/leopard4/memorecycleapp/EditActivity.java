package com.leopard4.memorecycleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leopard4.memorecycleapp.data.DatabaseHandler;
import com.leopard4.memorecycleapp.model.Memo;

public class EditActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editContent;
    Button btnSave;

    int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        Memo memo = (Memo) getIntent().getSerializableExtra("memo");
        id = memo.getId();
        editTitle.setText(memo.getTitle());
        editContent.setText(memo.getContent());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();


                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(EditActivity.this, "모두입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Memo memo = new Memo(id, title, content);

                // DB에 저장한다.
                DatabaseHandler db = new DatabaseHandler(EditActivity.this);
                db.updateMemo(memo);

                // 유저한테 잘 저장되었다고, 알려주고
                Toast.makeText(EditActivity.this, "수정되었습니다", Toast.LENGTH_SHORT).show();

                // 액티비티 종료, 메인 액티비티로 돌아간다.(메인액티비티는 뒤에 숨어있었으므로)
                finish();
            }
        });
    }
}
