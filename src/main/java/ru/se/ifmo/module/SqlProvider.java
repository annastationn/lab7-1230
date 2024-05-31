package ru.se.ifmo.module;

import ru.se.ifmo.model.Organization;
import ru.se.ifmo.table.OrganizationsTable;

import java.util.Collection;
import java.util.Comparator;

public class SqlProvider implements DataProvider {
    private final OrganizationsTable organizationsTable;

    public SqlProvider(OrganizationsTable organizationsTable) {
        this.organizationsTable = organizationsTable;
    }

    @Override
    public boolean save(CollectionService collectionService) {
        organizationsTable.cleanOrganizations();
        for (Organization organization : collectionService.getCollection().values().stream().sorted((Comparator.comparingLong(Organization::getId))).toList()) {
            organizationsTable.insertOrganization(organization);
        }
        return true;
    }

    @Override
    public boolean load(CollectionService collectionService) {
        Collection<Organization> organizations = organizationsTable.getOrganizations();
        collectionService.clear();
        collectionService.init(organizations);
        return organizations.size() == collectionService.getCollection().size();
    }
}
