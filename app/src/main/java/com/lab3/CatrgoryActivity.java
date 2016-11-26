package com.lab3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;

import java.util.ArrayList;
import java.util.Arrays;

public class CatrgoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final  String LOG_TAG = "CategoryActivity";
    private ListView listView;
    private ArrayAdapter<Category> adapter;
    private ArrayList<Category> lst;
    private SQLiteDatabase database;
    private DbUtils utils;
    private Category selectedCategory;
    private Category oldCategory;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrgory);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись
        listView = (ListView) findViewById(R.id.category_list);
        lst = new ArrayList<Category>();
        parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        adapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_list_item_1, lst);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    //закинуть id
    public void parseCursor(Cursor cursor) {
        String name;
        int id = 2;
        Category category;
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            int categoryIdx = cursor.getColumnIndex(DbUtils.CATEGORY_NAME);
            do {
                name = cursor.getString(categoryIdx);
                category = new Category(id,name);
                lst.add(category);
                i++;
            }
            while (cursor.moveToNext());
        }
        i = 0;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int position = i;
        selectedCategory = adapter.getItem(position);
    }

    public void onAddCategory(View view){
        title = getResources().getString(R.string.add_category_title);
       onCreateDialolg("",1,title);
    }

    public void onEditCategory(View  view){
        title = getResources().getString(R.string.edit_category_title);
        onCreateDialolg(selectedCategory.getCategoryName(),2,title);
    }

    public void onDeleteCategory(View view){
        int res =  database.delete (utils.CATEGORY_TABLE, utils.CATEGORY_NAME+"=?", new String[] {selectedCategory.getCategoryName()});
        adapter.remove(selectedCategory);
        adapter.notifyDataSetChanged();
    }

    public void onCreateDialolg(String text, final int action,String title){
        LayoutInflater layoutInflater = LayoutInflater.from(CatrgoryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_add_category, null);
        final EditText editText = (EditText) promptView.findViewById(R.id.add_cat);
        editText.setText(text);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CatrgoryActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(R.string.add_btn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (action==1){
                                    String category = editText.getText().toString();
                                    adapter.add(new Category(category));
                                    adapter.notifyDataSetChanged();
                                    utils.insertCatigories(database,category);
                                    dialog.cancel();
                                }
                                else {
                                    String newCategory ="";
                                    oldCategory = selectedCategory;
                                    adapter.remove(oldCategory);
                                }

                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
