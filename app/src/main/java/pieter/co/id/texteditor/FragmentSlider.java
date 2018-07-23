package pieter.co.id.texteditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.Objects;

public class FragmentSlider extends Fragment {
    private static final String ARG_PARAM1 = "KEY_PARAM";

    public FragmentSlider() {
    }

    public static FragmentSlider newInstance(String params) {
        FragmentSlider fragment = new FragmentSlider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, params);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String imageUrls = Objects.requireNonNull(getArguments()).getString(ARG_PARAM1);
        View view = inflater.inflate(R.layout.picture_view, container, false);
        ImageView imgView =view.findViewById(R.id.iVw);
        Bitmap d= BitmapFactory.decodeFile(imageUrls);
        int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
        imgView.setImageBitmap(scaled);
        return view;
    }
}
