package com.stockita.newxyzreader.data;

import android.net.Uri;

public class ItemsContract {

	// Constant for the authority
	public static final String AUTHORITY = "com.stockita.newxyzreader.data";

	// Constant for the base uri
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

	// Constant for the scheme
    public static final String SCHEME = "content://";
    public static final String URI = SCHEME + AUTHORITY;

    // To make the CONTENT_URI we need SCHEME, AUTHORITY
    public static final Uri BASE_CONTENT_URI = Uri.parse(URI);

    // Directory
    public static final String DIR_ITEM_MASTER = "items";


	/**
	 * Interface for the column name and index
	 */
    interface ItemsColumns {

        int col_ID = 0;
        int col_SERVER_ID = 1;
        int col_TITLE = 2;
        int col_AUTHOR = 3;
        int col_BODY = 4;
        int col_THUMB_URL = 5;
        int col_PHOTO_URL = 6;
        int col_ASPECT_RATIO = 7;
        int col_PUBLISH_DATE = 8;

		/** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
		String _ID = "_id";
		/** Type: TEXT */
		String SERVER_ID = "server_id";
		/** Type: TEXT NOT NULL */
		String TITLE = "title";
		/** Type: TEXT NOT NULL */
		String AUTHOR = "author";
		/** Type: TEXT NOT NULL */
		String BODY = "body";
        /** Type: TEXT NOT NULL */
        String THUMB_URL = "thumb_url";
		/** Type: TEXT NOT NULL */
		String PHOTO_URL = "photo_url";
		/** Type: REAL NOT NULL DEFAULT 1.5 */
		String ASPECT_RATIO = "aspect_ratio";
		/** Type: INTEGER NOT NULL DEFAULT 0 */
		String PUBLISHED_DATE = "published_date";
	}

	/**
	 * Class for the items master
	 */
	public static class Items implements ItemsColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DIR_ITEM_MASTER).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.stockita.newxyzreader.data.items";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.stockita.newxyzreader.data.items";

        public static final String DEFAULT_SORT = PUBLISHED_DATE + " DESC";

		/** Matches: /items/ */
		public static Uri buildDirUri() {
			return BASE_URI.buildUpon().appendPath(DIR_ITEM_MASTER).build();
		}

		/** Matches: /items/[_id]/ */
		public static Uri buildItemUri(long _id) {
			return BASE_URI.buildUpon().appendPath(DIR_ITEM_MASTER).appendPath(Long.toString(_id)).build();
		}

        /** Read item ID item detail URI. */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
	}

}
