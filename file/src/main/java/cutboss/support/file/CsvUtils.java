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
}
