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

    //пишешь в бд, по имени тянешь ид и потом обновляешь ад
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
                                    utils.insertCatigories(database,new Category(category));
                                    dialog.cancel();
                                }
                                else {
                                    String newCategory = editText.getText().toString();
                                    adapter.remove(selectedCategory);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DbUtils.CATEGORY_NAME,newCategory);
                                    int pldId = utils.getIdByName(selectedCategory.getCategoryName(),database);
                                    Category newObject = new Category(pldId,newCategory);
                                    adapter.add(newObject);
                                   int res =  utils.update(database,DbUtils.CATEGORY_TABLE,contentValues,DbUtils.CATEGORY_ID,new String[]{String.valueOf(pldId)});
                                    dialog.cancel();
                                }

                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
