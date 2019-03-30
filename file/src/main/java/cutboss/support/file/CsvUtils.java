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

package cutboss.support.file;

import java.util.ArrayList;
import java.util.List;

/**
 * CsvUtils.
 *
 * @author CUTBOSS
 */
@SuppressWarnings("UnusedDeclaration")
public class CsvUtils {
    /**
     *
     *
     * @param values
     * @return
     */
    public static String build(List<List<Object>> values) {
        // BOM UTF-8
        StringBuilder csv = new StringBuilder("\ufeff");

        // get line
        for (List<Object> line : values) {
            // get value
            for (int i = 0; i < line.size(); i++) {
                csv.append(CsvUtils.escape(String.valueOf(line.get(i))));
                if (i == (line.size() - 1)) {
                    csv.append("\r\n");
                } else {
                    csv.append(",");
                }
            }
        }

        // get string
        return csv.toString();
    }

    /**
     * Escape the value.
     *
     * @param value Value
     * @return Escaped value
     */
    @SuppressWarnings("WeakerAccess")
    public static String escape(String value) {
        if (null == value) {
            value = "";
        } else if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     *
     *
     * @param csv Csv
     * @return Values
     */
    public static List<List<Object>> parse(String csv) {
        // empty
        if ((null == csv) || csv.isEmpty()) {
            return new ArrayList<>();
        }

        // BOM?
        if (csv.startsWith("\ufeff")) {
            csv = csv.replace("\ufeff", "");
        }
        if (!csv.endsWith("\n")) {
            if (!csv.endsWith("\r")) {
                csv += "\r";
            }
            csv += "\n";
        }

        //
        List<List<Object>> values = new ArrayList<>();
        List<Object> line = new ArrayList<>();
        boolean doubleQuotation = false;
        int start = 0;
        for (int i = 0; i < csv.length(); i++) {
            char c = csv.charAt(i);
            if ('"' == c) {
                doubleQuotation = true;
                continue;
            }
            if (',' == c) {
                boolean cut = false;
                if (doubleQuotation) {
                    if (0 == (countDoubleQuotes(csv.substring(start, i)) % 2)) {
                        doubleQuotation = false;
                        cut = true;
                    } else {
                        continue;
                    }
                }
                String value = csv.substring(start, i);
                if (cut) {
                    value = value.substring(1, value.length() - 1);
                    value = value.replace("\"\"", "\"");
                }
                line.add(value);
                start = (i + 1);
                continue;
            }
            if ('\n' == c) {
                boolean cut = false;
                if (doubleQuotation) {
                    if (0 == (countDoubleQuotes(csv.substring(start, i)) % 2)) {
                        doubleQuotation = false;
                        cut = true;
                    } else {
                        continue;
                    }
                }
                String value = csv.substring(start, i).replace("\r", "");
                if (cut) {
                    value = value.substring(1, value.length() - 1);
                    value = value.replace("\"\"", "\"");
                }
                line.add(value);
                start = (i + 1);

                values.add(new ArrayList<>(line));
                line.clear();
            }
        }
        return values;
    }

    /**
     *
     *
     * @param text Text
     * @return Count
     */
    @SuppressWarnings("WeakerAccess")
    public static int countDoubleQuotes(String text) {
        if (null == text) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ('"' == c) {
                count++;
            }
        }
        return count;
    }
}
