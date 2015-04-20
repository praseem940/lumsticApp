package lumstic.example.com.lumstic.Utils;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Surveys;

/**
 * Created by work on 17/4/15.
 */
public class JSONParser {


    public Questions parseQuestions(JSONObject jsonObjectQuestions) {
        Questions questions = new Questions();
        try {
            try {
                questions.setId(jsonObjectQuestions.getInt("id"));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (jsonObjectQuestions.getBoolean("identifier"))
                    questions.setIdentifier(1);
                else
                    questions.setIdentifier(0);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            try {
                questions.setParentId(jsonObjectQuestions.getInt("parent_id"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                questions.setMinValue(jsonObjectQuestions.getInt("min_value"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                questions.setMaxVlue(jsonObjectQuestions.getInt("max_value"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                questions.setMaxLength(jsonObjectQuestions.getInt("max_length"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                questions.setImageUrl(jsonObjectQuestions.getString("image_url"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            questions.setType(jsonObjectQuestions.getString("type"));
            questions.setContent(jsonObjectQuestions.getString("content"));
            try {
                questions.setSurveyId(jsonObjectQuestions.getInt("survey_id"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                questions.setOrderNumber(jsonObjectQuestions.getInt("order_number"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                questions.setCategoryId(jsonObjectQuestions.getInt("category_id"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (jsonObjectQuestions.getBoolean("mandatory"))
                    questions.setMandatory(1);
                else
                    questions.setMandatory(0);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

return questions;
    }


    public void parseSurvey(JSONObject jsonObject) {
        Surveys surveys = new Surveys();
        try {
            surveys.setId(Integer.parseInt(jsonObject.getString("id")));

            surveys.setDescription(jsonObject.getString("description"));
            surveys.setExpiryDate(jsonObject.getString("expiry_date"));
            surveys.setName(jsonObject.getString("name"));
            surveys.setPublishedOn(jsonObject.getString("published_on"));
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectQuestion = jsonArray.getJSONObject(i);
               Questions questions= parseQuestions(jsonObjectQuestion);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
