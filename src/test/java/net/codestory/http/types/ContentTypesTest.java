/**
 * Copyright (C) 2013-2014 all@code-story.net
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
package net.codestory.http.types;

import static net.codestory.http.types.ContentTypes.*;
import static org.assertj.core.api.Assertions.*;

import java.nio.file.*;

import org.junit.*;

public class ContentTypesTest {
  @Test
  public void content_type_from_extension() {
    assertThat(get(Paths.get("index.html"))).isEqualTo("text/html;charset=UTF-8");
    assertThat(get(Paths.get("data.xml"))).isEqualTo("application/xml;charset=UTF-8");
    assertThat(get(Paths.get("style.css"))).isEqualTo("text/css;charset=UTF-8");
    assertThat(get(Paths.get("style.less"))).isEqualTo("text/css;charset=UTF-8");
    assertThat(get(Paths.get("style.css.map"))).isEqualTo("text/plain;charset=UTF-8");
    assertThat(get(Paths.get("text.md"))).isEqualTo("text/html;charset=UTF-8");
    assertThat(get(Paths.get("text.markdown"))).isEqualTo("text/html;charset=UTF-8");
    assertThat(get(Paths.get("text.txt"))).isEqualTo("text/plain;charset=UTF-8");
    assertThat(get(Paths.get("text.zip"))).isEqualTo("application/zip");
    assertThat(get(Paths.get("text.gz"))).isEqualTo("application/gzip");
    assertThat(get(Paths.get("text.pdf"))).isEqualTo("application/pdf");
    assertThat(get(Paths.get("image.gif"))).isEqualTo("image/gif");
    assertThat(get(Paths.get("image.jpeg"))).isEqualTo("image/jpeg");
    assertThat(get(Paths.get("image.jpg"))).isEqualTo("image/jpeg");
    assertThat(get(Paths.get("image.png"))).isEqualTo("image/png");
    assertThat(get(Paths.get("font.svg"))).isEqualTo("image/svg+xml");
    assertThat(get(Paths.get("font.eot"))).isEqualTo("application/vnd.ms-fontobject");
    assertThat(get(Paths.get("font.ttf"))).isEqualTo("application/x-font-ttf");
    assertThat(get(Paths.get("font.woff"))).isEqualTo("application/x-font-woff");
    assertThat(get(Paths.get("script.js"))).isEqualTo("application/javascript;charset=UTF-8");
    assertThat(get(Paths.get("script.coffee"))).isEqualTo("application/javascript;charset=UTF-8");
    assertThat(get(Paths.get("script.litcoffee"))).isEqualTo("application/javascript;charset=UTF-8");
    assertThat(get(Paths.get("favicon.ico"))).isEqualTo("image/x-icon");
    assertThat(get(Paths.get("unknown"))).isEqualTo("text/plain;charset=UTF-8");
  }

  @Test
  public void compatibility_with_templating() {
    assertThat(support_templating(Paths.get("index.html"))).isTrue();
    assertThat(support_templating(Paths.get("data.xml"))).isTrue();
    assertThat(support_templating(Paths.get("test.md"))).isTrue();
    assertThat(support_templating(Paths.get("test.markdown"))).isTrue();
    assertThat(support_templating(Paths.get("text.txt"))).isTrue();
    assertThat(support_templating(Paths.get("style.css.map"))).isFalse();
    assertThat(support_templating(Paths.get("style.css"))).isFalse();
    assertThat(support_templating(Paths.get("style.less"))).isFalse();
    assertThat(support_templating(Paths.get("text.zip"))).isFalse();
    assertThat(support_templating(Paths.get("text.gz"))).isFalse();
    assertThat(support_templating(Paths.get("text.pdf"))).isFalse();
    assertThat(support_templating(Paths.get("image.gif"))).isFalse();
    assertThat(support_templating(Paths.get("image.jpeg"))).isFalse();
    assertThat(support_templating(Paths.get("image.jpg"))).isFalse();
    assertThat(support_templating(Paths.get("image.png"))).isFalse();
    assertThat(support_templating(Paths.get("font.svg"))).isFalse();
    assertThat(support_templating(Paths.get("font.eot"))).isFalse();
    assertThat(support_templating(Paths.get("font.ttf"))).isFalse();
    assertThat(support_templating(Paths.get("font.woff"))).isFalse();
    assertThat(support_templating(Paths.get("script.js"))).isFalse();
    assertThat(support_templating(Paths.get("script.coffee"))).isFalse();
    assertThat(support_templating(Paths.get("script.litcoffee"))).isFalse();
    assertThat(support_templating(Paths.get("favicon.ico"))).isFalse();
    assertThat(support_templating(Paths.get("unknown"))).isFalse();
  }
}
