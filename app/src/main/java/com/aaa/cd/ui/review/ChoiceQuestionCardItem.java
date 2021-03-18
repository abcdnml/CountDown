package com.aaa.cd.ui.review;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.aaa.cd.R;

import java.util.List;

/**
 * Description
 * Created by aaa on 2018/1/1.
 */

public class ChoiceQuestionCardItem extends BaseCardItem
{
    private String question;
    private List<String> options;
    private List<Integer> li_answer;
    private String explaination;
    private OptionAdapter oa;
    private boolean showAnswer;

    public ChoiceQuestionCardItem(Context context, String question, List<String> options, List<Integer> answer, String explaination)
    {
        super(context);
        this.question = question;
        this.li_answer = answer;
        this.options = options;
        this.explaination = explaination;
        this.showAnswer=false;
        Log.e("choice question", "option : " + options.size());
        Log.e("choice question", "answer : " + answer.size());
    }

    @Override
    public View getView(View convertView, ViewGroup parent)
    {
        convertView = View.inflate(mContext, R.layout.item_exam_choice_question, null);
        TextView tv_question = (TextView) convertView.findViewById(R.id.tv_exam_choice_question);
        final RecyclerView rv_option = (RecyclerView) convertView.findViewById(R.id.rv_exam_choice_option);
        rv_option.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        final TextView tv_explaination = (TextView) convertView.findViewById(R.id.tv_exam_choice_explaination);
        Button btn_showAnswer=(Button)convertView.findViewById(R.id.btn_exam_choice_showanswer);

        oa = new OptionAdapter();
        tv_question.setText(question);
        rv_option.setAdapter(oa);

        tv_explaination.setText(explaination);
        if(showAnswer){
            tv_explaination.setVisibility(View.VISIBLE);
        }else{
            tv_explaination.setVisibility(View.GONE);
        }

        btn_showAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAnswer=!showAnswer;
                oa.notifyDataSetChanged();
                if(showAnswer){
                    tv_explaination.setVisibility(View.VISIBLE);
                }else{
                    tv_explaination.setVisibility(View.GONE);
                }
                Toast.makeText(mContext,"show answer : "+ showAnswer,Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    class OptionAdapter extends RecyclerView.Adapter<OptionViewHolder>
    {
        @Override
        public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.ri_choice_question_option, parent, false);
            return new OptionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OptionViewHolder holder, int position)
        {
            holder.cb_option.setText(options.get(position));

            if (showAnswer)
            {
                for (int i : li_answer)
                {
                    if (i == position)
                    {
                        holder.cb_option.setBackgroundColor(Color.GREEN);
                        holder.cb_option.setChecked(true);
                    }
                }
            }else{
                holder.cb_option.setBackgroundColor(Color.WHITE);
                holder.cb_option.setChecked(false);
            }
        }

        @Override
        public int getItemCount()
        {
            return options.size();
        }
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox cb_option;

        public OptionViewHolder(View itemView)
        {
            super(itemView);
            cb_option = Utils.findViewById(itemView, R.id.ri_cb_choice_question_option);
        }
    }
}
