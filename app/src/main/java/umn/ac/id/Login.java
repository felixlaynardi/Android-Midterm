package umn.ac.id;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private Button login;
    private String username_content, password_content;
    private TextView check;
    private EditText username, password;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //add back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        check = findViewById(R.id.check);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_content = username.getText().toString();
                password_content = password.getText().toString();
                //Only accepts on username = uasmobile and password = uasmobilegenap
                if(username_content.equals("uasmobile") && password_content.equals("uasmobilegenap")){
                    check.setText("");
                    Intent intent = new Intent(Login.this, List.class);
                    startActivity(intent);
                }
                else{
                    check.setText("Incorrect Username or Password");
                }
            }
        });
    }

    //Back navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}
