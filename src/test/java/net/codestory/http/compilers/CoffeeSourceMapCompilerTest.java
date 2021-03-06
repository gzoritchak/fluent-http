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
package net.codestory.http.compilers;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.*;

import org.junit.*;

public class CoffeeSourceMapCompilerTest {
  CoffeeSourceMapCompiler compiler = new CoffeeSourceMapCompiler();

  @Test
  public void generate_sourcemapfile_with_filename_and_sources() {
    String result = compiler.compile(Paths.get("polka.coffee.map"), "a=b=3\nc=4\nd=(a+b)*c");

    assertThat(result).isEqualTo("{\n" +
      " \"version\": 3,\n" +
      " \"file\": \"polka.coffee.source\",\n" +
      " \"sources\": [\n" +
      "  \"polka.coffee.source\"\n" +
      " ],\n" +
      " \"names\": [],\n" +
      " \"mappings\": \"AAAA,IAAA,UAAA;;AAAA,CAAA,GAAE,CAAA,GAAE,CAAJ,CAAA;;AAAA,CACA,GAAE,CADF,CAAA;;AAAA,CAEA,GAAE,CAAC,CAAA,GAAE,CAAH,CAAA,GAAM,CAFR,CAAA\"\n" +
      "}");
  }
}