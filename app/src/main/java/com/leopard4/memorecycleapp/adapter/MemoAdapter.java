package com.leopard4.memorecycleapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.memorecycleapp.EditActivity;
import com.leopard4.memorecycleapp.R;
import com.leopard4.memorecycleapp.data.DatabaseHandler;
import com.leopard4.memorecycleapp.model.Memo;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.viewHolder>{

    Context context;
    List<Memo> memoList;

    int deleteIndex;

    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public MemoAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_row, parent, false);
        return new MemoAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.viewHolder holder, int position) {

        Memo memo = memoList.get(position);

        holder.txtTitle.setText(memo.title);
        holder.txtContent.setText(memo.content);

    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtContent;
        ImageView imgDelete;
        CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 클릭한 아이템의 인덱스를 가져온다.
                    int index = getAdapterPosition();
                    Memo memo = memoList.get(index);

                    Intent intent = new Intent(context, EditActivity.class);

                    intent.putExtra("memo", memo);

                    context.startActivity(intent);


                }
            });
            // 삭제버튼을 누르면 alertdialog를 띄워서 삭제할지 물어본다.
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 0) 어느 주소록을 삭제할것인지
                    //    삭제할 주소록을 가져온다 (어떤 행을 눌렀는지 파악한다.)
                    deleteIndex = getAdapterPosition();

                    // 1) 알러트 다이얼로그 나온다.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("삭제");
                    builder.setMessage("정말 삭제하시겠습니까?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 2) 알러트 다이얼로그에서 Yes 눌렀을때
                            //    데이터베이스에서 삭제
                            DatabaseHandler db = new DatabaseHandler(context);

                            Memo memo = memoList.get(deleteIndex);

                            db.deleteMemo(memo);

                            // 알러트 다이얼로그는 액티비티가 아니므로
                            // 메인액티비티의 onResume 함수가 실행안된다.
                            // 따라서 화면 갱신이 안된다.

                            // 즉, 디비에 저장된 데이터 삭제했으니,
                            // 메모리에 저장된 데이터도 삭제한다.
                            memoList.remove(deleteIndex);
                            // 데이터가 변경되었으니, 화면 갱신하라고 어댑터의 함수호출 (상속받은기능)
                            notifyDataSetChanged();
                        }
                    });
                    // 3) 알러트 다이얼로그에서 No 눌렀을때
                    //    아무것도 안한다.
                    builder.setNegativeButton("No", null);
                    builder.show();
                }

            });

        }
    }
}
