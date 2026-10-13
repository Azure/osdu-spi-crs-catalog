/*
  Copyright 2021 Google LLC
  Copyright 2021 EPAM Systems, Inc

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.opengroup.osdu.crs.logging;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.logging.audit.AuditPayload;
import org.opengroup.osdu.core.common.logging.audit.AuditStatus;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.core.common.util.IpAddressUtil;
import org.opengroup.osdu.crs.constants.CrsCatalogRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequestScope
@RequiredArgsConstructor
public class AuditLogger {

  private final JaxRsDpsLog logger;
  private final DpsHeaders headers;
  private final HttpServletRequest httpRequest;

  private AuditEvents auditEvents;

  private AuditEvents getAuditEvents() {
    if (this.auditEvents == null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userIpAddress = IpAddressUtil.getClientIpAddress(this.httpRequest);
      String userAgent = httpRequest.getHeader("user-agent");

      if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Groups) {
          Groups groups = (Groups) authentication.getPrincipal();
          this.auditEvents = new AuditEvents(groups.getMemberEmail(), userIpAddress, userAgent, CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER);
        } else {
          this.auditEvents = new AuditEvents(this.headers.getUserEmail(), userIpAddress, userAgent, CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER);
        }
    }
    return this.auditEvents;
  }

  public void readCrsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCrsEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCrsFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCrsEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCrsByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCrsByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCrsByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCrsByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readLateBoundCrsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadLateBoundCrsEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readLateBoundCrsFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadLateBoundCrsEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readLateBoundCrsByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadLateBoundCrsByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readLateBoundCrsByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadLateBoundCrsByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readEarlyBoundCrsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadEarlyBoundCrsEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readEarlyBoundCrsFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadEarlyBoundCrsEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readEarlyBoundCrsByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadEarlyBoundCrsByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readEarlyBoundCrsByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadEarlyBoundCrsByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCompoundCrsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCrsEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCompoundCrsFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCrsEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCompoundCrsByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCrsByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCompoundCrsByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCrsByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void searchCrsSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getSearchCrsEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void searchCrsFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getSearchCrsEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCtSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCtEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCtFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCtEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCtByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCtByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCtByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCtByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readSingleCtSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadSingleCtEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readSingleCtFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadSingleCtEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readSingleCtByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadSingleCtByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readSingleCtByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadSingleCtByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCompoundCtSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCtEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCompoundCtFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCtEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readCompoundCtByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCtByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readCompoundCtByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadCompoundCtByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void searchCtSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getSearchCtEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void searchCtFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getSearchCtEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readAreaSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadAreaEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readAreaFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadAreaEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void readAreaByEssenceSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadAreaByEssenceEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void readAreaByEssenceFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getReadAreaByEssenceEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  public void searchAreaSuccess(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getSearchAreaEvent(AuditStatus.SUCCESS, resources, requiredGroupsForAction));
  }

  public void searchAreaFailure(List<String> resources, List<String> requiredGroupsForAction) {
    writeLog(getAuditEvents().getSearchAreaEvent(AuditStatus.FAILURE, resources, requiredGroupsForAction));
  }

  private void writeLog(AuditPayload log) {
    this.logger.audit(log);
  }
}
