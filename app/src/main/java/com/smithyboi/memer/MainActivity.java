package com.smithyboi.memer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;

import meme.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if(((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET))== PackageManager.PERMISSION_GRANTED)) {
            Log.d("TAG", "onCreate: i have internet");
        }
        else{requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);}

        if(((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE))== PackageManager.PERMISSION_GRANTED)) {
            Log.d("TAG", "onCreate: i have read");
        }
        else{requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);}

        if(((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE))== PackageManager.PERMISSION_GRANTED)) {
            Log.d("TAG", "onCreate: i have write");
        }
        else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Log.d("TAG", "onCreate: no write");
        }


        Log.d("TAG", "onCreate: internet permission is "+ ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET));
        GetMeme puller = new GetMeme();
        setContentView(R.layout.activity_main);
        ImageView meme = (ImageView)findViewById(R.id.meme);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET))== PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Getting you a meme", Toast.LENGTH_SHORT);
                    toast.show();
                    Meme meme = null;
                    try {
                        meme = new Meme();
                        ShortStorage.setMeme(meme);
                    } catch (ParseException e) {
                        Toast toast1 = Toast.makeText(getApplicationContext(), "parse exc", Toast.LENGTH_SHORT);
                        toast1.show();
                    } catch (JSONException e) {
                        Toast toast1 = Toast.makeText(getApplicationContext(), "json exc", Toast.LENGTH_SHORT);
                        toast1.show();                    }
                    try{
                        UrlImageViewHelper.setUrlDrawable(findViewById(R.id.meme), meme.getImageURL());
                        Log.d("TAG", "onClick: finding url: "+meme.getImageURL());
                    }catch(StringIndexOutOfBoundsException e){
                        toast.setText("Error, please try again");
                        toast.show();
                        Log.e("main", "onClick: -1 error again",e);
                    } catch (JSONException e) {
                        Toast toast1 = Toast.makeText(getApplicationContext(), "json exc", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                    //Log.d("TAG", "onClick: api = " + api);
                }else{
                    requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
                }
            }
        });


        FloatingActionButton save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String destination = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Memery";
                String fileName = System.currentTimeMillis()+".jpg";
                File f = new File(destination+"/tmp.t");
                f.mkdirs();
                Log.d("shit", "onClick: sending image to "+destination);
                try{
                    saveImage(ShortStorage.getMeme().getImageURL(), destination+"/"+fileName);
                }catch(Exception e){
                    Log.e("TAG", "onClick: FUKKEN ERROR",e );
                    Toast toast = Toast.makeText(getApplicationContext(), "there was an error", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsController.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goBackToMain(){
        setContentView(R.layout.activity_main);
    }

    public void saveImage(String imageUrl, String destinationFile) throws IOException {
        if(hasWritePermissions()){
            Log.d("TAG", "saveImage: i have the permissions so eat it");
            requestAppPermissions();
        }
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        File f = new File(destinationFile);
        f.mkdirs();
        f.createNewFile();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    private void saveMeme(Meme meme){
        try {
            requestAppPermissions();
            File containingFolder = new File(getExternalFilesDir("data")+"/SavedMemes/");
            Log.d("filesystem", "saveMeme: fs is "+containingFolder.getPath());
            File memeFile = new File(System.currentTimeMillis()+".meme");

            if(!containingFolder.exists()){
                containingFolder.mkdir();
            }
            memeFile.createNewFile();
            FileOutputStream fileout = getApplicationContext().openFileOutput(memeFile.getAbsolutePath(), Context.MODE_PRIVATE);
            ObjectOutputStream objout = new ObjectOutputStream(fileout);
            objout.writeObject(meme);
            objout.close();
            fileout.close();
        } catch (Exception e) {
            Toast y =Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
            y.show();
            Log.e("SaveMeme", "saveMeme: ", e);
            e.printStackTrace();
        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },1);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}