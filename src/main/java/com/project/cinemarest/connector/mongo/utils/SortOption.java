package com.project.cinemarest.connector.mongo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortOption {

    private String columnName;
    private SortDirection direction;

    public enum SortDirection {
        ASC("ASC"),
        DESC("DESC");

        private final String direction;

        @Override
        public String toString() {
            return String.valueOf(this.direction);
        }

        SortDirection(String direction) {
            this.direction = direction;
        }
    }

}
