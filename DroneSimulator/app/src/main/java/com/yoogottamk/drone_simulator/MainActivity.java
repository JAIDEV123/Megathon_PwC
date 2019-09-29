package com.yoogottamk.drone_simulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private int imagesTaken = 0;
    private Button cameraButton, submitButton;
    private ImageView[] imgs = new ImageView[3];
    private TextView log;

    private String[] imgStrs = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraButton = findViewById(R.id.camera);
        submitButton = findViewById(R.id.submit);

        imgs[0] = findViewById(R.id.img1);
        imgs[1] = findViewById(R.id.img2);
        imgs[2] = findViewById(R.id.img3);

        log = findViewById(R.id.main);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setText("");
                for(int j = 0; j < 3; j++) {
                    RequestParams params = new RequestParams();
                    params.put("img", imgStrs[j]);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://fa880883.ngrok.io", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                            Toast.makeText(getApplicationContext(),
                                    "Some error occurred while communicating with the server",
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(int i, Header[] headers, String s) {
                            String txt = log.getText().toString();
                            char[] charray = txt.toCharArray();

                            int l = 1;
                            for(char c : charray)
                                if(c == '\n')
                                    l++;

                            txt += ("Road " + l + ": " + s + "\n");

                            log.setText(txt);
                        }
                    });
               }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imagesTaken++;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgs[(imagesTaken - 1) % 3].setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            imgStrs[(imagesTaken - 1) % 3] = Base64.encodeToString(byte_arr, Base64.DEFAULT);

            if (imagesTaken > 2) {
                submitButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
