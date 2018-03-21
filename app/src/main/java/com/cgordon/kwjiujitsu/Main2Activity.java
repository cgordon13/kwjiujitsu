package com.cgordon.kwjiujitsu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    public static final String TAG = Main2Activity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSectionsPagerAdapter.refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_addCard:

                Intent myIntent = new Intent(this, AddCardActivity.class);
                this.startActivityForResult(myIntent, 1);

                Log.d(TAG, "Done waiting");


//                AlertDialog.Builder data = new AlertDialog.Builder(this);
//                data.setTitle("Add user");
//
////                data.setMessage("Name:");
////                final EditText name = new EditText(this);
////                data.setView(name);
////
////                data.setMessage("UID:");
////                final EditText number = new EditText(this);
////                number.setInputType(InputType.TYPE_CLASS_NUMBER);
////                data.setView(number);
//
//
//                LayoutInflater factory = LayoutInflater.from(this);
//                final View addCardView= factory.inflate(R.layout.add_card, null);
//
//                final EditText name = addCardView.findViewById(R.id.name);
//                final EditText number = addCardView.findViewById(R.id.number);
//
//                data.setView(addCardView);
//
//                data.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, name.getText() + " " + number.getText());
//
//                        String myStringToEncode = number.getText().toString();
//                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//
//                        //https://github.com/pethoalpar/QRGenerator
//                        BitMatrix bitMatrix = null;
//                        try {
//                            bitMatrix = multiFormatWriter.encode(myStringToEncode, BarcodeFormat.QR_CODE, 1024, 1024);
//                        } catch (WriterException e) {
//                            e.printStackTrace();
//                        }
//                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//
//                        FileOutputStream out = null;
//                        try {
//                            String filename = getFilesDir() +"/" +   name.getText().toString();
//                            out = new FileOutputStream(filename);
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//                            // PNG is a lossless format, the compression factor (100) is ignored
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                if (out != null) {
//                                    out.close();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        mSectionsPagerAdapter.refresh();
//
//                    }
//                });
//
//                data.setNegativeButton("Cancel", null);
//
//                data.show();
//

                return true;
            case R.id.action_deleteCard:

                new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Delete card?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mSectionsPagerAdapter.delete(mViewPager.getCurrentItem());
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    void printFileNames() {
        File filesDir = getFilesDir();
        String path = filesDir.getPath();

        File[] subFiles = filesDir.listFiles();

        Log.e("FILES", String.valueOf(subFiles.length));

        if (subFiles != null) {
            for (File file : subFiles) {
                Log.e("FILE:", path + "/" + file.getAbsolutePath());
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_FILE_PATH = "file_path";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String filename) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_FILE_PATH, filename);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            ImageView qrCode = rootView.findViewById(R.id.qr_image);

            String filePath = getArguments().getString(ARG_FILE_PATH);
//            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//
//            //https://github.com/pethoalpar/QRGenerator
//            BitMatrix bitMatrix = null;
//            try {
//                bitMatrix = multiFormatWriter.encode(myStringToEncode, BarcodeFormat.QR_CODE, 1024, 1024);
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            qrCode.setImageBitmap(bitmap);

            File file = new File(filePath);
            TextView name = rootView.findViewById(R.id.qr_name);
            name.setText(file.getName());

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private File[] subFiles;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            updateFiles();
        }

        @Override
        public Fragment getItem(int position) {
            if (subFiles.length == 0) {
                return NoCardsFragment.newInstance();
            } else {
                return PlaceholderFragment.newInstance(subFiles[position].getAbsolutePath());
            }
        }

        public void refresh() {
            updateFiles();
            notifyDataSetChanged();
        }

        public void delete(int index) {
            subFiles[index].delete();
            refresh();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        private void updateFiles() {
            File filesDir = getFilesDir();
            subFiles = filesDir.listFiles();
        }

        @Override
        public int getCount() {
            if (subFiles.length == 0) {
                return 1;
            } else {
                return subFiles.length;
            }
        }
    }
}
