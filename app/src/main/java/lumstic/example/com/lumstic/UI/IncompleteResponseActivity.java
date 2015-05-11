package lumstic.example.com.lumstic.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import lumstic.example.com.lumstic.Adapters.IncompleteResponsesAdapter;
import lumstic.example.com.lumstic.Models.IncompleteResponses;
import lumstic.example.com.lumstic.R;

public class IncompleteResponseActivity extends Activity {

    ListView listView;
    List<IncompleteResponses> incompleteResponseses;
    IncompleteResponsesAdapter incompleteResponsesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete_response);
        getActionBar().setTitle("Completed Responses");
        listView = (ListView) findViewById(R.id.listview);
        incompleteResponseses = new ArrayList<IncompleteResponses>();
        incompleteResponseses.add(0, new IncompleteResponses("1", "Name of the city: Pune"));
        incompleteResponseses.add(1, new IncompleteResponses("2", "Name of the city: Pune"));
        incompleteResponseses.add(2, new IncompleteResponses("3", "Name of the city: Pune"));
        incompleteResponseses.add(3, new IncompleteResponses("4", "Name of the city: Pune"));
        incompleteResponseses.add(4, new IncompleteResponses("5", "Name of the city: Pune"));
        incompleteResponseses.add(5, new IncompleteResponses("6", "Name of the city: Pune"));
        incompleteResponsesAdapter= new IncompleteResponsesAdapter(getApplicationContext(), incompleteResponseses);
        listView.setAdapter(incompleteResponsesAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.incomplete_response, menu);
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
