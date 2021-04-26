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

package com.github.anilople.object.storage.demo.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

/**
 * Demo for how to use {@link software.amazon.awssdk.services.s3.S3Client}.
 *
 * @author wxq
 */
@RestController
@RequestMapping("/demo/S3Client")
public class S3ClientController {

  @Autowired
  private S3Client s3Client;

  @GetMapping("/listBuckets")
  public ResponseEntity<Map<String, Instant>> listBuckets() {
    ListBucketsResponse listBucketsResponse = this.s3Client.listBuckets();
    List<Bucket> buckets = listBucketsResponse.buckets();
    Map<String, Instant> map = new HashMap<>();
    for (Bucket bucket : buckets) {
      map.put(bucket.name(), bucket.creationDate());
    }
    return ResponseEntity.ok(map);
  }

}
