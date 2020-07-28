package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.adapter.LanguageRecyclerAdapter;
import com.gulasehat.android.model.Language;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Preferences;

import java.util.ArrayList;

import butterknife.BindView;

public class LanguageActivity extends BaseActivity implements LanguageRecyclerAdapter.OnLanguageClickListener {

    @BindView(R.id.languagesRW)
    protected RecyclerView languagesRW;
    private LanguageRecyclerAdapter languageRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Settings Language");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_language);

        }

        setupLanguageList();

    }

    private void setupLanguageList() {

        ArrayList<Language> languageArrayList = new ArrayList<>();

        String[] languageCodes = getResources().getStringArray(R.array.language_codes);
        String[] languageNames = getResources().getStringArray(R.array.language_names);

        for (int i=0; i<languageCodes.length; i++){

            Language lang = new Language(languageCodes[i], languageNames[i], BuildConfig.BASEURL + BuildConfig.ASSETPATH +"/lang-flags/" + languageCodes[i] + ".png?v=4", false);

            if(Preferences.getString(Preferences.CURRENT_APP_LANG_CODE, "").equals(languageCodes[i])){
                lang.setSelected(true);
                languageArrayList.add(0, lang);
                continue;
            }
            languageArrayList.add(lang);
        }

        languageRecyclerAdapter = new LanguageRecyclerAdapter(this, languageArrayList);
        languageRecyclerAdapter.setOnLanguageClickListener(this);
        languagesRW.setLayoutManager(new LinearLayoutManager(this));
        languagesRW.addItemDecoration(new DividerItemDecoration(getResources()));
        languagesRW.setAdapter(languageRecyclerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        // change language
        if(languageRecyclerAdapter != null && languageRecyclerAdapter.getActiveLanguage() != null){
            Preferences.setString(Preferences.SETTINGS_APP_LANG, languageRecyclerAdapter.getActiveLanguage().getCode());
        }


        super.onBackPressed();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_language;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onClicked(Language language) {
        Preferences.setString(Preferences.SETTINGS_APP_LANG, language.getCode());
        Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        intent.putExtra("LANG_CHANGED", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
