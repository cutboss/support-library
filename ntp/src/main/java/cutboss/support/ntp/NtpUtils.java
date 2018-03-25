/*
 * MIT License
 *
 * Copyright (c) 2018 CUTBOSS
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

package cutboss.support.ntp;

import android.content.Context;
import android.content.res.Resources;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeStamp;

import java.net.InetAddress;

/**
 * NtpUtils.
 *
 * @author CUTBOSS
 */
public class NtpUtils {
    /** DEFAULT TIMEOUT */
    private static final int DEFAULT_TIMEOUT = (3 * 1000);

    /**
     *
     *
     * @param context
     * @return
     */
    public static String getDefaultHost(Context context) {
        final int id = Resources.getSystem().getIdentifier(
                "config_ntpServer", "string", "android");
        return context.getResources().getString(id);
    }

    public static long getTransmitTime(Context context) {
        return getTransmitTime(getDefaultHost(context), DEFAULT_TIMEOUT);
    }

    public static long getTransmitTime(String host, int timeout) {
        TimeStamp timeStamp = getTransmitTimeStamp(host, timeout);
        if (null == timeStamp) {
            return 0L;
        }
        return timeStamp.getTime();
    }

    public static TimeStamp getTransmitTimeStamp(String host, int timeout) {
        NtpV3Packet ntpV3Packet = getNtpV3Packet(host, timeout);
        if (null == ntpV3Packet) {
            return null;
        }
        return ntpV3Packet.getTransmitTimeStamp();
    }

    public static long getReferenceTime(Context context) {
        return getReferenceTime(getDefaultHost(context), DEFAULT_TIMEOUT);
    }

    public static long getReferenceTime(String host, int timeout) {
        TimeStamp timeStamp = getReferenceTimeStamp(host, timeout);
        if (null == timeStamp) {
            return 0L;
        }
        return timeStamp.getTime();
    }

    public static TimeStamp getReferenceTimeStamp(String host, int timeout) {
        NtpV3Packet ntpV3Packet = getNtpV3Packet(host, timeout);
        if (null == ntpV3Packet) {
            return null;
        }
        return ntpV3Packet.getReferenceTimeStamp();
    }

    public static long getOriginateTime(Context context) {
        return getOriginateTime(getDefaultHost(context), DEFAULT_TIMEOUT);
    }

    public static long getOriginateTime(String host, int timeout) {
        TimeStamp timeStamp = getOriginateTimeStamp(host, timeout);
        if (null == timeStamp) {
            return 0L;
        }
        return timeStamp.getTime();
    }

    public static TimeStamp getOriginateTimeStamp(String host, int timeout) {
        NtpV3Packet ntpV3Packet = getNtpV3Packet(host, timeout);
        if (null == ntpV3Packet) {
            return null;
        }
        return ntpV3Packet.getOriginateTimeStamp();
    }

    public static long getReceiveTime(Context context) {
        return getReceiveTime(getDefaultHost(context), DEFAULT_TIMEOUT);
    }

    public static long getReceiveTime(String host, int timeout) {
        TimeStamp timeStamp = getReceiveTimeStamp(host, timeout);
        if (null == timeStamp) {
            return 0L;
        }
        return timeStamp.getTime();
    }

    public static TimeStamp getReceiveTimeStamp(String host, int timeout) {
        NtpV3Packet ntpV3Packet = getNtpV3Packet(host, timeout);
        if (null == ntpV3Packet) {
            return null;
        }
        return ntpV3Packet.getReceiveTimeStamp();
    }

    /**
     *
     *
     * @param host
     * @param timeout
     * @return
     */
    private static NtpV3Packet getNtpV3Packet(String host, int timeout) {
        NTPUDPClient ntpUdpClient = new NTPUDPClient();
        ntpUdpClient.setDefaultTimeout(timeout);
        try {
            ntpUdpClient.open();
            return ntpUdpClient.getTime(InetAddress.getByName(host)).getMessage();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ntpUdpClient.close();
        }
        return null;
    }
}
