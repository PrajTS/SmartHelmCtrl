package data;

import android.provider.BaseColumns;

public final class HelmContract {

    private HelmContract() {
    }

    public static final class HelmetEntry implements BaseColumns {

        public final static String TABLE_NAME = "helms";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HELM_NAME = "name";
        public final static String COLUMN_MAC_ADDRESS = "mac_address";
        public final static String COLUMN_DEFAULT = "default";

    }
}