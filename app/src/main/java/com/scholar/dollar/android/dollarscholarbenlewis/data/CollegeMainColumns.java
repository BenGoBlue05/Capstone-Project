package com.scholar.dollar.android.dollarscholarbenlewis.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;

/**
 * Created by bplewis5 on 10/4/16.
 */

public class CollegeMainColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(DataType.Type.TEXT) @NotNull public static final String COLLEGE_ID = "college_id";
    @DataType(DataType.Type.TEXT) @NotNull public static final String NAME = "name";
    @DataType(DataType.Type.TEXT) @NotNull public static final String LOGO_URL = "logo_url";
    @DataType(DataType.Type.TEXT) @NotNull public static final String CITY = "city";
    @DataType(DataType.Type.TEXT) @NotNull public static final String STATE = "state";
    @DataType(DataType.Type.INTEGER) @NotNull public static final String OWNERSHIP = "school.ownership";
    @DataType(DataType.Type.INTEGER) @NotNull public static final String TUITION_IN_STATE = "tuition_in_state";
    @DataType(DataType.Type.INTEGER) @NotNull public static final String TUITION_OUT_STATE = "tuition_out_state";
    @DataType(DataType.Type.INTEGER) @NotNull public static final String MED_EARNINGS_2012 = "earnings_med_2012_coh";
    @DataType(DataType.Type.REAL) @NotNull public static final String GRADUATION_RATE_6_YEAR = "graduation_rate_6_years";


}
