/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.jkstudio.jksticker;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public abstract class AddStickerPackActivity extends BaseActivity {
    private static final int ADD_PACK = 200;
    private static final String TAG = "AddStickerPackActivity";



    //--------random ad chance-----
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);

    }

   public void loadAd()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.adunitI), adRequest, new InterstitialAdLoadCallback()
        {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd)
            {
                // The mInterstitialAd reference will be null until
                // an ad is loaded
                mInterstitialAd = interstitialAd;
                Log.i("------->ad is loaded", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
            {
                // Handle the error

                Log.i("---------onAdLoaded", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

    }
    public void ShowAd()
    {
        mInterstitialAd.show(this);
    }

    private void loadshowad()
    {
        int randomint = getRandomNumber(0,100);
        if(randomint <74)
        {
            //loadAd();
            //loading in advance in starting of activity


            if (mInterstitialAd != null)
            {
               ShowAd();
            } else
                {
                    Toast.makeText(this, getString(R.string.internet_saved_you_from_ad), Toast.LENGTH_SHORT).show();
                Log.d("---------------->>admob", "The interstitial ad wasn't ready yet.");
                //reload the ad
                }
            //-----------ad end--------------
        }else
        {
            Toast.makeText(this, getString(R.string.no_ads), Toast.LENGTH_LONG).show();
        }

    }










    protected void addStickerPackToWhatsApp(String identifier, String stickerPackName) {

            loadAd();


        try {
            //if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
            if (!WhitelistCheck.isWhatsAppConsumerAppInstalled(getPackageManager()) && !WhitelistCheck.isWhatsAppSmbAppInstalled(getPackageManager())) {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
                return;
            }
            final boolean stickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, identifier);
            final boolean stickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this, identifier);
            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
                //ask users which app to add the pack to.
                launchIntentToAddPackToChooser(identifier, stickerPackName);
            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME);
            } else if (!stickerPackWhitelistedInWhatsAppSmb) {
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME);
            } else {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "error adding sticker pack to WhatsApp", e);
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();

        }

    }

    private void launchIntentToAddPackToSpecificPackage(String identifier, String stickerPackName, String whatsappPackageName) {
        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
        intent.setPackage(whatsappPackageName);
        try {
            startActivityForResult(intent, ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }
    }

    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private void launchIntentToAddPackToChooser(String identifier, String stickerPackName) {
        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.add_to_whatsapp)), ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    private Intent createIntentToAddStickerPack(String identifier, String stickerPackName) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_ID, identifier);
        intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_NAME, stickerPackName);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //showing ad anyway

        loadshowad();


        if (requestCode == ADD_PACK) {

            if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null) {
                    final String validationError = data.getStringExtra("validation_error");
                    if (validationError != null) {
                        if (BuildConfig.DEBUG) {
                            //validation error should be shown to developer only, not users.
                            MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");

                        }
                        Log.e(TAG, "Validation failed:" + validationError);


                    }
                } else {
                    new StickerPackNotAddedMessageFragment().show(getSupportFragmentManager(), "sticker_pack_not_added");
                }
            }else {
                loadshowad();


            }
        }
    }

    public static final class StickerPackNotAddedMessageFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.add_pack_fail_prompt_update_whatsapp)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dismiss())
                    .setNeutralButton(R.string.add_pack_fail_prompt_update_play_link, (dialog, which) -> launchWhatsAppPlayStorePage());

            return dialogBuilder.create();
        }

        private void launchWhatsAppPlayStorePage() {
            if (getActivity() != null) {
                final PackageManager packageManager = getActivity().getPackageManager();
                final boolean whatsAppInstalled = WhitelistCheck.isPackageInstalled(WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME, packageManager);
                final boolean smbAppInstalled = WhitelistCheck.isPackageInstalled(WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME, packageManager);
                final String playPackageLinkPrefix = "http://play.google.com/store/apps/details?id=";
                if (whatsAppInstalled && smbAppInstalled) {
                    launchPlayStoreWithUri("https://play.google.com/store/apps/developer?id=WhatsApp+LLC");
                } else if (whatsAppInstalled) {
                    launchPlayStoreWithUri(playPackageLinkPrefix + WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME);
                } else if (smbAppInstalled) {
                    launchPlayStoreWithUri(playPackageLinkPrefix + WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME);
                }
            }
        }

        private void launchPlayStoreWithUri(String uriString) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriString));
            intent.setPackage("com.android.vending");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), R.string.cannot_find_play_store, Toast.LENGTH_LONG).show();
            }
        }
    }
}
