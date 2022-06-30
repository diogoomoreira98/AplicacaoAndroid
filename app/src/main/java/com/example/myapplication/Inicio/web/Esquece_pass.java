package com.example.myapplication.Inicio.web;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication.R;

public class Esquece_pass extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esquece_pass);

        webView = (WebView) findViewById(R.id.webview1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://friocas.dynip.sapo.pt:3000");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}