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

package com.github.anilople.object.storage.config;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.utils.StringUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Object Storage support.
 *
 * @author wxq
 */
@Configuration
@ConditionalOnClass(S3Client.class)
@ConditionalOnProperty(name = "object.storage.enabled", havingValue = "true")
@EnableConfigurationProperties(ObjectStorageProperties.class)
public class ObjectStorageAutoConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectStorageAutoConfiguration.class);

  private final ObjectStorageProperties properties;

  public ObjectStorageAutoConfiguration(ObjectStorageProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnMissingBean
  public AwsCredentialsProvider awsCredentialsProvider() {
    if (StringUtils.isNotBlank(this.properties.getAccessKeyId()) && StringUtils.isNotBlank(this.properties.getSecretAccessKey())) {
      AwsCredentials awsCredentials = AwsBasicCredentials.create(
          this.properties.getAccessKeyId(), this.properties.getSecretAccessKey());
      AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);
      LOGGER.debug("AwsCredentialsProvider from user's config");
      return awsCredentialsProvider;
    }

    LOGGER.debug("AwsCredentialsProvider from software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider");
    return DefaultCredentialsProvider.create();
  }

  @Bean
  @ConditionalOnMissingBean
  public AwsRegionProvider awsRegionProvider() {
    if (StringUtils.isNotBlank(this.properties.getRegion())) {
      LOGGER.debug("AwsRegionProvider from user's config region = [{}]", this.properties.getRegion());
      return () -> Region.of(this.properties.getRegion());
    }
    LOGGER.debug("AwsRegionProvider from software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain");
    return DefaultAwsRegionProviderChain.builder().build();
  }

  @Bean
  @ConditionalOnMissingBean
  public S3Client s3Client(AwsRegionProvider awsRegionProvider, AwsCredentialsProvider awsCredentialsProvider) {
    final S3ClientBuilder s3ClientBuilder = S3Client.builder();

    s3ClientBuilder.region(awsRegionProvider.getRegion());

    if (StringUtils.isNotBlank(this.properties.getEndpoint())) {
      final URI endpoint = URI.create(this.properties.getEndpoint());
      s3ClientBuilder.endpointOverride(endpoint);
    }

    // make error happen in bootstrap time, it is more earlier
    awsCredentialsProvider.resolveCredentials();
    s3ClientBuilder.credentialsProvider(awsCredentialsProvider);

    S3Client s3Client = s3ClientBuilder.build();
    return s3Client;
  }

  @Bean
  @ConditionalOnMissingBean
  public S3Presigner s3Presigner(AwsCredentialsProvider awsCredentialsProvider) {
    return null;
  }
}
