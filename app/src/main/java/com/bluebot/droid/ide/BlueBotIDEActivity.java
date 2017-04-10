package com.bluebot.droid.ide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluebot.R;
import com.bluebot.droid.ide.runtime.CodeExecutor;


public class BlueBotIDEActivity extends AppCompatActivity {

    private CodeExecutor codeExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_bot_ide);
        ((Button) findViewById(R.id.runButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Editable code = ((EditText) findViewById(R.id.codeEntry)).getText();
                codeExecutor.executeCode(code.toString());
            }
        });
    }

    public void setCodeExecutor(CodeExecutor codeExecutor) {
        this.codeExecutor = codeExecutor;
    }
}
