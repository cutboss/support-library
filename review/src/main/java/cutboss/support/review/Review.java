/*
 * Copyright (C) 2017-2019 CUTBOSS
 */

package cutboss.support.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Review.
 *
 * @author CUTBOSS
 */
public class Review {
    /** TAG */
    private static final String TAG = "Review";

    // ---------------------------------------------------------------------------------------------
    // REVIEW
    // ---------------------------------------------------------------------------------------------

    /** JUDGMENT LAUNCH COUNT */
    private static final int JUDGMENT_LAUNCH_COUNT = 5;

    /** JUDGMENT PERIOD */
    private static final long JUDGMENT_PERIOD = (3 * 24 * 60 * 60 * 1000);

    /** KEY */
    private static final String KEY_REVIEW = "review";
    private static final String KEY_HISTORY = "history";
    private static final String KEY_DATE = "date";

    /** Review */
    private static JSONObject sReview;

    /**
     * Record launch count.
     *
     * @param context context
     */
    public static void recordLaunch(Context context) {
        // is review?
        if (isReview(context)) {
            // already review, ignore
            Log.d(TAG, "recordLaunch: already review!");
            return;
        }

        // add date
        JSONObject date = new JSONObject();
        try {
            date.put(KEY_DATE, System.currentTimeMillis());
            JSONArray history = getHistory(context);
            history.put(date);
            if (JUDGMENT_LAUNCH_COUNT < history.length()) {
                history.remove(0);
                // TODO 19-
            }
            sReview.put(KEY_HISTORY, history);

            // save review
            saveReview(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is need review?
     * (Launch of a specified number of times within a specified period?)
     *
     * @param context context
     * @return is need review
     */
    public static boolean isNeedReview(Context context) {
        // is review?
        if (isReview(context)) {
            // already review, no review required
            Log.d(TAG, "isNeedReview: already review!");
            return false;
        }

        // get history
        JSONArray history = getHistory(context);
        Log.d(TAG, "isNeedReview: history.size(): " + history.length());
        if (JUDGMENT_LAUNCH_COUNT > history.length()) {
            // launch is not within the specified number of times, no review required
            return false;
        }

        // launch within the period?
        for (int i = 0; i < history.length(); i++) {
            // get date
            long date = 0L;
            try {
                date = history.getJSONObject(i).getLong(KEY_DATE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (date < (System.currentTimeMillis() - JUDGMENT_PERIOD)) {
                // launch is not within the period, no review required
                return false;
            }
        }

        // need review!
        return true;
    }

    /**
     * Set review.
     *
     * @param context context
     */
    public static void setReview(Context context) {
        try {
            getReview(context).put(KEY_REVIEW, true);
            saveReview(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the review.
     *
     * @param context context
     * @return review
     */
    private static JSONObject getReview(Context context) {
        // is null?
        if (null != sReview) {
            return sReview;
        }

        // load review
        try {
            sReview = new JSONObject(load(context));
            if (!sReview.has(KEY_REVIEW) || !sReview.has(KEY_HISTORY)) {
                // init review
                initReview();
            }
        } catch (JSONException e) {
            e.printStackTrace();

            // init review
            initReview();
        }
        return sReview;
    }

    /**
     * Initialize review.
     */
    private static void initReview() {
        if (null == sReview) {
            sReview = new JSONObject();
        }
        try {
            sReview.put(KEY_REVIEW, false);
            sReview.put(KEY_HISTORY, new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is review?
     *
     * @param context context
     * @return is review
     */
    private static boolean isReview(Context context) {
        try {
            return getReview(context).getBoolean(KEY_REVIEW);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get history.
     *
     * @param context context
     * @return history
     */
    private static JSONArray getHistory(Context context) {
        try {
            return getReview(context).getJSONArray(KEY_HISTORY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * Clear launch count.
     */
    private static void clearLaunchCount(Context context) {
        try {
            getReview(context).put(KEY_HISTORY, new JSONArray());
            saveReview(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save review.
     *
     * @param context context
     */
    private static void saveReview(Context context) {
        if (null == sReview) {
            sReview = new JSONObject();
        }
        String json = sReview.toString();
        Log.d(TAG, "saveReview: json: " + json);
        save(context, json);
    }

    // ---------------------------------------------------------------------------------------------
    // DIALOG
    // ---------------------------------------------------------------------------------------------

    /**
     * Show dialog if needed.
     *
     * @param activity activity
     * @param url url
     */
    public static boolean showDialogIfNeeded(FragmentActivity activity, String url) {
        return showDialogIfNeeded(activity, url, 0);
    }

    /**
     * Show dialog if needed.
     *
     * @param activity activity
     * @param url url
     * @param themeResId the resource ID of the theme
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean showDialogIfNeeded(
            FragmentActivity activity, String url, int themeResId) {
        // is need review?
        if (!Review.isNeedReview(activity)) {
            return false;
        }
        showDialog(activity, url, themeResId);
        return true;
    }

    /**
     * Show dialog.
     *
     * @param activity activity
     * @param url url
     */
    public static void showDialog(FragmentActivity activity, String url) {
        showDialog(activity, url, 0);
    }

    /**
     * Show dialog.
     *
     * @param activity activity
     * @param url url
     * @param themeResId the resource ID of the theme
     */
    @SuppressWarnings("WeakerAccess")
    public static void showDialog(FragmentActivity activity, String url, int themeResId) {
        // set args
        Bundle args = new Bundle();
        args.putString(ReviewDialog.KEY_URL, url);
        if (0 < themeResId) {
            args.putInt(ReviewDialog.KEY_THEME_RES_ID, themeResId);
        }

        // show dialog
        ReviewDialog reviewDialog = new ReviewDialog();
        reviewDialog.setArguments(args);
        reviewDialog.setCancelable(false);
        reviewDialog.show(activity.getSupportFragmentManager(), ReviewDialog.TAG);
    }

    /**
     * ReviewDialog.
     */
    public static class ReviewDialog extends DialogFragment {
        /** TAG */
        public static final String TAG = ReviewDialog.class.getSimpleName();

        /** KEY */
        public static final String KEY_URL = "key_url";
        public static final String KEY_THEME_RES_ID = "key_theme_res_id";

        /**
         * Default constructor.
         */
        public ReviewDialog() {
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // get args
            Bundle args = getArguments();
            String url = "";
            int themeResId = 0;
            if (null != args) {
                // get url
                if (args.containsKey(KEY_URL)) {
                    url = args.getString(KEY_URL);
                }

                // get theme res id
                if (args.containsKey(KEY_THEME_RES_ID)) {
                    themeResId = args.getInt(KEY_THEME_RES_ID);
                }
            }

            // get uri
            final Uri uri = Uri.parse(url);

            // create dialog
            final FragmentActivity activity = getActivity();
            AlertDialog.Builder builder;
            if (0 < themeResId) {
                builder = new AlertDialog.Builder(activity, themeResId);
            } else {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle(R.string.review_dialog_title);
            builder.setMessage(R.string.review_dialog_message);
            builder.setPositiveButton(R.string.review_dialog_button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null == activity) {
                        return;
                    }
                    Review.setReview(activity);
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    activity.finish();
                }
            });
            builder.setNeutralButton(R.string.review_dialog_button_skip, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null == activity) {
                        return;
                    }
                    Review.clearLaunchCount(activity);
                    activity.finish();
                }
            });
            builder.setNegativeButton(R.string.review_dialog_button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null == activity) {
                        return;
                    }
                    Review.setReview(activity);
                    activity.finish();
                }
            });
            return builder.create();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // SHARED PREFERENCES
    // ---------------------------------------------------------------------------------------------

    /** NAME */
    private final static String SHARED_PREFERENCES_NAME = "cutboss_support_review";

    /** KEY */
    private final static String SHARED_PREFERENCES_KEY_REVIEW = "review";

    /**
     * Get the SharedPreferences.
     *
     * @param context context
     * @return SharedPreferences
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save string.
     *
     * @param context context
     * @param value string
     */
    private static void save(Context context, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SHARED_PREFERENCES_KEY_REVIEW, value);
        editor.apply();
    }

    /**
     * Load string.
     *
     * @param context context
     * @return string
     */
    private static String load(Context context) {
        return getSharedPreferences(context)
                .getString(SHARED_PREFERENCES_KEY_REVIEW, "");
    }
}
