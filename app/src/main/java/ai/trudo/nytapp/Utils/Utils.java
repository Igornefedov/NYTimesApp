package ai.trudo.nytapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {


    public static String NYT_API_KEY = "c4wmfcD2mq5Bam7pDCzqvhcAyGn2PkLy";

    //checks whether the internet is available, in not no action will be taken
    public static boolean isNetworkAvailable(Context context) {
        //Initiate the Connectivity manager and check if the phone has internet connection.
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
