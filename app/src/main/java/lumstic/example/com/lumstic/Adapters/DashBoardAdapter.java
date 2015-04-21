package lumstic.example.com.lumstic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import lumstic.example.com.lumstic.Models.Survey;
import lumstic.example.com.lumstic.Models.Surveys;
import lumstic.example.com.lumstic.R;
import lumstic.example.com.lumstic.SurveyDetailsActivity;
import lumstic.example.com.lumstic.Utils.IntentConstants;

public class DashBoardAdapter extends BaseAdapter {

    Context context;
    ViewHolder viewHolder;
    List<Surveys> surveyList;

    public DashBoardAdapter(Context context, List<Surveys> surveyList) {
        this.context = context;
        this.surveyList = surveyList;
    }


    @Override
    public int getCount() {
        return surveyList.size();
    }

    @Override
    public Object getItem(int i) {
        return surveyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_active_survey, null);
        viewHolder = new ViewHolder();
        viewHolder.surveyName = (TextView) view.findViewById(R.id.survey_name_text);
        viewHolder.completedSurvey= (TextView) view.findViewById(R.id.complete_survey_text);
        viewHolder.incompleteSurvey = (TextView) view.findViewById(R.id.incomplete_survey_text);
        viewHolder.uploadedSurvey = (TextView) view.findViewById(R.id.uploaded_survey_text);
        viewHolder.endDate = (TextView) view.findViewById(R.id.end_date_text);
        viewHolder.linearLayout=(LinearLayout)view.findViewById(R.id.container);
        view.setTag(viewHolder);
        final Surveys survey= (Surveys) getItem(i);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.surveyName.setText(survey.getName());
        //viewHolder.completedSurvey.setText("Complete  "+survey.getCompletedSurvey());
        //viewHolder.incompleteSurvey.setText("Incomplete  "+survey.getIncompleteSurvey());
        //viewHolder.uploadedSurvey.setText("Uploaded  "+survey.getUploadedSurvey());
        viewHolder.endDate.setText(survey.getExpiryDate());
//        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, SurveyDetailsActivity.class);
//
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.putExtra(IntentConstants.SURVEY,survey);
//                context.startActivity(i);
//            }
//        });

        return view;
    }


    private static class ViewHolder {
        TextView surveyName,completedSurvey,incompleteSurvey,uploadedSurvey,endDate;
        LinearLayout linearLayout;
    }
}
