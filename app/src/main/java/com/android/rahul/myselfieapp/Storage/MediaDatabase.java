package com.android.rahul.myselfieapp.Storage;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by rkrde on 07-11-2016.
 */
@Database(version = MediaDatabase.VERSION)
public   class MediaDatabase {
    public static final int VERSION = 1;

    @Table(MediaColumns.class) public static final String MEDIA_TABLE = "media";
}
