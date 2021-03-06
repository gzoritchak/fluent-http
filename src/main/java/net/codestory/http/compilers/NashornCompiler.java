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

import static java.nio.charset.StandardCharsets.*;
import static javax.script.ScriptContext.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.codestory.http.io.*;

import javax.script.*;

public final class NashornCompiler {
  private static final ConcurrentMap<String, NashornCompiler> CACHE_BY_SCRIPT = new ConcurrentHashMap<>();

  private final CompiledScript compiledScript;
  private final Bindings bindings;

  private NashornCompiler(String script) {
    ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");
    try {
      compiledScript = ((Compilable) nashorn).compile(script);
      bindings = nashorn.getBindings(ENGINE_SCOPE);
    } catch (ScriptException e) {
      throw new IllegalStateException("Unable to compile javascript", e);
    }
  }

  public static NashornCompiler get(String... scriptPaths) {
    String script = readScripts(scriptPaths);
    return CACHE_BY_SCRIPT.computeIfAbsent(script, NashornCompiler::new);
  }

  private static String readScripts(String... scriptPaths) {
    StringBuilder concatenatedScript = new StringBuilder();

    for (String scriptPath : scriptPaths) {
      try (InputStream input = InputStreams.getResourceAsStream(scriptPath)) {
        String content = InputStreams.readString(input, UTF_8);

        concatenatedScript.append(content).append("\n");
      } catch (IOException e) {
        throw new IllegalStateException("Unable to read script " + scriptPath, e);
      }
    }

    return concatenatedScript.toString();
  }

  public synchronized String compile(Path path, String source, Map<String, Object> options) {
    bindings.put("__filename", getFileName(path));
    bindings.put("__source", source);
    options.forEach((name, value) -> bindings.put(name, value));

    try {
      return compiledScript.eval(bindings).toString();
    } catch (ScriptException e) {
      String message = cleanMessage(path, e.getCause().getMessage());
      throw new CompilerException(message);
    }
  }

  private String getFileName(Path path) {
    return path.getFileName().toString().replace(".map", ".source");
  }

  private static String cleanMessage(Path path, String message) {
    return message.replace(
      "Unable to compile CoffeeScript [stdin]:",
      "Unable to compile " + path + ":"
    );
  }
}
