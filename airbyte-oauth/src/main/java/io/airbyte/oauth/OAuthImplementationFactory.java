/*
 * Copyright (c) 2021 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.oauth;

import com.google.common.collect.ImmutableMap;
import io.airbyte.config.persistence.ConfigRepository;
import io.airbyte.oauth.flows.FacebookMarketingOAuthFlow;
import io.airbyte.oauth.flows.google.GoogleAdsOAuthFlow;
import io.airbyte.oauth.flows.google.GoogleAnalyticsOAuthFlow;
import io.airbyte.oauth.flows.google.GoogleSearchConsoleOAuthFlow;
import io.airbyte.oauth.flows.zendesk.ZendeskOAuthFlow;
import java.util.Map;
import java.util.UUID;

public class OAuthImplementationFactory {

  private final Map<String, OAuthFlowImplementation> OAUTH_FLOW_MAPPING;

  public OAuthImplementationFactory(ConfigRepository configRepository) {
    OAUTH_FLOW_MAPPING = ImmutableMap.<String, OAuthFlowImplementation>builder()
        .put("airbyte/source-facebook-marketing", new FacebookMarketingOAuthFlow(configRepository))
        .put("airbyte/source-google-ads", new GoogleAdsOAuthFlow(configRepository))
        .put("airbyte/source-google-analytics-v4", new GoogleAnalyticsOAuthFlow(configRepository))
        .put("airbyte/source-google-search-console", new GoogleSearchConsoleOAuthFlow(configRepository))
        .put("airbyte/source-zendesk-sunshine", new ZendeskOAuthFlow(configRepository))
        .build();
  }


  public OAuthFlowImplementation create(String imageName, UUID workspaceId) {
    if (OAUTH_FLOW_MAPPING.containsKey(imageName)) {
      var impl =  OAUTH_FLOW_MAPPING.get(imageName);
      impl.setWorkspaceId(workspaceId);
      return impl;
    } else {
      throw new IllegalStateException(String.format("Requested OAuth implementation for %s, but it is not included in the oauth mapping.", imageName));
    }
  }

}
