package com.sarya.graphql.client.application.port.in;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.sarya.graphql.client.codegen.client.FetchProductsGraphQLQuery;
import com.sarya.graphql.client.codegen.client.FetchProductsProjectionRoot;
import org.intellij.lang.annotations.Language;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

  @GetMapping
  public Mono<Map<String, Object>> fetchProducts() {
    @Language("graphql")
    var graphQLQueryRequest = new GraphQLQueryRequest(
        new FetchProductsGraphQLQuery(),
        new FetchProductsProjectionRoot<>().productId().name().active().productType()
    ).serialize();
    //Configure a WebClient for your needs, e.g. including authentication headers and TLS.
    WebClient webClient = WebClient.create("http://localhost:8080/graphql");

    WebClientGraphQLClient webClientGraphQLClient = new WebClientGraphQLClient(webClient);
    return webClientGraphQLClient.reactiveExecuteQuery(graphQLQueryRequest)
        .map(GraphQLResponse::getData);
  }
}
