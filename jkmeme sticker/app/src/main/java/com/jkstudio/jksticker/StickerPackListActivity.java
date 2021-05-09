/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.jkstudio.jksticker;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;


public class StickerPackListActivity extends AddStickerPackActivity {
    public static final String EXTRA_STICKER_PACK_LIST_DATA = "sticker_pack_list";
    private static final int STICKER_PREVIEW_DISPLAY_LIMIT = 5;
    private LinearLayoutManager packLayoutManager;
    private RecyclerView packRecyclerView;
    private StickerPackListAdapter allStickerPacksListAdapter;
    private WhiteListCheckAsyncTask whiteListCheckAsyncTask;
    private ArrayList<StickerPack> stickerPackList;



    //-----surprise----
    public boolean surpriseopen = false;



    //------------review dialog---------

    public void ratedilogini()
    {
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(2) // default 10
                .setRemindInterval(1) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {

                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
    }



    //---------------review----------------
    ReviewManager manager;
    ReviewInfo reviewInfo;

    public void iniReview()
    {
        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
              reviewInfo = task.getResult();

            } else {
                // There was some problem, log or handle the error code.
                Toast.makeText(this, "rating failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
            private void startReview(ReviewInfo reviewinfo)
            {
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);

                flow.addOnSuccessListener(result -> Toast.makeText(getBaseContext(), "review success", Toast.LENGTH_SHORT).show());
            }





    //--------------backpress ----------------
    private long Backpresstime;
    private  Toast Backtoast;


    @Override
    public void onBackPressed() {

        if (Backpresstime + 2000 >System.currentTimeMillis())
        {    Backtoast.cancel();
            super.onBackPressed();

            return;
        }else {
           Backtoast = Toast.makeText(getBaseContext(), "Press back again to Exit", Toast.LENGTH_SHORT);
           Backtoast.show();
        }
        Backpresstime = System.currentTimeMillis();
    }
    //-------------back press end------------






   public void loadAd() {

       if(true) {
           //--------------------------ads---------------
           AdRequest adRequest = new AdRequest.Builder().build();
           InterstitialAd.load(this, getString(R.string.adunitI), adRequest, new InterstitialAdLoadCallback() {
               @Override
               public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                   // The mInterstitialAd reference will be null until
                   // an ad is loaded
                   mInterstitialAd = interstitialAd;
                   //-------------debug-----------
                  // Toast.makeText(StickerPackListActivity.this, "adloaded list", Toast.LENGTH_SHORT).show();
                   Log.i("------->ad is loaded", "onAdLoaded");

               }

               @Override
               public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                   // Handle the error
                  // Toast.makeText(StickerPackListActivity.this, "failed to load ad", Toast.LENGTH_SHORT).show();



                   Log.i("---------onAdLoaded", loadAdError.getMessage());
                   mInterstitialAd = null;
               }
           });

       }

    }

