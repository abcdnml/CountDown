package com.aaa.cd.ui.review;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by aaa on 2018/1/30.
 */

public class Questions
{

    public static final String essay_question = "{\n" +
            "\"type\":\"question_essay\",\n" +
            "\"question\":\"aaaaaaaaaaaaaaa ?\",\n" +
            "\"answer\":\"bbbbbbbbbbbbbbbbbbbb\"\n" +
            "}";

    public static final String single_choice_question = "{\n" +
            "\"type\":\"question_single_choice\",\n" +
            "\"question\":\"aaaaaaaaaaaaaaa ?\",\n" +
            "\"option\":\n" +
            "[\n" +
            "\"A. aaaaaaaaaa\",\n" +
            "\"B. bbbbbbbbbb\",\n" +
            "\"C. ccccccccccc\",\n" +
            "\"D. dddddddddd\",\n" +
            "\"E. eeeeeeeeee\"\n" +
            "],\n" +
            "\"answer\":\"1\",\n" +
            "\"explaination\":\"it is A  so  the answer is B\"\n" +
            "}";
    public static final String multi_choice_question = "{\n" +
            "\"type\":\"question_multi_choice\",\n" +
            "\"question\":\"aaaaaaaaaaaaaaa ?\",\n" +
            "\"option\":\n" +
            "[\n" +
            "\"A. aaaaaaaaaa\",\n" +
            "\"B. bbbbbbbbbb\",\n" +
            "\"C. ccccccccccc\",\n" +
            "\"D. dddddddddd\",\n" +
            "\"E. eeeeeeeeee\"\n" +
            "],\n" +
            "\"answer\":\"BCD\",\n" +
            "\"explaination\":\"it is a  so  the answer is BCD\"\n" +
            "}";

    public static String getQuestionFromJsonFile()
    {
        return null;
    }

    public static BaseCardItem getEssayQuestionCard(Context context, String json)
    {
        try
        {
            JSONObject essayJson = new JSONObject(json);
            String type = essayJson.getString("type");
            if ("question_essay".equals(type))
            {
                String question = essayJson.getString("question");
                String answer = essayJson.getString("answer");
                EssayQuestionCardItem eqci = new EssayQuestionCardItem(context, question, answer);
                return eqci;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static BaseCardItem getSingleChoiceQuestionCard(Context context, String json)
    {
        try
        {
            JSONObject singleChoiceQuestionJson = new JSONObject(json);
            String type = singleChoiceQuestionJson.getString("type");
            if ("question_single_choice".equals(type))
            {
                String question = singleChoiceQuestionJson.getString("question");
                JSONArray option = singleChoiceQuestionJson.getJSONArray("option");
                List<String> ls = new ArrayList<>();
                if (option != null && option.length() > 0)
                {
                    for (int i = 0; i < option.length(); i++)
                    {
                        ls.add(option.getString(i));
                    }
                }
                String answer = singleChoiceQuestionJson.getString("answer");

                List<Integer> li = new ArrayList<>();
                String[] arrayAnswer = answer.split(",");
                if (arrayAnswer.length > 0)
                {
                    try
                    {
                        for (int i = 0; i < arrayAnswer.length; i++)
                        {
                            if (TextUtils.isDigitsOnly(arrayAnswer[i]))
                            {
                                li.add(Integer.parseInt(arrayAnswer[i]));
                            }
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                String explaination = singleChoiceQuestionJson.getString("explaination");
                ChoiceQuestionCardItem cqci = new ChoiceQuestionCardItem(context, question, ls,li , explaination);
                return cqci;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
