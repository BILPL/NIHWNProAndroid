package com.phleby;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.windowsazure.messaging.notificationhubs.NotificationHub;

public class DashboardActivity extends AppCompatActivity {


    public static DashboardActivity dashboardActivity;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent1 = this.getIntent();
        if (null != intent1 && null != intent1.getData() && null != intent1.getData().getHost()) {
            String urlscheme = intent1.getData().getHost();
            String scheme = intent1.getData().toString();
            switch (urlscheme){
                case "loginsuccess":
                    String uId = scheme.split("id=")[1].split("&CN=")[0];
                    Utility.SetUserPreferences(Constants.UseruniqueId, uId, this);
                    Utility.RegisterOrUpdateDeviceToken(uId, this);
                    NotificationHub.start(this.getApplication(),NotificationSettings.HubName, NotificationSettings.HubListenConnectionString);

                    NotificationHub.setInstallationSavedListener(i -> {
                        Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
                        String regID = NotificationHub.getInstallationId();
                        Utility.SetUserPreferences(Constants.registrationID, regID, this);
                    });
                    NotificationHub.setInstallationSaveFailureListener(e -> Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show());
                    NotificationHub.addTag(uId);
                    break;
            }
        }
        this.finish();
    }

}