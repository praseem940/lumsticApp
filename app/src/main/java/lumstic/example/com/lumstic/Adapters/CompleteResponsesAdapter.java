package lumstic.example.com.lumstic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lumstic.example.com.lumstic.Models.CompleteResponses;
import lumstic.example.com.lumstic.R;


public class CompleteResponsesAdapter extends BaseAdapter {

    Context context;
    ViewHolder viewHolder;
    List<CompleteResponses> completeResponseses;

    public CompleteResponsesAdapter(Context context, List<CompleteResponses> completeResponseses) {
        this.context = context;
        this.completeResponseses = completeResponseses;
    }

    public int getCount() {
        return completeResponseses.size();
    }

    @Override
    public Object getItem(int i) {
        return completeResponseses.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_complete_responses, null);
        viewHolder = new ViewHolder();
        viewHolder.responseNumber = (TextView) view.findViewById(R.id.response_number_text);
        viewHolder.responseText = (TextView) view.findViewById(R.id.response_description_text);

        view.setTag(viewHolder);
        final CompleteResponses completeResponses = (CompleteResponses) getItem(i);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.responseNumber.setText("Response: " + completeResponses.getResponseNumber());
        viewHolder.responseText.setText(completeResponses.getResponseText());
        return view;
    }

    private static class ViewHolder {
        TextView responseNumber, responseText;
    }
}
