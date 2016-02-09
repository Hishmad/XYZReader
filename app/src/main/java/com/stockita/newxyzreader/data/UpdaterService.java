package com.stockita.newxyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;


import com.stockita.newxyzreader.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This service class to sync/fetch data from the internet, then parse the JSON
 * insert them in to the database, delete old data to prevent duplicate
 */
public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.stockita.newxyzreader.data.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.stockita.newxyzreader.data.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            // TODO : notify user
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        Uri dirUri = ItemsContract.Items.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        try {
            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
            if (array == null) {
                throw new JSONException("Invalid parsed item array" );
            }

            // local variables for JSON keys...
            final String SERVER_ID = "id";
            final String AUTHOR = "author";
            final String TITLE = "title";
            final String BODY = "body";
            final String THUMB_URL = "thumb";
            final String PHOTO_URL = "photo";
            final String ASPECT_RATION = "aspect_ratio";
            final String PUBLISHED_DATE = "published_date";



            for (int i = 0; i < array.length(); i++) {
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                values.put(ItemsContract.Items.SERVER_ID, object.getString(SERVER_ID));
                values.put(ItemsContract.Items.AUTHOR, object.getString(AUTHOR));
                values.put(ItemsContract.Items.TITLE, object.getString(TITLE ));
                values.put(ItemsContract.Items.BODY, object.getString(BODY));
                values.put(ItemsContract.Items.THUMB_URL, object.getString(THUMB_URL ));
                values.put(ItemsContract.Items.PHOTO_URL, object.getString(PHOTO_URL ));
                values.put(ItemsContract.Items.ASPECT_RATIO, object.getString(ASPECT_RATION ));
                time.parse3339(object.getString(PUBLISHED_DATE));
                values.put(ItemsContract.Items.PUBLISHED_DATE, time.toMillis(false));
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            getContentResolver().applyBatch(ItemsContract.AUTHORITY, cpo);

        } catch (JSONException | RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
