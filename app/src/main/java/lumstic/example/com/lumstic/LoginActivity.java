package lumstic.example.com.lumstic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lumstic.example.com.lumstic.Adapters.DBAdapter;
import lumstic.example.com.lumstic.Models.Answers;
import lumstic.example.com.lumstic.Models.Categories;
import lumstic.example.com.lumstic.Models.Choices;
import lumstic.example.com.lumstic.Models.Options;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Records;
import lumstic.example.com.lumstic.Models.Responses;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.Utils.JsonHelper;


public class LoginActivity extends Activity {

    ActionBar actionBar;
    Button loginButton;
    TextView  fogotPassword;
    DBAdapter dbAdapter;
    Questions questions;
    JSONArray jsonArray;
    JsonHelper jsonHelper;

    int ctr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbAdapter= new DBAdapter(LoginActivity.this);

        actionBar= getActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(false);
        loginButton=(Button)findViewById(R.id.login_button);
        fogotPassword=(TextView)findViewById(R.id.forgot_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,DashBoardActivity.class);
                startActivity(i);

            }
        });
        fogotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });


        // Toast.makeText(LoginActivity.this,dbAdapter.insertDataOptionsTable(new Options(1,2,1,"zaz"))+"",Toast.LENGTH_LONG).show();


            jsonHelper= new JsonHelper(LoginActivity.this);
            jsonHelper.tryParsing(jsonHelper.getStringFromJson());



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
