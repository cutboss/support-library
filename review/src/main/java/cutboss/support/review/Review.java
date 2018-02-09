/*
 * Copyright (C) 2017 CUTBOSS
 */

package cutboss.support.review;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
    private static final String TAG = Review.class.getSimpleName();

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
            sReview = new JSONObject(load(context, SHARED_PREFERENCES_KEY_REVIEW));
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
        save(context, SHARED_PREFERENCES_KEY_REVIEW, json);
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
    public static boolean showDialogIfNeeded(Activity activity, String url) {
        // is need review?
        if (!Review.isNeedReview(activity)) {
            return false;
        }
        showDialog(activity, url);
        return true;
    }

    /**
     * Show dialog.
     *
     * @param activity activity
     * @param url url
     */
    public static void showDialog(Activity activity, String url) {
        // set args
        Bundle args = new Bundle();
        args.putString(ReviewDialog.KEY_URL, url);

        // show
        ReviewDialog reviewDialog = new ReviewDialog();
        reviewDialog.setArguments(args);
        reviewDialog.setCancelable(false);
        reviewDialog.show(activity.getFragmentManager(), ReviewDialog.TAG);
    }

    /**
     * ReviewDialog.
     */
    public static class ReviewDialog extends DialogFragment {
        /** TAG */
        public static final String TAG = ReviewDialog.class.getSimpleName();

        /** KEY */
        public static final String KEY_URL = "key_url";

        /**
         * Default constructor.
         */
        public ReviewDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // get args
            Bundle args = getArguments();
            String url = "";
            if (null != args) {
                // get url
                if (args.containsKey(KEY_URL)) {
                    url = args.getString(KEY_URL);
                }
            }

            // get uri
            final Uri uri = Uri.parse(url);

            // create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.review_dialog_title);
            builder.setMessage(R.string.review_dialog_message);
            builder.setPositiveButton(R.string.review_dialog_button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Review.setReview(getActivity());
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    getActivity().finish();
                }
            });
            builder.setNeutralButton(R.string.review_dialog_button_skip, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Review.clearLaunchCount(getActivity());
                    getActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.review_dialog_button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Review.setReview(getActivity());
                    getActivity().finish();
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
     * @param key key
     * @param value string
     */
    private static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Load string.
     *
     * @param context context
     * @param key key
     * @return string
     */
    private static String load(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }
}
