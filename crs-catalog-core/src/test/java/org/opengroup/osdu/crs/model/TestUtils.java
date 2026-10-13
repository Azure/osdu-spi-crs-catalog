// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class TestUtils {
    public static String readFile(String fileName) throws IOException {
        InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(fileName);
        if(inputStream == null) {
            throw new IOException();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toString(StandardCharsets.UTF_8.toString());
    }

	/**
	 * Create, initialize, and return the catalog instance from given test catalog file.
     * @param fileName input source catalog file file
	 * @return {@link CatalogImpl}
	 * @throws Exception indicates failure to create the catalog
	 */
    public static CatalogImpl createTestCatalog(String fileName) throws Exception {
        InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(fileName);
        if(inputStream == null) {
            throw new IOException();
        }
        Reader reader = new InputStreamReader(inputStream);
        return CatalogImpl.createCatalog(reader);
    }

}