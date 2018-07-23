package pieter.co.id.texteditor;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class SliderPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFrags;
    private Toolbar mToolBar;
    private int curPos;

    SliderPagerAdapter(Toolbar tlBar, FragmentManager fm, List<Fragment> frags, int currentPosition) {
        super(fm);
        mFrags = frags;
        mToolBar =tlBar;
        curPos=currentPosition;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DefaultLocale")
    @Override
    public Fragment getItem(int position) {
        String imagePath;
        Fragment frgmt;
        if (position!=curPos){
            this.curPos=position % mFrags.size();
        }
        mToolBar.setTitle(String.format("%d/%d",this.curPos,mFrags.size()));
        if (mFrags!=null){
            imagePath= Objects.requireNonNull(mFrags.get(this.curPos).getArguments()).getString("KEY_PARAM");
        }else{
            Uri fileUri = Uri.parse("android.resource://pieter.co.id.texteditor/" + R.drawable.ic_no_img);
            File fl=new File(fileUri.getPath());
            imagePath=fl.getPath();
        }
        frgmt=FragmentSlider.newInstance(imagePath);
        return frgmt;
    }

    @Override
    public int getCount() {
        int maxCount;
        if (mFrags!=null){
            maxCount=mFrags.size();
        }
        else {
            maxCount=0;
        }
        return maxCount;
    }
}
