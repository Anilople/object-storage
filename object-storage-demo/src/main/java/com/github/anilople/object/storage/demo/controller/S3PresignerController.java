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

import java.net.URL;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@RestController
@RequestMapping("/demo/S3Presigner")
public class S3PresignerController {

  @Autowired private S3Presigner s3Presigner;

  @GetMapping("/presignGetObject")
  public ResponseEntity<URL> presignGetObject(
      @RequestParam String bucket, @RequestParam String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
    Duration duration = Duration.ofDays(1);

    GetObjectPresignRequest getObjectPresignRequest =
        GetObjectPresignRequest.builder()
            .getObjectRequest(getObjectRequest)
            .signatureDuration(duration)
            .build();

    PresignedGetObjectRequest presignedGetObjectRequest =
        this.s3Presigner.presignGetObject(getObjectPresignRequest);
    URL url = presignedGetObjectRequest.url();
    return ResponseEntity.ok(url);
  }
}
