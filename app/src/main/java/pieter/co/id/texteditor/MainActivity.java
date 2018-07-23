package pieter.co.id.texteditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    Toolbar myToolBar;
    TextView txtFol;
    ListView dlgListView;
    Menu myMenu;

    File rt;
    File curFol;
    final File NILAI_AWAL=new File(Environment.getExternalStorageDirectory().getAbsolutePath());

    private ArrayList<String> flList=new ArrayList<>();
    private ArrayList<String> flLs=new ArrayList<>();
    private ArrayList<String> strPhts = new ArrayList<>();
    public static ArrayList<String> gmb= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},10001);

        }


        initToolBar();
        initView();
    }

    private void ListDir(File ff){
        if (ff!=null){

            curFol= ff;
            txtFol.setText(ff.getPath());
            File[] fls= ff.listFiles();
            if (fls==null){
                fls=NILAI_AWAL.listFiles();
                txtFol.setText(NILAI_AWAL.getPath());
            }
            Arrays.sort(fls, new SortFolder());

            flList.clear();
            flLs.clear();
            gmb.clear();

            for (File f1:fls){
                if (f1.isDirectory()){
                    flLs.add("Folder: " + f1.getName());
                }else
                {
                    flLs.add(f1.getName());
                }
                flList.add(f1.getPath());
                if (f1.getName().toUpperCase().endsWith(".JPG")||
                        f1.getName().toUpperCase().endsWith(".PNG")||
                        f1.getName().toUpperCase().endsWith(".BMP")||
                        f1.getName().toUpperCase().endsWith(".GIF")){
                    gmb.add(f1.getPath());
                }
            }
            ArrayAdapter<String> arDt= new ArrayAdapter<>(this,R.layout.item_listview,flLs);
            dlgListView.setAdapter(arDt);
            arDt.notifyDataSetChanged();
        }
    }

    private void initView() {
        rt = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFol = rt;
        txtFol = findViewById(R.id.txtPath);
        dlgListView = findViewById(R.id.lVw);
        ListDir(curFol);

        dlgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File pilih = new File(flList.get(position));
                if (pilih.isDirectory()){
                    ListDir(pilih);
                }else
                {
                    if (pilih.getName().toUpperCase().endsWith(".TXT")){
                        String test=pilih.getParent() + File.separator + pilih.getName();
                        Intent mIntent=new Intent(getApplicationContext(),EditActivity.class);
                        Uri extr=Uri.fromFile(pilih);
                        mIntent.setAction(Intent.ACTION_VIEW);
                        mIntent.setData(extr);
                        mIntent.putExtra("PATH_EDIT",test);
                        startActivity(mIntent);
                    }

                    if (pilih.getName().toUpperCase().endsWith(".JPG")||
                            pilih.getName().toUpperCase().endsWith(".PNG")||
                            pilih.getName().toUpperCase().endsWith(".BMP")||
                            pilih.getName().toUpperCase().endsWith(".GIF")){
                        String namaNatural = pilih.getName();
                        int pos;
                        if (gmb.contains(namaNatural)){
                            pos=gmb.indexOf(namaNatural);
                        }else
                        {
                            pos=position;
                        }

                        Intent mIntent=new Intent(getApplicationContext(),ViewImage.class);
                        mIntent.putExtra("POSISI_GAMBAR",pos);
                        startActivity(mIntent);
                    }


                    if (pilih.getName().toUpperCase().endsWith(".3GP")||
                            pilih.getName().toUpperCase().endsWith(".3GPP")||
                            pilih.getName().toUpperCase().endsWith(".MP4")){
                        try {
                            Intent tostart = new Intent(Intent.ACTION_VIEW);
                            tostart.setDataAndType(Uri.parse(pilih.getPath()), "video/*");
                            startActivity(tostart);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        Resources res = getResources();
        Drawable shape = res.getDrawable(R.drawable.selector_listview);
        dlgListView.setSelector(shape);
        dlgListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        dlgListView.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener(){
            @SuppressLint({"DefaultLocale", "ResourceAsColor"})
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                boolean chec=false;
                if (checked){
                    if (!strPhts.isEmpty()){
                        for (String st: strPhts){
                            if (st.equals(flList.get(position))){
                                chec=true;
                            }
                        }
                        if (!chec){
                            strPhts.add(flList.get(position));
                        }
                    }else
                    {
                        strPhts.add(flList.get(position));
                    }
                }else {
                    if (!strPhts.isEmpty()){
                        for (int i=0;i<strPhts.size();i++){
                            if (strPhts.get(i).equals(flList.get(position))){
                                strPhts.remove(i);
                            }
                        }
                    }
                }
                Collections.sort(strPhts);
                mode.setSubtitle(String.format("%d items selected",strPhts.size()));
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                if (getSupportActionBar() != null){
                    getSupportActionBar().hide();
                }
                mode.getMenuInflater().inflate(R.menu.action_menu,menu);
                mode.setTitle("Select Items");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:{
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        sharingIntent.setType("text/plain");

                        ArrayList<Uri> files = new ArrayList<>();

                        if (!strPhts.isEmpty()){
                            for(String path : strPhts) {
                                File file = new File(path);
                                Uri uri = Uri.fromFile(file);
                                files.add(uri);
                            }
                            sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                            startActivity(sharingIntent);
                        }

                        return true;
                    }
                    case R.id.action_rename:{
                        AlertDialog.Builder bld = new AlertDialog.Builder(MainActivity.this);
                        bld.setTitle("Ganti nama file");

                        final EditText inpt = new EditText(MainActivity.this);
                        inpt.setInputType(InputType.TYPE_CLASS_TEXT);
                        bld.setView(inpt);
                        bld.setMessage(R.string.pesan_rename);
                        bld.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (strPhts!=null){
                                    String pathRename="";
                                    for (String strRename:strPhts){
                                        pathRename=strRename;
                                    }
                                    String namaBaru=inpt.getText().toString();
                                    File fileRename=new File(pathRename);
                                    String ss;
                                    if (fileRename.exists()){
                                        String exten=fileRename.getName();
                                        int terakhir;
                                        terakhir=exten.lastIndexOf(".");
                                        String exten1;
                                        if (terakhir>0){
                                            exten1=exten.substring(terakhir,exten.length());
                                        }else {
                                            exten1=".txt";
                                        }
                                        namaBaru = namaBaru + exten1;
                                        String pathBaru=fileRename.getParent()+ File.separator + namaBaru;
                                        File toFile = new File(pathBaru);
                                        boolean hsl=fileRename.renameTo(toFile);
                                        if (hsl){
                                            ss="Success to rename file to " + namaBaru;
                                        }else{
                                            ss="Failed to rename file";

                                        }
                                    }else {
                                        ss="File not exists";
                                    }

                                    Toast tst=Toast.makeText(MainActivity.this,ss,Toast.LENGTH_SHORT);
                                    tst.setGravity(Gravity.CENTER, 0, 0);
                                    tst.show();
                                }

                            }
                        });
                        bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        bld.show();

                        return true;
                    }
                    case R.id.action_delete:{
                        StringBuilder sb=new StringBuilder();
                        String parFile="";
                        for(String path : strPhts) {
                            File fle = new File(path);
                            parFile=fle.getParent();
                            sb.append(fle.getName()).append(System.getProperty("line.separator"));
                        }

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Delete");
                        alert.setMessage(String.format("%s Are you sure you want to delete?",sb.toString()));
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                            @SuppressLint("DefaultLocale")
                            public void onClick(DialogInterface dialog, int which) {
                                if (!strPhts.isEmpty()){
                                    int jmlItem=strPhts.size();
                                    for(String path : strPhts) {
                                        File fle = new File(path);
                                        if (fle.exists()) {
                                            if (fle.delete()){
                                                Toast.makeText(MainActivity.this,String.format("%d has deleted",jmlItem),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alert.show();

                        File kasihNilai = new File(parFile);
                        ListDir(kasihNilai);
                        return true;
                    }
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                if (getSupportActionBar() != null){
                    getSupportActionBar().show();
                }
                strPhts.clear();
            }
        });



    }

    private void initToolBar() {
        myToolBar = findViewById(R.id.mytoolbar);
        myToolBar.setTitleTextColor(getResources().getColor(R.color.toolbartext));
        myToolBar.setTitle(R.string.app_name);
        myToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(myToolBar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        myToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_up:{
                ListDir(curFol.getParentFile());
                break;
            }
            default:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10001:{
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this,"Permission is not granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    //sorts based on a file or folder. folders will be listed first
    private class SortFolder implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            if (f1.isDirectory() == f2.isDirectory())
                return 0;
            else if (f1.isDirectory() && !f2.isDirectory())
                return -1;
            else
                return 1;
        }
    }
}
