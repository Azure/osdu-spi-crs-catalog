package org.opengroup.osdu.crs.di;


import lombok.RequiredArgsConstructor;
import org.opengroup.osdu.core.common.http.json.HttpResponseBodyMapper;
import org.opengroup.osdu.core.common.search.ISearchFactory;
import org.opengroup.osdu.core.common.search.SearchAPIConfig;
import org.opengroup.osdu.core.common.search.SearchFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchClientFactory extends AbstractFactoryBean<ISearchFactory> {

	private final HttpResponseBodyMapper bodyMapper;

	@Value("${SEARCH_API:NOT_FOUND}")
	private String SEARCH_API;

	@Override
	public Class<?> getObjectType() {
		return ISearchFactory.class;
	}

	@Override
	protected ISearchFactory createInstance() throws Exception {
		return new SearchFactory(
				SearchAPIConfig
					.builder()
					.rootUrl(SEARCH_API)
					.build(), bodyMapper);
	}
}