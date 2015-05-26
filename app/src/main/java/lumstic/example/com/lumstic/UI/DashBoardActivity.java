package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.LumsticApp;


public class DashBoardActivity extends Activity {

        LumsticApp lumsticApp;
    ActionBar actionBar ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        lumsticApp= (LumsticApp)getApplication();
        actionBar = getActionBar();
        actionBar.setTitle("Dashboard");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_fetch) {
            Intent i = new Intent(DashBoardActivity.this, ActiveSurveyActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_logout) {

            lumsticApp.getPreferences().setAccessToken("");
            Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
