package com.cgordon.kwjiujitsu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    private ImageView m_qrCodeBitmap;
    private TextView m_helpText;
    private FloatingActionButton m_btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_qrCodeBitmap = (ImageView) findViewById(R.id.img_result_qr);
        m_helpText = (TextView) findViewById(R.id.helpText);
        m_btnScan = (FloatingActionButton) findViewById(R.id.fab);

        qrScan = new IntentIntegrator(this);

        FileInputStream in = null;

        try {
            in = new FileInputStream(getFilesDir() + "kwjiujitsu");
            Bitmap qrCode = BitmapFactory.decodeStream(in);

            setBitmap(qrCode);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            m_qrCodeBitmap.setImageBitmap(bitmap);
            m_qrCodeBitmap.setVisibility(View.VISIBLE);
            m_helpText.setVisibility(View.GONE);
            m_btnScan.setVisibility(View.GONE);
        } else {
            m_qrCodeBitmap.setVisibility(View.GONE);
            m_helpText.setVisibility(View.VISIBLE);
            m_btnScan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_scanMembership) {
            QrScanner(findViewById(android.R.id.content));
        }

        return super.onOptionsItemSelected(item);
    }


    public void QrScanner(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        qrScan.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                String myStringToEncode = result.getContents();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                //https://github.com/pethoalpar/QRGenerator
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = multiFormatWriter.encode(myStringToEncode, BarcodeFormat.QR_CODE, 1024, 1024);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                setBitmap(bitmap);

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(getFilesDir() +  "kwjiujitsu");
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

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
