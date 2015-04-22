package lumstic.example.com.lumstic;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Adapters.CompleteResponsesAdapter;
import lumstic.example.com.lumstic.Adapters.DashBoardAdapter;
import lumstic.example.com.lumstic.Models.CompleteResponses;
import lumstic.example.com.lumstic.Models.Survey;


public class CompleteResponsesActivity extends Activity {
    ListView listView;
    List<CompleteResponses> completeResponseses;
    CompleteResponsesAdapter completeResponsesAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_responses);
        getActionBar().setTitle("Completed Responses");
        listView = (ListView) findViewById(R.id.listview);
        completeResponseses = new ArrayList<CompleteResponses>();
        completeResponseses.add(0, new CompleteResponses("1", "Name of the city: Pune"));
        completeResponseses.add(1, new CompleteResponses("2", "Name of the city: Pune"));
        completeResponseses.add(2, new CompleteResponses("3", "Name of the city: Pune"));
        completeResponseses.add(3, new CompleteResponses("4", "Name of the city: Pune"));
        completeResponseses.add(4, new CompleteResponses("5", "Name of the city: Pune"));
        completeResponseses.add(5, new CompleteResponses("6", "Name of the city: Pune"));
        completeResponsesAdapter = new CompleteResponsesAdapter(getApplicationContext(), completeResponseses);
        listView.setAdapter(completeResponsesAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_responses, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
