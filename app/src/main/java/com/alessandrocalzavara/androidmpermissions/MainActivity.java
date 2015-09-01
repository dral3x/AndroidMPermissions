package com.alessandrocalzavara.androidmpermissions;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    private EditText _results;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._results = (EditText) findViewById(R.id.main_result);

        this.findViewById(R.id.main_button_normal).setOnClickListener(new FetchPermissions(PermissionInfo.PROTECTION_NORMAL));
        this.findViewById(R.id.main_button_dangerous).setOnClickListener(new FetchPermissions(PermissionInfo.PROTECTION_DANGEROUS));
        this.findViewById(R.id.main_button_signature).setOnClickListener(new FetchPermissions(PermissionInfo.PROTECTION_SIGNATURE));
        this.findViewById(R.id.main_button_all).setOnClickListener(new FetchPermissions(-1));

        this.findViewById(R.id.main_button_share).setOnClickListener(new Share());
    }

    private class FetchPermissions implements View.OnClickListener
    {
        private final int _level;

        public FetchPermissions(int level)
        {
            this._level = level;
        }

        @Override
        public void onClick(View v)
        {
            StringBuffer stringBuffer = new StringBuffer();

            try {
                if (this._level < 0) {
                    stringBuffer.append("| Permission | Group | Level |\n");
                    stringBuffer.append("| ---------- | ----- | ----- |\n");
                } else {
                    stringBuffer.append("| Permission | Group |\n");
                    stringBuffer.append("| ---------- | ----- |\n");
                }

                PackageInfo packageInfo = getPackageManager().getPackageInfo("android", PackageManager.GET_PERMISSIONS);
                for (PermissionInfo permission : packageInfo.permissions) {
                    if (this._level < 0) {
                        stringBuffer.append("| " + permission.name + " | " + permission.group + " | " + permission.protectionLevel + "|\n");
                    } else if (permission.protectionLevel == this._level) {
                        stringBuffer.append("| " + permission.name + " | " + permission.group + " |\n");
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                stringBuffer.append("ERROR: " + e.getMessage());
            }

            _results.setText(stringBuffer.toString());
        }
    }

    private class Share implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, _results.getText());

            startActivity(Intent.createChooser(intent, "Android M Permissions"));
        }
    }
}
