/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.anilople.object.storage.autoconfigure;

import com.github.anilople.object.storage.autoconfigure.constant.ObjectStorageConstants;
import com.github.anilople.object.storage.core.config.ObjectStorageEndpoint;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Object Storage support.
 *
 * @author wxq
 */
@Configuration
@ConditionalOnProperty(name = ObjectStorageConstants.ENABLED, havingValue = "true")
@EnableConfigurationProperties(ObjectStorageProperties.class)
public class ObjectStorageAutoConfiguration {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ObjectStorageAutoConfiguration.class);

  private final ObjectStorageProperties properties;

  public ObjectStorageAutoConfiguration(ObjectStorageProperties properties) {
    this.properties = properties;
  }

  /** @see ObjectStorageProperties#getRegion() */
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = ObjectStorageConstants.PREFIX, value = "region")
  public Region region() {
    Region region = Region.of(this.properties.getRegion());
    LOGGER.debug(
        "define bean software.amazon.awssdk.regions.Region from user's config. region = [{}]",
        region);
    return region;
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = ObjectStorageConstants.PREFIX, value = "endpoint")
  public ObjectStorageEndpoint objectStorageEndpoint() {
    URI endpoint = URI.create(this.properties.getEndpoint());
    LOGGER.debug("use endpoint [{}]", endpoint);
    return new ObjectStorageEndpoint(endpoint);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(
      prefix = ObjectStorageConstants.PREFIX,
      value = {"access-key-id", "secret-access-key"})
  public AwsCredentialsProvider awsCredentialsProvider() {
    AwsCredentials awsCredentials =
        AwsBasicCredentials.create(
            this.properties.getAccessKeyId(), this.properties.getSecretAccessKey());
    LOGGER.debug("AwsCredentialsProvider from user's config. {}", awsCredentials);
    return StaticCredentialsProvider.create(awsCredentials);
  }
}
