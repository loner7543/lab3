package com.lab3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CatrgoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final  String LOG_TAG = "CategoryActivity";
    public static final int ADD_RESULT_CODE =1;
    public static final int EDIT_CODE = 2;
    private ListView listView;
    private ArrayAdapter<Category> adapter;
    private ArrayList<Category> lst;
    private SQLiteDatabase database;
    private DbUtils utils;
    private Category selectedCategory;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrgory);
        setTitle("Категории");
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись
        listView = (ListView) findViewById(R.id.category_list);
        lst = utils.parseCursor(utils.getAllRecords(database,DbUtils.CATEGORY_TABLE));
        adapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_list_item_1, lst);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCategory = adapter.getItem(i);
    }

    public void onAddCategory(View view){
        Intent intent = new Intent(this,AddEditCategoryActivity.class);
        intent.putExtra("title","Добавить категорию");
        intent.putExtra("Action",ADD_RESULT_CODE);
        intent.putExtra("btn_text","Добавить запись");
        startActivityForResult(intent,ADD_RESULT_CODE);
    }

    public void onEditCategory(View  view){
        Intent intent = new Intent(this,AddEditCategoryActivity.class);
        intent.putExtra("edited",selectedCategory);
        intent.putExtra("Action",EDIT_CODE);
        intent.putExtra("title","Изменить категорию");
        intent.putExtra("btn_text","Изменить запись");
        startActivityForResult(intent,EDIT_CODE);
    }

    public void onDeleteCategory(View view){
        int res =  database.delete (utils.CATEGORY_TABLE, utils.CATEGORY_NAME+"=?", new String[] {selectedCategory.getCategoryName()});
        adapter.remove(selectedCategory);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case ADD_RESULT_CODE:{
                    Category category = (Category) data.getSerializableExtra("cat");
                    adapter.add(category);
                    adapter.notifyDataSetChanged();
                    break;
                }
                case EDIT_CODE:{
                    adapter.remove(selectedCategory);
                    Category newCategory = (Category) data.getSerializableExtra("cat");
                    adapter.add(newCategory);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
