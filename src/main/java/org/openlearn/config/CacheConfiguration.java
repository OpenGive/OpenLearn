package org.openlearn.config;

import java.util.concurrent.TimeUnit;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.openlearn.domain.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

	private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

	public CacheConfiguration(final JHipsterProperties jHipsterProperties) {
		final JHipsterProperties.Cache.Ehcache ehcache =
				jHipsterProperties.getCache().getEhcache();

		jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
				CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
						ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
				.withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
				.build());
	}

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cm -> {
			cm.createCache(User.class.getName(), jcacheConfiguration);
			cm.createCache(Authority.class.getName(), jcacheConfiguration);
			cm.createCache(User.class.getName() + ".authorities", jcacheConfiguration);
			cm.createCache(User.class.getName() + ".organizations",
					jcacheConfiguration);
			cm.createCache(SocialUserConnection.class.getName(), jcacheConfiguration);
			cm.createCache(Organization.class.getName(), jcacheConfiguration);
			cm.createCache(Organization.class.getName() + ".courses", jcacheConfiguration);
			cm.createCache(Organization.class.getName() + ".users", jcacheConfiguration);
			cm.createCache(Address.class.getName(), jcacheConfiguration);
			cm.createCache(Portfolio.class.getName(), jcacheConfiguration);
			cm.createCache(Portfolio.class.getName() + ".portfolioItems", jcacheConfiguration);
			cm.createCache(Course.class.getName(), jcacheConfiguration);
			cm.createCache(Course.class.getName() + ".resources", jcacheConfiguration);
			cm.createCache(Course.class.getName() + ".students", jcacheConfiguration);
			cm.createCache(Course.class.getName() + ".milestones", jcacheConfiguration);
			cm.createCache(Milestone.class.getName(), jcacheConfiguration);
			cm.createCache(Milestone.class.getName() + ".resources", jcacheConfiguration);
			cm.createCache(Milestone.class.getName() + ".achievements", jcacheConfiguration);
			cm.createCache(Achievement.class.getName(), jcacheConfiguration);
			cm.createCache(Achievement.class.getName() + ".achievedBy", jcacheConfiguration);
			cm.createCache(ItemLink.class.getName(), jcacheConfiguration);
			cm.createCache(Session.class.getName(), jcacheConfiguration);
			cm.createCache(Session.class.getName() + ".programs", jcacheConfiguration);
			cm.createCache(Program.class.getName(), jcacheConfiguration);
			cm.createCache(Program.class.getName() + ".courses", jcacheConfiguration);
			cm.createCache(School.class.getName(), jcacheConfiguration);
			cm.createCache(School.class.getName() + ".programs", jcacheConfiguration);
			cm.createCache(PortfolioItem.class.getName(), jcacheConfiguration);
			cm.createCache(PortfolioItem.class.getName() + ".resources", jcacheConfiguration);
			// jhipster-needle-ehcache-add-entry
		};
	}
}
