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

package com.github.anilople.object.storage.core.client;

import software.amazon.awssdk.services.s3.S3Client;

/**
 * always use a fixed bucket.
 *
 * @author wxq
 */
public final class FixedBucketS3Client extends DelegatedS3Client {

  private final String bucket;

  public FixedBucketS3Client(String bucket, S3Client delegate) {
    super(delegate);
    this.bucket = bucket;
  }

}
