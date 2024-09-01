package com.arms.config;

import static com.arms.config.ApplicationContextProvider.*;

import com.arms.egovframework.javaservice.esframework.annotation.ElasticSearchTemplateConfig;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.admin.indices.alias.get.GetAliasesAction;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.index.AliasData;
import org.springframework.data.elasticsearch.core.index.PutTemplateRequest;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class EsIndexTemplateConfig {

	private final ElasticsearchOperations operations;


	@PostConstruct
	public void run() {

		Set<String> annotatedClasses
			= this.findAnnotatedClasses(ElasticSearchTemplateConfig.class, "com.arms.*");
		annotatedClasses.stream().map(clazz -> {
				try {
					return Class.forName(clazz);
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException(e);
				}
			}).forEach(clazz->{
				Document document = AnnotationUtils.findAnnotation(clazz, Document.class);

				if(document!=null){

					var templateName = document.indexName()+"-template";
					var templatePattern = document.indexName()+"-*";
					var indexOperations = operations.indexOps(clazz);

					if (!indexOperations.existsTemplate(templateName)) {
						log.info("template-{} 생성진행",templateName);
						var mapping = indexOperations.createMapping();

						try(RestHighLevelClient client = getBean(RestHighLevelClient.class)) {
							GetIndexResponse getIndexResponse = client.indices()
								.get(new GetIndexRequest(document.indexName() + "*"), RequestOptions.DEFAULT);

							AliasActions add = new AliasActions().add(
								new AliasAction.Add(AliasActionParameters.builderForTemplate()
									.withAliases(indexOperations.getIndexCoordinates().getIndexNames())
									.withIndices(getIndexResponse.getIndices())
									.build())
							);

							indexOperations.alias(add);

						} catch (IOException e) {
							throw new RuntimeException(e);
						}

						var request = PutTemplateRequest.builder(templateName, templatePattern)
							.withMappings(mapping)
							.withAliasActions(new AliasActions().add(
								new AliasAction.Add(AliasActionParameters.builderForTemplate()
									.withAliases(indexOperations.getIndexCoordinates().getIndexNames())
									.build())
							))
							.build();
						try{
							indexOperations.putTemplate(request);
						}catch (Exception e){
							throw new RuntimeException(e);
						}
					}
				}
			});

	}

	public Set<String> findAnnotatedClasses(Class<? extends Annotation> annotationType, String... packagesToBeScanned)
	{
		var provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new
			AnnotationTypeFilter(annotationType));

		Set<String> ret = new HashSet<>();

		for (var pkg : packagesToBeScanned) {
			Set<BeanDefinition> beanDefs = provider.findCandidateComponents(pkg);
			beanDefs.stream()
				.map(BeanDefinition::getBeanClassName)
				.forEach(ret::add);
		}

		return ret;
	}

}
