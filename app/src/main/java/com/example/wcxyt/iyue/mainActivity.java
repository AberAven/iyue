package com.example.wcxyt.iyue;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mainActivity extends AppCompatActivity {
    //设置图片自动播放的时间
    private static final int IMAGE_SCROLL_TIME = 4000;
    //自动播放消息标记
    private final int MSG_SCROLLAUTO = 1;
    //定义控件
    private ViewPager viewPager;
    private ListView listView;
    //储存自动播放的ImageView
    List<View> viewList = new ArrayList<View>();
    //viewPager适配器
    private ViewPagerAdepter adepter = new ViewPagerAdepter();
    //图片资源数组
    int[] resId = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    //Handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SCROLLAUTO:
                    synchronized (viewPager) {
                        message.arg1 = viewPager.getCurrentItem();
                        if (message.arg1 == 2) {
                            viewPager.setCurrentItem(0, true);
                        } else {
                            viewPager.setCurrentItem(message.arg1 + 1, true);
                        }
                        handler.sendEmptyMessageDelayed(MSG_SCROLLAUTO, IMAGE_SCROLL_TIME);
                        break;
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_main);
        initViewPager();
        initListView();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listView_main);
        List<Map<String, String>> textList = new ArrayList<Map<String, String>>();
        /**
         * 测试  添加ListView填充信息
         */
        for (int i = 0; i < 3; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("title", "2016年春季大学生职业发展公益巡回讲座");
            map.put("timeMsg", "2016-04-20 星期一  12.30");
            map.put("manMsg", "限200人");
            map.put("locationMsg", "地点：文二105");
            textList.add(map);
        }
        ListViewAdepter listViewAdepter = new ListViewAdepter(textList,resId,mainActivity.this);
        listView.setAdapter(listViewAdepter);
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager_image);
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(mainActivity.this);
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), resId[i]));
            viewList.add(imageView);
        }
        viewPager.setAdapter(adepter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //activity启动两秒钟后，发送一个message，用来将viewPager中的图片切换到下一个
        handler.sendEmptyMessageDelayed(MSG_SCROLLAUTO, IMAGE_SCROLL_TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止viewPager中图片的自动切换
        handler.removeMessages(MSG_SCROLLAUTO);
    }

    public class ViewPagerAdepter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        //view与object是否有关联
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public class ListViewAdepter extends BaseAdapter {
        //自定义的组件类
        private class ViewHolder {
            public TextView title;
            public ImageView timeIcon;
            public ImageView manIcon;
            public ImageView locationIcon;
            public TextView timeMsg;
            public TextView manMsg;
            public TextView locationMsg;
            public ImageView image;
        }

        //文字信息集合
        private List<Map<String, String>> textMsg;
        //图片资源信息
        private int[] imageResId;
        //LayoutInflater 获得布局
        private LayoutInflater myLayoutInflater = null;
        private Context context = null;


        public ListViewAdepter(List<Map<String, String>> allTextMsg, int[] imageResId,Context context) {
            this.textMsg = allTextMsg;
            this.imageResId = imageResId;
            this.context = context;
            myLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return imageResId.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = myLayoutInflater.inflate(R.layout.listview_items, null);
                holder.title = (TextView) view.findViewById(R.id.listView_items_title);
                holder.image = (ImageView) view.findViewById(R.id.listView_items_image);
                holder.timeMsg = (TextView) view.findViewById(R.id.listView_items_timeMsg);
                holder.manMsg = (TextView) view.findViewById(R.id.listView_items_manMsg);
                holder.locationMsg = (TextView) view.findViewById(R.id.listView_items_locationMsg);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.title.setText(textMsg.get(position).get("title"));
            holder.timeMsg.setText(textMsg.get(position).get("timeMsg"));
            holder.manMsg.setText(textMsg.get(position).get("manMsg"));
            holder.locationMsg.setText(textMsg.get(position).get("locationMsg"));
            holder.image.setImageResource(imageResId[position]);
            return view;
        }
    }
}