    public void ShowAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
            Toast.makeText(this, getString(R.string.surprise_open_toast), Toast.LENGTH_SHORT).show();
            surpriseopen = true;
        } else {
            Toast.makeText(this, getString(R.string.internet_saved_you_from_ad), Toast.LENGTH_SHORT).show();
            Log.d("---------------->>admob", "The interstitial ad wasn't ready yet.");

        }
    }
     // -----------------------ad-end---------------

    boolean firstload = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, initializationStatus -> { });

            ////==========
              iniReview();
              ratedilogini();



            if(!firstload) {
               // Toast.makeText(this, "first adload", Toast.LENGTH_SHORT).show();
                loadAd();
                firstload = true;
            }

        setContentView(R.layout.activity_sticker_pack_list);
        packRecyclerView = findViewById(R.id.sticker_pack_list);
        stickerPackList = getIntent().getParcelableArrayListExtra(EXTRA_STICKER_PACK_LIST_DATA);
        showStickerPackList(stickerPackList);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getQuantityString(R.plurals.title_activity_sticker_packs_list, stickerPackList.size()));
        }

    }


    // menuoption
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;





    }
    //option functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();

        if(id==R.id.menu_shareapp)
        {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,"subject");
                String  sAUX =getString(R.string.share_app_msg);
                sAUX = sAUX + "https://play.google.com/store/apps/details?id=com.jkstudio.jksticker";
                i.putExtra(Intent.EXTRA_TEXT,sAUX);
                startActivity(Intent.createChooser(i,"choose one"));

            }catch (Exception e)
            {
                Toast.makeText(this, "share app error", Toast.LENGTH_SHORT).show();
            }



        }else if (id==R.id.menu_rateme){
           // Toast.makeText(this, "rate me", Toast.LENGTH_SHORT).show();

           try
           {
               startReview(reviewInfo);
           }catch(ActivityNotFoundException e)
           {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.review_link))));
           }




        }else if (id==R.id.menu_howtoremove_stiker){



            Intent intent = new Intent(this, RemoveSticker.class);
            startActivity(intent);

        } else if (id==R.id.menu_privecypolicy){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privcy_link))));
            }catch (Exception e)
            {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }


           // Toast.makeText(this, "privacy policy", Toast.LENGTH_SHORT).show();

        }else if (id==R.id.menu_viewAD){
        if(!surpriseopen) {
            ShowAd();

        }else
        {
            Toast.makeText(this, getString(R.string.surprise_already_opened), Toast.LENGTH_SHORT).show();
        }



        }else if (id==R.id.menu_about){
            Toast.makeText(this,getString(R.string.about_Toast)+"\n version : "+getString(R.string.version_name), Toast.LENGTH_LONG).show();


        }else if (id==R.id.playstore){
            //Toast.makeText(this," playstore", Toast.LENGTH_LONG).show();
            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.playstore_open_page))));

            }catch(ActivityNotFoundException e)
            {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.playstorpage_link))));
            }

        }else if (id==R.id.In_app){


            Intent intenti = new Intent(this, otherapps_list.class);
            startActivity(intenti);


        }else if (id==R.id.menu_contact_me){


            Intent intent = new Intent(this, contact_me.class);
            startActivity(intent);
          //  Toast.makeText(this, "contact me", Toast.LENGTH_SHORT).show();

        }else if (id==R.id.menu_exit){


            this.finishAffinity();


        }   else if (id==R.id.menu_exit2){


        this.finishAffinity();}

        return super.onOptionsItemSelected(item);
    }







    @Override
    protected void onResume() {
        super.onResume();
        whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
        whiteListCheckAsyncTask.execute(stickerPackList.toArray(new StickerPack[0]));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (whiteListCheckAsyncTask != null && !whiteListCheckAsyncTask.isCancelled()) {
            whiteListCheckAsyncTask.cancel(true);
        }
    }

    private void showStickerPackList(List<StickerPack> stickerPackList) {
        allStickerPacksListAdapter = new StickerPackListAdapter(stickerPackList, onAddButtonClickedListener);
        packRecyclerView.setAdapter(allStickerPacksListAdapter);
        packLayoutManager = new LinearLayoutManager(this);
        packLayoutManager.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                packRecyclerView.getContext(),
                packLayoutManager.getOrientation()
        );
        packRecyclerView.addItemDecoration(dividerItemDecoration);
        packRecyclerView.setLayoutManager(packLayoutManager);
        packRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this::recalculateColumnCount);
    }


    private final StickerPackListAdapter.OnAddButtonClickedListener onAddButtonClickedListener = pack -> addStickerPackToWhatsApp(pack.identifier, pack.name);


    private void recalculateColumnCount() {
        final int previewSize = getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size);
        int firstVisibleItemPosition = packLayoutManager.findFirstVisibleItemPosition();
        StickerPackListItemViewHolder viewHolder = (StickerPackListItemViewHolder) packRecyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition);
        if (viewHolder != null) {
            final int widthOfImageRow = viewHolder.imageRowView.getMeasuredWidth();
            final int max = Math.max(widthOfImageRow / previewSize, 1);
            int maxNumberOfImagesInARow = Math.min(STICKER_PREVIEW_DISPLAY_LIMIT, max);
            int minMarginBetweenImages = (widthOfImageRow - maxNumberOfImagesInARow * previewSize) / (maxNumberOfImagesInARow - 1);
            allStickerPacksListAdapter.setImageRowSpec(maxNumberOfImagesInARow, minMarginBetweenImages);
        }
    }


    static class WhiteListCheckAsyncTask extends AsyncTask<StickerPack, Void, List<StickerPack>> {
        private final WeakReference<StickerPackListActivity> stickerPackListActivityWeakReference;

        WhiteListCheckAsyncTask(StickerPackListActivity stickerPackListActivity) {
            this.stickerPackListActivityWeakReference = new WeakReference<>(stickerPackListActivity);
        }

        @Override
        protected final List<StickerPack> doInBackground(StickerPack... stickerPackArray) {
            final StickerPackListActivity stickerPackListActivity = stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity == null) {
                return Arrays.asList(stickerPackArray);
            }
            for (StickerPack stickerPack : stickerPackArray) {
                stickerPack.setIsWhitelisted(WhitelistCheck.isWhitelisted(stickerPackListActivity, stickerPack.identifier));
            }
            return Arrays.asList(stickerPackArray);
        }

        @Override
        protected void onPostExecute(List<StickerPack> stickerPackList) {
            final StickerPackListActivity stickerPackListActivity = stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity != null) {
                stickerPackListActivity.allStickerPacksListAdapter.setStickerPackList(stickerPackList);
                stickerPackListActivity.allStickerPacksListAdapter.notifyDataSetChanged();
            }
        }
    }
}
