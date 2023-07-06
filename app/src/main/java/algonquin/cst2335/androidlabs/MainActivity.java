package algonquin.cst2335.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity is the main activity of the password checker app.
 * @author Loveleen Loveleen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * TextView to display the result
     */
     private TextView textView=null;
/**
 * EditText to input the password.
 */
private EditText editText=null;

/**
 * Button to initiate the password check.
 */
private  Button button=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         textView=findViewById(R.id.TextView);
         editText=findViewById(R.id.EditText);
        button=findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString();
                boolean isValidPassword=checkPasswordComplexity(password);
                if(isValidPassword){
                  textView.setText( "Your password meets the requirements");
                }else{
                   textView.setText("You shall not pass!");
                }
            }
        });
    }
    /**
     * Checks the complexity of the given password.
     * @param password The password string to be checked.
     * @return True if the password meets the complexity requirements, false otherwise.
     */
    boolean checkPasswordComplexity(String password){
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        boolean hasSpecialSymbol = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if ("#$%^&*!@?".contains(String.valueOf(c))) {
                hasSpecialSymbol = true;
            }
        }
        if (!hasUpperCase) {
            showToast("Your password does not have an upper case letter");
            return false;
        } else if (!hasLowerCase) {
            showToast("Your password does not have a lower case letter");
            return false;
        } else if (!hasNumber) {
            showToast("Your password does not have a number");
            return false;
        } else if (!hasSpecialSymbol) {
            showToast("Your password does not have a special symbol");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}

