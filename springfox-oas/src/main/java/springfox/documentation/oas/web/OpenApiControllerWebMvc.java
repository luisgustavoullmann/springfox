/*
 *
 *  Copyright 2017-2018 the original author or authors.
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

package springfox.documentation.oas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UrlPathHelper;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.oas.mappers.ServiceModelToOpenApiMapper;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.MimeTypeUtils.*;
import static springfox.documentation.oas.web.OpenApiControllerWebMvc.*;

@ApiIgnore
@RestController
@RequestMapping(OPEN_API_SPECIFICATION_PATH)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiControllerWebMvc extends OpenApiControllerWeb {

  @Autowired
  public OpenApiControllerWebMvc(
      DocumentationCache documentationCache,
      ServiceModelToOpenApiMapper mapper,
      JsonSerializer jsonSerializer,
      @Value(OPEN_API_SPECIFICATION_PATH)
          String oasPath) {
    super(documentationCache, mapper, jsonSerializer, oasPath);
  }

  @GetMapping(
      produces = {
          APPLICATION_JSON_VALUE,
          HAL_MEDIA_TYPE})
  public ResponseEntity<Json> getDocumentation(
      @RequestParam(value = "group", required = false) String swaggerGroup,
      HttpServletRequest servletRequest) {
    ForwardedHeaderExtractingRequest filter
        = new ForwardedHeaderExtractingRequest(servletRequest, new UrlPathHelper());
    return toJsonResponse(swaggerGroup, decode(filter.adjustedRequestURL()));
  }
}
