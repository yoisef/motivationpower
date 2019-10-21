package ebrhimelfea.motivation.motivationpower;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class Main2Activity extends BaseActivity {

    PDFView pdfViewer;

    Toolbar toolbar;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    ImageView rightpage,leftpage;
    TextView currentpagetxt,pagescounttxt;

    int startedpage;
    EditText nump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        pdfViewer=findViewById(R.id.pdfView);
        rightpage=findViewById(R.id.rightarrow);
        leftpage=findViewById(R.id.leftarrow);
        currentpagetxt=findViewById(R.id.currentpage);
        pagescounttxt=findViewById(R.id.pagescount);

        Intent intent=getIntent();
        startedpage=intent.getIntExtra("cur",0);

        toolbar=findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);


        loadinterstial();
        interstiallistener();


        intilizebannerad();
        bannerlisener();





        pdfViewer.fromAsset("mopower.pdf")
                //   .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
               .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(startedpage)

                // allows to draw something on the current page, usually visible in the middle of the screen
                //   .onDraw(onDrawListener)
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                //   .onDrawAll(onDrawListener)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                        pagescounttxt.setText(String.valueOf(pdfViewer.getPageCount()));
                        currentpagetxt.setText(String.valueOf(pdfViewer.getCurrentPage()+1));

                    }
                }) // called after document is loaded and starts to be rendered
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }

                        currentpagetxt.setText(String.valueOf(pdfViewer.getCurrentPage()+1));

                    }
                })
               //  .onPageScroll(onPageScrollListener)
                //  .onError(onErrorListener)
                //  .onPageError(onPageErrorListener)
                //   .onRender(onRenderListener) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                //   .onTap(onTapListener)
                //   .onLongPress(onLongPressListener)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
               .scrollHandle(new DefaultScrollHandle(this))
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen
                // .linkHandler(DefaultLinkHandler)
                .pageFitPolicy(FitPolicy.WIDTH)
              //  .pageSnap(true) // snap pages to screen boundaries
                .pageFling(true) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load();



        rightpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pdfViewer.getCurrentPage()!=pdfViewer.getPageCount())
                {
                    pdfViewer.jumpTo(pdfViewer.getCurrentPage()+1);
                    currentpagetxt.setText(String.valueOf(pdfViewer.getCurrentPage()+1));
                }
            }
        });

        leftpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pdfViewer.getCurrentPage()!=0)
                {
                    pdfViewer.jumpTo(pdfViewer.getCurrentPage()-1);
                    currentpagetxt.setText(String.valueOf(pdfViewer.getCurrentPage()+1));
                }





            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.go:
                choosepage();
                return true;
            case R.id.save:

                  getSharedPreferences("curpage", Context.MODE_PRIVATE).edit().putInt("cur",pdfViewer.getCurrentPage()).apply();
                return true;
            case R.id.rate:
                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
    public void choosepage()
    {
        Button go ,cancel;

        builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.choosepage,null);

        go=view.findViewById(R.id.gobut);
        cancel=view.findViewById(R.id.cancelbut);
        nump=view.findViewById(R.id.pagenum);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nump.getText().toString().equals(""))
                {
                    pdfViewer.jumpTo(Integer.parseInt(nump.getText().toString())-1);
                    alertDialog.cancel();


                }else {

                    nump.setError("Enter Number");
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.cancel();
            }
        });



    }


}
