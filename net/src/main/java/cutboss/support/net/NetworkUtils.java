/*
 * MIT License
 *
 * Copyright (c) 2018-2019 CUTBOSS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cutboss.support.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkUtils.
 *
 * @author CUTBOSS
 */
@SuppressWarnings("UnusedDeclaration")
public class NetworkUtils {
    /**
     * Is network connected?
     *
     * @param context Context
     * @return is network connected
     */
    public static boolean isNetworkConnected(Context context) {
        // get network info
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (null == networkInfo) {
            return false;
        }

        // connected?
        return networkInfo.isConnected();
    }

    /**
     * Is wifi connected?
     *
     * @param context Context
     * @return is wifi connected
     */
    public static boolean isWifiConnected(Context context) {
        // get network info
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (null == networkInfo) {
            return false;
        }

        // get type
        int type = networkInfo.getType();
        if (ConnectivityManager.TYPE_WIFI != type) {
            return false;
        }

        // connected?
        return networkInfo.isConnected();
    }

    /**
     * Is mobile connected?
     *
     * @param context Context
     * @return is mobile connected
     */
    public static boolean isMobileConnected(Context context) {
        // get network info
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (null == networkInfo) {
            return false;
        }

        // get type
        int type = networkInfo.getType();
        if (ConnectivityManager.TYPE_MOBILE != type) {
            return false;
        }

        // connected?
        return networkInfo.isConnected();
    }

    /**
     * Get active network info.
     *
     * @param context Context
     * @return NetworkInfo
     */
    @SuppressWarnings("WeakerAccess")
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return null;
        }
        return connectivityManager.getActiveNetworkInfo();
    }
}
