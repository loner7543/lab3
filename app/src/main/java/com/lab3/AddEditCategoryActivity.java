package com.lab3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lab3.db.DbUtils;
import com.lab3.domain.Category;
public class AddEditCategoryActivity extends AppCompatActivity {
    private EditText categoryEditText;
    private DbUtils utils;
    private SQLiteDatabase database;
    private Button addCategoryBtn;
    private Intent textIntent;
    private Intent intent;
    private Toast toast;
    private Category selectedCategory;
    private int action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);
        textIntent = getIntent();
        action = textIntent.getIntExtra("Action",10);
        setTitle(textIntent.getStringExtra("title"));
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        database = utils.getWritableDatabase();//дает бд на запись

        categoryEditText = (EditText) findViewById(R.id.addNewCategory);
        addCategoryBtn = (Button) findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setText(textIntent.getStringExtra("btn_text"));
        if (action==CatrgoryActivity.EDIT_CODE){
            selectedCategory = (Category) textIntent.getSerializableExtra("edited");
            categoryEditText.setText(selectedCategory.getCategoryName());
        }
    }

    public void onAddCategory(View view){
        switch (action){
            case 1:{
                String category = categoryEditText.getText().toString();
                if (categoryValidation(category)){
                    utils.insertCatigories(database,new Category(category));
                    intent = new Intent();
                    intent.putExtra("cat",new Category(category));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            }
            case 2:{
                String newCategory = categoryEditText.getText().toString();
                if (categoryValidation(newCategory)){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DbUtils.CATEGORY_NAME,newCategory);
                    int pldId = utils.getIdByName(selectedCategory.getCategoryName(),database);
                    Category newObject = new Category(pldId,newCategory);
                    int res =  utils.update(database,DbUtils.CATEGORY_TABLE,contentValues,DbUtils.CATEGORY_ID,new String[]{String.valueOf(pldId)});

                    intent = new Intent();
                    intent.putExtra("cat",newObject);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                }

            }
            default:{
                Toast.makeText(this,"Пришел неизвестный Action",Toast.LENGTH_LONG);
            }
        }
    }

    public boolean categoryValidation(String s){
        if (s.isEmpty()){
           toast =  Toast.makeText(this,"Category is empty",Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        else return true;
    }
}
