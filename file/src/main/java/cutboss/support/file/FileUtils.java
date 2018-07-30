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

package cutboss.support.file;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * FileUtils.
 *
 * @author CUTBOSS
 */
public class FileUtils {
    /**  */
    public static final String RESULT_KEY_PATH = "path";

    /**  */
    public static final int FILE_NAME_LENGTH = 15;

    /**
     *
     *
     * @param fileName
     * @return Extension
     */
    public static String getExtension(final @NonNull String fileName) {
        String extension = "";
        int point = fileName.lastIndexOf('.');
        if (-1 < point) {
            extension = fileName.substring(point + 1);
        }
        return extension;
    }

    /**
     * Remove file name extension.
     *
     * @param fileName file name
     * @return file name with extension removed
     */
    public static String removeFileNameExtension(final @NonNull String fileName) {
        int point = fileName.lastIndexOf(".");
        if (-1 < point) {
            return fileName.substring(0, point);
        }
        return fileName;
    }

    /**
     * Write to file.
     *
     * @param context Context
     * @param fileName The name of the file to open
     * @param text Text
     * @return Result
     */
    public static boolean writeFile(Context context, String fileName, @NonNull String text) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(text.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     *
     *
     * @param path
     * @param text
     * @param charset
     * @return
     */
    public static boolean writeFile(String path, @NonNull String text, String charset) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(text.getBytes(charset));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Read from file.
     *
     * @param context Context
     * @param fileName The name of the file to open
     * @return Text
     */
    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] bytes = new byte[fis.available()];
            if (0 > fis.read(bytes)) {
                return "";
            }
            return new String(bytes);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Map<String, Object> writeFileToExternalStorage(
            final @NonNull Activity activity, @NonNull String fileName, @NonNull String text,
            String directoryName, String charset, boolean temp) {
        // get extension
        String extension = getExtension(fileName);

        // remove extension
        fileName = removeFileNameExtension(fileName);
        if ("".equals(fileName)) {
            // 禁則処理
            fileName = text.replace("\n", "")
                    .replace("\\", "").replace("\u00a5", "").replace("/", "").replace(":", "")
                    .replace("*", "").replace("?", "").replace("\"", "")
                    .replace("<", "").replace(">", "").replace("|", "")
                    .replace(" ", "_").replace("　", "_");
        }
        if (FILE_NAME_LENGTH < fileName.length()) {
            fileName = fileName.substring(0, FILE_NAME_LENGTH);
        }
        if ("".equals(fileName)) {
            fileName = "notitle";
        }

        //
        String path;
        boolean exists;
        String suffix = "0";
        String fileNameOrigin = fileName;
        do {
            // create external storage path
            path = createExternalStoragePath(activity, directoryName, temp);
            if (null == path) {
                return null;
            }
            path = (path + fileName + "." + extension);

            // exists?
            exists = new File(path).exists();
            if (exists) {
                // SUFFIXを付与して同一ファイル名を回避
                suffix = Integer.toString(Integer.parseInt(suffix) + 1);
                if (1 == suffix.length()) {
                    suffix = "000" + suffix;
                } else if (2 == suffix.length()) {
                    suffix = "00" + suffix;
                } else if (3 == suffix.length()) {
                    suffix = "0" + suffix;
                }
                fileName = fileNameOrigin + "_" + suffix;
            }
        } while (exists);

        // write file
        if (!writeFile(path, text, charset)) {
            return null;
        }

        //
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        //
        Uri uri = null;
        if (!temp) {
            uri = insertContentResolver(activity, fileName, mimeType, path);
        }

        // set result
        Map<String, Object> result = new HashMap<>();
        result.put("file_name", fileName);
        result.put(RESULT_KEY_PATH, path);
        result.put("uri", uri);
        return result;
    }

    /**
     *
     *
     * @param activity
     * @param directoryName
     * @param init
     * @return
     */
    public static String createExternalStoragePath(
            final @NonNull Activity activity, final @NonNull String directoryName, boolean init) {
        // granted?
        if (!checkSelfPermissionWriteExternalStorage(activity)) {
            // denied
            return null;
        }

        // empty?
        if ("".equals(directoryName)) {
            return null;
        }

        // get external storage directory
        File directory =
                new File(Environment.getExternalStorageDirectory() + directoryName);
        try {
            // exists?
            if (!directory.exists()) {
                // make directory
                if (!directory.mkdir()) {
                    return null;
                }
            } else {
                // フォルダを初期化
                if (init) {
                    // delete all files
                    File[] files = directory.listFiles();
                    for (File file : files) {
                        // delete file
                        if (file.exists() && !file.delete()) {
                            // 削除失敗

                            // VM終了時に削除
                            file.deleteOnExit();
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }

        // get absolute path
        return (directory.getAbsolutePath() + "/");
    }

    /**  */
    public static final int REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 2013122702;

    /**
     *
     *
     * @param activity
     * @return
     */
    public static boolean checkSelfPermissionWriteExternalStorage(
            final @NonNull Activity activity) {
        return !requestPermissions(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
    }

    /**
     *
     *
     * @param activity
     * @param permission
     * @param permissions
     * @param requestCode
     * @return Result
     */
    public static boolean requestPermissions(
            final @NonNull Activity activity, @NonNull String permission,
            final @NonNull String[] permissions, final int requestCode) {
        int result = ActivityCompat.checkSelfPermission(activity, permission);
        if (PackageManager.PERMISSION_GRANTED != result) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
            // denied
            return true;
        }
        // granted
        return false;
    }

    /**
     *
     *
     * @param context
     * @param title
     * @param mimeType
     * @param data
     * @return
     */
    public static Uri insertContentResolver(
            final @NonNull Context context, String title, String mimeType, String data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, title);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.Images.Media.DATA, data);
        Uri createdRow = context.getContentResolver().insert(
                MediaStore.Files.getContentUri("external"), contentValues);
        return createdRow;
    }
}
