package com.teamgold.goldenharvest.domain.customersupport.command.application.dto.request.comment;

import lombok.Builder;
import lombok.Getter;

public record CommentCreateRequest (
        String comment
){}
