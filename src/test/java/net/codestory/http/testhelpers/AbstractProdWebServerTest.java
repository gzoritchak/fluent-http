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
package net.codestory.http.testhelpers;

import net.codestory.rest.FluentRestTest;
import org.junit.*;

public abstract class AbstractProdWebServerTest implements FluentRestTest {
  @ClassRule
  public static ProdWebServerRule server = new ProdWebServerRule();

  @Override
  public int port() {
    return server.port();
  }
}
