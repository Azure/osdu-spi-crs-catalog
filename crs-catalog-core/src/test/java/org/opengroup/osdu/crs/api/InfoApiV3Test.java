// Copyright © Amazon
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http:#www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.info.VersionInfoBuilder;
import org.opengroup.osdu.core.common.model.info.VersionInfo;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class InfoApiV3Test {

  @InjectMocks
  private InfoApiV3 sut;

  @Mock
  private VersionInfoBuilder versionInfoBuilder;

  @Test
  public void should_returnVersionInfo() throws IOException {
    VersionInfo versionInfo = VersionInfo.builder()
        .groupId("group")
        .artifactId("artifact")
        .version("0.1.0")
        .buildTime("1000")
        .branch("master")
        .commitId("7777")
        .commitMessage("Merge commit")
        .build();
    when(versionInfoBuilder.buildVersionInfo()).thenReturn(versionInfo);
    VersionInfo response = this.sut.info();
    assertEquals(versionInfo, response);
  }
}
