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
    @DataType(INTEGER) @PrimaryKey
    String _ID = "_id";

    @DataType(TEXT)
    String _PATH = "_path";

    @DataType(TEXT)
    String _URL = "_url";

    @DataType(INTEGER)
    String _UPLOAD_STATUS = "_upload_status";

    @DataType(INTEGER)
    String _FROM_KINVEY = "_from_kinvey";

    @DataType(INTEGER)
    String _DOWN_STATUS = "_down_status";

    @DataType(TEXT)
    String _KINVEY_ID = "_kinvey_id";

//    upload_status - fail(0),uploading(1),uploaded(2)
}
