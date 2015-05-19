package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import lumstic.example.com.lumstic.Utils.LumsticApp;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.CommonUtil;
import lumstic.example.com.lumstic.api.ApiRequestHelper;
import lumstic.example.com.lumstic.api.ApiResponse;

public class LoginActivity extends Activity {
    ActionBar actionBar;
    Button loginButton;
    TextView fogotPassword;
    LumsticApp lumsticApp;
    private EditText emailEditText, passwordEditText;
    private ProgressDialog progressDialog;
    private String email = null, password = null;
    int ctr;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lumsticApp = (LumsticApp) getApplication();
        actionBar = getActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(false);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);
        fogotPassword = (TextView) findViewById(R.id.forgot_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Logging in");
                progressDialog.show();
                if (!TextUtils.isEmpty(email) && CommonUtil.validateEmail(email)) {

                    lumsticApp.getApiRequestHelper().loginUser(email, password, new ApiRequestHelper.onRequestComplete() {
                        @Override
                        public void onSuccess(Object object) {
                            lumsticApp.getPreferences().setAddAuthInHeader(true);
                            lumsticApp.getPreferences().setAuthToken((String) object);
                            progressDialog.dismiss();
                            Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
                            startActivity(i);
                            finish();
                        }
                        @Override
                        public void onFailure(ApiResponse apiResponse) {
                            lumsticApp.showToast(apiResponse.getError().getMessage());
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    lumsticApp.showToast("Enter Valid Email ");
                }
                Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
                startActivity(i);
                finish();
            }
        });
        fogotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

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

        if (id == R.id.action_help) {
            Intent i = new Intent(LoginActivity.this, HelpActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
