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
package net.codestory.http.compilers;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.ValueResolver;
import net.codestory.http.misc.*;
import net.codestory.http.templating.*;

public class CompilerFacade {
  protected final Compilers compilers;
  protected final HandlebarsCompiler handlebars;

  public CompilerFacade(Env env) {
    this.compilers = new Compilers(env);
    this.handlebars = new HandlebarsCompiler(compilers);
  }

  public CompilerFacade(Compilers compilers, HandlebarsCompiler handlebar) {
    this.compilers = compilers;
    this.handlebars = handlebar;
  }

  // Configuration

  public void configureHandlebars(Consumer<Handlebars> action) {
    handlebars.configure(action);
  }

  public void addHandlebarResolver(ValueResolver resolver) {
    handlebars.addResolver(resolver);
  }

  public void registerCompiler(Supplier<Compiler> compilerFactory, String targetExtension, String firstExtension, String... moreExtensions) {
    compilers.register(compilerFactory, targetExtension, firstExtension, moreExtensions);
  }

  // Compilation

  public Set<String> extensionsThatCompileTo(String extension) {
    return compilers.extensionsThatCompileTo(extension);
  }

  public CacheEntry compile(Path path, String content) {
    return compilers.compile(path, content);
  }

  public String handlebar(String template, Map<String, ?> variables) throws IOException {
    return handlebars.compile(template, variables);
  }
}
