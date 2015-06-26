package lumstic.example.com.lumstic.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Adapters.DashBoardAdapter;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;
import lumstic.example.com.lumstic.Utils.JsonHelper;
import lumstic.example.com.lumstic.Utils.LumsticApp;


public class DashBoardActivity extends Activity {

    LumsticApp lumsticApp;
    ActionBar actionBar;
    List<Surveys> surveysList;
    JsonHelper jsonHelper;
    ListView listView;
    DashBoardAdapter dashBoardAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        lumsticApp = (LumsticApp) getApplication();
        actionBar = getActionBar();
        actionBar.setTitle("Dashboard");

        surveysList = new ArrayList<Surveys>();
        jsonHelper = new JsonHelper(DashBoardActivity.this);
        if (!lumsticApp.getPreferences().getSurveyData().equals("")) {
            setContentView(R.layout.test);
            surveysList = jsonHelper.tryParsing(lumsticApp.getPreferences().getSurveyData());
            listView = (ListView) findViewById(R.id.active_survey_list);

            if(surveysList!=null){
            dashBoardAdapter = new DashBoardAdapter(getApplicationContext(), surveysList);
            listView.setAdapter(dashBoardAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Surveys surveys = surveysList.get(i);
                    Intent intent = new Intent(DashBoardActivity.this, SurveyDetailsActivity.class);
                    intent.putExtra(IntentConstants.SURVEY, surveys);
                    startActivity(intent);
                }
            });
        }
    }}

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_fetch) {
            Intent i = new Intent(DashBoardActivity.this, ActiveSurveyActivity.class);
            startActivity(i);
            finish();
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
