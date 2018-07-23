package pieter.co.id.texteditor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditActivity extends AppCompatActivity {
    Toolbar edToolBar;
    EditText edText1;
    File pathEdit;
    String strPathEdit="";
    Menu myMenuEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
        initToolBar();
    }

    private void initToolBar() {
        edToolBar = findViewById(R.id.editTool);
        File aa = new File(strPathEdit);

        edToolBar.setTitle(aa.getName());
        edToolBar.setSubtitle(aa.getParent());
        edToolBar.setTitleTextColor(getResources().getColor(R.color.toolbartext));
        edToolBar.setSubtitleTextColor(getResources().getColor(R.color.toolbartext));
        edToolBar.setSubtitleTextAppearance(getApplicationContext(),R.style.txtSubtitleAppear);
        edToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(edToolBar);

        edToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        Intent itet = getIntent();
        String act = itet.getAction();

        if (Intent.ACTION_VIEW.equals(act)){
            Uri fUris = itet.getData();
            if (fUris!=null){
                File cc = new File(fUris.getPath());
                strPathEdit=cc.getParent() + File.separator + cc.getName();
            }else {
                strPathEdit = itet.getStringExtra("PATH_EDIT");
            }
        }else {
            strPathEdit = itet.getStringExtra("PATH_EDIT");
        }

        if (strPathEdit==null){
            finish();
        }

        pathEdit = new File(strPathEdit);
        edText1 = findViewById(R.id.edText);
        edText1.setText(BukaFile(pathEdit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        myMenuEdit = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_simpan:{
                if (pathEdit!=null){
                    SimpanFile(edText1.getText().toString(),pathEdit);
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String BukaFile(File flPilih){
        String baris;

        String hasil="";
        if (flPilih.getName().toUpperCase().endsWith(".TXT")){
            try {
                FileInputStream fIn = new FileInputStream (flPilih);
                InputStreamReader inRead = new InputStreamReader(fIn);
                BufferedReader bfRead = new BufferedReader(inRead);
                StringBuilder strAll = new StringBuilder();

                while((baris = bfRead.readLine())!=null)
                {
                    strAll.append(baris).append(System.getProperty("line.separator"));
                }

                hasil = strAll.toString();

                fIn.close();
                bfRead.close();
            }
            catch(FileNotFoundException ex) {
                Log.d("NoteEditor", ex.getMessage());
            }
            catch(IOException ex) {
                Log.d("NoteEditor", ex.getMessage());
            }
        }else {
            Toast tst=Toast.makeText(EditActivity.this,"Not a text file",Toast.LENGTH_SHORT);
            tst.setGravity(Gravity.CENTER, 0, 0);
            tst.show();
            hasil="";
        }

        return hasil;
    }

    public void SimpanFile(String data, File flPilih){
        String ss;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(flPilih,false);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
            ss="Success save file";
        }  catch(FileNotFoundException ex) {
            Log.d("NoteEditor", ex.getMessage());
            ss="Filed to save";
        }  catch(IOException ex) {
            Log.d("NoteEditor", ex.getMessage());
            ss="Filed to save";
        }
        Toast tst=Toast.makeText(EditActivity.this,ss,Toast.LENGTH_SHORT);
        tst.setGravity(Gravity.CENTER, 0, 0);
        tst.show();
    }
}
