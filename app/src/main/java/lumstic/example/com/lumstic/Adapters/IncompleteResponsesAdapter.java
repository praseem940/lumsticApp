package lumstic.example.com.lumstic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lumstic.example.com.lumstic.Models.CompleteResponses;
import lumstic.example.com.lumstic.Models.IncompleteResponses;
import lumstic.example.com.lumstic.Models.Survey;
import lumstic.example.com.lumstic.R;

/**
 * Created by work on 14/4/15.
 */
public class IncompleteResponsesAdapter extends BaseAdapter {

    Context context;
    ViewHolder viewHolder;
    List<IncompleteResponses> incompleteResponseses;

    public IncompleteResponsesAdapter(Context context, List<IncompleteResponses> incompleteResponseses) {
        this.context = context;
        this.incompleteResponseses= incompleteResponseses;
    }

    @Override
    public int getCount() {
        return incompleteResponseses.size();
    }

    @Override
    public Object getItem(int i) {
        return incompleteResponseses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_complete_responses, null);
        viewHolder = new ViewHolder();
        viewHolder.responseNumber = (TextView) view.findViewById(R.id.response_number_text);
        viewHolder.responseText=(TextView)view.findViewById(R.id.response_description_text);

        view.setTag(viewHolder);
        final IncompleteResponses incompleteResponses= (IncompleteResponses) getItem(i);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.responseNumber.setText("Response: "+incompleteResponses.getResponseNumber());
        viewHolder.responseText.setText(incompleteResponses.getResponseText());
        return view;
    }
    private static class ViewHolder {
        TextView responseNumber,responseText;
    }
}
