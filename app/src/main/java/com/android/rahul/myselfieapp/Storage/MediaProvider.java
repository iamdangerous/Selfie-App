package com.android.rahul.myselfieapp.Storage;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by rkrde on 07-11-2016.
 */
@ContentProvider(authority = MediaProvider.AUTHORITY,
        database = MediaProvider.class)
public  class MediaProvider {
    public static final String AUTHORITY = "com.android.rahul.myselfieapp.storage.MediaProvider";

    @TableEndpoint(table = MediaDatabase.MEDIA_TABLE)
    public static class MediaLists {

        @ContentUri(
                path = "media",
                type = "vnd.android.cursor.dir/media_item"
//                defaultSort = MediaColumns._PATH + " ASC"
        )
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/media");

        @InexactContentUri(
                path = "media/*",
                name = "MEDIA_ID",
                type = "vnd.android.cursor.item/media_item",
                whereColumn = MediaColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/media/" + id);
        }
    }
}
