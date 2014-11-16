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
package net.codestory.http;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import net.codestory.http.filters.auth.*;
import net.codestory.http.filters.basic.*;
import net.codestory.http.filters.mixed.*;
import net.codestory.http.security.*;
import net.codestory.http.testhelpers.*;

import org.junit.*;

public class AuthenticationTest extends AbstractProdWebServerTest {
  @Test
  public void public_page() {
    server.configure(routes -> routes.
      filter(new BasicAuthFilter("/secure", "codestory", of("jl", "polka"))).
      get("/", "Public"));

    get("/").produces(200, "text/html", "Public");
  }

  @Test
  public void unauthorized() {
    server.configure(routes -> routes.
      filter(new BasicAuthFilter("/secure", "codestory", of("jl", "polka"))).
      get("/secure", "Private"));

    get("/secure").produces(401).producesHeader("WWW-Authenticate", "Basic realm=\"codestory\"");
  }

  @Test
  public void secured() {
    server.configure(routes -> routes.
      filter(new BasicAuthFilter("/secure", "codestory", of("jl", "polka"))).
      get("/secure", "Private"));

    getWithAuth("/secure", "jl", "polka").produces(200, "text/html", "Private");
  }

  @Test
  public void wrong_password() {
    server.configure(routes -> routes.
      filter(new BasicAuthFilter("/secure", "codestory", of("jl", "polka"))).
      get("/secure", "Private"));

    getWithAuth("/secure", "jl", "wrongpassword").produces(401);
  }

  @Test
  public void get_user_id() {
    server.configure(routes -> routes.
      filter(new BasicAuthFilter("/secure", "codestory", of("Dave", "pwd"))).
      get("/secure", context -> "Hello " + context.currentUser().login()));

    getWithAuth("/secure", "Dave", "pwd").produces(200, "text/html", "Hello Dave");
  }

  @Test
  public void support_basic_auth_with_mixed_filter() {
    server.configure(routes -> routes.
      filter(new MixedAuthFilter("/secure", "codestory", Users.forMap(of("Dave", "pwd")), SessionIdStore.inMemory())).
      get("/secure", context -> "Hello " + context.currentUser().login()));

    getWithPreemptiveAuth("/secure", "Dave", "pwd")
      .produces(200, "text/html", "Hello Dave")
      .producesCookie("auth", null);
  }

  @Test
  public void support_form_auth_with_mixed_filter() {
    server.configure(routes -> routes.
      filter(new MixedAuthFilter("/secure", "codestory", Users.forMap(of("Dave", "pwd")), SessionIdStore.inMemory())).
      get("/secure", context -> "Hello " + context.currentUser().login()));

    post("/secure").produces("Sign in");
    post("/auth/signin", "login", "Dave", "password", "pwd").producesCookie("auth", AuthData.class, authData -> {
      assertThat(authData.login).isEqualTo("Dave");
      assertThat(authData.roles).isEmpty();
      assertThat(authData.sessionId).isNotEmpty();
      assertThat(authData.redirectAfterLogin).isEqualTo("/");
    });
  }

  private static Map<String, String> of(String user, String pwd) {
    return Collections.singletonMap(user, pwd);
  }
}
