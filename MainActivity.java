package com.example.applocker;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText passwordInput;
    private Spinner appSelector;
    private Button setPasswordBtn, requestPermissionBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordInput = findViewById(R.id.password);
        appSelector = findViewById(R.id.appSelector);
        setPasswordBtn = findViewById(R.id.setPassword);
        requestPermissionBtn = findViewById(R.id.requestPermission);
        
        requestPermissionBtn.setOnClickListener(v -> requestUsageAccess());
        setPasswordBtn.setOnClickListener(v -> lockApp());
    }

    private void requestUsageAccess() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if (appOps != null && appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName()) != AppOpsManager.MODE_ALLOWED) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            Toast.makeText(this, "Grant Usage Access Permission", Toast.LENGTH_LONG).show();
        }
    }

    private void lockApp() {
        String selectedApp = appSelector.getSelectedItem().toString();
        String password = passwordInput.getText().toString();
        
        if (selectedApp.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Select an app and set a password", Toast.LENGTH_SHORT).show();
            return;
        }
        
        getSharedPreferences("AppLockerPrefs", MODE_PRIVATE).edit()
                .putString(selectedApp, password)
                .apply();
        
        Toast.makeText(this, "App locked successfully!", Toast.LENGTH_SHORT).show();
    }
}

