package com.example.sharemap2.Upload;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sharemap2.R;
import com.example.sharemap2.sqlite.LocationOpenHelper;


public class EditWindowFragment extends Fragment {

    private static final String ARGS_ID = "id";
    private static final String ARGS_FIXED = "fixed";
    private static final String ARGS_COMMENT = "comment";
    private LocationOpenHelper helper;
    private EditText editText;

    public static EditWindowFragment createInstance(long id, String title, String comment) {
        //memoするためのフラグメントを作成
        EditWindowFragment fragment = new EditWindowFragment();
        //fragmentに格納するのはタップしたitemのidとtitleとcomment
        Bundle args = new Bundle();
        //左側の大文字はタグまたはキー
        //検索するときに利用されたりする、primary Key的なもの
        args.putLong(ARGS_ID, id);
        args.putString(ARGS_FIXED, title);
        args.putString(ARGS_COMMENT, comment);
        fragment.setArguments(args);
        return fragment;
    }

    public interface Regist {
        void onRegist(long id, String text);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DB作成
        helper = new LocationOpenHelper(getContext().getApplicationContext());
    }

    private Regist regist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.memo_add, container, false);

        TextView memoTitle=v.findViewById(R.id.memotitle);
        editText = v.findViewById(R.id.memoedit);
        //createInstanceで格納していた文字をget
        Bundle args = getArguments();
        if(args != null) {
            //containsKeyはキー検索
            //ヒットしたらString型の文字列をゲット、ないなら空白
            memoTitle.setText(args.containsKey(ARGS_FIXED) ? args.getString(ARGS_FIXED) : "");
            editText.setText(args.containsKey(ARGS_COMMENT) ? args.getString(ARGS_COMMENT) : "");
        }

        Button button = v.findViewById(R.id.memoup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //argsはインスタンス作成時のitemの情報
                Bundle args = getArguments();
                if(args != null && args.containsKey(ARGS_ID)) {
                    //fragment生成時のargsのidでitemがわかるのでmemoをitem.commentに書き込む
                    //RouteListFragmentを生成
                    regist.onRegist(args.getLong(ARGS_ID), editText.getText().toString());
                   // saveMemotoDB(editText.getText().toString());
                }
            }
        });
        return v;
    }
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if(context instanceof Regist) {
            regist = (Regist)context;
        }
    }

   /* public void readData(View view){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                "testdb",
                new String[] { "title", "comment" },
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append(cursor.getString(0));
            sbuilder.append(":    ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("点\n\n");
            cursor.moveToNext();
        }

        cursor.close();

    }*/

   /* public void  saveMemotoDB(String memo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Memo", memo);

        db.execSQL("");
        Log.d("database", "writed memo to DB");
    }*/

}
