package com.example.shiza.muslimmemo;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    String url = "https://muslimmemo.com";
    Elements heading;
    Elements headingLink;
    Elements headingSummary;
    Elements author;
    Elements category;
    Elements published;
    Elements next;
    ProgressBar progressBar;
    ListView listView;
    FloatingActionButton fab;
    CustomAdapter customAdapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Muslim Memo");
//        toolbar.setLogo();
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        listView = (ListView) findViewById(R.id.listView);
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //fab = (FloatingActionButton) findViewById(R.id.fab);
        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.setDrawerListener(drawerToggle);
        Log.d("notification", "calling notification");
//        createNotification();
// calling the asynctask
//        fab.setVisibility(View.GONE);
        Title title = new Title(this);

        title.execute(url);
//        new NotificationCreaterWithParsing(this).createNotification(this,"I am testing.");

        drawerToggle.syncState();

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Title title = new Title(getApplicationContext());
//
//                title.execute("https://muslimmemo.com");
//            }
//        });

    }

    public void createNotification() {
//        Get current time
        Calendar c = Calendar.getInstance();

//       Create an intent which will create your notification
        Intent alertIntent = new Intent(this, NotificationCreater.class);

//       calling alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//the alarm manager will repeatedly call the notification after specified time
        Log.d("createNotification","I am alarm creator");

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60 * 1000, PendingIntent.getBroadcast(this, 1, alertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT));

    }


    public class Wrapper {
//        ArrayList<String> category = new ArrayList<>();
//        ArrayList<String> heading = new ArrayList<>();
//        ArrayList<String> headingLinks = new ArrayList<>();
//        ArrayList<String> headingSummary = new ArrayList<>();
//        ArrayList<String> author = new ArrayList<>();
//        ArrayList<String> published = new ArrayList<>();


        String category;
        String heading;
        String headingLink;
        String headingSummary;
        String author;
        String published;
        boolean flag = false;
        Bitmap bitmap;
//        ArrayList<Bitmap> bitmaps = new ArrayList<>();
    }


    private class Title extends AsyncTask<String, Wrapper, Void> {
        Wrapper w = new Wrapper();
        Context mContext;
        String nextUrl;
        int count = 0;
        View footerView;
        public Title(Context context) {
            mContext = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            //.setVisibility(View.VISIBLE);
//            new NotificationCreaterWithParsing(getApplicationContext()).createNotification(getApplicationContext(),"hello");
        }


        @Override
        protected Void doInBackground(String... params) {
            boolean put_value = true;
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("NOTIFICATION", Activity.MODE_PRIVATE);
            nextUrl = params[0];
            while (!nextUrl.isEmpty()) {
                Log.d("tag", "I am in while.");
                try {
                    Log.d("tag", "Trying to connect.the url is " + nextUrl);

                    Document document = Jsoup.connect(nextUrl).get();

                    Log.d("tag", "connected.the url is " + nextUrl);

                    category = document.getElementsByClass("cat-links");
                    heading = document.getElementsByClass("entry-title");
                    headingLink = document.select("h1.entry-title > a[href]");
                    headingSummary = document.getElementsByClass("entry-summary");
                    author = document.getElementsByClass("author");
                    published = document.getElementsByClass("published");
                    next = document.select("a.next");

                    for (int i = 0; i < heading.size(); i++) {
                        Log.d("tag","I am in for loop");
                        Wrapper w = new Wrapper();

                        w.category = category.get(i).text();
                        w.heading = heading.get(i).text();
                        if ( put_value )
                        {
                            Log.d("NotificationError","I am putting" + w.heading);
                            sharedPreferences.edit().putBoolean(w.heading, true).apply();
                        }


                        w.headingLink = headingLink.get(i).attr("href");

                        Log.d("heading","heading linke is" + headingLink);
                        w.headingSummary = headingSummary.get(i).text();
                        w.author = author.get(i).text();
                        w.published = published.get(i).text();
                        w.bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                R.drawable.bg);

                        try {
                            Document doc = Jsoup.connect(headingLink.get(i).attr("href")).get();
                            // Using Elements to get the class data
                            Element img = doc.select("header.site-header").first();
                            // Locate the src attribute
                            String temp = img.getElementsByAttribute("style")
                                    .toString();
                            // URL of image
                            String imageStrg = temp
                                    .substring(temp.indexOf("(") + 1, temp.indexOf(")"));
                            // Download image from URL
                            InputStream input = new java.net.URL(imageStrg).openStream();
                            // Decode Bitmap

                            Bitmap bit = BitmapFactory.decodeStream(input);
                            Bitmap resized = Bitmap.createScaledBitmap(bit, 100, 100, true);
                            w.bitmap = (resized);

//                        if ( w.bitmaps.get(w.bitmaps.size() - 1) != null )
//                        {
//                            w.bitmaps.get(w.bitmaps.size() - 1).recycle();
//                        }
                        } catch (IOException e) {
                            w.bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.bg);
                            e.printStackTrace();

                        }

                        count++;

                        publishProgress(w);
                    }
                    put_value = false;
                    createNotification();

                   if (next.isEmpty())
                   {

                       Log.d("tag", "Next is empty .");
                       break;
                   }
                    nextUrl = next.attr("href");
                    Log.d("tag", "Next url is ." + nextUrl);
//                   } else {
//                       nextUrl = null;
//                   }
                } catch (IOException e) {
                    w.flag = true;
                    Log.d("tag", "I am in catch" + e);
//                    Toast.makeText(mContext,"Please check your internet connection.",Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(final Wrapper... values) {
            customAdapter = new CustomAdapter(mContext, values[0]);
            super.onProgressUpdate(values);
            if (count == 1) {
                Log.d("HeadingLink", "The heading link : " + values[0].headingLink);
                listView.setAdapter(customAdapter);
//                listView.addFooterView(progressBar);
                footerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress_bar, null, false);
                Log.d("footerview", "Next is empty .");
//add the footer view
                progressBar.setVisibility(View.GONE);
                listView.addFooterView(footerView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        put the heading link when passing data


                        Object listItem = listView.getItemAtPosition(position);

                        Toast.makeText(mContext,"list item Is " + listItem,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), FullContent.class);

//                        intent.putExtra("headingLink", listItem.tit);
//                        intent.putExtra("heading", values[0].heading);
//                        intent.putExtra("headingSummary", values[0].headingSummary);
//                        intent.putExtra("category", values[0].category);
//                        intent.putExtra("author", values[0].author);
//                        intent.putExtra("published", values[0].published);
//
//                        startActivity(intent);
                    }
                });




            } else {
                customAdapter.addItem(values[0]);
            }



            if ( next.isEmpty() )
            {
                listView.removeFooterView(footerView);
            }


        }


        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            //fab.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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
        if (id == R.id.refresh) {
            Title title = new Title(this);

            title.execute("https://muslimmemo.com");

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Intent intent;

        switch (menuItem.getItemId()) {
            case R.id.home:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.web:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://muslimmemo.com/"));
                startActivity(intent);
                break;
            case R.id.contribute:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://muslimmemo.com/contribute/"));
                startActivity(intent);
                break;
            case R.id.discuss:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://muslimmemo.com/discuss/"));
                startActivity(intent);
                break;

            case R.id.about:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://muslimmemo.com/about/"));
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(this,Settings.class);
                startActivity(intent);
                break;
            default:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

        }

        return false;
    }

}
