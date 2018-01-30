package com.aaa.cd.ui.review;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaa.cd.R;

/**
 * Description
 * Created by aaa on 2018/1/1.
 */

public class EssayQuestionCardItem extends BaseCardItem
{
    protected String question;
    protected String answer;
    public EssayQuestionCardItem(Context context){
        this(context,"Question","Answer");
    }
    public EssayQuestionCardItem(Context context,String question,String answer){
        super(context);
        this.question=question;
        this.answer=answer;
    }
    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        convertView = View.inflate(mContext, R.layout.item_exam_essay_question, null);
        TextView tv_question=Utils.findViewById(convertView, R.id.tv_exam_essay_question);
        TextView tv_answer=Utils.findViewById(convertView, R.id.tv_exam_essay_answer);

        tv_question.setText(question);
        tv_answer.setText(answer);
        return convertView;
    }
}
