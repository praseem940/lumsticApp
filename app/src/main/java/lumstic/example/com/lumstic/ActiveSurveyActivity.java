package lumstic.example.com.lumstic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Adapters.DashBoardAdapter;
import lumstic.example.com.lumstic.Models.Survey;


public class ActiveSurveyActivity extends Activity {
    ListView listView;
    List<Survey> survey;

    DashBoardAdapter dashBoardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_survey);
        getActionBar().setTitle("DashBoard");
        listView=(ListView)findViewById(R.id.active_surve_list);
        survey= new ArrayList<Survey>();
        survey.add(0,new Survey("survey name 1",00,01,04,"12-oct-2012"));
        survey.add(0,new Survey("survey name 2",01,02,02,"12-oct-2012"));
        survey.add(0,new Survey("survey name 3",00,03,05,"12-oct-2012"));
        survey.add(0,new Survey("survey name 4",01,04,01,"12-oct-2012"));
        survey.add(0,new Survey("survey name 5",02,05,02,"12-oct-2012"));
        survey.add(0,new Survey("survey name 6",01,05,03,"12-oct-2012"));
        dashBoardAdapter= new DashBoardAdapter(getApplicationContext(),survey);
        listView.setAdapter(dashBoardAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.active_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_logout)
        {
            Intent i = new Intent(ActiveSurveyActivity.this,LoginActivity.class);
            startActivity(i);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
