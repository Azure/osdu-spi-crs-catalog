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

import com.google.common.base.Strings;
import java.util.List;
import org.opengroup.osdu.core.common.logging.audit.AuditAction;
import org.opengroup.osdu.core.common.logging.audit.AuditPayload;
import org.opengroup.osdu.core.common.logging.audit.AuditStatus;

public class AuditEvents {

  private static final String READ_CRS_ACTION_ID = "CR001";
  private static final String READ_CRS_MESSAGE = "Read CRSResults";

  private static final String READ_CRS_BY_ESSENCE_ACTION_ID = "CR002";
  private static final String READ_CRS_BY_ESSENCE_MESSAGE = "Read CRS by essence";

  private static final String READ_LATE_BOUND_CRS_ACTION_ID = "CR003";
  private static final String READ_LATE_BOUND_CRS_MESSAGE = "Read LateBoundCRSResults";

  private static final String READ_LATE_BOUND_CRS_BY_ESSENCE_ACTION_ID = "CR004";
  private static final String READ_LATE_BOUND_CRS_BY_ESSENCE_MESSAGE = "Read LateBoundCRS by essence";

  private static final String READ_EARLY_BOUND_CRS_ACTION_ID = "CR005";
  private static final String READ_EARLY_BOUND_CRS_MESSAGE = "Read EarlyBoundCRSResults";

  private static final String READ_EARLY_BOUND_CRS_BY_ESSENCE_ACTION_ID = "CR006";
  private static final String READ_EARLY_BOUND_CRS_BY_ESSENCE_MESSAGE = "Read EarlyBoundCRS by essence";

  private static final String READ_COMPOUND_CRS_ACTION_ID = "CR007";
  private static final String READ_COMPOUND_CRS_MESSAGE = "Read CompoundCRSResults";

  private static final String READ_COMPOUND_CRS_BY_ESSENCE_ACTION_ID = "CR008";
  private static final String READ_COMPOUND_CRS_BY_ESSENCE_MESSAGE = "Read CompoundCRS by essence";

  private static final String SEARCH_CRS_ACTION_ID = "CR009";
  private static final String SEARCH_CRS_MESSAGE = "Search CRS";

  private static final String READ_CT_ACTION_ID = "CR010";
  private static final String READ_CT_MESSAGE = "Read CTResults";

  private static final String READ_CT_BY_ESSENCE_ACTION_ID = "CR011";
  private static final String READ_CT_BY_ESSENCE_MESSAGE = "Read CT by essence";

  private static final String READ_SINGLE_CT_ACTION_ID = "CR012";
  private static final String READ_SINGLE_CT_MESSAGE = "Read SingleCTResults";

  private static final String READ_SINGLE_CT_BY_ESSENCE_ACTION_ID = "CR013";
  private static final String READ_SINGLE_CT_BY_ESSENCE_MESSAGE = "Read SingleCT by essence";

  private static final String READ_COMPOUND_CT_ACTION_ID = "CR014";
  private static final String READ_COMPOUND_CT_MESSAGE = "Read CompoundCTResults";

  private static final String READ_COMPOUND_CT_BY_ESSENCE_ACTION_ID = "CR015";
  private static final String READ_COMPOUND_CT_BY_ESSENCE_MESSAGE = "Read CompoundCT by essence";

  private static final String SEARCH_CT_ACTION_ID = "CR0016";
  private static final String SEARCH_CT_MESSAGE = "Search CT";

  private static final String READ_AREA_ACTION_ID = "CR017";
  private static final String READ_AREA_MESSAGE = "Read AreaOfUseResults";

  private static final String READ_AREA_BY_ESSENCE_ACTION_ID = "CR018";
  private static final String READ_AREA_BY_ESSENCE_MESSAGE = "Read AreaOfUse by essence";

  private static final String SEARCH_AREA_ACTION_ID = "CR0019";
  private static final String SEARCH_AREA_MESSAGE = "Search Area";


  private final String user;
  private final String userIpAddress;
  private final String userAgent;
  private final String userAuthorizedGroupName;

  public AuditEvents(String user, String userIpAddress, String userAgent, String userAuthorizedGroupName) {
    this.user = requireNonEmpty(user, "User not provided for audit events.");
    this.userIpAddress = requireNonEmpty(userIpAddress, "User's IP address is not provided for audit events.");
    this.userAgent = requireNonEmpty(userAgent, "User's agent is not provided for audit events.");
    this.userAuthorizedGroupName = requireNonEmpty(userAuthorizedGroupName, "User's authorized group name is not provided for audit events.");
  }

  public AuditPayload getReadCrsEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_CRS_ACTION_ID)
        .message(getStatusMessage(status, READ_CRS_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCrsByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_CRS_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_CRS_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadLateBoundCrsEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_LATE_BOUND_CRS_ACTION_ID)
        .message(getStatusMessage(status, READ_LATE_BOUND_CRS_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadLateBoundCrsByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_LATE_BOUND_CRS_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_LATE_BOUND_CRS_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadEarlyBoundCrsEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_EARLY_BOUND_CRS_ACTION_ID)
        .message(getStatusMessage(status, READ_EARLY_BOUND_CRS_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadEarlyBoundCrsByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_EARLY_BOUND_CRS_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_EARLY_BOUND_CRS_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCompoundCrsEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_COMPOUND_CRS_ACTION_ID)
        .message(getStatusMessage(status, READ_COMPOUND_CRS_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCompoundCrsByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_COMPOUND_CRS_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_COMPOUND_CRS_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getSearchCrsEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(SEARCH_CRS_ACTION_ID)
        .message(getStatusMessage(status, SEARCH_CRS_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCtEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_CT_ACTION_ID)
        .message(getStatusMessage(status, READ_CT_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCtByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_CT_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_CT_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadSingleCtEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_SINGLE_CT_ACTION_ID)
        .message(getStatusMessage(status, READ_SINGLE_CT_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadSingleCtByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_SINGLE_CT_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_SINGLE_CT_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCompoundCtEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_COMPOUND_CT_ACTION_ID)
        .message(getStatusMessage(status, READ_COMPOUND_CT_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadCompoundCtByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_COMPOUND_CT_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_COMPOUND_CT_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getSearchCtEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(SEARCH_CT_ACTION_ID)
        .message(getStatusMessage(status, SEARCH_CT_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadAreaEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_AREA_ACTION_ID)
        .message(getStatusMessage(status, READ_AREA_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getReadAreaByEssenceEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(READ_AREA_BY_ESSENCE_ACTION_ID)
        .message(getStatusMessage(status, READ_AREA_BY_ESSENCE_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  public AuditPayload getSearchAreaEvent(AuditStatus status, List<String> resources, List<String> requiredGroupsForAction) {
    return AuditPayload.builder()
        .action(AuditAction.READ)
        .status(status)
        .user(this.user)
        .actionId(SEARCH_AREA_ACTION_ID)
        .message(getStatusMessage(status, SEARCH_AREA_MESSAGE))
        .resources(resources)
        .requiredGroupsForAction(requiredGroupsForAction)
        .userIpAddress(this.userIpAddress)
        .userAgent(this.userAgent)
        .userAuthorizedGroupName(this.userAuthorizedGroupName)
        .build();
  }

  private String getStatusMessage(AuditStatus status, String message) {
    return "%s - %s".formatted(message, status.name().toLowerCase());
  }

  private static String requireNonEmpty(String value, String message) {
    if (Strings.isNullOrEmpty(value)) {
        throw new IllegalArgumentException(message);
    }
    return value;
  }
}
