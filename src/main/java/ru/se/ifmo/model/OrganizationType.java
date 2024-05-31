package ru.se.ifmo.model;

public enum OrganizationType {
        COMMERCIAL ("Коммерческая"),
        GOVERNMENT ("Государственная"),
        TRUST ("Доверенная"),
        PRIVATE_LIMITED_COMPANY ("Закрытое акционерное общество"),
        OPEN_JOINT_STOCK_COMPANY ("Открытое акционерное общество");

        private final String type;
        OrganizationType(String type) {
                this.type = type;
        }
        public String getType() {
                return type;
        }
    }


