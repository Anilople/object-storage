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

import com.github.anilople.object.storage.constant.ObjectStorageConstants;
import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;

/**
 * {@link ConfigurationProperties} for Object Storage.
 *
 * @author wxq
 */
@ConfigurationProperties(prefix = ObjectStorageConstants.PREFIX)
public class ObjectStorageProperties {

  /** Enable Object Storage auto configure or not. */
  private Boolean enabled;

  /**
   * If you don't know which {@link Region} should be use, we recommend use {@link
   * Region#US_EAST_1}, i.e write <code>us-east-1</code> in config
   *
   * @see Region
   */
  private String region;

  /** @see software.amazon.awssdk.services.s3.S3ClientBuilder#endpointOverride(URI) */
  private String endpoint;

  /** @see AwsCredentials#accessKeyId() */
  private String accessKeyId;

  /** @see AwsCredentials#secretAccessKey() */
  private String secretAccessKey;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getSecretAccessKey() {
    return secretAccessKey;
  }

  public void setSecretAccessKey(String secretAccessKey) {
    this.secretAccessKey = secretAccessKey;
  }
}
