package com.project.cinemarest.connector.jdbc.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JdbcResponse<T> {

    private List<T> result;
    private Object resultSetExtr;
    private Map<String, Object> storedProcedureResult;
}
