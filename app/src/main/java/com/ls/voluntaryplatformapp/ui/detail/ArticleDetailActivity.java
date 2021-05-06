package com.ls.voluntaryplatformapp.ui.detail;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.ls.voluntaryplatformapp.R;
import com.ls.voluntaryplatformapp.model.Article;

public class ArticleDetailActivity extends ComponentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
    }

    public static void startFeedDetailActivity(Context context, Article article){

    }

}
