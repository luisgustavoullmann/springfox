/*
 *
 *  Copyright 2015-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.swagger.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static java.util.Optional.*;

@RestController
@ApiIgnore
@RequestMapping({
    "${springfox.documentation.swagger-ui.base-url:}/swagger-resources"})
public class ApiResourceController {

  @Autowired(required = false)
  private SecurityConfiguration securityConfiguration;
  @Autowired(required = false)
  private UiConfiguration uiConfiguration;

  private final SwaggerResourcesProvider swaggerResources;

  @Autowired
  public ApiResourceController(
      SwaggerResourcesProvider swaggerResources) {
    this.swaggerResources = swaggerResources;
  }

  @GetMapping(value = "/configuration/security", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SecurityConfiguration> securityConfiguration() {
    return new ResponseEntity<>(
        ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK);
  }

  @GetMapping(value = "/configuration/ui", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UiConfiguration> uiConfiguration() {
    return new ResponseEntity<>(
        ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SwaggerResource>> swaggerResources() {
    return new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK);
  }
}
