package fi.softala.passi;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Context context = getApplicationContext();
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        Account[] tilit = accountManager.getAccountsByType("fi.softala.passi");

        for (Account account : tilit) {
            Log.e("Passi", account.name);
        }
        if (tilit.length > 0){
            Intent intent = new Intent(MainActivity.this, ValikkoActivity.class);
            startActivity(intent);
        }
        Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KirjautumisActivity.class);
                startActivity(intent);
            }
        });

    }



}
