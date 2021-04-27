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

import com.github.anilople.object.storage.core.config.ObjectStorageEndpoint;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

/** @author wxq */
@Configuration
@ConditionalOnClass(S3Client.class)
@AutoConfigureAfter(ObjectStorageAutoConfiguration.class)
public class S3ClientAutoConfiguration {

  @Autowired private Optional<Region> regionOptional;

  @Autowired private Optional<ObjectStorageEndpoint> objectStorageEndpointOptional;

  @Autowired private Optional<AwsCredentialsProvider> awsCredentialsProviderOptional;

  @Autowired private Optional<S3Configuration> s3ConfigurationOptional;

  @Autowired private Optional<ClientOverrideConfiguration> clientOverrideConfigurationOptional;

  @Autowired private Optional<SdkHttpClient> sdkHttpClientOptional;

  @Autowired private Optional<SdkHttpClient.Builder> sdkHttpClientBuilderOptional;

  @Bean
  @ConditionalOnMissingBean
  public S3Client s3Client() {
    final S3ClientBuilder builder = S3Client.builder();
    this.regionOptional.ifPresent(builder::region);
    this.objectStorageEndpointOptional
        .map(ObjectStorageEndpoint::getEndpoint)
        .ifPresent(builder::endpointOverride);
    this.awsCredentialsProviderOptional.ifPresent(builder::credentialsProvider);
    this.s3ConfigurationOptional.ifPresent(builder::serviceConfiguration);
    this.clientOverrideConfigurationOptional.ifPresent(builder::overrideConfiguration);

    this.sdkHttpClientOptional.ifPresent(builder::httpClient);
    this.sdkHttpClientBuilderOptional.ifPresent(builder::httpClientBuilder);

    S3Client s3Client = builder.build();
    return s3Client;
  }
}
