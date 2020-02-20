package com.example.sharemap2;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sharemap2.Upload.EditWindowFragment;
import com.example.sharemap2.Upload.RouteListFragment;
import com.example.sharemap2.Upload.UploadRouteFragment;
import com.example.sharemap2.sqlite.LocationOpenHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity implements EditWindowFragment.Regist, UploadRouteFragment.AdapterInActivity{

    private PersonFragment mfragment_person;
    private SearchRouteFragment mfragment_search_root;
    private UploadRouteFragment mfragment_upload_root;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            switch (Item.getItemId()) {
                case R.id.navigation_upload:
                    mfragment_upload_root = new UploadRouteFragment();
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.frame, mfragment_upload_root);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    return true;
                case R.id.navigation_search:
                    mfragment_search_root = new SearchRouteFragment();
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.frame, mfragment_search_root);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    return true;
                case R.id.navigation_person:
                    mfragment_person = new PersonFragment();
                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.frame, mfragment_person);
                    transaction3.addToBackStack(null);
                    transaction3.commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        mfragment_upload_root = new UploadRouteFragment();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.frame, mfragment_upload_root);
        transaction1.commit();

        //ナビゲーションバー
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    @Override
    public void AdapterSet(List<String> itemtitlelist) {
        //adapter = new Adapter(this, itemtitlelist);
        showListFragment();
    }

    //fragment_upload_routeのlist_frameにRouteListフラグメントでリストを生成する
    void showListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.list_frame, RouteListFragment.createInstance());
        transaction.commit();
    }
    //LayoutMainにあるframeに編集画面のEditWindowFragmentを生成する
    void showItemFragment(long id, String title, String comment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, EditWindowFragment.createInstance(id, title, comment));
        transaction.addToBackStack(null);
        transaction.commit();
    }

       private  Adapter adapter;
    @Override
    public void onRegist(long id, String text) {
        adapter.setComment(id, text);
        showListFragment();
    }
}

//activityの処理は書いてはいけない-----------------------------------------------------

class Item {
    long id;
    String title, comment;
}

//-----------------------------------------------------------------------------------------------

class Adapter extends BaseAdapter {
    private class ViewHolder {
        TextView text1;
        TextView text2;
    }
    private LayoutInflater inflater;

    private List<Item> itemlist = new ArrayList<>();

    //各itemのタイトルはリストになっていてtitleDataとして引数にはいる、上記ではitemtitlelistと定義している
    //コンストラクタでは引数をもとにアイテムの生成をしている
    //itemの枠組みは上記のItem classでおこなっている
    Adapter(Context context, List<String> titleData) {
        inflater = LayoutInflater.from(context);
        long id = 0;
        for(String data : titleData) {
            Item item = new Item();
            //各itemにはidがある
            item.id = id++;
            item.title = data;
            item.comment = "";
            //結局やりたいのはitemlistの生成
            itemlist.add(item);
        }
    }

    //コンストラクタでitemlistは生成しているのでidを指定すれば得たいitemを獲得できる
    void setComment(long id, String text) {
        Item item = getItem(id);
        if(item != null) {
            item.comment = text;
            //アイテムを変更した場合、notifyDataSetChanged()を呼ばないとその変更はListViewに反映されません。
            // データを追加した後はリストのadapterに対してnotifyDataSetChanged()を呼びましょう。
            notifyDataSetChanged();
        }
    }

    //得られたidからitemlistの要素と一つずつ確かめていく
    Item getItem(long id) {
        for(Item item : itemlist)
            if(item.id == id) return item;
        return null;
    }
    @Override
    public int getCount() {
        return itemlist.size();
    }
    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }
    @Override
    public long getItemId(int position) {
        return itemlist.get(position).id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertViewはlist_items.xmlのこと
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_items, null);
            holder = new ViewHolder();
            holder.text1 = convertView.findViewById(R.id.item_title);
            holder.text2 = convertView.findViewById(R.id.item_comment);
            //textviewの入ったholderをタグとしてセット
            convertView.setTag(holder);
        } else {
            //既存のテキストビューホルダーをゲット
            holder = (ViewHolder)convertView.getTag();
        }
        //テキスト書き込み
        holder.text1.setText(itemlist.get(position).title);
        holder.text2.setText(itemlist.get(position).comment);
        return convertView;
    }
}

