package com.project.cinemarest.connector.jdbc.request;

import com.project.cinemarest.connector.jdbc.utils.JdbcQueryType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JdbcRequest<T> {

    private String query;
    private RowMapper rowMapper;
    private ResultSetExtractor resultSetExtractor;
    private Object[] params;
    private JdbcQueryType type;
    private List<SqlParameter> storedProcedureParameters;
    private CallableStatementCreator callableStatementCreator;
}
