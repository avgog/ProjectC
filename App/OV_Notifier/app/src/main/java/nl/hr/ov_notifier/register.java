package nl.hr.ov_notifier;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class    register extends AppCompatActivity {
    int view = R.layout.activity_register;
    Button button;
    EditText emailId;
    EditText passwordId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        button = findViewById(R.id.registerButton);
        emailId = findViewById(R.id.setemailField);
        passwordId = findViewById(R.id.setpasswordField);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailId.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter email address",Toast.LENGTH_SHORT).show();
                }else {
                    if (!emailId.getText().toString().trim().matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
                if(passwordId.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
                }else {
                    if (!passwordId.getText().toString().trim().matches(passwordPattern)) {
                        Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
