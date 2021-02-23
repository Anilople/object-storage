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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/** @author wxq */
@Configuration
@EnableConfigurationProperties(ObjectStorageProperties.class)
@ConditionalOnClass(S3Client.class)
public class ObjectStorageConfiguration {

  private final ObjectStorageProperties properties;

  public ObjectStorageConfiguration(ObjectStorageProperties properties) {
    this.properties = properties;
  }

  public Region resolveRegion() {
    return Region.of(this.properties.getRegion());
  }

  public URI resolveEndpoint() {
    return URI.create(this.properties.getEndpoint());
  }

  @Bean
  @ConditionalOnMissingBean
  public AwsCredentials awsCredentials() {
    return AwsBasicCredentials.create(
        this.properties.getAccessKeyId(), this.properties.getSecretAccessKey());
  }

  @Bean
  @ConditionalOnMissingBean
  public AwsCredentialsProvider awsCredentialsProvider(AwsCredentials awsCredentials) {
    return StaticCredentialsProvider.create(awsCredentials);
  }

  @Bean
  @ConditionalOnMissingBean
  public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
    final Region region = this.resolveRegion();
    final URI endpoint = this.resolveEndpoint();

    S3Client s3Client =
        S3Client.builder()
            .region(region)
            .endpointOverride(endpoint)
            .credentialsProvider(awsCredentialsProvider)
            .build();
    return s3Client;
  }

  @Bean
  @ConditionalOnMissingBean
  public S3Presigner s3Presigner(AwsCredentialsProvider awsCredentialsProvider) {
    final Region region = this.resolveRegion();
    final URI endpoint = this.resolveEndpoint();

    S3Presigner s3Presigner =
        S3Presigner.builder()
            .region(region)
            .endpointOverride(endpoint)
            .credentialsProvider(awsCredentialsProvider)
            .build();
    return s3Presigner;
  }
}
