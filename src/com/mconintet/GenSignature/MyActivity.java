package com.mconintet.GenSignature;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText edt = (EditText) findViewById(R.id.editText);
        final TextView tv = (TextView) findViewById(R.id.textView);
        
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String sign = gen(edt.getText().toString());
                    tv.setText(sign);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MyActivity.this,"Cannot fine package by name",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MyActivity.this,"Exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)MyActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tv.getText());
                Toast.makeText(MyActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected String gen(String packageName) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        Signature[] signs = MyActivity.this.getPackageManager().getPackageInfo(packageName,
                PackageManager.GET_SIGNATURES).signatures;

        if(signs.length > 0){
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(signs[0].toByteArray());
            BigInteger bigInt = new BigInteger(1, md5.digest());
            return bigInt.toString(16);
        }

        return "";
    }
}
