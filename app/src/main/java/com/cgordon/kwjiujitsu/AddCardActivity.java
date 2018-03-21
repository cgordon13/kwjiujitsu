package com.cgordon.kwjiujitsu;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.FileOutputStream;
import java.io.IOException;

public class AddCardActivity extends AppCompatActivity implements TextWatcher {

    private final static String TAG = AddCardActivity.class.getSimpleName();

    private Button m_addButton;
    private Button m_cancelButton;
    private TextView m_nameText;
    private TextView m_numberText;
    private ProgressBar m_progressBar;
    private TextView m_progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        m_nameText = findViewById(R.id.member_name);
        m_nameText.addTextChangedListener(this);

        m_numberText = findViewById(R.id.member_number);
        m_numberText.addTextChangedListener(this);

        m_addButton = findViewById(R.id.add_button);
        m_addButton.setEnabled(false);
        m_cancelButton = findViewById(R.id.cancel_button);

        m_progressBar = findViewById(R.id.progressBar);
        m_progressBar.setVisibility(View.INVISIBLE );

        m_progressText = findViewById(R.id.progressText);
        m_progressText.setVisibility(View.INVISIBLE);

    }

    public void cancel(View view) {
        finish();
    }

    public void addMember(View view) {
        Log.d(TAG, m_nameText.getText() + " " + m_numberText.getText());

        m_nameText.setEnabled(false);
        m_numberText.setEnabled(false);
        m_cancelButton.setEnabled(false);
        m_addButton.setEnabled(false);
        m_progressBar.setVisibility(View.VISIBLE);
        m_progressText.setVisibility(View.VISIBLE);

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {
                String myStringToEncode = m_numberText.getText().toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                //https://github.com/pethoalpar/QRGenerator
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = multiFormatWriter.encode(myStringToEncode, BarcodeFormat.QR_CODE, 1024, 1024);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
//                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                int bitMatrixWidth = bitMatrix.getWidth();
                int bitMatrixHeight = bitMatrix.getHeight();
                int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

                int colorWhite = 0xFFFFFFFF;
                int colorBlack = 0xFF000000;

                for (int y = 0; y < bitMatrixHeight; y++) {
                    int offset = y * bitMatrixWidth;
                    for (int x = 0; x < bitMatrixWidth; x++) {
                        pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

                bitmap.setPixels(pixels, 0, 1024, 0, 0, bitMatrixWidth, bitMatrixHeight);

                FileOutputStream out = null;
                try {
                    String filename = getFilesDir() +"/" +   m_nameText.getText().toString();
                    out = new FileOutputStream(filename);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }.execute(m_nameText.getText().toString(), m_numberText.getText().toString());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (m_nameText.getText().length() > 0 && m_numberText.getText().length() > 0) {
            m_addButton.setEnabled(true);
        } else {
            m_addButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
