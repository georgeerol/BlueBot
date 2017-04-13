package com.bluebot.droid.ide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.bluebot.R;
import com.bluebot.ui.RequestProcessor;
import com.bluebot.ui.ResponseHandler;
import com.bluebot.ui.UIRequestTypes;
import com.bluebot.ui.UIResponseType;

import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class BlueBotIDEActivity extends AppCompatActivity implements ResponseHandler {

    private RequestProcessor requestProcessor;
    private View errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_bot_ide);
        findViewById(R.id.runButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Editable code = ((EditText) findViewById(R.id.codeEntry)).getText();
                requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE, code.toString());
            }
        });
        errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setVisibility(INVISIBLE);
    }

    public void setRequestProcessor(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
        requestProcessor.bind(this);
    }

    @Override
    public void responseForRequest(String requestType, final Map<String, String> response) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (response.containsKey(UIResponseType.ERROR)) {
                    errorMessage.setVisibility(VISIBLE);
                }
            }
        });
    }
}
