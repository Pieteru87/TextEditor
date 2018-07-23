package pieter.co.id.texteditor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ViewImage extends AppCompatActivity {
    ViewPager vwPager;
    Toolbar myToolBar;
    private ArrayList<String> dtGmb=MainActivity.gmb;
    int poss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        initToolBar();
        initView();
    }

    @SuppressLint("DefaultLocale")
    private void initToolBar() {
        Intent itet = getIntent();
        myToolBar = findViewById(R.id.mytoolbarimage);
        myToolBar.setTitleTextColor(getResources().getColor(R.color.toolbartext));
        poss=itet.getIntExtra("POSISI_GAMBAR",0);
        myToolBar.setTitle(String.format("%d/%d",poss+1,dtGmb.size()-1));
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

    private void initView(){
        vwPager =findViewById(R.id.vp_base_app);
        List<Fragment> lFrag=new ArrayList<>();
        for (String pathImage:this.dtGmb){
            lFrag.add(FragmentSlider.newInstance(pathImage));
        }
        SliderPagerAdapter mAdapter = new SliderPagerAdapter(myToolBar, getSupportFragmentManager(), lFrag, poss);
        vwPager.setAdapter(mAdapter);
    }
}
