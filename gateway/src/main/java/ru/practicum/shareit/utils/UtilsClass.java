package ru.practicum.shareit.utils;

import lombok.experimental.UtilityClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@UtilityClass
public class UtilsClass {
  public static Pageable getPageable(Integer from, Integer size, Sort sort) {
    if (from != null && size != null) {
      return PageRequest.of(from / size, size, sort);
    } else {
      return Pageable.unpaged();
    }
  }

  public static Pageable getPageable(Integer from, Integer size) {
    if (from != null && size != null) {
      return PageRequest.of(from / size, size);
    } else {
      return Pageable.unpaged();
    }
  }

  public static RestTemplate getRestTemplate(
      String serverUrl, String apiPrefix, RestTemplateBuilder builder) {
    return builder
        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + apiPrefix))
        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
        .build();
  }
}
