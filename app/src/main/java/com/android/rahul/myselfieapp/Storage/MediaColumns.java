package com.android.rahul.myselfieapp.Storage;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by rkrde on 07-11-2016.
 */

public interface MediaColumns {
    @DataType(INTEGER)
    @AutoIncrement
    String _ID = "_id";

    @DataType(TEXT) @PrimaryKey
    String _PATH = "path";

    @DataType(INTEGER) @NotNull
    String _UPLOAD_STATUS = "upload_status";

//    upload_status - fail(0),uploading(1),uploaded(2)
}
