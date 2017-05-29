package com.example.mainuddin.icab12;

/**
 * Created by mainuddin on 5/24/2017.
**/
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {

        private ArrayList<Integer> images;
        private LayoutInflater inflater;
        private Context context;
        private ArrayList<String> string;
        public MyAdapter(Context context, ArrayList<Integer> images,ArrayList<String> s) {
            this.context = context;
            this.images=images;
            this.string = s;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View myImageLayout = inflater.inflate(R.layout.slide, view, false);
            ImageView myImage = (ImageView) myImageLayout
                    .findViewById(R.id.image);
            TextView textView = (TextView) myImageLayout.findViewById(R.id.text);
            myImage.setImageResource(images.get(position));
            textView.setText(string.get(position));
            view.addView(myImageLayout, 0);
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }


    }





