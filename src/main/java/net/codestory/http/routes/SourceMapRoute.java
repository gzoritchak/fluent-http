/**
 * Copyright (C) 2013 all@code-story.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package net.codestory.http.routes;

import static java.nio.charset.StandardCharsets.*;
import static net.codestory.http.constants.Methods.*;

import java.io.*;
import java.nio.file.*;

import net.codestory.http.*;
import net.codestory.http.compilers.*;
import net.codestory.http.io.*;
import net.codestory.http.payload.*;
import net.codestory.http.types.*;

class SourceMapRoute implements Route {
  @Override
  public boolean matchUri(String uri) {
    return uri.endsWith(".map") && Resources.isPublic(pathSource(uri));
  }

  @Override
  public boolean matchMethod(String method) {
    return GET.equalsIgnoreCase(method) || HEAD.equalsIgnoreCase(method);
  }

  @Override
  public Object body(Context context) throws IOException {
    String uri = context.uri();
    Path pathLess = pathSource(uri);
    Path pathMap = Paths.get(uri);
    String contentType = ContentTypes.get(pathMap);
    String less = Resources.read(pathLess, UTF_8);
    CacheEntry map = Compilers.INSTANCE.compile(pathMap, less);

    return new Payload(contentType, map);
  }

  private static Path pathSource(String uri) {
    if (uri.endsWith(".css.map")) { // special case for css/less map
      return Paths.get(Strings.substringBeforeLast(uri, ".css.map") + ".less");
    }
    return Paths.get(Strings.substringBeforeLast(uri, ".map"));
  }
}


