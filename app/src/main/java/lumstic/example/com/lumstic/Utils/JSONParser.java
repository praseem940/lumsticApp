package lumstic.example.com.lumstic.Utils;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lumstic.example.com.lumstic.Models.Categories;
import lumstic.example.com.lumstic.Models.Options;
import lumstic.example.com.lumstic.Models.Questions;
import lumstic.example.com.lumstic.Models.Survey;
import lumstic.example.com.lumstic.Models.Surveys;

/**
 * Created by work on 17/4/15.
 */
public class JSONParser {

    List<Questions> questionses;
    List<Surveys> surveyses;
    List<Options> optionses;
    List<Categories> categorieses;

    public Categories parseCategories(JSONObject jsonObjectCategories) {
        Categories categories = new Categories();
     try{   try {
            categories.setId(jsonObjectCategories.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            categories.setSurveyId(jsonObjectCategories.getInt("survey_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            categories.setOrderNumber(jsonObjectCategories.getInt("order_number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            categories.setCategoryId(jsonObjectCategories.getInt("category_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            categories.setContent(jsonObjectCategories.getString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            categories.setType(jsonObjectCategories.getString("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            categories.setParentId(jsonObjectCategories.getInt("parent_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }catch (Exception  e){
         e.printStackTrace();
     }
        return categories;
    }

    public Options parseOptions(JSONObject jsonObjectOptions){
        Options options= new Options();
        try {
            options.setId(jsonObjectOptions.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            options.setOrderNumber(jsonObjectOptions.getInt("order_number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            options.setContent(jsonObjectOptions.getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            options.setQuestionId(jsonObjectOptions.getInt("question_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return options;

    }

    public Questions parseQuestions(JSONObject jsonObjectQuestions) {
        Questions questions = new Questions();
        optionses= new ArrayList<Options>();

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
            } catch (Exception e) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
           if(jsonObjectQuestions.has("options"))
             Log.e("ithas","ithas");
        try {
            JSONArray jsonArrayOptions= jsonObjectQuestions.getJSONArray("options");
            for(int k=0;k< jsonArrayOptions.length();k++){
                JSONObject jsonObjectOptions = jsonArrayOptions.getJSONObject(k);
                Options options= parseOptions(jsonObjectOptions);
                optionses.add(k,options);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questions.setOptions(optionses);


        return questions;
    }


    public List<Surveys> parseSurvey(JSONArray jsonArrayMain) {


        surveyses= new ArrayList<Surveys>();
        for (int i = 0; i < jsonArrayMain.length(); i++) {
            Surveys surveys;
            surveys= new Surveys();
            try {
                JSONObject jsonObject = jsonArrayMain.getJSONObject(i);
                questionses = new ArrayList<Questions>();
                categorieses = new ArrayList<Categories>();
                surveys.setId(Integer.parseInt(jsonObject.getString("id")));
                surveys.setDescription(jsonObject.getString("description"));
                surveys.setExpiryDate(jsonObject.getString("expiry_date"));
                surveys.setName(jsonObject.getString("name"));
                surveys.setPublishedOn(jsonObject.getString("published_on"));

                JSONArray jsonArray = jsonObject.getJSONArray("questions");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObjectQuestion = jsonArray.getJSONObject(j);
                    Questions questions = parseQuestions(jsonObjectQuestion);
                    questionses.add(j, questions);
                }
                surveys.setQuestions(questionses);



                JSONArray jsonArrayCategories = jsonObject.getJSONArray("categories");
                for(int l=0;l<jsonArrayCategories.length();l++){
                    JSONObject jsonObjectCategories = jsonArrayCategories.getJSONObject(l);
                    Categories categories= parseCategories(jsonObjectCategories);
                    categorieses.add(l,categories);

                }
                surveys.setCategories(categorieses);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            surveyses.add(surveys);
        }

        return  surveyses;
    }
}