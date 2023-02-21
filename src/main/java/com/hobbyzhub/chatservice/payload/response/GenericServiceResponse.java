package com.hobbyzhub.chatservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author bikathi_martin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenericServiceResponse<T> {
    private String apiVersion;
    private String organizationName;
    private String message;
    private Integer responseCode;
    private T data;
}
