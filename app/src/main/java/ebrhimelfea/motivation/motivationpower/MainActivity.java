package ebrhimelfea.motivation.motivationpower;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends BaseActivity{


    Button read,readon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        read=findViewById(R.id.readid);
        readon=findViewById(R.id.readonid);


        loadinterstial();
        interstiallistener();

        intilizebannerad();
        bannerlisener();







        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("cur",0);
                startActivity(intent);

            }
        });


        readon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i=getSharedPreferences("curpage", Context.MODE_PRIVATE).getInt("cur",0);
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("cur",i);
                startActivity(intent);

            }
        });
    }



}
