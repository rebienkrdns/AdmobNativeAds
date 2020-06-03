package com.example.admobnativeads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class MainActivity extends AppCompatActivity {
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key = getString(R.string.admob_ad);

        final ProgressBar progress = findViewById(R.id.progress);

        MobileAds.initialize(this, key);
        final RecyclerView recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, key)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        final ExampleAdapter adapter = new ExampleAdapter(unifiedNativeAd);
                        recyclerview.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Log.d("iferror", String.valueOf(errorCode));
                        final ExampleAdapter adapter = new ExampleAdapter(null);
                        recyclerview.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        UnifiedNativeAd NativeAd;
        int index = 0;

        public ExampleAdapter(UnifiedNativeAd ad) {
            NativeAd = ad;
            index = 0;
        }

        public class AdNativeViewHolder extends RecyclerView.ViewHolder {
            TemplateView adnative;

            public AdNativeViewHolder(View itemView) {
                super(itemView);
                adnative = itemView.findViewById(R.id.ad_native);
            }
        }

        public class ItemExampleViewHolder extends RecyclerView.ViewHolder {
            ImageView image;

            public ItemExampleViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh = null;
            if (((index++ + 2) % 3 == 0) == true) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_native, parent, false);
                vh = new AdNativeViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_example, parent, false);
                vh = new ItemExampleViewHolder(v);
            }
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AdNativeViewHolder) {
                final AdNativeViewHolder v = (AdNativeViewHolder) holder;
                if (NativeAd != null) {
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    v.adnative.setStyles(styles);
                    v.adnative.setNativeAd(NativeAd);
                } else {
                    v.adnative.setVisibility(View.GONE);
                }
            } else {
                final ItemExampleViewHolder v = (ItemExampleViewHolder) holder;
                v.image.setImageDrawable(getDrawable(R.drawable.image));
            }
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}