package com.fioneer.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponse {

    private String responseCode;
    private String responseMessage;
    private IssueDetails issueDetails;

    private List<IssueDetails> issueDetailsList;
}
