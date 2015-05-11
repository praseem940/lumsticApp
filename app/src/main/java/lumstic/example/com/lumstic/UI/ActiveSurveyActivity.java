package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import lumstic.example.com.lumstic.Adapters.DashBoardAdapter;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.Utils.IntentConstants;
import lumstic.example.com.lumstic.Utils.JsonHelper;


public class ActiveSurveyActivity extends Activity {
    ListView listView;
    List<Surveys> surveysList;
    JsonHelper jsonHelper;
    DashBoardAdapter dashBoardAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_survey);
        getActionBar().setTitle("DashBoard");
        jsonHelper = new JsonHelper(ActiveSurveyActivity.this);
        surveysList = new ArrayList<Surveys>();
        surveysList = jsonHelper.tryParsing(jsonHelper.getStringFromJson());
        listView = (ListView) findViewById(R.id.active_survey_list);
        dashBoardAdapter = new DashBoardAdapter(getApplicationContext(), surveysList);
        listView.setAdapter(dashBoardAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Surveys surveys = surveysList.get(i);
                Intent intent = new Intent(ActiveSurveyActivity.this, SurveyDetailsActivity.class);
                intent.putExtra(IntentConstants.SURVEY, surveys);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.active_survey, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            Intent i = new Intent(ActiveSurveyActivity.this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_help) {
            Intent i = new Intent(ActiveSurveyActivity.this, HelpActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
