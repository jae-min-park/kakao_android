package com.kakao.gridviewapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView mGv;
    private MyAdapter mAdapter;
    private ArrayList<String> mList = new ArrayList();

    private WebView mWv;
    //    private ImageView mIv;

    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mList = savedInstanceState.getStringArrayList("mList");
        }
//        mIv = findViewById(R.id.viewer);
        mGv = findViewById(R.id.gv);
        mWv = findViewById(R.id.wv);
        mWv.setWebChromeClient(new WebChromeClient());
        mWv.setWebViewClient(new WebViewClientClass());
        new MyTask().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mList",mList);
    }

    @Override
    public void onBackPressed() {
        if (mWv.getVisibility() == View.GONE) {
            super.onBackPressed();
        }
        mWv.loadUrl("");
        mWv.setVisibility(View.GONE);
//        if (mIv.getVisibility() == View.GONE) {
//            super.onBackPressed();
//        }
//        mIv.setVisibility(View.GONE);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPd = new ProgressDialog(MainActivity.this);
            mPd.setTitle("Dialog");
            mPd.setMessage("Loading...");
            mPd.setIndeterminate(false);
            mPd.setCancelable(false);
            mPd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new MyAdapter(MainActivity.this, R.layout.item, mList);
            mGv.setAdapter(mAdapter);
            mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*
                     * item을 클릭했을때 보여주는 이미지는 원본 화질로 보여주고자 webView에서 보여주는 것을 선택
                     * 기존 view를 bitmap으로 받아 ImageVIew에서 보여주는 것은 속도는 빠르나 저화질로 보여줌
                     */
                    mWv.loadUrl(parent.getItemAtPosition(position) + "");
                    mWv.setVisibility(View.VISIBLE);
//                    Bitmap b = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.RGBA_F16);
//                    Canvas c = new Canvas(b);
//                    view.draw(c);
//                    mIv.setImageBitmap(b);
//                    mIv.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, parent.getItemAtPosition(position) + "", Toast.LENGTH_LONG).show();
                }
            });

            try {
                mPd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(mList.isEmpty()) {
                try {
                    long beginTime = System.currentTimeMillis();
                    /*
                     * Jsoup Parser 사용시 2.4~2.9초 걸렷던 내용이 jericho 사용시 0.8 ~2.7초 걸림.
                     * -> jericho 사용
                     */
                    String urlStr = "https://www.gettyimagesgallery.com/collection/celebrities/";
//                Document doc = Jsoup.connect(urlStr).get();
//                Log.d("!DOC", doc + "\n");
//                Elements elements = doc.select("img");
//                Log.d("!ELMTS", elements + "\n");
//                for (Element element : elements) {
//                    Log.d("!ELM", element + "\n");
//                    String str = element.attr("src");
//                    Log.d("!STR", str + "\n");
//                    mList.add(str);
//                }
                    URL url = new URL(urlStr);
                    Source src = new Source(url);
                    Log.d("!SRC", src + "\n");
                    List<net.htmlparser.jericho.Element> elements = src.getAllElements("img");
                    Log.d("!ELMTS", elements + "\n");
                    for (net.htmlparser.jericho.Element element : elements) {
                        Log.d("!ELM", element + "\n");
                        String str = element.getAttributeValue("src");
                        Log.d("!STR", str + "\n");
                        mList.add(str);
                    }
                    long intervalTime = System.currentTimeMillis() - beginTime;
                    Log.d("!TIME_PARSE", intervalTime + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    protected class WebViewClientClass extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

